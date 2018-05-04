public class CoffeeResponse implements HttpResponseCommand{
    @Override
    public StringBuilder process() {
        return new ResponseConstructor(418, "I'm a teapot", "Standard")
                .getResponse();
    }
}
