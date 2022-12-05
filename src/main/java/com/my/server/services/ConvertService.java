package com.my.server.services;

import com.my.shared.Node;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class ConvertService {

    public String converterToJSON(Node tree) throws JSONException {
        JSONObject treeJSON = new JSONObject();

        recursiveConvertToJSON(tree, treeJSON);

        return treeJSON.toString();
    }

    private void recursiveConvertToJSON(Node parent, JSONObject parentJSON) throws JSONException {
        parentJSON.put("parent", parent.getData());
        JSONArray childrenJSONArray = new JSONArray();
        for (Node child : parent.getTopChildren()) {
            JSONObject childJSON = new JSONObject();
            childJSON.put("parent", child.getData());
            if (!child.getTopChildren().isEmpty()) {
                recursiveConvertToJSON(child, childJSON);
            }
            childrenJSONArray.put(childJSON);
        }
        parentJSON.put("children", childrenJSONArray);
    }

    public Node converterToNode(String jsonString) throws JSONException {
        JSONObject objectFromStr = new JSONObject(jsonString);
        Node tree = new Node();

        recursiveConvertToNode(tree, objectFromStr);

        return tree;
    }

    private void recursiveConvertToNode(Node parent, JSONObject parentJSON) throws JSONException {
        parent.setData(parentJSON.getString("parent"));
        JSONArray children = parentJSON.getJSONArray("children");
        for (int i = 0; i < children.length(); i++) {
            JSONObject childJSON = children.getJSONObject(i);
            Node child = new Node(childJSON.getString("parent"));
            parent.addChild(child);
            if (childJSON.has("children")) {
                recursiveConvertToNode(child, childJSON);
            }
        }
    }
}
