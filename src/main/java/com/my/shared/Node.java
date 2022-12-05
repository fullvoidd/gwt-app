package com.my.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.List;

public class Node implements IsSerializable {
    private String data = "";
    private int id;
    private static int idCounter = 0;
    private List<Node> children = new ArrayList<>();
    private int parentId;

    public Node() {
        this.id = idCounter++;
        parentId = -1;
    }

    public Node(String data) {
        this.id = idCounter++;
        this.data = data;
        parentId = -1;
    }

    public Node(String data, int parentId) {
        this.id = idCounter++;
        this.data = data;
        this.parentId = parentId;
    }

    public static void clearIdCounter() {
        idCounter = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void addChild(Node child) {
        children.add(child);
    }

    public String getData() {
        return data;
    }

    public List<Node> getTopChildren() {
        return children;
    }

    public List<Node> recursiveFindAllChildren() {
        List<Node> resultList = new ArrayList<>();
        for (Node node : children) {
            resultList.add(node);
            List<Node> nodeChildren = node.getTopChildren();
            if (nodeChildren.size() > 0) {
                recursiveFindAllChildren(node, resultList);
            }
        }
        return resultList;
    }

    private void recursiveFindAllChildren(Node tree, List<Node> resultList) {
        List<Node> children = tree.getTopChildren();
        for (int i = 0; i < children.size(); i++) {
            Node node = children.get(i);
            resultList.add(node);
            List<Node> nodeChildren = tree.getTopChildren();
            if (nodeChildren.size() > 0) {
                recursiveFindAllChildren(node, resultList);
            }
        }
    }
}
