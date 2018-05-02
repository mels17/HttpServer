public class CoffeeResponse implements HttpResponseCommand{
    @Override
    public StringBuilder process() {
        return new HttpRequestResponseHandler().getResponse(418, "I'm a teapot", "");
    }
}
