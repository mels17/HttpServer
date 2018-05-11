package Entities;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Response {
    private String _header;
    private String _body;
    private int _statusCode;

    public Response(int statusCode, String additionalHeader, String content, String contentType) {
        _statusCode = statusCode;
        _header = constructResponseHeader(additionalHeader, contentType);
        _body = content;
    }

    private static String getTimeAndDate() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
        return sdf.format(date);
    }

    public String constructResponseHeader(String additionalHeader, String contentType) {
        return getHttpHeaderLine() + getContentTypeHeader(contentType) + getAdditionalHeaderIfNotStandard(additionalHeader)
                + getDateHeader() + HeaderDetails.HEADER_END;
    }

    private String getContentTypeHeader(String contentType) {
        return HeaderDetails.CONTENT_TYPE_HEADER + contentType + "\r\n";
    }
    private String getAdditionalHeaderIfNotStandard(String additionalHeader) {
        return additionalHeader.equals("Standard") ? "" : additionalHeader;
    }

    private String getHttpHeaderLine() {
        return HeaderDetails.HTTP_VERSION + _statusCode + " " + Constants.HTTP_CODES_AND_MESSAGES.get(_statusCode) + "\r\n";
    }

    private String getDateHeader() {
        return HeaderDetails.DATE_HEADER + getTimeAndDate() + "\r\n";
    }

    public String get_header() {
        return _header;
    }

    public String get_body() {
        return _body;
    }
}
