public class DeleteResponse implements HttpResponseCommand {
    @Override
    public StringBuilder process(String request) {
        String path = RequestParser.getPath(request);
        ResponseConstructor.deleteFile(path);
        return new ResponseConstructor(200, "OK", "Standard", "text/plain").getResponse();
    }
}
