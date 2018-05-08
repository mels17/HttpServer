package Responses;

import Services.ResponseConstructor;

public class CoffeeResponse implements HttpResponseCommand {
    @Override
    public StringBuilder process(String request) {
        return new ResponseConstructor(418, "I'm a teapot", "Standard", "text/plain")
                .getResponse();
    }
}
