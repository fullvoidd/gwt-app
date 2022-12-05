package com.my.client.frames;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONException;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import com.my.client.service.SpringService;
import com.my.shared.Node;
import com.my.shared.ServerToClientString;
import com.sencha.gxt.widget.core.client.button.SplitButton;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import java.util.Iterator;
import java.util.List;

public class SaveAndLoadButtons {

    public static void addSaveAndLoadButtons(ListBox listBox, Tree tree, ScrollPanel treeBox,
                                             HorizontalPanel bottomPanel) {
        HorizontalPanel buttonPanel = new HorizontalPanel();
        buttonPanel.setSpacing(10);
        HorizontalPanel creationPanel = new HorizontalPanel();
        creationPanel.setSpacing(10);

        TextBox textBox = new TextBox();
        textBox.setVisible(false);
        textBox.setWidth("200px");
        SplitButton createElemButton = new CreatingElementButton(textBox, listBox, treeBox).create(tree);
        creationPanel.add(createElemButton);
        creationPanel.add(textBox);

        Button saveButton = getSaveButton(tree);
        Button loadButton = getLoadButton(listBox, tree);

        saveButton.setPixelSize(100, 40);
        saveButton.getElement().getStyle().setFontSize(15, Style.Unit.PX);
        loadButton.setPixelSize(100, 40);
        loadButton.getElement().getStyle().setFontSize(15, Style.Unit.PX);
        buttonPanel.add(saveButton);
        buttonPanel.add(loadButton);

        bottomPanel.add(creationPanel);
        bottomPanel.add(buttonPanel);
        bottomPanel.setCellHorizontalAlignment(creationPanel, HasHorizontalAlignment.ALIGN_LEFT);
        bottomPanel.setCellHorizontalAlignment(buttonPanel, HasHorizontalAlignment.ALIGN_RIGHT);
    }

    private static Button getSaveButton(final Tree tree) {
        return new Button("Сохранить", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (tree.getItemCount() == 0) {
                    Window.alert("Ошибка! Сохранение пустого дерева.");
                    return;
                }

                JSONObject treeJSON = new JSONObject();
                recursiveConvertToJSON(getNewTree(tree), treeJSON);

                SpringService service = GWT.create(SpringService.class);
                service.saveTree(treeJSON.toString(), new MethodCallback<Void>() {
                    @Override
                    public void onFailure(Method method, Throwable exception) {
                        Window.alert(exception.getMessage());
                    }

                    @Override
                    public void onSuccess(Method method, Void response) {
                        Window.alert("saved");
                    }
                });
            }
        });
    }

    private static Node getNewTree(Tree tree) {
        Node newTree = new Node("root");

        Iterator<TreeItem> iterator = tree.treeItemIterator();
        while (iterator.hasNext()) {
            TreeItem child = iterator.next();
            if (child.getParentItem() == null) {
                Node nodeChild = new Node(child.getText());
                newTree.addChild(nodeChild);
                if (child.getChildCount() > 0) {
                    recursiveAddChild(child, nodeChild);
                }
            }
        }
        return newTree;
    }

    private static void recursiveAddChild(TreeItem parent, Node nodeParent) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            TreeItem child = parent.getChild(i);
            Node nodeChild = new Node(child.getText());
            nodeParent.addChild(nodeChild);
            if (child.getChildCount() > 0) {
                recursiveAddChild(child, nodeChild);
            }
        }
    }

    private static void recursiveConvertToJSON(Node parent, JSONObject parentJSON) throws JSONException {
        parentJSON.put("parent", new JSONString(parent.getData()));
        JSONArray childrenJSONArray = new JSONArray();
        List<Node> children = parent.getTopChildren();
        for (int i = 0; i < children.size(); i++ ) {
            Node child = children.get(i);
            JSONObject childJSON = new JSONObject();
            childJSON.put("parent", new JSONString(child.getData()));
            if (!child.getTopChildren().isEmpty()) {
                recursiveConvertToJSON(child, childJSON);
            }
            childrenJSONArray.set(i, childJSON);
        }
        parentJSON.put("children", childrenJSONArray);
    }

    private static Button getLoadButton(final ListBox listBox, final Tree tree) {
        return new Button("Загрузить", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                SpringService service = GWT.create(SpringService.class);
                service.loadTree(new MethodCallback<ServerToClientString>() {
                    @Override
                    public void onFailure(Method method, Throwable exception) {
                        Window.alert(exception.getMessage());
                    }

                    @Override
                    public void onSuccess(Method method, ServerToClientString response) {
                        JSONObject objectFromStr = new JSONObject(JsonUtils.safeEval(response.getValue()));
                        Node nodeTree = new Node();

                        recursiveConvertToNode(nodeTree, objectFromStr);
                        if (nodeTree.getTopChildren().isEmpty()) {
                            Window.alert("Ошибка! Пустое дерево. Перед загрузкой сохраните что-нибудь.");
                            return;
                        }

                        tree.clear();
                        listBox.clear();

                        for (Node child : nodeTree.getTopChildren()) {
                            TreeItem childTreeItem = new TreeItem();
                            childTreeItem.setText(child.getData());
                            tree.addItem(childTreeItem);
                            if (!child.getTopChildren().isEmpty()) {
                                recursiveFillTree(child, childTreeItem);
                            }
                        }
                    }
                });
            }
        });
    }

    private static void recursiveConvertToNode(Node parent, JSONObject parentJSON) throws JSONException {
        parent.setData(parentJSON.get("parent").isString().stringValue());
        JSONArray children = parentJSON.get("children").isArray();
        for (int i = 0; i < children.size(); i++) {
            JSONObject childJSON = children.get(i).isObject();
            Node child = new Node(childJSON.get("parent").isString().stringValue());
            parent.addChild(child);
            if (childJSON.containsKey("children")) {
                recursiveConvertToNode(child, childJSON);
            }
        }
    }

    private static void recursiveFillTree(Node parent, TreeItem parentTreeItem) {
        for (Node child : parent.getTopChildren()) {
            TreeItem childTreeItem = new TreeItem();
            childTreeItem.setText(child.getData());
            parentTreeItem.addItem(childTreeItem);
            if (!child.getTopChildren().isEmpty()) {
                recursiveFillTree(child, childTreeItem);
            }
        }
    }
}
