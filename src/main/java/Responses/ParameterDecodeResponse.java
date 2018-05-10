package Responses;

import Entities.HeaderDetails;
import Entities.Request;
import Entities.Response;
import Entities.STATUS_CODES;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class ParameterDecodeResponse implements HttpResponseCommand {
    @Override
    public Response process(Request request) {
        String[] parameters = request.get_queryString().split("&");
        String body = "";
        for (String param : parameters) {
            String[] keyValue = param.split("=");
            try {
                body += URLDecoder.decode(keyValue[0], "UTF-8") + " = " + URLDecoder.decode(keyValue[1], "UTF-8") + "\r\n";
            } catch (UnsupportedEncodingException e) {
                return new Response(STATUS_CODES.NOT_FOUND, HeaderDetails.STANDARD_HEADER, e.getMessage(),
                        HeaderDetails.TEXT_CONTENT_TYPE);
            }
        }
        return new Response(STATUS_CODES.OK, HeaderDetails.STANDARD_HEADER, body, HeaderDetails.TEXT_CONTENT_TYPE);
    }
}

