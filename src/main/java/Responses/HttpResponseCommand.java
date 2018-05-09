package Responses;

import Entities.Response;

public interface HttpResponseCommand {
    Response process(String request);
}
