package Responses;

import Entities.HeaderDetails;
import Entities.Response;

public class TeaPartyResponse implements HttpResponseCommand {
    @Override
    public Response process(String request) {
        return new Response(200, HeaderDetails.STANDARD_HEADER, "Tea Response",
                HeaderDetails.TEXT_CONTENT_TYPE);
    }
}
