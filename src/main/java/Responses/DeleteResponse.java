package Responses;

import Entities.HeaderDetails;
import Entities.Request;
import Entities.Response;
import Entities.STATUS_CODES;
import Services.FileOperations;

public class DeleteResponse implements HttpResponseCommand {
    @Override
    public Response process(Request request) {
        FileOperations.deleteFile(request.get_path());
        return new Response(STATUS_CODES.OK, HeaderDetails.STANDARD_HEADER, "OK", HeaderDetails.TEXT_CONTENT_TYPE);
    }
}
