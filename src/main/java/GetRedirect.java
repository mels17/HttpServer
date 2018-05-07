public class GetRedirect implements HttpResponseCommand {
    @Override
    public StringBuilder process(String request) {
        return new ResponseConstructor(302, "Found Response", "Location: /\r\n").getResponse();
    }
}
