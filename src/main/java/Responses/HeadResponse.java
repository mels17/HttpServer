package Responses;

import Entities.HeaderDetails;
import Services.RequestParser;
import Services.ResponseConstructor;

public class HeadResponse implements HttpResponseCommand {
    @Override
    public StringBuilder process(String request) {
        ResponseConstructor rc = RequestParser.getPath(request).equals("/") ?
                new ResponseConstructor(200, "Response: OK", "Standard", HeaderDetails.TEXT_CONTENT_TYPE)
                : new ResponseConstructor(404, "Response: Not Found", "Standard", HeaderDetails.TEXT_CONTENT_TYPE);
        return rc.getResponse();
    }
}
