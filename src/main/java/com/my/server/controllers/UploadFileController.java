package com.my.server.controllers;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@EnableWebMvc
public class UploadFileController extends HttpServlet {

    private String fileOut;

    @Override
    @RequestMapping("/parseFile")
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        ServletFileUpload upload = new ServletFileUpload();

        try{
            FileItemIterator iter = upload.getItemIterator(request);

            while (iter.hasNext()) {
                FileItemStream item = iter.next();

                String name = item.getFieldName();
                InputStream stream = item.openStream();

                // Process the input stream
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                int len;
                byte[] buffer = new byte[8192];
                while ((len = stream.read(buffer, 0, buffer.length)) != -1) {
                    out.write(buffer, 0, len);
                }

                int maxFileSize = 10*(1024*1024); //10 megs max
                if (out.size() > maxFileSize) {
                    throw new RuntimeException("File is > than " + maxFileSize);
                }
                fileOut = out.toString();
            }
        }
        catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    @RequestMapping("/uploadFile")
    public List<String> uploadFile() {
        List<String> responseList = new ArrayList<>();
        Pattern pattern = Pattern.compile(".+[\r\n]");
        Matcher matcher = pattern.matcher(fileOut);
        while (matcher.find()) {
            responseList.add(fileOut.substring(matcher.start(), matcher.end() - 1));
        }
        responseList.add(fileOut.substring(fileOut.lastIndexOf("\n") + 1));

        return responseList;
    }
}
