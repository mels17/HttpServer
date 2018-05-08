package Responses;

import Entities.HeaderDetails;
import Services.ResponseConstructor;

public class GetRedirect implements HttpResponseCommand {
    @Override
    public StringBuilder process(String request) {
        return new ResponseConstructor(302, "Found Response",
                "Location: /\r\n", HeaderDetails.TEXT_CONTENT_TYPE).getResponse();
    }
}
