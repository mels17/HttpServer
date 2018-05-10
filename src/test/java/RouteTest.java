import Entities.RegExHashMap;
import Responses.*;
import httpServer.HttpServer;
import org.junit.Assert;
import org.junit.Test;

public class RouteTest {

    RegExHashMap<String, HttpResponseCommand> route = HttpServer.getRouteMap();

    @Test
    public void testGetCoffeeRegex() {
        Assert.assertTrue(route.get("GET /coffee") instanceof CoffeeResponse);
        Assert.assertTrue(route.get("GET /tea") instanceof TeaPartyResponse);
        Assert.assertTrue(route.get("GET /logs") instanceof LogResponse);
        Assert.assertTrue(route.get("GET /parameters") instanceof ParameterDecodeResponse);
        Assert.assertTrue(route.get("GET /image.jpeg") instanceof ImageResponse);
        Assert.assertTrue(route.get("GET / example") instanceof FileLinksGetResponse);
        Assert.assertTrue(route.get("GET /redirect") instanceof GetRedirect);
        Assert.assertTrue(route.get("GET /cookie") instanceof GetCookieResponse);
        Assert.assertTrue(route.get("GET /eat_cookie HTTP/1.1") instanceof EatCookieResponse);
        Assert.assertTrue(route.get("GET /file.txt Range") instanceof PartialContentResponse);
        Assert.assertTrue(route.get("GET /file1.txt") instanceof GetFileContentResponse);

        Assert.assertTrue(route.get("HEAD /") instanceof HeadResponse);
        Assert.assertTrue(route.get("OPTIONS /") instanceof OptionsResponse);
        Assert.assertTrue(route.get("PUT /") instanceof PutResponse);
        Assert.assertTrue(route.get("POST /") instanceof PostResponse);
        Assert.assertTrue(route.get("DELETE /") instanceof DeleteResponse);
        Assert.assertTrue(route.get("PATCH /") instanceof PatchResponse);
    }

}
