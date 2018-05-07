import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class ParameterDecodeResponse implements HttpResponseCommand {
    @Override
    public StringBuilder process(String request) {
        String queryString = RequestParser.getQueryString(request);
        String[] parameters = queryString.split("&");
        for (String param : parameters) {
            String[] keyValue = param.split("=");
            try {
                return new ResponseConstructor(200, URLDecoder.decode(keyValue[0],
                        "UTF-8") + " = " + URLDecoder.decode(keyValue[1], "UTF-8") + "\r\n",
                        "Standard").getResponse();
            } catch (UnsupportedEncodingException e) {
                e.getStackTrace();
            }
        }
        return new ResponseConstructor(404, "Not Found\r\n",
                "Standard").getResponse();
    }
}
