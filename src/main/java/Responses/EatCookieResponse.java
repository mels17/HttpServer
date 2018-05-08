package Responses;

import Entities.Constants;
import Services.RequestParser;
import Services.ResponseConstructor;
import httpServer.HttpServer;

public class EatCookieResponse implements HttpResponseCommand {
    @Override
    public StringBuilder process(String request) {
        String cookie = RequestParser.getCookieFromRequest(request);
        String body = "mmmm" + " " + cookie.split("=")[1] + "\r\n";
        return new ResponseConstructor(200, body, "Set-Cookie: "
                + cookie + "\r\n", Constants.TEXT_CONTENT_TYPE).getResponse();
    }
}
