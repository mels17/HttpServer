package Responses;

import Entities.HeaderDetails;
import Entities.Response;
import Services.RequestParser;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class ParameterDecodeResponse implements HttpResponseCommand {
    @Override
    public Response process(String request) {
        String queryString = RequestParser.getQueryString(request);
        String[] parameters = queryString.split("&");
        String body = "";
        for (String param : parameters) {
            String[] keyValue = param.split("=");
            try {
                body += URLDecoder.decode(keyValue[0], "UTF-8") + " = " + URLDecoder.decode(keyValue[1], "UTF-8") + "\r\n";
            } catch (UnsupportedEncodingException e) {
                return new Response(404, "Standard", e.getMessage(),
                        HeaderDetails.TEXT_CONTENT_TYPE);
            }
        }
        return new Response(200, "Standard", body, HeaderDetails.TEXT_CONTENT_TYPE);
    }
}

