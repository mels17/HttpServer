import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class ImageResponse implements HttpResponseCommand {
    @Override
    public StringBuilder process(String request) {
        String extension = RequestParser.getFileExtension(request);
        String responseHeader = ResponseConstructor.constructImageHeader(extension);

        try {
            HttpRequestResponseHandler.out.write(responseHeader.getBytes());
            FileInputStream fis = new FileInputStream("/Users/malavika.vasudevan/IdeaProjects/HttpServer/public" + RequestParser.getPath(request));
            DataOutputStream ds = new DataOutputStream(HttpRequestResponseHandler.out);
            int i;
            while ((i = fis.read()) > -1)
                HttpRequestResponseHandler.out.write(i);

            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
