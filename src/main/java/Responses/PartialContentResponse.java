package Responses;

import Entities.*;
import Services.FileOperations;
import Services.RequestParser;

import java.io.File;

public class PartialContentResponse implements HttpResponseCommand {
    @Override
    public Response process(Request request) {
        Long bytes = new File(Constants.PUBLIC_DIR_PATH + request.get_path()).length();
        ByteRange range = RequestParser.getResponseContentRange(request.get_rangeHeaderValue(), bytes);

        return getPartialResponseObject(range, bytes, request.get_path());
    }

    private Response getPartialResponseObject(ByteRange byteRange, Long bytes, String path) {
        Long start = byteRange.get_start();
        Long end = byteRange.get_end();
        if (rangeIsValid(start, end)) {
            return getResponseForValidRange(new String(FileOperations.getPartialContentFromFile(byteRange, path)), bytes, start, end);
        }
        return new Response(416, "Standard", "", HeaderDetails.TEXT_CONTENT_TYPE);
    }

    private Response getResponseForValidRange(String content, Long bytes, Long start, Long end) {
        if (!rangeIsSpanningOverTheEntireFile(bytes, start, end)) {
            return new Response(206, "Content-Range: bytes " + start.toString()
                    + "-" + end.toString() + "/" + bytes.toString() + "\r\n", content, HeaderDetails.TEXT_CONTENT_TYPE);
        }
        return new Response(416, "Content-Range: bytes */" + bytes.toString() + "\r\n",
                content, HeaderDetails.TEXT_CONTENT_TYPE);
    }

    private boolean rangeIsValid(Long start, Long end) {
        return !start.equals(Long.valueOf(-1)) && !end.equals(Long.valueOf(-1));
    }

    private boolean rangeIsSpanningOverTheEntireFile(Long bytes, Long start, Long end) {
        return start.equals(Long.valueOf(0)) && end.equals(Long.valueOf(bytes - 1));
    }
}
