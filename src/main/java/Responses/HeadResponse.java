package Responses;

import Entities.HeaderDetails;
import Entities.Response;
import Services.RequestParser;

public class HeadResponse implements HttpResponseCommand {
    @Override
    public Response process(String request) {
        return RequestParser.getPath(request).equals("/") ?
                new Response(200, "Standard", "Response: OK", HeaderDetails.TEXT_CONTENT_TYPE)
                : new Response(404, "Standard", "Response: Not Found", HeaderDetails.TEXT_CONTENT_TYPE);
    }
}
