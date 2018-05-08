package Entities;

import java.util.HashMap;

public class Constants {
    public static final int PORT = 5000;
    public static final String DIRECTORY = "public";
    public static final String PUBLIC_DIR_PATH = "/Users/malavika.vasudevan/IdeaProjects/HttpServer/public";

    public static final HashMap<Integer, String> HTTP_CODES_AND_MESSAGES = new HashMap<Integer, String>() {
        {
            put(200, "OK");
            put(418, "I'm a teapot");
            put(401, "Unauthorized");
            put(404, "Not Found");
            put(405, "Method Not Allowed");
            put(302, "Found");
            put(204, "No Content");
            put(206, "Partial Content");
            put(416, "Range Not Satisfiable");
        }
    };

    public static final String TEXT_CONTENT_TYPE = "text/plain";
    public static final String HTML_CONTENT_TYPE = "text/html";
}
