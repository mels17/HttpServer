package Responses;

import Entities.HeaderDetails;
import Entities.Request;
import Entities.Response;
import Entities.STATUS_CODES;

public class RedirectResponse implements HttpResponseCommand {
    @Override
    public Response process(Request request) {
        return new Response(STATUS_CODES.FOUND, "Location: /\r\n", "Found Response",
                HeaderDetails.TEXT_CONTENT_TYPE);
    }
}
