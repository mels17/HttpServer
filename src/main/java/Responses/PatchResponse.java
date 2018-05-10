package Responses;

import Entities.*;
import Services.FileOperations;

import java.io.IOException;

public class PatchResponse implements HttpResponseCommand {
    @Override
    public Response process(Request request) {
        try {
            FileOperations.overWriteFile(request.get_path(), request.get_body());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Response(STATUS_CODES.NO_CONTENT, HeaderDetails.STANDARD_HEADER, Constants.NO_CONTENT,
                HeaderDetails.TEXT_CONTENT_TYPE);
    }
}
