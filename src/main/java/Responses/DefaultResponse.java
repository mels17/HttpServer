package Responses;

import Entities.HeaderDetails;
import Services.ResponseConstructor;

public class DefaultResponse implements HttpResponseCommand {
    @Override
    public StringBuilder process(String request) {
        return new ResponseConstructor(405, "",
                "Standard", HeaderDetails.TEXT_CONTENT_TYPE)
                .getResponse();
    }
}
