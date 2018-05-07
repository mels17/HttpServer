import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class PostResponse implements HttpResponseCommand{
    @Override
    public StringBuilder process(String request) {
        String path = RequestParser.getPath(request);
        String filePath = "/Users/malavika.vasudevan/IdeaProjects/HttpServer/public/";
        String content = RequestParser.getDataFromRequest(request);
        if (content.equals("")) return new ResponseConstructor(405, "Method Not Allowed", "Standard").getResponse();
        if (path.equals("/cat-form")) {
            filePath += path.substring(1);

            String fileName = content.split("=")[0];
            try {
                File file = new File(filePath + "/" + fileName);
                BufferedWriter output = new BufferedWriter(new FileWriter(file));
                output.write(content);
                output.flush();
                output.close();
                return ResponseConstructor.getPostResponseHeaderWithRedirect(fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new ResponseConstructor(200, "OK", "Standard").getResponse();
    }
}
