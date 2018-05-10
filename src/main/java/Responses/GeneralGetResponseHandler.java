package Responses;

import Entities.RegExHashMap;
import Entities.Request;
import Entities.Response;

public class GeneralGetResponseHandler implements HttpResponseCommand {
    @Override
    public Response process(Request request) {
        RegExHashMap<String, HttpResponseCommand> route = new RegExHashMap<String, HttpResponseCommand>();

        route.put("/coffee", new CoffeeResponse());
        route.put("/tea", new TeaPartyResponse());
        route.put("/logs", new LogResponse());
//
        route.put("^/parameters.*$", new ParameterDecodeResponse());
        route.put("(?s).*\\b.(jpeg|png|gif)\\b.*", new ImageResponse());
//        route.put(".*$.(jpeg|png|gif).*$", new ImageResponse());
//
        route.put("/redirect", new GetRedirect());


        System.out.println("*******************");
        System.out.println(request.get_path());
        System.out.println(request.get_path().length());
        System.out.println(request.get_path().equals("/cookie"));
        System.out.println("%%%%%%%%%%%%%%%%%%%");


//        route.put("(?s).*\\b/cookie?\\b.*", new GetCookieResponse());
        route.put("^/cookie.*$", new GetCookieResponse());

//
        route.put("/eat_cookie", new EatCookieResponse());
//
//        route.put("(?s).*\\b/\\b.*\\bRange\\b.*", new PartialContentResponse());
//
        route.put("/", new FileLinksGetResponse());
        route.put("^/.*$", new GetFileContentResponse());

        return route.get(request.get_path()).process(request);
    }
}
