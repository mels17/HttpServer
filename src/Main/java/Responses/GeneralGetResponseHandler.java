//package Responses;
//
//import Entities.Constants;
//import Entities.Request;
//import Entities.Response;
//
//public class GeneralGetResponseHandler implements HttpResponseCommand {
//    @Override
//    public Response process(Request request) {
//        return Constants.GET_ROUTES.getOrDefault(request.get_justPath(), new FileContentResponse()).process(request);
//    }
//}
