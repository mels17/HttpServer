public class TeaPartyResponse implements HttpResponseCommand {
    @Override
    public StringBuilder process() {
        return new ResponseConstructor(200, "Tea Response", "Standard")
                .getResponse();
    }
}
