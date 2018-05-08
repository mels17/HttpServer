package Responses;

import Entities.HeaderDetails;
import Services.RequestParser;
import Services.ResponseConstructor;

public class GetCookieResponse implements HttpResponseCommand {
    @Override
    public StringBuilder process(String request) {
        String path = RequestParser.getPath(request);
        return new ResponseConstructor(200, "Eat\r\n", "Set-Cookie: "
                + path.split("\\?")[1] + "\r\n", HeaderDetails.TEXT_CONTENT_TYPE).getResponse();
    }
}
