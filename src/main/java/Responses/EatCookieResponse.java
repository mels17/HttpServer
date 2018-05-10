package Responses;

import Entities.HeaderDetails;
import Entities.Request;
import Entities.Response;
import Services.RequestParser;

public class EatCookieResponse implements HttpResponseCommand {
    @Override
    public Response process(Request request) {
        String cookie = request.get_cookieHeaderValue();
        String body = "mmmm" + " " + cookie.split("=")[1] + "\r\n";
        return new Response(200, "Set-Cookie: "
                + cookie + "\r\n", body, HeaderDetails.TEXT_CONTENT_TYPE);
    }
}
