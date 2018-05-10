package Responses;

import Entities.HeaderDetails;
import Entities.Request;
import Entities.Response;
import Services.FileOperations;
import Services.RequestParser;

import java.io.IOException;

public class GetFileContentResponse implements HttpResponseCommand {
    @Override
    public Response process(Request request) {
        try {
            return new Response(200, "Standard", FileOperations.getTextFileContents(request.get_path()) + "\r\n",
                    HeaderDetails.TEXT_CONTENT_TYPE);
        } catch (IOException e) {
            e.printStackTrace();
            return new Response(404, "Standard", "Not Found", HeaderDetails.TEXT_CONTENT_TYPE);
        }
    }
}
