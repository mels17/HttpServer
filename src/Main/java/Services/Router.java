package Services;

import Entities.Constants;
import Entities.Request;
import Responses.*;
import javafx.util.Pair;

import java.util.Arrays;
import java.util.List;

public class Router {
    static List<Pair<MyRequestMatcher, HttpResponseCommand>> requestMapping = Arrays.asList(
            new Pair(new MyRequestMatcher(Constants.Methods.GET, "/coffee"), new CoffeeResponse()),
            new Pair(new MyRequestMatcher(Constants.Methods.GET, "/tea"), new TeaPartyResponse()),
            new Pair(new MyRequestMatcher(Constants.Methods.GET, "/logs"), new LogResponse()),
            new Pair(new MyRequestMatcher(Constants.Methods.GET, "/parameters"), new ParameterDecodeResponse()),
            new Pair(new MyRequestMatcher(Constants.Methods.GET, "/redirect"), new RedirectResponse()),
            new Pair(new MyRequestMatcher(Constants.Methods.GET, "/cookie"), new CookieResponse()),
            new Pair(new MyRequestMatcher(Constants.Methods.GET, "/eat_cookie"), new EatCookieResponse()),
            new Pair(new MyRequestMatcher(Constants.Methods.GET, "/timer"), new TimerResponse()),
            new Pair(new MyRequestMatcher(Constants.Methods.GET, "/"), new FileLinksResponse()),
            new Pair(new MyRequestMatcher(Constants.Methods.GET), new FileContentResponse()),
            new Pair(new MyRequestMatcher(Constants.Methods.HEAD), new HeadResponse()),
            new Pair(new MyRequestMatcher(Constants.Methods.OPTIONS), new OptionsResponse()),
            new Pair(new MyRequestMatcher(Constants.Methods.PUT), new PutResponse()),
            new Pair(new MyRequestMatcher(Constants.Methods.POST), new PostResponse()),
            new Pair(new MyRequestMatcher(Constants.Methods.DELETE), new DeleteResponse()),
            new Pair(new MyRequestMatcher(Constants.Methods.PATCH), new PatchResponse()),
            new Pair(new MyRequestMatcher(Constants.Methods.BOGUS), new DefaultResponse())
    );


    public HttpResponseCommand findCommandForRequest(Request request) {
        for (Pair<MyRequestMatcher, HttpResponseCommand> mapping : requestMapping) {
            if (mapping.getKey().matches(request)) {
                return mapping.getValue();
            }
        }
        return new DefaultResponse();
    }

}
