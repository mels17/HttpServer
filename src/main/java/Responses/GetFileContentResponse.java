package Responses;

import Entities.HeaderDetails;
import Entities.Response;
import Services.FileOperations;
import Services.RequestParser;

import java.io.IOException;

public class GetFileContentResponse implements HttpResponseCommand {
    @Override
    public Response process(String request) {
        String filename = RequestParser.getPath(request);
        try {
            return new Response(200, "Standard", FileOperations.getTextFileContents(filename) + "\r\n",
                    HeaderDetails.TEXT_CONTENT_TYPE);
        } catch (IOException e) {
            e.printStackTrace();
            return new Response(404, "Standard", "Not Found", HeaderDetails.TEXT_CONTENT_TYPE);
        }
    }
}
