public class OptionsResponse implements HttpResponseCommand{
    @Override
    public StringBuilder process(String request) {
        String path = RequestParser.getPath(request);
        StringBuilder sb =  path.equals("/method_options") ? new ResponseConstructor(200, "OK",
                "Allow: GET, HEAD, POST, OPTIONS, PUT\r\n", "text/plain").getResponse() :
                new ResponseConstructor(200, "OK", "Allow:GET, OPTIONS, HEAD\r\n", "text/plain").getResponse();
        return sb;

    }
}
