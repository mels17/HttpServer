package Responses;

import Entities.HeaderDetails;
import Services.FileOperations;
import Services.RequestParser;
import Services.ResponseConstructor;

import java.io.IOException;

public class GetFileContentResponse implements HttpResponseCommand {
    @Override
    public StringBuilder process(String request) {
        String filename = RequestParser.getPath(request);
        try {
            return new ResponseConstructor(200, FileOperations.getTextFileContents(filename) + "\r\n",
                    "Standard", HeaderDetails.TEXT_CONTENT_TYPE).getResponse();
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseConstructor(404, "Not Found", "Standard",
                    "text/plain").getResponse();
        }
    }
}
