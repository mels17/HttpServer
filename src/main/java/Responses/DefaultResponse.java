package Responses;

import Entities.Constants;
import Services.ResponseConstructor;

public class DefaultResponse implements HttpResponseCommand {
    @Override
    public StringBuilder process(String request) {
        return new ResponseConstructor(405, "",
                "Standard", Constants.TEXT_CONTENT_TYPE)
                .getResponse();
    }
}
