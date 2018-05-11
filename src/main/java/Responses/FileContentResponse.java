package Responses;

import Entities.HeaderDetails;
import Entities.Request;
import Entities.Response;
import Entities.STATUS_CODES;
import Services.FileOperations;

import java.io.IOException;

public class FileContentResponse implements HttpResponseCommand {
    @Override
    public Response process(Request request) {
        if (!request.get_rangeHeaderValue().equals("-1")) return new PartialContentResponse().process(request);

        try {
            return new Response(STATUS_CODES.OK, HeaderDetails.STANDARD_HEADER,
                    FileOperations.getTextFileContents(request.get_path()) + "\r\n",
                    HeaderDetails.TEXT_CONTENT_TYPE);
        } catch (IOException e) {
            e.printStackTrace();
            return new Response(STATUS_CODES.NOT_FOUND, HeaderDetails.STANDARD_HEADER, "Not Found",
                    HeaderDetails.TEXT_CONTENT_TYPE);
        }
    }
}
