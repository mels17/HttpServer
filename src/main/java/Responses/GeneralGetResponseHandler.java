package Responses;

import Entities.Constants;
import Entities.Request;
import Entities.Response;

public class GeneralGetResponseHandler implements HttpResponseCommand {
    @Override
    public Response process(Request request) {
        return Constants.GET_REQUEST_ROUTES.get(request.get_path()).process(request);
    }
}
