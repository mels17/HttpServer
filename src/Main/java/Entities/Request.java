package Entities;

public class Request {

    String _path;
    String _requestType;
    String _rangeHeaderValue;
    String _authorizationHeaderValue;
    String _cookieHeaderValue;
    String _queryString;
    String _body;
    String _params;
    String _justPath;

    Constants.Methods requestType;

    public Request(String requestString) {
        _path = getPath(requestString);
        _requestType = getRequestType(requestString);
        _rangeHeaderValue = getHeaderValueThatContainsThisString(requestString, "Range");
        _authorizationHeaderValue = getHeaderValueThatContainsThisString(requestString, "Authorization");
        _cookieHeaderValue = getHeaderValueThatContainsThisString(requestString, "Cookie");
        _queryString = getQueryString(_path);
        _body = getDataFromRequest(requestString);
        _params = getParams(requestString);
        _justPath = getJustPath();


        requestType = setRequestTypeEnum();
    }

    private Constants.Methods setRequestTypeEnum() {
        try {
            return Constants.Methods.valueOf(_requestType);
        } catch(IllegalArgumentException e) {
            return Constants.Methods.BOGUS;
        }
    }

    private String getJustPath() {
        String path = get_path();
        String[] params = path.split("\\?");
        return (params.length == 2) ? params[0] : path;
    }

    private static String getHeaderValueThatContainsThisString(String request, String headerProperty) {
        String[] headers = getRequestHeaders(request);
        for (String header : headers) {
            if (header.contains(headerProperty)) {
                return header.split(":")[1];
            }
        }
        return "-1";
    }

    public static String getQueryString(String path) {
        String[] urlStrings = path.split("\\?");
        return (urlStrings.length == 2) ? urlStrings[1] : "-1";
    }

    private static String[] getRequestHeaders(String request) {
        return request.split("\r\n\r\n")[0].split("\r\n");
    }

    public static String getDataFromRequest(String request) {
        if (request.split("\r\n\r\n").length == 2) {
            return request.split("\r\n\r\n")[1];
        }
        return "";
    }

    public static String getPath(String request) {
        return request.split("\\s")[1];
    }

    public static String getRequestType(String request) {
        return request.split("\\s")[0];
    }

    public static String getParams(String request) {
        String path = request.split("\n")[0].split("\\s")[1];
        if (path.contains("=") && path.contains("&")) {
            return path.split("=")[1].split("&")[0];
        }
        return path.contains("=") ? path.split("=")[1] : "World";
    }

    public String get_path() {
        return _path;
    }

    public String get_rangeHeaderValue() {
        return _rangeHeaderValue;
    }

    public String get_authorizationHeaderValue() {
        return _authorizationHeaderValue;
    }

    public String get_cookieHeaderValue() {
        return _cookieHeaderValue;
    }

    public String get_queryString() {
        return _queryString;
    }

    public String get_body() {
        return _body;
    }

    public String get_params() {
        return _params;
    }

    public String get_justPath() {
        return _justPath;
    }

    public Constants.Methods getRequestType() {
        return requestType;
    }
}
