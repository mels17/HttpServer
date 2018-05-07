import java.io.IOException;

public class GetFileContentResponse implements HttpResponseCommand {
    @Override
    public StringBuilder process(String request) {
        String filename = RequestParser.getPath(request).substring(1);
        StringBuilder sb = new StringBuilder();

        String additionalHeaders = "Standard";
        int statusCode = 200;

        if (ResponseConstructor.containsContentRangeInRequestHeader(request)) {
            additionalHeaders = ResponseConstructor.getPartialResponseHeader(sb, request, filename);
            statusCode = 206;
        }

        String body = "";
        try {
            body = ResponseConstructor.getTextFileContents(filename) + "\r\n";
            return new ResponseConstructor(statusCode, body, additionalHeaders, "text/plain").getResponse();
        } catch (IOException e) {
            sb = new StringBuilder();
            sb.append("HTTP/1.1 " + 404 + " " + "Not Found" + "\r\n\r\n");
            sb.append("File Not Found.");
            e.printStackTrace();
        }
        return sb;
    }
}
