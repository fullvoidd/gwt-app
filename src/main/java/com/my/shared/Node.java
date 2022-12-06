package com.my.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.List;

public class Node implements IsSerializable {
    private String data = "";
    private List<Node> children = new ArrayList<>();

    public Node() {
    }

    public Node(String data) {
        this.data = data;
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

}
