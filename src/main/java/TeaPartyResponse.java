

public class TeaPartyResponse implements HttpResponseCommand {
    @Override
    public StringBuilder process() {
        return new HttpRequestResponseHandler().getResponse(200, "OKAY", "");
    }
}
