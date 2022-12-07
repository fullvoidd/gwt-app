package com.my.server.mongodb;

import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.my.shared.Node;
import org.bson.Document;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class DatabaseManipulator {

    private final MongoCollection<Document> collection;

    public DatabaseManipulator() throws IOException {
        Properties props = new Properties();
        props.load(Files.newInputStream(Paths.get("mongo.properties")));
        MongoClient mongoClient = new MongoClient(props.getProperty("host"), Integer.parseInt(props.getProperty("port")));
        MongoDatabase database = mongoClient.getDatabase(props.getProperty("databaseName"));
        collection = database.getCollection(props.getProperty("collectionName"));
    }

    public void saveTreeInDB(Node tree) {
        collection.drop();
        Document root = new Document()
                .append("parent", tree.getData())
                .append("children", new ArrayList<>());
        collection.insertOne(root);

        recursiveFillDB(tree);
    }

    public Node loadTreeFromDB() {
        return recursiveOutDB(new Node("root"));
    }

    private Node recursiveOutDB(Node newTree) {
        collection.find(Filters.eq("parent", newTree.getData()))
                .forEach((Consumer<Document>) doc -> {
                    List<String> childList = doc.getList("children", String.class);
                    childList.forEach(child -> {
                        Node newChild = new Node(child);
                        newTree.addChild(newChild);
                        recursiveOutDB(newChild);
                    });
                });
        return newTree;
    }

    private void recursiveFillDB(Node tree) {
        AtomicBoolean isInDB = new AtomicBoolean(false);
        collection.find(Filters.eq("parent", tree.getData()))
                .forEach((Consumer<Document>) doc -> {
                    isInDB.set(true);
                });
        if (!isInDB.get()) {
            Document document = new Document()
                    .append("parent", tree.getData())
                    .append("children", new ArrayList<>());
            collection.insertOne(document);
        }

        List<Object> list = new ArrayList<>();
        for (Node node : tree.getTopChildren()) {
            list.add(node.getData());
            recursiveFillDB(node);
        }

        collection.findOneAndUpdate(Filters.eq("parent", tree.getData()),
                Updates.pushEach("children", list));
    }

}
