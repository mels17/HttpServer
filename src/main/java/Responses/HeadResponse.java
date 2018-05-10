package Responses;

import Entities.HeaderDetails;
import Entities.Request;
import Entities.Response;
import Services.RequestParser;

public class HeadResponse implements HttpResponseCommand {
    @Override
    public Response process(Request request) {
        return request.get_path().equals("/") ?
                new Response(200, "Standard", "Response: OK", HeaderDetails.TEXT_CONTENT_TYPE)
                : new Response(404, "Standard", "Response: Not Found", HeaderDetails.TEXT_CONTENT_TYPE);
    }
}
