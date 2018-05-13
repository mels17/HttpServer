package Services;

import Entities.Constants;
import Entities.Request;

public class MyRequestMatcher {

    Constants.Methods _requestType;
    String _path;

    public MyRequestMatcher(Constants.Methods requestType, String path) {
        _requestType = requestType;
        _path = path;
    }

    public MyRequestMatcher(Constants.Methods requestType) {
        _requestType = requestType;
    }

    public boolean matches(Request request) {
        if(_path == null && request.getRequestType().equals(_requestType)) return true;
        if(request.getRequestType().equals(_requestType) && request.get_justPath().equals(_path)) return true;
        return false;
    }


}
