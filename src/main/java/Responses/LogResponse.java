package Responses;

import Entities.Constants;
import Responses.HttpResponseCommand;
import Services.RequestParser;
import Services.ResponseConstructor;
import httpServer.HttpServer;

public class LogResponse implements HttpResponseCommand {
    @Override
    public StringBuilder process(String request) {
        boolean isAuthorized = RequestParser.isAuthorizedToAccessLog(request);
        if (isAuthorized) {
            return new ResponseConstructor(200, getLogs(), "Standard", Constants.TEXT_CONTENT_TYPE).getResponse();
        }
        String additionalHeader = "WWW-Authenticate: Basic\r\n" + "Authorization: Basic\r\n";
        return new ResponseConstructor(401, "Logs Access Denied", additionalHeader, Constants.TEXT_CONTENT_TYPE).getResponse();
    }

    private String getLogs() {
        String logResponse = "";
        for (String log : HttpServer.logs) {
            logResponse += (log + "\r\n");
        }
        return logResponse;
    }

}
