package Responses;

import Services.RequestParser;
import Services.ResponseConstructor;
import httpServer.HttpServer;

public class GetCookieResponse implements HttpResponseCommand {
    @Override
    public StringBuilder process(String request) {
        String path = RequestParser.getPath(request);
        HttpServer.cookies.add(path.split("\\?")[1]);
        return new ResponseConstructor(200, "Eat\r\n", "Set-Cookie: "
                + path.split("\\?")[1] + "\r\n", "text/plain").getResponse();
    }
}
