package Responses;

import Entities.HeaderDetails;
import Entities.Request;
import Entities.Response;

public class GetRedirect implements HttpResponseCommand {
    @Override
    public Response process(Request request) {
        return new Response(302, "Location: /\r\n", "Found Response",
                HeaderDetails.TEXT_CONTENT_TYPE);
    }
}
