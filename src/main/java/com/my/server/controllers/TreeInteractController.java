package com.my.server.controllers;

import com.my.server.mongodb.DatabaseManipulator;
import com.my.server.services.ConvertService;
import com.my.shared.ServerToClientString;
import org.json.JSONException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.io.IOException;

@RestController
@EnableWebMvc
public class TreeInteractController {

    @RequestMapping("/loadTree")
    public ServerToClientString loadTree() throws JSONException, IOException {
        DatabaseManipulator manipulator = new DatabaseManipulator();
        ConvertService service = new ConvertService();
        String treeJSON = service.converterToJSON(manipulator.loadTreeFromDB());

        return new ServerToClientString(treeJSON);

    }

    @RequestMapping("/saveTree")
    public void saveTree(@RequestParam("tree") String treeStr) throws JSONException, IOException {
        DatabaseManipulator manipulator = new DatabaseManipulator();
        ConvertService service = new ConvertService();
        manipulator.saveTreeInDB(service.converterToNode(treeStr));
    }

}