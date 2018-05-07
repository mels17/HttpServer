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
    public static List<String> cookies = new ArrayList<String>();
    public static List<String> logs = new ArrayList<String>();

    private static HashMap<String, HttpResponseCommand> router = new HashMap<String, HttpResponseCommand>();

    private String errorMessage;

    public HttpServer(int port, File directory) {
        _port = port;
        _directory = directory;

        logger = Logger.getLogger("java-httpserver");

        initializeRouter();
    }

    public static HashMap<String, HttpResponseCommand> initializeRouter() {
        router.put("GET /coffee", new CoffeeResponse());
        router.put("GET /tea", new TeaPartyResponse());
        router.put("GET /logs", new LogResponse());
        router.put("^HEAD.*$", new HeadResponse());
        return router;
    }

    public static RegExHashMap<String, HttpResponseCommand> initializeRegexMap() {
        RegExHashMap<String, HttpResponseCommand> route = new RegExHashMap<String, HttpResponseCommand>();

        route.put("GET /coffee", new CoffeeResponse());
        route.put("GET /tea", new TeaPartyResponse());
        route.put("GET /logs", new LogResponse());
        route.put("^HEAD.*$", new HeadResponse());
        route.put("^OPTIONS.*$", new OptionsResponse());
        route.put("^PUT.*$", new PutResponse());

        return route;
    }


    public void run(){
        try (ServerSocket socket = new ServerSocket(_port)){
            errorMessage = "Creating server socket failed.";
            logger.info("Http server started at http://localhost:" + _port);
            logger.info("Enter Ctrl+C for stopping the server.");
            while (true) {
                Socket clientConnectionSocket = socket.accept();
                pool.submit(new HttpRequestResponseHandler(clientConnectionSocket, _directory));
            }
        } catch (SocketException e) {
            logger.log(Level.SEVERE, errorMessage, e);
        } catch (IOException e) {
            logger.log(Level.SEVERE, errorMessage, e);
        }
    }
}
