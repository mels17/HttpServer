package Responses;

import Services.FileOperations;
import Services.RequestParser;
import Services.ResponseConstructor;

public class DeleteResponse implements HttpResponseCommand {
    @Override
    public StringBuilder process(String request) {
        String path = RequestParser.getPath(request);
        FileOperations.deleteFile(path);
        return new ResponseConstructor(200, "OK", "Standard", "text/plain").getResponse();
    }
}
