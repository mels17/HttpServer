import Entities.Constants;
import Entities.HttpServerArguments;
import httpServer.SimpleHttpServer;

import java.io.*;

public class App {

    enum example{
        ABC, DEF
    };

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
        SimpleHttpServer httpServer = new SimpleHttpServer(arguments.getPort(), directory);
        httpServer.run();
    }
}
