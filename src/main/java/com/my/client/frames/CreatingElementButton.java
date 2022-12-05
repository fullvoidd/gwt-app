package com.my.client.frames;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import com.sencha.gxt.widget.core.client.button.SplitButton;
import com.sencha.gxt.widget.core.client.event.CheckChangeEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.menu.CheckMenuItem;
import com.sencha.gxt.widget.core.client.menu.Item;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;

public class CreatingElementButton {

    private final CheckMenuItem checkItemList = new CheckMenuItem("Простой список");
    private final CheckMenuItem checkItemTree = new CheckMenuItem("Древовидный список");
    private final CheckMenuItem checkItemDir = new CheckMenuItem("Папка");
    private final TextBox textBox;
    private final ListBox listBox;
    private final ScrollPanel treeBox;
    private final SplitButton createElemButton = new SplitButton("Добавить");

    public CreatingElementButton(TextBox textBox, ListBox listBox, ScrollPanel treeBox) {
        this.textBox = textBox;
        this.listBox = listBox;
        this.treeBox = treeBox;
    }

    public SplitButton create(final Tree tree) {
        createElemButton.setWidth("100px");
        createElemButton.setMenu(createCreationMenu());
        configureElemButton(tree);

        return createElemButton;
    }

    private Menu createCreationMenu() {
        final Style listBoxStyle = listBox.getElement().getStyle();
        final Style treeBoxStyle = treeBox.getElement().getStyle();

        Menu creationMenu = new Menu();

        checkItemDir.addCheckChangeHandler(new CheckChangeEvent.CheckChangeHandler<CheckMenuItem>() {
            @Override
            public void onCheckChange(CheckChangeEvent<CheckMenuItem> event) {
                if (checkItemDir.isChecked()) {
                    textBox.setVisible(true);
                    listBoxStyle.setBorderColor("#000000");
                    listBoxStyle.setBorderWidth(1, Style.Unit.PX);
                    treeBoxStyle.setBorderColor("#000000");
                    treeBoxStyle.setBorderWidth(1, Style.Unit.PX);
                    createElemButton.setText("Добавить папку");
                    checkItemList.setChecked(false);
                    checkItemTree.setChecked(false);
                } else if (!checkItemTree.isChecked() && !checkItemList.isChecked()) {
                    textBox.setVisible(false);
                    treeBoxStyle.setBorderColor("#000000");
                    treeBoxStyle.setBorderWidth(1, Style.Unit.PX);
                    createElemButton.setText("Добавить");
                }
            }
        });
        checkItemDir.setGroup("elements");
        creationMenu.add(checkItemDir);

        MenuItem lists = new MenuItem("Запись");
        creationMenu.add(lists);
        creationMenu.add(createRadioMenu(lists, listBoxStyle, treeBoxStyle));
        return creationMenu;
    }

    private Menu createRadioMenu(MenuItem lists, final Style listBoxStyle, final Style treeBoxStyle) {
        Menu radioMenu = new Menu();
        lists.setSubMenu(radioMenu);

        checkItemList.addCheckChangeHandler(new CheckChangeEvent.CheckChangeHandler<CheckMenuItem>() {
            @Override
            public void onCheckChange(CheckChangeEvent<CheckMenuItem> event) {
                if (checkItemList.isChecked()) {
                    textBox.setVisible(true);
                    listBoxStyle.setBorderColor("#32CD32");
                    listBoxStyle.setBorderWidth(2, Style.Unit.PX);
                    treeBoxStyle.setBorderColor("#000000");
                    treeBoxStyle.setBorderWidth(1, Style.Unit.PX);
                    createElemButton.setText("Добавить запись");
                    checkItemDir.setChecked(false);
                } else {
                    listBoxStyle.setBorderColor("#000000");
                    listBoxStyle.setBorderWidth(1, Style.Unit.PX);
                    if (!checkItemDir.isChecked() && !checkItemTree.isChecked()) {
                        textBox.setVisible(false);
                        createElemButton.setText("Добавить");
                    }
                }
            }
        });
        checkItemList.setGroup("lists");
        radioMenu.add(checkItemList);

        checkItemTree.addCheckChangeHandler(new CheckChangeEvent.CheckChangeHandler<CheckMenuItem>() {
            @Override
            public void onCheckChange(CheckChangeEvent<CheckMenuItem> event) {
                if (checkItemTree.isChecked()) {
                    textBox.setVisible(true);
                    listBoxStyle.setBorderColor("#000000");
                    listBoxStyle.setBorderWidth(1, Style.Unit.PX);
                    treeBoxStyle.setBorderColor("#32CD32");
                    treeBoxStyle.setBorderWidth(2, Style.Unit.PX);
                    createElemButton.setText("Добавить запись");
                    checkItemDir.setChecked(false);
                } else {
                    treeBoxStyle.setBorderColor("#000000");
                    treeBoxStyle.setBorderWidth(1, Style.Unit.PX);
                    if (!checkItemDir.isChecked() && !checkItemList.isChecked()) {
                        textBox.setVisible(false);
                        createElemButton.setText("Добавить");
                    }
                }
            }
        });
        checkItemTree.setGroup("lists");
        radioMenu.add(checkItemTree);
        return radioMenu;
    }

    private void configureElemButton(final Tree tree) {
        createElemButton.addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                if (!checkItemList.isChecked() && !checkItemTree.isChecked() && !checkItemDir.isChecked()) {
                    Window.alert("Ошибка ввода! Выберите пункт меню.");
                    return;
                }
                if (textBox.getText().isEmpty()) {
                    Window.alert("Ошибка ввода! Пустое текстовое поле.");
                    return;
                }
                if (checkItemDir.isChecked()) {
                    TreeItem selectedItem = tree.getSelectedItem();
                    TreeItem insertableItem = new TreeItem();
                    insertableItem.setText(textBox.getText());
                    if (MoveBoxToBoxButtons.addItemAsDir(tree, selectedItem, insertableItem)) return;
                    return;
                }
                if (checkItemList.isChecked()) {
                    listBox.addItem(textBox.getText());
                    return;
                }
                TreeItem insertableItem = new TreeItem();
                insertableItem.setText(textBox.getText());
                TreeItem selectedItem = tree.getSelectedItem();
                if (selectedItem == null) {
                    tree.addItem(insertableItem);
                    return;
                }
                selectedItem.addItem(insertableItem);
            }
        });
    }
}
