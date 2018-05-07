import java.io.IOException;

public class PatchResponse implements HttpResponseCommand {
    @Override
    public StringBuilder process(String request) {
        String filename = RequestParser.getPath(request);
        try {
            FileOperations.overWriteFile(filename, RequestParser.getDataFromRequest(request));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseConstructor(204, "No Content",
                "Standard", "text/plain")
                .constructResponseHeader();
    }
}
