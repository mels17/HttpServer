public class HeadResponse implements HttpResponseCommand{
    @Override
    public StringBuilder process(String request) {
        ResponseConstructor rc = RequestParser.getPath(request).equals("/") ?
                new ResponseConstructor(200, "Response: OK", "Standard")
                : new ResponseConstructor(404, "Response: Not Found", "Standard");
        return rc.getResponse();
    }
}
