package Responses;

import Entities.HeaderDetails;
import Entities.Request;
import Entities.Response;
import Entities.STATUS_CODES;

public class CoffeeResponse implements HttpResponseCommand {
    @Override
    public Response process(Request request) {
        return new Response( STATUS_CODES.TEAPOT, HeaderDetails.STANDARD_HEADER,"I'm a teapot",
                HeaderDetails.TEXT_CONTENT_TYPE);
    }
}
