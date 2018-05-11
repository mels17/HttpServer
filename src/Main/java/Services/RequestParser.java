package Services;

import Entities.ByteRange;
import Entities.Request;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Base64;

public class RequestParser {

    public static final String USERNAME = "admin";
    public static final String PASSWORD = "hunter2";

    public static String getFileExtension(String path) {
        return path.substring(1).split("\\.")[1];
    }

    private static String[] getUsernameAndPasswordFromAuthorizationHeader(String authorizationHeader) {
        String authHeader = new String(Base64.getDecoder().decode(authorizationHeader.split("\\s")[2]));
        return authHeader.split(":");

    }

    public static boolean isAuthorizedToAccessLog(String authorizationHeaderValue) {

        if (authorizationHeaderValue.equals("-1")) return false;
        String[] usernameAndPassword = getUsernameAndPasswordFromAuthorizationHeader(authorizationHeaderValue);
        return usernameAndPassword[0].equals(USERNAME) && usernameAndPassword[1].equals(PASSWORD);
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

    public static ByteRange getResponseContentRange(String rangeHeaderValue, Long bytes) {
        ByteRange byteRange = getInvalidatedRangeFromHeader(rangeHeaderValue, bytes);
        return isValidRange(byteRange, bytes) ? byteRange
                : byteRange.setStartAndEnd(Long.valueOf(0), Long.valueOf(bytes - 1));
    }


    private static ByteRange getInvalidatedRangeFromHeader(String rangeHeaderValue, Long totalNoOfBytes) {
        if (rangeHeaderValue.equals(-1)) return new ByteRange();
        String[] range = rangeHeaderValue.split("=")[1].split("-");
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

}
