package Responses;

import Entities.HeaderDetails;
import Entities.Request;
import Entities.Response;
import Entities.STATUS_CODES;

public class TeaPartyResponse implements HttpResponseCommand {
    @Override
    public Response process(Request request) {
        return new Response(STATUS_CODES.OK, HeaderDetails.STANDARD_HEADER, "Tea Response",
                HeaderDetails.TEXT_CONTENT_TYPE);
    }
}
