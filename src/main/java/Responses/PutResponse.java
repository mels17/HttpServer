package Responses;

import Entities.HeaderDetails;
import Entities.Request;
import Entities.Response;
import Services.FileOperations;
import Services.RequestParser;

import java.io.IOException;

public class PutResponse implements HttpResponseCommand {
    @Override
    public Response process(Request request) {
        if (!request.get_path().equals("/")) {
            if (request.get_body().equals("")) return new Response(405, HeaderDetails.STANDARD_HEADER,
                    "Method Not Allowed", HeaderDetails.TEXT_CONTENT_TYPE);
            try {
                FileOperations.overWriteFile(request.get_path(), request.get_body());
            } catch (IOException e) {
                return new Response(404, "Not Found", HeaderDetails.STANDARD_HEADER,
                        HeaderDetails.TEXT_CONTENT_TYPE);
            }
        }
        return new Response(200, HeaderDetails.STANDARD_HEADER, "OK", HeaderDetails.TEXT_CONTENT_TYPE);
    }
}
