package Responses;

import Entities.Constants;
import Entities.HeaderDetails;
import Entities.Response;
import Services.FileOperations;
import Services.RequestParser;

import java.io.IOException;

public class PatchResponse implements HttpResponseCommand {
    @Override
    public Response process(String request) {
        String filename = RequestParser.getPath(request);
        try {
            FileOperations.overWriteFile(filename, RequestParser.getDataFromRequest(request));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Response(204, HeaderDetails.STANDARD_HEADER, Constants.NO_CONTENT,
                HeaderDetails.TEXT_CONTENT_TYPE);
    }
}
