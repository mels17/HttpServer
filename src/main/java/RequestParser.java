import java.io.BufferedReader;
import java.io.IOException;
import java.util.Base64;

public class RequestParser {

    public static final String USERNAME = "admin";
    public static final String PASSWORD = "hunter2";

    private static String[] getRequestHeaders(String request) {
        return request.split("\r\n\r\n")[0].split("\r\n");
    }

    public static String getRequestType(String request) {
        return request.split("\\s")[0];
    }

    public static String getQueryString(String request) {
        String[] urlStrings = request.split("\\?");
        return (urlStrings.length == 2) ? urlStrings[1].split("\\s")[0] : "";
    }

    public static String getFileExtension(String request) {
        return getPath(request).substring(1).split("\\.")[1];
    }

    public static String getParams(String request) {
        String path = request.split("\n")[0].split("\\s")[1];
        if (path.contains("=") && path.contains("&")) {
            return path.split("=")[1].split("&")[0];
        }
        return path.contains("=") ? path.split("=")[1] : "World";
    }

    public static String getPath(String request) {
        return request.split("\\s")[1];
    }

    public static String getRequestStringFromBufferedReader(BufferedReader request) throws IOException {
        StringBuilder requestString = new StringBuilder();
        int inputChar;
        do {
            inputChar = request.read();
            requestString.append(Character.toChars(inputChar));
        }
        while (request.ready());

        System.out.println(requestString);

        return requestString.toString();
    }

    private static String getHeaderThatContainsThisString(String request, String headerProperty) {
        String[] headers = getRequestHeaders(request);
        for (String header : headers) {
            if (header.contains("Authorization")) {
                return header;
            }
        }
        return "-1";
    }

    private static String[] getUsernameAndPasswordFromAuthorizationHeader(String request) {
        String header = getHeaderThatContainsThisString(request, "Authorization");
        String authHeader = (header.equals("-1"))? "username:password" : new String(Base64.getDecoder().decode(header.split("\\s")[2]));
        return authHeader.split(":");

    }

    public static boolean isAuthorizedToAccessLog(String request) {
        String[] usernameAndPassword = getUsernameAndPasswordFromAuthorizationHeader(request);
        return usernameAndPassword[0].equals(USERNAME) && usernameAndPassword[1].equals(PASSWORD);
    }

    public static String getDataFromRequest(String req) {
        if (req.split("\r\n\r\n").length == 2) {
            return req.split("\r\n\r\n")[1];
        }
        return "";
    }

}
