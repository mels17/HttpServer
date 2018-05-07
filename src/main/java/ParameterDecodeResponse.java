import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class ParameterDecodeResponse implements HttpResponseCommand {
    @Override
    public StringBuilder process(String request) {
        String queryString = RequestParser.getQueryString(request);
        String[] parameters = queryString.split("&");
        String body = "";
        for (String param : parameters) {
            String[] keyValue = param.split("=");
            try {
                body += URLDecoder.decode(keyValue[0], "UTF-8") + " = " + URLDecoder.decode(keyValue[1], "UTF-8") + "\r\n";
            } catch (UnsupportedEncodingException e) {
                return new ResponseConstructor(404, e.getMessage(),
                        "Standard", "text/plain").getResponse();
            }
        }
        return new ResponseConstructor(200, body,"Standard", "text/plain")
                .getResponse();
    }
}

