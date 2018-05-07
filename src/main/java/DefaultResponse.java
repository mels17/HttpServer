public class DefaultResponse implements HttpResponseCommand {
    @Override
    public StringBuilder process(String request) {
        return new ResponseConstructor(405, "", "Standard", "text/plain").getResponse();
    }
}
