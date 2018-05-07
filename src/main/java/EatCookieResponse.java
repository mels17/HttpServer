public class EatCookieResponse implements HttpResponseCommand {
    @Override
    public StringBuilder process(String request) {
        String body = "mmmm";
        for (String cookie: HttpServer.cookies) {
            body = body + " " + cookie.split("=")[1] + "\r\n";
        }

        return new ResponseConstructor(200, body, "Set-Cookie: "
                + HttpServer.cookies.get(0) + "\r\n", "text/plain").getResponse();
    }
}
