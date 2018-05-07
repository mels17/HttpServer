import java.io.BufferedWriter;
import java.io.File;
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
    private String _contentType;

    public ResponseConstructor(int statusCode, String content, String stringToAddToHeaderOrStandard, String contentType) {
        _response = new StringBuilder();
        _statusCode = statusCode;
        _responseContent = content;
        _additionalHeaderOrNot = stringToAddToHeaderOrStandard;
        _contentType = contentType;
    }

    private static String getTimeAndDate() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
        return sdf.format(date);
    }

    public StringBuilder getResponse() {
        constructResponseHeader();
        constructResponseBody();
        return _response;
    }

    public StringBuilder constructResponseHeader() {
        _response.append("HTTP/1.1 " + _statusCode + " " + Constants.HTTP_CODES_AND_MESSAGES.get(_statusCode) + "\r\n");
        if(!_additionalHeaderOrNot.equals("Standard")) _response.append(_additionalHeaderOrNot);
        _response.append("Date:" + getTimeAndDate() + "\r\n");
        _response.append("Server:localhost\r\n");
        _response.append("Content-Type: " + _contentType + "\r\n");
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

    public static void deleteFile(String filename) {
        String filePath = "/Users/malavika.vasudevan/IdeaProjects/HttpServer/public";
        File file = new File(filePath + filename);
        if (file.exists() && !file.isDirectory()) file.delete();
    }

    public static String constructImageHeader(String extension) {
        return "HTTP/1.1 " + 200 + " " + Constants.HTTP_CODES_AND_MESSAGES.get(200) + "\r\n"
        + "Date:" + getTimeAndDate() + "\r\n"
        + "Server:localhost\r\n"
        + "Content-Type: image/" + extension + "\r\n"
        + "Connection: Closed\r\n\r\n";
    }

    public static boolean containsContentRangeInRequestHeader(String request) {
        return request.contains("Range");
    }

    public static String getPartialResponseHeader(StringBuilder sb, String request, String filename) {
        ByteRange br = getContentRange(request);
//        sb.append("HTTP/1.1 " + 206 + " " + "Partial Content" + "\r\n");
        return "Accept-Ranges: bytes" + "Content-Range: bytes " + br.get_start() + "-" + br.get_end() + "\r\n\r\n";
    }

    private static ByteRange getContentRange(String req) {
        String[] reqContent = req.split("\r\n");
        ByteRange br = new ByteRange();
        for (String line : reqContent) {
            if (line.startsWith("Range")) {
                String[] range = line.split("\\s")[2].split("/")[0].split("-");
                br.setStart(Integer.parseInt(range[0]));
                br.setEnd(Integer.parseInt(range[1]));
            }
        }
        return br;
    }
}
