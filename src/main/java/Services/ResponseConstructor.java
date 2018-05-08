package Services;

import Entities.ByteRange;
import Entities.Constants;
import Entities.HeaderDetails;
import Entities.ResponseObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ResponseConstructor {

    private StringBuilder _response;
    private int _statusCode;
    private String _responseContent;
    private String _additionalHeaderOrNot;
    private String _contentType;

    public ResponseConstructor(int statusCode, String content, String stringToAddToHeaderOrStandard, String contentType) {
        _response = new StringBuilder();
        _statusCode = statusCode;
        _responseContent = content;
        _additionalHeaderOrNot = stringToAddToHeaderOrStandard;
        _contentType = contentType;
    }

    public ResponseConstructor(ResponseObject responseObject) {
        _response = new StringBuilder();
        _statusCode = responseObject._statusCode;
        _additionalHeaderOrNot = responseObject._additionalHeaderOrStandard;
        _responseContent = responseObject._body;
        _contentType = responseObject._contentType;
    }

    private static String getTimeAndDate() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
        return sdf.format(date);
    }

    public StringBuilder getResponse() {
        constructResponseHeader();
        constructResponseBody();
        return _response;
    }

    public StringBuilder constructResponseHeader() {
        _response.append(HeaderDetails.HTTP_VERSION + _statusCode + " " + Constants.HTTP_CODES_AND_MESSAGES.get(_statusCode) + "\r\n");
        if(!_additionalHeaderOrNot.equals("Standard")) _response.append(_additionalHeaderOrNot);
        _response.append("Content-Type: " + _contentType + "\r\n");
        _response.append("Date:" + getTimeAndDate() + "\r\n");
        _response.append("Server:localhost\r\n");
        _response.append("Connection: Closed\r\n\r\n");

        return _response;
    }

    private StringBuilder constructResponseBody() {
        _response.append(_responseContent);
        return _response;
    }

    public static StringBuilder getPostResponseHeaderWithRedirect(String newFile) {
        StringBuilder sb = new StringBuilder();
        sb.append(HeaderDetails.HTTP_VERSION + 201 + " " + "Created" + "\r\n");
        sb.append("Location: /cat-form/" + newFile + "\r\n\r\n");

        return sb;
    }

    public static String constructImageHeader(String extension) {
        return HeaderDetails.HTTP_VERSION + 200 + " " + Constants.HTTP_CODES_AND_MESSAGES.get(200) + "\r\n"
                + "Content-Type: image/" + extension + "\r\n"
                + "Date:" + getTimeAndDate() + "\r\n"
                + "Server:localhost\r\n"
                + "Connection: Closed\r\n\r\n";
    }

    public static String getAdditionalHeaderForPartialResponse(String request) {
        ByteRange br = RequestParser.getContentRange(request);
        return "Accept-Ranges: bytes" + "Content-Range: bytes " + br.get_start() + "-" + br.get_end() + "\r\n\r\n";
    }

}
