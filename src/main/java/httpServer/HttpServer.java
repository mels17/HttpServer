package httpServer;

import Entities.RegExHashMap;
import Responses.*;
import Services.HttpRequestHandler;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HttpServer {
    /*
     What does a server have?
     1. port number
     2. localhost url
     3. client listener
     4. i. client sends a request to the server - get/post/put/delete - only doing get, so do we need a router?
        ii. server receives request
        iii. routes request to request handler corresponding to request
        iii. server then sends response
     5. sockets - one endpoint in a two-way communication - need two sockets - server-side and client-side
    */

    private Logger logger;
    private int _port;
    private File _directory;
    ExecutorService pool = Executors.newFixedThreadPool(10);
    public static List<String> logs = new ArrayList<String>();

    private String errorMessage;

    public HttpServer(int port, File directory) {
        _port = port;
        _directory = directory;

        logger = Logger.getLogger("java-httpserver");
    }

    public static RegExHashMap<String, HttpResponseCommand> getRouteMap() {
        RegExHashMap<String, HttpResponseCommand> route = new RegExHashMap<String, HttpResponseCommand>();

        route.put("(?s).*\\bGET /coffee\\b.*", new CoffeeResponse());
        route.put("(?s).*\\bGET /tea\\b.*", new TeaPartyResponse());
        route.put("(?s).*\\bGET /logs\\b.*", new LogResponse());
        route.put("(?s).*\\bGET /parameters\\b.*", new ParameterDecodeResponse());
        route.put("(?s).*\\bGET\\b.*\\b.(jpeg|png|gif)\\b.*", new ImageResponse());

        route.put("(?s).*\\bGET /redirect\\b.*", new GetRedirect());
        route.put("(?s).*\\bGET /cookie\\b.*", new GetCookieResponse());
        route.put("(?s).*\\bGET /eat_cookie \\b.*", new EatCookieResponse());

        route.put("(?s).*\\bGET\\b.*\\b /\\b.*\\bRange\\b.*", new PartialContentResponse());

        route.put("(?s).*\\bGET /\\b.*", new GetFileContentResponse());
        route.put("(?s).*\\bGET / \\b.*", new FileLinksGetResponse());

        route.put("(?s).*\\bHEAD\\b.*", new HeadResponse());
        route.put("(?s).*\\bOPTIONS\\b.*", new OptionsResponse());
        route.put("(?s).*\\bPUT\\b.*", new PutResponse());
        route.put("(?s).*\\bPOST\\b.*", new PostResponse());
        route.put("(?s).*\\bDELETE\\b.*", new DeleteResponse());
        route.put("(?s).*\\bPATCH\\b.*", new PatchResponse());

        return route;
    }



    public static HashMap<String, HttpResponseCommand> getGeneralRequestRouter() {

        HashMap<String, HttpResponseCommand> router = new HashMap<String, HttpResponseCommand>();

        router.put("GET", new GeneralGetResponseHandler());
        router.put("HEAD", new HeadResponse());
        router.put("OPTIONS", new OptionsResponse());
        router.put("PUT", new PutResponse());
        router.put("POST", new PostResponse());
        router.put("DELETE", new DeleteResponse());
        router.put("PATCH", new PatchResponse());

        return router;
    }




    public void run(){
        try (ServerSocket socket = new ServerSocket(_port)){
            errorMessage = "Creating server socket failed.";
            logger.info("Http server started at http://localhost:" + _port);
            logger.info("Enter Ctrl+C for stopping the server.");
            while (true) {
                Socket clientConnectionSocket = socket.accept();
                pool.submit(new HttpRequestHandler(clientConnectionSocket, _directory));
            }
        } catch (SocketException e) {
            logger.log(Level.SEVERE, errorMessage, e);
        } catch (IOException e) {
            logger.log(Level.SEVERE, errorMessage, e);
        }
    }
}
