package Responses;

import Entities.Constants;
import Services.FileOperations;
import Services.RequestParser;
import Services.ResponseConstructor;

import java.io.IOException;

public class PatchResponse implements HttpResponseCommand {
    @Override
    public StringBuilder process(String request) {
        String filename = RequestParser.getPath(request);
        try {
            FileOperations.overWriteFile(filename, RequestParser.getDataFromRequest(request));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseConstructor(204, "No Content",
                "Standard", Constants.TEXT_CONTENT_TYPE)
                .constructResponseHeader();
    }
}
