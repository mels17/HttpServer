package Responses;

import Entities.HeaderDetails;
import Entities.Response;
import Services.FileOperations;
import Services.RequestParser;

public class DeleteResponse implements HttpResponseCommand {
    @Override
    public Response process(String request) {
        FileOperations.deleteFile(RequestParser.getPath(request));
        return new Response(200, "Standard", "OK", HeaderDetails.TEXT_CONTENT_TYPE);
    }
}
