import java.text.SimpleDateFormat;
import java.util.Date;

public class ResponseConstructor {

    private StringBuilder _response;
    private int _statusCode;
    private String _responseContent;
    private String _additionalHeaderOrNot;

    public ResponseConstructor(int statusCode, String content, String stringToAddToHeaderOrStandard) {
        _response = new StringBuilder();
        _statusCode = statusCode;
        _responseContent = content;
        _additionalHeaderOrNot = stringToAddToHeaderOrStandard;
    }

    private String getTimeAndDate() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
        return sdf.format(date);
    }

    public StringBuilder getResponse() {
        constructResponseHeader();
        constructResponseBody();
        return _response;
    }

    private StringBuilder constructResponseHeader() {
        _response.append("HTTP/1.1 " + _statusCode + " " + Constants.HTTP_CODES_AND_MESSAGES.get(_statusCode) + "\r\n");
        if(!_additionalHeaderOrNot.equals("Standard")) _response.append(_additionalHeaderOrNot);
        _response.append("Date:" + getTimeAndDate() + "\r\n");
        _response.append("Server:localhost\r\n");
        _response.append("Content-Type: text/html\r\n");
        _response.append("Connection: Closed\r\n\r\n");

        return _response;
    }

    private StringBuilder constructResponseBody() {
        _response.append(_responseContent);
        return _response;
    }
}
