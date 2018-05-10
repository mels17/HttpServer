package Responses;

import Entities.HeaderDetails;
import Entities.Request;
import Entities.Response;
import Services.FileOperations;
import Services.RequestParser;

public class DeleteResponse implements HttpResponseCommand {
    @Override
    public Response process(Request request) {
        FileOperations.deleteFile(request.get_path());
        return new Response(200, "Standard", "OK", HeaderDetails.TEXT_CONTENT_TYPE);
    }
}
