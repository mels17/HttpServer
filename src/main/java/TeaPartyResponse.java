public class TeaPartyResponse implements HttpResponseCommand {
    @Override
    public StringBuilder process(String request) {
        return new ResponseConstructor(200, "Tea Response", "Standard", "text/plain")
                .getResponse();
    }
}
