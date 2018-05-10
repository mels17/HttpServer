package Responses;

import Entities.HeaderDetails;
import Entities.Request;
import Entities.Response;
import Entities.STATUS_CODES;
import Services.RequestParser;
import httpServer.HttpServer;

public class LogResponse implements HttpResponseCommand {
    @Override
    public Response process(Request request) {
        boolean isAuthorized = RequestParser.isAuthorizedToAccessLog(request.get_authorizationHeaderValue());
        if (isAuthorized) {
            return new Response(STATUS_CODES.OK, HeaderDetails.STANDARD_HEADER, getLogs(), HeaderDetails.TEXT_CONTENT_TYPE);
        }
        String additionalHeader = "WWW-Authenticate: Basic\r\n" + "Authorization: Basic\r\n";
        return new Response(STATUS_CODES.UNAUTHORIZED, additionalHeader, "Logs Access Denied", HeaderDetails.TEXT_CONTENT_TYPE);
    }

    private String getLogs() {
        String logResponse = "";
        for (String log : HttpServer.logs) {
            logResponse += (log + "\r\n");
        }
        return logResponse;
    }

}
