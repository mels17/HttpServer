import java.io.IOException;

public class MainServer {

    public static void main(String[] args) throws IOException {
        HttpServerArguments arguments = HttpServerArguments.createServerArguments(args);
        HttpServer httpServer = new HttpServer(arguments.getPort(), arguments.getDirectory());
        httpServer.start();
    }
}
