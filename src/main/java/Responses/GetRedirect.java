package Responses;

import Services.ResponseConstructor;

public class GetRedirect implements HttpResponseCommand {
    @Override
    public StringBuilder process(String request) {
        return new ResponseConstructor(302, "Found Response",
                "Location: /\r\n", "text/plain").getResponse();
    }
}
