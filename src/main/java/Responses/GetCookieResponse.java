package Responses;

import Entities.HeaderDetails;
import Entities.Response;
import Services.RequestParser;

public class GetCookieResponse implements HttpResponseCommand {
    @Override
    public Response process(String request) {
        String path = RequestParser.getPath(request);
        return new Response(200, "Set-Cookie: "
                + path.split("\\?")[1] + "\r\n", "Eat\r\n", HeaderDetails.TEXT_CONTENT_TYPE);
    }
}
