package Responses;

import Entities.HeaderDetails;
import Entities.Request;
import Entities.Response;
import Entities.STATUS_CODES;

public class DefaultResponse implements HttpResponseCommand {
    @Override
    public Response process(Request request) {
        return new Response(STATUS_CODES.NOT_ALLOWED, HeaderDetails.STANDARD_HEADER, "No content", HeaderDetails.TEXT_CONTENT_TYPE);
    }
}
