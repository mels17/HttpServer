package Responses;

import Services.FileOperations;
import Services.RequestParser;
import Services.ResponseConstructor;

import java.io.IOException;

public class PutResponse implements HttpResponseCommand {
    @Override
    public StringBuilder process(String request) {
        String filename = RequestParser.getPath(request);
        if (!filename.equals("/")) {
            String content = RequestParser.getDataFromRequest(request);
            if (content.equals("")) return new ResponseConstructor(405, "Method Not Allowed",
                    "Standard", "text/plain").getResponse();
            try {
                FileOperations.overWriteFile(filename, content);
            } catch (IOException e) {
                return new ResponseConstructor(404, "Not Found",
                        "Standard", "text/plain")
                        .getResponse();
            }
        }
        return new ResponseConstructor(200, "OK",
                "Standard", "text/plain")
                .getResponse();
    }
}
