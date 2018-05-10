package Entities;

import Responses.*;

import java.util.HashMap;

public class Constants {
    public static final int PORT = 5000;
    public static final String DIRECTORY = "public";
    public static final String PUBLIC_DIR_PATH = "/Users/malavika.vasudevan/IdeaProjects/HttpServer/public";

    public static final HashMap<Integer, String> HTTP_CODES_AND_MESSAGES = new HashMap<Integer, String>() {
        {
            put(STATUS_CODES.OK, "OK");
            put(STATUS_CODES.TEAPOT, "I'm a teapot");
            put(STATUS_CODES.UNAUTHORIZED, "Unauthorized");
            put(STATUS_CODES.NOT_FOUND, "Not Found");
            put(STATUS_CODES.NOT_ALLOWED, "Method Not Allowed");
            put(STATUS_CODES.FOUND, "Found");
            put(STATUS_CODES.NO_CONTENT, "No Content");
            put(STATUS_CODES.PARTIAL_CONTENT, "Partial Content");
            put(STATUS_CODES.RANGE_NOT_SATISFIABLE, "Range Not Satisfiable");
        }
    };

    public static final String NO_CONTENT = "";

    public static final HashMap<String, HttpResponseCommand> SERVER_REQUEST_ROUTER = new HashMap<String, HttpResponseCommand>() {
        {
            put("GET", new GeneralGetResponseHandler());
            put("HEAD", new HeadResponse());
            put("OPTIONS", new OptionsResponse());
            put("PUT", new PutResponse());
            put("POST", new PostResponse());
            put("DELETE", new DeleteResponse());
            put("PATCH", new PatchResponse());
        }
    };

    public static final RegExHashMap<String, HttpResponseCommand> GET_REQUEST_ROUTES = new RegExHashMap<String, HttpResponseCommand>() {
        {
            put("/coffee", new CoffeeResponse());
            put("/tea", new TeaPartyResponse());
            put("/logs", new LogResponse());
            put("^/parameters.*$", new ParameterDecodeResponse());
            put("(?s).*\\b.(jpeg|png|gif)\\b.*", new ImageResponse());
            put("/redirect", new GetRedirect());
            put("^/cookie.*$", new GetCookieResponse());
            put("/eat_cookie", new EatCookieResponse());
            put("/", new FileLinksGetResponse());
            put("^/.*$", new GetFileContentResponse());
        }
    };

}
