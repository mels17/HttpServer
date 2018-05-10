package Responses;

import Entities.HeaderDetails;
import Entities.Request;
import Entities.Response;

public class DefaultResponse implements HttpResponseCommand {
    @Override
    public Response process(Request request) {
        return new Response(405, "Standard", "", HeaderDetails.TEXT_CONTENT_TYPE);
    }
}
