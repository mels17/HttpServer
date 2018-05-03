import java.io.*;
import java.util.Base64;

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
//        System.out.println(new File("/Users/malavika.vasudevan/IdeaProjects/HttpServer/public/partial_content.txt").length());
//        System.out.println(new String(Base64.getDecoder().decode("YWRtaW46aHVudGVyMg==")));
        System.out.println("4-".split("-").length);
        System.out.println("4-".split("-")[0]);
        HttpServer httpServer = new HttpServer(arguments.getPort(), directory);
        httpServer.run();
    }
}
