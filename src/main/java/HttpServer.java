import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HttpServer {
    /*
     What does a server have?
     1. port number
     2. localhost url
     3. client listener
     4. i. client sends a request to the server - get/post/put/delete
        ii. server receives request
        iii. routes request to request handler corresponding to request
        iii. server then sends response
     5. sockets - one endpoint in a two-way communication - need two sockets - server-side and client-side
    */

    private Logger logger;
    private boolean running;
    private int _port;
    private String _directory;

    private String errorMessage;

    public HttpServer(int port, String directory) {
        _port = port;
        _directory = directory;

        running = true;
        logger = Logger.getLogger("java-httpserver");
    }

    public void start(){

        running = true;
        // Create a socket for server-side
        try {
            errorMessage = "Creating server socket failed.";
            ServerSocket socket = new ServerSocket();

            logger.info("Http server started at http://localhost:" + _port);
            logger.info("Enter Ctrl+C for stopping the server.");

            // bind the socket to the socket address even though the address is in a timeout state
            socket.setReuseAddress(true);

            // now bind
            errorMessage = "Binding server socket to port failed.";
            socket.bind(new InetSocketAddress(_port));

            while (running) createClientRequestResponseHandlerOnNewThread(socket);
        } catch (SocketException e) {
            logger.log(Level.SEVERE, errorMessage, e);
        } catch (IOException e) {
            logger.log(Level.SEVERE, errorMessage, e);
        }
    }

    private void createClientRequestResponseHandlerOnNewThread(ServerSocket serverSocket) throws IOException {
        errorMessage = "Accepting client connection failed";
        Socket clientConnectionSocket = serverSocket.accept();
        HttpCustomRequest handler = new HttpCustomRequest(clientConnectionSocket, _directory);
        Thread thread = new Thread(handler);
        thread.start();
    }
}
