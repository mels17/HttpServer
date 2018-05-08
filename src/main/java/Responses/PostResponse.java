package Responses;

import Entities.Constants;
import Services.RequestParser;
import Services.ResponseConstructor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class PostResponse implements HttpResponseCommand {
    @Override
    public StringBuilder process(String request) {
        String path = RequestParser.getPath(request);
        String content = RequestParser.getDataFromRequest(request);
        if (content.equals("")) return new ResponseConstructor(405, "Method Not Allowed",
                "Standard", "text/plain").getResponse();
        if (path.equals("/cat-form")) {
            String fileName = content.split("=")[0];
            try {
                File file = new File(Constants.PUBLIC_DIR_PATH + path + "/" + fileName);
                BufferedWriter output = new BufferedWriter(new FileWriter(file));
                output.write(content);
                output.flush();
                output.close();
                return ResponseConstructor.getPostResponseHeaderWithRedirect(fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new ResponseConstructor(200, "OK",
                "Standard", Constants.TEXT_CONTENT_TYPE)
                .getResponse();
    }
}
