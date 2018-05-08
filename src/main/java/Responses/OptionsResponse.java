package Responses;

import Entities.Constants;
import Services.RequestParser;
import Services.ResponseConstructor;

public class OptionsResponse implements HttpResponseCommand {
    @Override
    public StringBuilder process(String request) {
        String path = RequestParser.getPath(request);
        StringBuilder sb =  path.equals("/method_options") ? new ResponseConstructor(200, "OK",
                "Allow: GET, HEAD, POST, OPTIONS, PUT\r\n", Constants.TEXT_CONTENT_TYPE)
                .getResponse()
                : new ResponseConstructor(200, "OK",
                        "Allow:GET, OPTIONS, HEAD\r\n", Constants.TEXT_CONTENT_TYPE)
                        .getResponse();
        return sb;

    }
}
