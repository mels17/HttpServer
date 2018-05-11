package Responses;

import Entities.HeaderDetails;
import Entities.Request;
import Entities.Response;
import Entities.STATUS_CODES;
import Services.FileOperations;

import java.io.IOException;

public class PutResponse implements HttpResponseCommand {
    @Override
    public Response process(Request request) {
        if (!request.get_path().equals("/")) {
            if (request.get_body().equals("")) return new Response(STATUS_CODES.NOT_ALLOWED, HeaderDetails.STANDARD_HEADER,
                    "Method Not Allowed", HeaderDetails.TEXT_CONTENT_TYPE);
            try {
                FileOperations.overWriteFile(request.get_path(), request.get_body());
            } catch (IOException e) {
                return new Response(STATUS_CODES.NOT_FOUND, "Not Found", HeaderDetails.STANDARD_HEADER,
                        HeaderDetails.TEXT_CONTENT_TYPE);
            }
        }
        return new Response(STATUS_CODES.OK, HeaderDetails.STANDARD_HEADER, "OK", HeaderDetails.TEXT_CONTENT_TYPE);
    }
}
