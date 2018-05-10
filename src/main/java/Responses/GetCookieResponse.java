package Responses;

import Entities.HeaderDetails;
import Entities.Request;
import Entities.Response;
import Services.RequestParser;

public class GetCookieResponse implements HttpResponseCommand {
    @Override
    public Response process(Request request) {
        return new Response(200, "Set-Cookie: "
                + request.get_path().split("\\?")[1] + "\r\n", "Eat\r\n", HeaderDetails.TEXT_CONTENT_TYPE);
    }
}
