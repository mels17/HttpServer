package Responses;

import Entities.HeaderDetails;
import Entities.Request;
import Entities.Response;
import Services.RequestParser;

public class OptionsResponse implements HttpResponseCommand {
    @Override
    public Response process(Request request) {
        return request.get_path().equals("/method_options") ? new Response(200, "Allow: GET, HEAD, POST, OPTIONS, PUT\r\n", "OK",
                HeaderDetails.TEXT_CONTENT_TYPE)
                : new Response(200, "Allow:GET, OPTIONS, HEAD\r\n", "OK",
                HeaderDetails.TEXT_CONTENT_TYPE);
    }
}
