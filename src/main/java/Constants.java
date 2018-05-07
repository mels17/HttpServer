import java.util.HashMap;

public class Constants {
    public static final int PORT = 5000;
    public static final String DIRECTORY = "public";
    public static final String GET_REQUEST = "GET";
    public static final String POST_REQUEST = "POST";
    public static final String PUT_REQUEST = "PUT";
    public static final String HEAD_REQUEST = "HEAD";
    public static final String OPTIONS_REQUEST = "OPTIONS";
    public static final String PATCH_REQUEST = "PATCH";
    public static final String DELETE_REQUEST = "DELETE";

    public static final HashMap<Integer, String> HTTP_CODES_AND_MESSAGES = new HashMap<Integer, String>() {
        {
            put(200, "OK");
            put(418, "I'm a teapot");
            put(401, "Unauthorized");
            put(404, "Not Found");
        }
    };
}
