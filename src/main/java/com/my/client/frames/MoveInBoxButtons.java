package com.my.client.frames;

import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.ui.*;

import java.util.Iterator;

public class MoveInBoxButtons {
    
    private static final int KEY_BACKSLASH = 47;
    private static final int KEY_LEFT_ANGLE_BRACKET = 44;
    private static final int KEY_RIGHT_ANGLE_BRACKET = 46;

    public static void createButtonsForMoveElementsInListBox(final ListBox listBox, HorizontalPanel topPanel) {
        VerticalPanel buttonPanel = new VerticalPanel();
        buttonPanel.setSpacing(1);

        Button upButton = new Button("↑", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                moveUpListBox(listBox);
            }
        });

        Button downButton = new Button("↓", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                moveDownListBox(listBox);
            }
        });

        Button deleteButton = new Button("✕", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                int selectedId = listBox.getSelectedIndex();
                listBox.removeItem(selectedId);
                if (listBox.getItemCount() != selectedId) {
                    listBox.setSelectedIndex(selectedId);
                } else {
                    listBox.setSelectedIndex(selectedId - 1);
                }
            }
        });

        createDeletePressHandlerForListBox(listBox);
        createMoveUpPressHandlerForListBox(listBox);
        createMoveDownPressHandlerForListBox(listBox);
        buttonPanel.add(deleteButton);
        buttonPanel.add(upButton);
        buttonPanel.add(downButton);
        topPanel.add(buttonPanel);
        topPanel.setCellVerticalAlignment(buttonPanel, HasVerticalAlignment.ALIGN_MIDDLE);
    }

    public static void createButtonsForMoveElementsInTreeBox(final Tree tree, HorizontalPanel topPanel) {
        VerticalPanel buttonPanel = new VerticalPanel();
        buttonPanel.setSpacing(1);

        Button upButton = createUpButtonForTree(tree);
        Button downButton = createDownButtonForTree(tree);

        Button deleteButton = new Button("✕", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                TreeItem selectedItem = tree.getSelectedItem();
                TreeItem itemParent = selectedItem.getParentItem();
                if (itemParent != null) {
                    itemParent.removeItem(selectedItem);
                } else {
                    tree.removeItem(selectedItem);
                }
            }
        });

        createDeletePressHandlerForTree(tree);
        createMoveUpPressHandlerForTree(tree);
        createMoveDownPressHandlerForTree(tree);
        buttonPanel.add(deleteButton);
        buttonPanel.add(upButton);
        buttonPanel.add(downButton);
        topPanel.add(buttonPanel);
        topPanel.setCellVerticalAlignment(buttonPanel, HasVerticalAlignment.ALIGN_MIDDLE);
    }

    private static Button createUpButtonForTree(final Tree tree) {
        return new Button("↑", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                moveUpTree(tree);
            }
        });
    }

    private static Button createDownButtonForTree(final Tree tree) {
        return new Button("↓", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                moveDownTree(tree);
            }
        });
    }

    private static void createDeletePressHandlerForListBox(final ListBox listBox) {
        listBox.addKeyPressHandler(new KeyPressHandler() {
            @Override
            public void onKeyPress(KeyPressEvent event) {
                if (event.getNativeEvent().getKeyCode() == KEY_BACKSLASH) {
                    int selectedId = listBox.getSelectedIndex();
                    listBox.removeItem(selectedId);
                    if (listBox.getItemCount() != selectedId) {
                        listBox.setSelectedIndex(selectedId);
                    } else {
                        listBox.setSelectedIndex(selectedId - 1);
                    }
                }
            }
        });
    }

    private static void createDeletePressHandlerForTree(final Tree tree) {
        tree.addKeyPressHandler(new KeyPressHandler() {
            @Override
            public void onKeyPress(KeyPressEvent event) {
                if (event.getNativeEvent().getKeyCode() == KEY_BACKSLASH) {
                    TreeItem selectedItem = tree.getSelectedItem();
                    TreeItem itemParent = selectedItem.getParentItem();
                    if (itemParent != null) {
                        itemParent.removeItem(selectedItem);
                    } else {
                        tree.removeItem(selectedItem);
                    }
                }
            }
        });
    }

    private static void createMoveUpPressHandlerForListBox(final ListBox listBox) {
        listBox.addKeyPressHandler(new KeyPressHandler() {
            @Override
            public void onKeyPress(KeyPressEvent event) {
                if (event.getNativeEvent().getKeyCode() == KEY_LEFT_ANGLE_BRACKET) {
                    moveUpListBox(listBox);
                }
            }
        });
    }

    private static void createMoveDownPressHandlerForListBox(final ListBox listBox) {
        listBox.addKeyPressHandler(new KeyPressHandler() {
            @Override
            public void onKeyPress(KeyPressEvent event) {
                if (event.getNativeEvent().getKeyCode() == KEY_RIGHT_ANGLE_BRACKET) {
                    moveDownListBox(listBox);
                }
            }
        });
    }

    private static void createMoveUpPressHandlerForTree(final Tree tree) {
        tree.addKeyPressHandler(new KeyPressHandler() {
            @Override
            public void onKeyPress(KeyPressEvent event) {
                if (event.getNativeEvent().getKeyCode() == KEY_LEFT_ANGLE_BRACKET) {
                    moveUpTree(tree);
                }
            }
        });
    }

    private static void createMoveDownPressHandlerForTree(final Tree tree) {
        tree.addKeyPressHandler(new KeyPressHandler() {
            @Override
            public void onKeyPress(KeyPressEvent event) {
                if (event.getNativeEvent().getKeyCode() == KEY_RIGHT_ANGLE_BRACKET) {
                    moveDownTree(tree);
                }
            }
        });
    }

    private static void moveDownListBox(ListBox listBox) {
        int itemId = listBox.getSelectedIndex();
        if (itemId < listBox.getItemCount() - 1) {
            String item = listBox.getValue(itemId);
            listBox.removeItem(itemId);
            listBox.insertItem(item, itemId + 1);
            listBox.setItemSelected(itemId + 1, true);
        }
    }

    private static void moveUpListBox(ListBox listBox) {
        int itemId = listBox.getSelectedIndex();
        if (itemId > 0) {
            String item = listBox.getValue(itemId);
            listBox.removeItem(itemId);
            listBox.insertItem(item, itemId - 1);
            listBox.setItemSelected(itemId - 1, true);
        }
    }

    private static void moveDownTree(Tree tree) {
        TreeItem selectedItem = tree.getSelectedItem();

        if (selectedItem.getParentItem() == null && tree.getItemCount() > 1) {
            Iterator<TreeItem> iterator = tree.treeItemIterator();

            int itemId = 0;
            while (iterator.hasNext()) {
                TreeItem item = iterator.next();
                if (item.getParentItem() != null) {
                    continue;
                }
                if (item == selectedItem && itemId < tree.getItemCount() - 1) {
                    tree.removeItem(selectedItem);
                    tree.insertItem(itemId + 1, selectedItem);
                    tree.setSelectedItem(selectedItem);
                    return;
                }
                itemId++;
            }
        }

        TreeItem itemParent = selectedItem.getParentItem();
        int itemId= itemParent.getChildIndex(selectedItem);
        if (itemId < itemParent.getChildCount() - 1) {
            itemParent.removeItem(selectedItem);
            itemParent.insertItem(itemId + 1, selectedItem);
            tree.setSelectedItem(selectedItem);
        }
    }

    private static void moveUpTree(Tree tree) {
        TreeItem selectedItem = tree.getSelectedItem();

        if (selectedItem.getParentItem() == null && tree.getItemCount() > 1) {
            Iterator<TreeItem> iterator = tree.treeItemIterator();
            int itemId = 0;
            while (iterator.hasNext()) {
                TreeItem item = iterator.next();
                if (item.getParentItem() != null) {
                    continue;
                }
                if (item == selectedItem && itemId > 0) {
                    tree.removeItem(selectedItem);
                    tree.insertItem(itemId - 1, selectedItem);
                    tree.setSelectedItem(selectedItem);
                    return;
                }
                itemId++;
            }
        }

        TreeItem itemParent = selectedItem.getParentItem();
        int itemId = itemParent.getChildIndex(selectedItem);

        if (itemParent.getChildCount() > 1 && itemId > 0) {
            itemParent.removeItem(selectedItem);
            itemParent.insertItem(itemId - 1, selectedItem);
            tree.setSelectedItem(selectedItem);
        }
    }
}
