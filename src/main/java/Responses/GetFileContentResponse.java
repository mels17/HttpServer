package Responses;

import Entities.HeaderDetails;
import Services.FileOperations;
import Services.RequestParser;
import Services.ResponseConstructor;

import java.io.IOException;

public class GetFileContentResponse implements HttpResponseCommand {
    @Override
    public StringBuilder process(String request) {
        String filename = RequestParser.getPath(request);
        String additionalHeaders = "Standard";
        int statusCode = 200;

        if (RequestParser.containsContentRangeInRequestHeader(request)) {
            additionalHeaders = ResponseConstructor.getAdditionalHeaderForPartialResponse(request);
            statusCode = 206;
        }

        String body = "";
        try {
            body = FileOperations.getTextFileContents(filename) + "\r\n";
            return new ResponseConstructor(statusCode, body, additionalHeaders, HeaderDetails.TEXT_CONTENT_TYPE).getResponse();
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseConstructor(404, "Not Found", "Standard",
                    "text/plain").getResponse();
        }
    }
}
