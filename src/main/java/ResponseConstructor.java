import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ResponseConstructor {

    private StringBuilder _response;
    private int _statusCode;
    private String _responseContent;
    private String _additionalHeaderOrNot;

    public ResponseConstructor(int statusCode, String content, String stringToAddToHeaderOrStandard) {
        _response = new StringBuilder();
        _statusCode = statusCode;
        _responseContent = content;
        _additionalHeaderOrNot = stringToAddToHeaderOrStandard;
    }

    private String getTimeAndDate() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
        return sdf.format(date);
    }

    public StringBuilder getResponse() {
        constructResponseHeader();
        constructResponseBody();
        return _response;
    }

    private StringBuilder constructResponseHeader() {
        _response.append("HTTP/1.1 " + _statusCode + " " + Constants.HTTP_CODES_AND_MESSAGES.get(_statusCode) + "\r\n");
        if(!_additionalHeaderOrNot.equals("Standard")) _response.append(_additionalHeaderOrNot);
        _response.append("Date:" + getTimeAndDate() + "\r\n");
        _response.append("Server:localhost\r\n");
        _response.append("Content-Type: text/html\r\n");
        _response.append("Connection: Closed\r\n\r\n");

        return _response;
    }

    private StringBuilder constructResponseBody() {
        _response.append(_responseContent);
        return _response;
    }

    public static void overWriteFile(String filename, String content) throws IOException {
        BufferedWriter out = new BufferedWriter(new FileWriter("/Users/malavika.vasudevan/IdeaProjects/HttpServer/public/" + filename));
        out.write(content);
        out.close();
    }

    public static StringBuilder getPostResponseHeaderWithRedirect(String newFile) {
        StringBuilder sb = new StringBuilder();
        sb.append("HTTP/1.1 " + 201 + " " + "Created" + "\r\n");
        sb.append("Location: /cat-form/" + newFile + "\r\n\r\n");

        return sb;
    }

    public static String getTextFileContents(String filename) throws IOException {
        String path = "/Users/malavika.vasudevan/IdeaProjects/HttpServer/public/" + filename;
        return new String(Files.readAllBytes(Paths.get(path)), "UTF-8");
    }
}
