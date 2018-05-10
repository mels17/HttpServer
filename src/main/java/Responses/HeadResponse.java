package Responses;

import Entities.HeaderDetails;
import Entities.Request;
import Entities.Response;
import Entities.STATUS_CODES;

public class HeadResponse implements HttpResponseCommand {
    @Override
    public Response process(Request request) {
        return request.get_path().equals("/") ?
                new Response(STATUS_CODES.OK, HeaderDetails.STANDARD_HEADER, "Response: OK",
                        HeaderDetails.TEXT_CONTENT_TYPE)
                : new Response(STATUS_CODES.NOT_FOUND, HeaderDetails.STANDARD_HEADER, "Response: Not Found",
                HeaderDetails.TEXT_CONTENT_TYPE);
    }
}
