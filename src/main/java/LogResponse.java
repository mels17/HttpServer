public class LogResponse implements HttpResponseCommand {
    @Override
    public StringBuilder process(String request) {
        boolean isAuthorized = RequestParser.isAuthorizedToAccessLog(request);
        if (isAuthorized) {
            return new ResponseConstructor(200, getLogs(), "Standard").getResponse();
        }
        String additionalHeader = "WWW-Authenticate: Basic\r\n" + "Authorization: Basic\r\n";
        return new ResponseConstructor(401, "Logs Access Denied", additionalHeader).getResponse();
    }

    private String getLogs() {
        String logResponse = "";
        for (String log : HttpServer.logs) {
            logResponse += (log + "\r\n");
        }
        return logResponse;
    }

}
