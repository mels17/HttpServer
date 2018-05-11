import Entities.Constants;
import Entities.Request;
import Entities.Response;
import Responses.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;

public class RouteTest {

    HashMap<String, HttpResponseCommand> route = Constants.SERVER_REQUEST_ROUTER;

    @Test
    public void testBasicRequestMethodsReturnResponses() {
        Assert.assertTrue(route.get("HEAD") instanceof HeadResponse);
        Assert.assertTrue(route.get("OPTIONS") instanceof OptionsResponse);
        Assert.assertTrue(route.get("PUT") instanceof PutResponse);
        Assert.assertTrue(route.get("POST") instanceof PostResponse);
        Assert.assertTrue(route.get("DELETE") instanceof DeleteResponse);
        Assert.assertTrue(route.get("PATCH") instanceof PatchResponse);
        Assert.assertTrue(route.get("GET") instanceof GeneralGetResponseHandler);
    }

    @Test
    public void whenBogusRequestReturnMethodNotAllowedDefaultResponse() {
        Assert.assertTrue(route.getOrDefault("BOGUS", new DefaultResponse()) instanceof DefaultResponse);
    }

    @Test
    public void whenGetDirectoryLinksGetRequestGetResponseObjectWithContentTypeHtml() {
        Request request = new Request("GET / HTTP/1.1\r\n\r\n");
        GeneralGetResponseHandler handler = new GeneralGetResponseHandler();
        Response actualResponse = handler.process(request);
        Assert.assertEquals(actualResponse.get_header().split("\r\n")[1], ("Content-type: text/html"));
    }

    @Test
    public void whenGetFileReturnResponseObjectWithFileContentsAsBody() {
        Request request = new Request("GET /file1 HTTP/1.1\r\n\r\n");
        GeneralGetResponseHandler handler = new GeneralGetResponseHandler();
        Response actualResponse = handler.process(request);
        Assert.assertEquals(actualResponse.get_body(), ("file1 contents\r\n"));
    }

    @Test
    public void whenGetFileThatDoesNotExistReturn404StatusCode() {
        Request request = new Request("GET /abc HTTP/1.1\r\n\r\n");
        GeneralGetResponseHandler handler = new GeneralGetResponseHandler();
        Response actualResponse = handler.process(request);
        Assert.assertEquals("404", actualResponse.get_header().split("\\s")[1]);
    }
}
