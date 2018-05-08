package Responses;

import Entities.HeaderDetails;
import Services.RequestParser;
import Services.ResponseConstructor;

public class EatCookieResponse implements HttpResponseCommand {
    @Override
    public StringBuilder process(String request) {
        String cookie = RequestParser.getCookieFromRequest(request);
        String body = "mmmm" + " " + cookie.split("=")[1] + "\r\n";
        return new ResponseConstructor(200, body, "Set-Cookie: "
                + cookie + "\r\n", HeaderDetails.TEXT_CONTENT_TYPE).getResponse();
    }
}
