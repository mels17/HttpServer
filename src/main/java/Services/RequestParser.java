package Services;

import Entities.ByteRange;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Base64;

public class RequestParser {

    public static final String USERNAME = "admin";
    public static final String PASSWORD = "hunter2";

    private static String[] getRequestHeaders(String request) {
        return request.split("\r\n\r\n")[0].split("\r\n");
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

    private static String getHeaderThatContainsThisString(String request, String headerProperty) {
        String[] headers = getRequestHeaders(request);
        for (String header : headers) {
            if (header.contains(headerProperty)) {
                return header;
            }
        }
        return "-1";
    }

    private static String[] getUsernameAndPasswordFromAuthorizationHeader(String request) {
        String header = getHeaderThatContainsThisString(request, "Authorization");
        String authHeader = (header.equals("-1")) ? "username:password" : new String(Base64.getDecoder().decode(header.split("\\s")[2]));
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

    public static String getRequestString(BufferedReader request) throws IOException {
        StringBuilder r = new StringBuilder();
        int inputChar;
        do {
            inputChar = request.read();
            r.append(Character.toChars(inputChar));
        }
        while (request.ready());
        System.out.println(r);
        return r.toString();
    }

    public static ByteRange getContentRange(String req) {
        String[] reqContent = req.split("\r\n");
        ByteRange br = new ByteRange();
        for (String line : reqContent) {
            if (line.startsWith("Range")) {
                String[] range = line.split("\\s")[2].split("/")[0].split("-");
                br.setStart(Long.parseLong(range[0]));
                br.setEnd(Long.parseLong(range[1]));
            }
        }
        return br;
    }

    public static ByteRange getResponseContentRange(String request, Long bytes) {
        ByteRange byteRange = getInvalidatedRangeFromHeader(request, bytes);
        return isValidRange(byteRange, bytes) ? byteRange
                : byteRange.setStartAndEnd(Long.valueOf(0), Long.valueOf(bytes - 1));
    }


    private static ByteRange getInvalidatedRangeFromHeader(String request, Long totalNoOfBytes) {
        String rangeHeader = getHeaderThatContainsThisString(request, "Range");
        if (rangeHeader.equals(-1)) return new ByteRange();
        String[] range = rangeHeader.split("\\s")[1].split("=")[1].split("-");
        return (range.length == 1) ? new ByteRange(Long.valueOf(range[0]), totalNoOfBytes - 1)
                : (getRangeWhenBothStartAndEndAreGiven(range));
    }

    private static ByteRange getRangeWhenBothStartAndEndAreGiven(String[] range) {
        ByteRange byteRange = new ByteRange();
        if (range[0].equals("")) {
            byteRange.setStartAndEnd(Long.valueOf(71), Long.valueOf(Integer.parseInt(range[1]) + 70));
        } else if (!range[0].isEmpty() && !range[1].isEmpty()) {
            byteRange.setStartAndEnd(Long.valueOf(range[0]), Long.valueOf(range[1]));
        }
        return byteRange;
    }


    private static boolean isValidRange(ByteRange byteRange, Long totalBytes) {
        Long start = byteRange.get_start();
        Long end = byteRange.get_end();
        return start >= 0 && start < totalBytes && end > 0 && end <= totalBytes && start <= end;
    }


    public static String getCookieFromRequest(String request) {
        String header = getHeaderThatContainsThisString(request, "Cookie");
        return header.split("\\s")[1];
    }

}
