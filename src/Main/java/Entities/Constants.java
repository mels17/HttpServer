package Entities;

import java.util.HashMap;

public class Constants {
    public static final int PORT = 5000;
    public static final String DIRECTORY = "public";
    public static final String PUBLIC_DIR_PATH = "/Users/malavika.vasudevan/IdeaProjects/NewHttpServer/HttpServer/public";

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

    public enum Methods {
        GET, HEAD, OPTIONS, PUT, POST, DELETE, PATCH, BOGUS
    };
}
