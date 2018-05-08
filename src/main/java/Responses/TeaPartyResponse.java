package Responses;

import Entities.HeaderDetails;
import Services.ResponseConstructor;

public class TeaPartyResponse implements HttpResponseCommand {
    @Override
    public StringBuilder process(String request) {
        return new ResponseConstructor(200, "Tea Response",
                "Standard", HeaderDetails.TEXT_CONTENT_TYPE)
                .getResponse();
    }
}
