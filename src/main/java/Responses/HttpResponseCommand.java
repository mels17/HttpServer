package Responses;

import Entities.Request;
import Entities.Response;

public interface HttpResponseCommand {
    Response process(Request request);
}
