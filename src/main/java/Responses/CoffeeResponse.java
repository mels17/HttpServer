package Responses;

import Entities.HeaderDetails;
import Services.ResponseConstructor;

public class CoffeeResponse implements HttpResponseCommand {
    @Override
    public StringBuilder process(String request) {
        return new ResponseConstructor(418, "I'm a teapot",
                "Standard", HeaderDetails.TEXT_CONTENT_TYPE)
                .getResponse();
    }
}
