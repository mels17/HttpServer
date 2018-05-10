package Responses;

import Entities.HeaderDetails;
import Entities.Request;
import Entities.Response;
import Entities.STATUS_CODES;

public class GetCookieResponse implements HttpResponseCommand {
    @Override
    public Response process(Request request) {
        return new Response(STATUS_CODES.OK, "Set-Cookie: "
                + request.get_path().split("\\?")[1] + "\r\n", "Eat\r\n", HeaderDetails.TEXT_CONTENT_TYPE);
    }
}
