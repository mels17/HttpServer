import java.io.*;

public class MainServer {

    public static void main(String[] args) throws IOException {
        HttpServerArguments arguments = HttpServerArguments.createServerArguments(args);
        File directory = new File("/Users/malavika.vasudevan/IdeaProjects/HttpServer/public");
        if (!directory.exists()) {
            System.out.println("Directory cannot be found!");
            return;
        }

        if (!directory.isDirectory()) {
            System.out.println("The input is not a directory");
            return;
        }
        HttpServer httpServer = new HttpServer(arguments.getPort(), directory);
        httpServer.run();
    }
}
