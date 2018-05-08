package Responses;

import Entities.Constants;
import Services.ResponseConstructor;

public class GetRedirect implements HttpResponseCommand {
    @Override
    public StringBuilder process(String request) {
        return new ResponseConstructor(302, "Found Response",
                "Location: /\r\n", Constants.TEXT_CONTENT_TYPE).getResponse();
    }
}
