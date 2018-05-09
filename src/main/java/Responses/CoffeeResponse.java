package Responses;

import Entities.HeaderDetails;
import Entities.Response;

public class CoffeeResponse implements HttpResponseCommand {
    @Override
    public Response process(String request) {
        return new Response( 418, "Standard",
                "I'm a teapot", HeaderDetails.TEXT_CONTENT_TYPE);
    }
}
