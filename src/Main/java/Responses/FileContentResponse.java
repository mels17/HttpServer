package Responses;

import Entities.HeaderDetails;
import Entities.Request;
import Entities.Response;
import Entities.STATUS_CODES;
import Services.FileOperations;

import java.io.IOException;
import java.util.Arrays;

public class FileContentResponse implements HttpResponseCommand {
    @Override
    public Response process(Request request) {
        if (isPartialContentRequest(request)) return new PartialContentResponse().process(request);
        if (isImageFile(request.get_path())) return new ImageResponse().process(request);
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

    private boolean isImageFile(String path) {
        return path.contains(".jpeg") || path.contains(".png") || path.contains(".gif");
    }

    private boolean isPartialContentRequest(Request request) {
        return !request.get_rangeHeaderValue().equals("-1");
    }
}
