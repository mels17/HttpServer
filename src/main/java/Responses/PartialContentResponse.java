package Responses;

import Entities.ByteRange;
import Entities.Constants;
import Entities.HeaderDetails;
import Entities.ResponseObject;
import Services.FileOperations;
import Services.RequestParser;
import Services.ResponseConstructor;

import java.io.File;

public class PartialContentResponse implements HttpResponseCommand {
    @Override
    public StringBuilder process(String request){
        String path = RequestParser.getPath(request);
        Long bytes = new File(Constants.PUBLIC_DIR_PATH + path).length();
        ByteRange range = RequestParser.getResponseContentRange(request, bytes);

        return new ResponseConstructor(getPartialResponseObject(range, bytes, path)).getResponse();
    }

    private ResponseObject getPartialResponseObject(ByteRange byteRange, Long bytes, String path) {
        ResponseObject responseObject = new ResponseObject();
        responseObject._statusCode = 416;
        responseObject._contentType = HeaderDetails.TEXT_CONTENT_TYPE;
        byte[] content;
        Long start = byteRange.get_start();
        Long end = byteRange.get_end();
        if(rangeIsValid(start, end)) {
            content = FileOperations.getPartialContentFromFile(byteRange, path);
            responseObject = getResponseObjectWithStatusCodeAndAdditionalHeader(bytes, responseObject, start, end);
            responseObject._body = new String(content);
        }
        return responseObject;
    }

    private boolean rangeIsValid(Long start, Long end) {
        return !start.equals(Long.valueOf(-1)) && !end.equals(Long.valueOf(-1));
    }

    private ResponseObject getResponseObjectWithStatusCodeAndAdditionalHeader(Long bytes, ResponseObject responseObject, Long start, Long end) {
        if (rangeIsSpanningOverTheEntireFile(bytes, start, end)) {
            responseObject._additionalHeaderOrStandard = "Content-Range: bytes */" + bytes.toString() + "\r\n";
        } else {
            responseObject._statusCode = 206;
            responseObject._additionalHeaderOrStandard = "Content-Range: bytes " + start.toString() + "-" + end.toString() + "/" + bytes.toString() + "\r\n";
        }
        return responseObject;
    }

    private boolean rangeIsSpanningOverTheEntireFile(Long bytes, Long start, Long end) {
        return start.equals(Long.valueOf(0)) && end.equals(Long.valueOf(bytes-1));
    }
}
