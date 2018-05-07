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
            FileInputStream fis = new FileInputStream(Constants.PUBLIC_DIR_PATH + RequestParser.getPath(request));
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
