package Responses;

import Entities.Constants;
import Services.FileOperations;
import Services.RequestParser;
import Services.ResponseConstructor;

public class DeleteResponse implements HttpResponseCommand {
    @Override
    public StringBuilder process(String request) {
        FileOperations.deleteFile(RequestParser.getPath(request));
        return new ResponseConstructor(200, "OK", "Standard",
                Constants.TEXT_CONTENT_TYPE)
                .getResponse();
    }
}
