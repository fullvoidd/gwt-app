package com.my.client.frames;

import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.ui.*;

import java.util.Iterator;

public class MoveBoxToBoxButtons {

    private static final int KEY_ENTER_CTRL = 10;

    public static void addButtonsForMoveBoxToBox(final ListBox listBox, final Tree tree, HorizontalPanel topPanel) {
        VerticalPanel moveButtonPanel = new VerticalPanel();
        moveButtonPanel.setSpacing(1);

        final Button leftButton = createButtonLeft(listBox, tree);

        Button allLeftButton = createButtonAllLeft(listBox, tree);

        Button rightButton = createButtonRight(listBox, tree);

        Button rightAsDirButton = createButtonRightAsDir(listBox, tree);

        moveButtonPanel.add(leftButton);
        moveButtonPanel.add(allLeftButton);
        moveButtonPanel.add(rightButton);
        moveButtonPanel.add(rightAsDirButton);
        addDoubleClickHandlerRight(listBox, tree);
        addDoubleClickHandlerLeft(listBox, tree);
        addEnterPressHandlerRight(listBox, tree);
        addEnterPressHandlerLeft(listBox, tree);
        addSpacePressHandlerFocusRight(listBox, tree);
        addSpacePressHandlerFocusLeft(listBox, tree);

        topPanel.add(moveButtonPanel);
        topPanel.setCellVerticalAlignment(moveButtonPanel, HasVerticalAlignment.ALIGN_MIDDLE);
    }

    private static Button createButtonLeft(final ListBox listBox, final Tree tree) {
        return new Button("←", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                addAlgorithmLeft(listBox, tree);
            }
        });
    }

    private static Button createButtonAllLeft(final ListBox listBox, final Tree tree) {
        return new Button("⇇", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                addAlgorithmLeftAll(listBox, tree);
            }
        });
    }

    private static void addDoubleClickHandlerLeft(final ListBox listBox, final Tree tree) {
        tree.addDomHandler(new DoubleClickHandler() {
            @Override
            public void onDoubleClick(DoubleClickEvent event) {
                if (event.isControlKeyDown()) {
                    addAlgorithmLeftAll(listBox, tree);
                } else {
                    addAlgorithmLeft(listBox, tree);
                }
            }
        }, DoubleClickEvent.getType());
    }

    private static void addEnterPressHandlerLeft(final ListBox listBox, final Tree tree) {
        tree.addKeyPressHandler(new KeyPressHandler() {
            @Override
            public void onKeyPress(KeyPressEvent event) {
                if (event.getNativeEvent().getKeyCode() == KEY_ENTER_CTRL) {
                    addAlgorithmLeftAll(listBox, tree);
                } else if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
                    addAlgorithmLeft(listBox, tree);
                }
            }
        });
    }

    private static void addAlgorithmLeftAll(ListBox listBox, Tree tree) {
        Iterator<TreeItem> iterator = tree.treeItemIterator();
        while(iterator.hasNext()) {
            TreeItem item = iterator.next();
            if (item.getParentItem() == null) {
                tree.removeItem(item);
                listBox.addItem(item.getText());
                addTreeItemToListBoxRecursive(item, listBox);
            }
        }
    }

    private static void addAlgorithmLeft(ListBox listBox, Tree tree) {
        TreeItem selectedItemTB = tree.getSelectedItem();
        if (selectedItemTB == null) {
            return;
        }
        listBox.addItem(selectedItemTB.getText());
        addTreeItemToListBoxRecursive(selectedItemTB, listBox);
        if (selectedItemTB.getParentItem() == null) {
            Iterator<TreeItem> iterator = tree.treeItemIterator();
            int itemId = 0;
            while (iterator.hasNext()) {
                TreeItem item = iterator.next();
                if (item.getParentItem() != null) {
                    continue;
                }
                if (item == selectedItemTB) {
                    tree.removeItem(selectedItemTB);
                    if (itemId >= tree.getItemCount()) {
                        tree.setSelectedItem(tree.getItem(itemId - 1));
                    } else {
                        tree.setSelectedItem(tree.getItem(itemId));
                    }
                    return;
                }
                itemId++;
            }
        }
        TreeItem parentItem = selectedItemTB.getParentItem();
        int selectedId = parentItem.getChildIndex(selectedItemTB);
        parentItem.removeItem(selectedItemTB);
        if (selectedId >= parentItem.getChildCount()) {
            tree.setSelectedItem(parentItem.getChild(selectedId - 1));
        } else {
            tree.setSelectedItem(parentItem.getChild(selectedId));
        }
    }

    private static Button createButtonRight(final ListBox listBox, final Tree tree) {
        return new Button("→", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                addAlgorithmRight(listBox, tree);
            }
        });
    }

    private static Button createButtonRightAsDir(final ListBox listBox, final Tree tree) {
        return new Button("\uD83D\uDDC0→", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                addAlgorithmRightAsDir(listBox, tree);
            }
        });
    }

    private static void addDoubleClickHandlerRight(final ListBox listBox, final Tree tree) {
        listBox.addDoubleClickHandler(new DoubleClickHandler() {
            @Override
            public void onDoubleClick(DoubleClickEvent event) {
                if (event.isControlKeyDown()) {
                    addAlgorithmRightAsDir(listBox, tree);
                } else {
                    addAlgorithmRight(listBox, tree);
                }
            }
        });
    }

    private static void addEnterPressHandlerRight(final ListBox listBox, final Tree tree) {
        listBox.addKeyPressHandler(new KeyPressHandler() {
            @Override
            public void onKeyPress(KeyPressEvent event) {
                if (event.getNativeEvent().getKeyCode() == KEY_ENTER_CTRL) {
                    addAlgorithmRightAsDir(listBox, tree);
                } else if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
                    addAlgorithmRight(listBox, tree);
                }
            }
        });
    }

    private static void addAlgorithmRight(ListBox listBox, Tree tree) {
        int selectedIdLB = listBox.getSelectedIndex();
        String selectedItemLB = listBox.getSelectedValue();
        if (selectedItemLB == null) {
            return;
        }
        listBox.removeItem(listBox.getSelectedIndex());
        if (listBox.getItemCount() == selectedIdLB && listBox.getItemCount() != 0) {
            listBox.setSelectedIndex(selectedIdLB - 1);
        } else {
            listBox.setSelectedIndex(selectedIdLB);
        }
        TreeItem selectedItemTB = tree.getSelectedItem();
        if (selectedItemTB == null) {
            tree.addTextItem(selectedItemLB);
            return;
        }
        selectedItemTB.addTextItem(selectedItemLB);
    }

    private static void addAlgorithmRightAsDir(ListBox listBox, Tree tree) {
        int selectedIdLB = listBox.getSelectedIndex();
        String selectedItemLB = listBox.getSelectedValue();
        if (selectedItemLB.isEmpty()) {
            return;
        }
        listBox.removeItem(listBox.getSelectedIndex());
        if (listBox.getItemCount() == selectedIdLB && listBox.getItemCount() != 0) {
            listBox.setSelectedIndex(selectedIdLB - 1);
        } else {
            listBox.setSelectedIndex(selectedIdLB);
        }
        TreeItem insertableItem = new TreeItem();
        insertableItem.setText(selectedItemLB);
        TreeItem selectedItemTB = tree.getSelectedItem();
        addItemAsDir(tree, selectedItemTB, insertableItem);
    }


    private static void addTreeItemToListBoxRecursive(TreeItem item, ListBox listBox) {
        for (int i = 0; i < item.getChildCount(); i++) {
            TreeItem child = item.getChild(i);
            listBox.addItem(child.getText());
            if (child.getChildCount() > 0) {
                addTreeItemToListBoxRecursive(child, listBox);
            }
        }
    }

    public static boolean addItemAsDir(Tree tree, TreeItem selectedItem, TreeItem insertableItem) {
        if (selectedItem == null) {
            tree.addItem(insertableItem);
            return true;
        }
        if (selectedItem.getParentItem() == null) {
            Iterator<TreeItem> iterator = tree.treeItemIterator();
            int itemId = 0;
            while (iterator.hasNext()) {
                TreeItem item = iterator.next();
                if (item.getParentItem() != null) {
                    continue;
                }
                if (item == selectedItem) {
                    tree.insertItem(itemId, insertableItem);
                    return true;
                }
                itemId++;
            }
        }

        TreeItem itemParent = selectedItem.getParentItem();
        itemParent.insertItem(itemParent.getChildIndex(selectedItem), insertableItem);
        return false;
    }

    private static void addSpacePressHandlerFocusRight(ListBox listBox, final Tree tree) {
        listBox.addKeyPressHandler(new KeyPressHandler() {
            @Override
            public void onKeyPress(KeyPressEvent event) {
                if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_SPACE) {
                    tree.setFocus(true);
                }
            }
        });
    }

    private static void addSpacePressHandlerFocusLeft(final ListBox listBox, Tree tree) {
        tree.addKeyPressHandler(new KeyPressHandler() {
            @Override
            public void onKeyPress(KeyPressEvent event) {
                if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_SPACE) {
                    listBox.setFocus(true);
                }
            }
        });
    }
}
