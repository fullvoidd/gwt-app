package com.my.client.service;

import com.my.shared.ServerToClientString;
import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import java.util.List;

public interface SpringService extends RestService {

    @GET
    @Path("controllers/loadTree")
    void loadTree(MethodCallback<ServerToClientString> methodCallback);

    @POST
    @Path("controllers/saveTree")
    void saveTree(@QueryParam("tree") String treeStr, MethodCallback<Void> methodCallback);

    @POST
    @Path("controllers/uploadFile")
    void uploadFile(MethodCallback<List<String>> methodCallback);
}
