package Responses;

import Entities.Constants;
import Entities.HeaderDetails;
import Entities.Request;
import Entities.Response;
import Services.FileOperations;
import Services.RequestParser;

import java.io.IOException;

public class PatchResponse implements HttpResponseCommand {
    @Override
    public Response process(Request request) {
        try {
            FileOperations.overWriteFile(request.get_path(), request.get_body());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Response(204, HeaderDetails.STANDARD_HEADER, Constants.NO_CONTENT,
                HeaderDetails.TEXT_CONTENT_TYPE);
    }
}
