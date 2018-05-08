package Responses;

import Entities.ByteRange;
import Entities.Constants;
import Entities.HeaderDetails;
import Services.FileOperations;
import Services.RequestParser;
import Services.ResponseConstructor;

import java.io.File;

public class PartialContentResponse implements HttpResponseCommand {
    @Override
    public StringBuilder process(String request){
        int statusCode = 206;
        String additionalHeader = "";
        byte[] content = new byte[0];
        String path = RequestParser.getPath(request);
        Long bytes = new File(Constants.PUBLIC_DIR_PATH + path).length();

        ByteRange range = RequestParser.getContentRangeFromHeader(request, bytes);

        Long start = range.get_start();
        Long end = range.get_end();

        if(start != Long.valueOf(-1) && end != Long.valueOf("-1")) {
            content = FileOperations.getPartialContentFromFile(range, path);
            if(start == Long.valueOf(0) && end == Long.valueOf(bytes-1)){
                statusCode = 416;
                additionalHeader = "Content-Range: bytes */" + bytes.toString() + "\r\n";
            } else {
                additionalHeader = "Content-Range: bytes " + start.toString() + "-" + end.toString() + "/" + bytes.toString() + "\r\n";
            }
        }

        return new ResponseConstructor(statusCode, new String(content), additionalHeader, HeaderDetails.TEXT_CONTENT_TYPE).getResponse();
    }
}
