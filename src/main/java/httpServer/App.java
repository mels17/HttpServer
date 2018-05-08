package httpServer;

import Entities.Constants;
import Entities.HttpServerArguments;

import java.io.*;

public class App {

    public static void main(String[] args) throws IOException {
        HttpServerArguments arguments = HttpServerArguments.createServerArguments(args);
        File directory = new File(Constants.PUBLIC_DIR_PATH);
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
