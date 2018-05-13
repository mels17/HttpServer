import Entities.Request;
import Responses.*;
import Services.Router;
import org.junit.Assert;
import org.junit.Test;

public class RouterTest {
    Router router = new Router();

    @Test
    public void testGetRequestWithFilePathReturnsInstanceOfFileContentResponse() {
        String reqString = "GET /example HTTP/1.1\r\n\r\n";
        Assert.assertTrue(router.findCommandForRequest(new Request(reqString)) instanceof FileContentResponse);
    }

    @Test
    public void getFileLinksRequestReturnsFileLinksResponseInstance() {
        String reqString = "GET / HTTP/1.1\r\n\r\n";
        Assert.assertTrue(router.findCommandForRequest(new Request(reqString)) instanceof FileLinksResponse);
    }

    @Test
    public void testGetRequestWithParametersReturnParameterDecodeResponseInstance() {
        String reqString = "GET /parameters?aBgkn+mhuzxc HTTP/1.1\r\n\r\n";
        Assert.assertTrue(router.findCommandForRequest(new Request(reqString)) instanceof ParameterDecodeResponse);
    }

    @Test
    public void getCoffeeRequestReturnsCoffeeResponseInstance() {
        String reqString = "GET /coffee HTTP/1.1\r\n\r\n";
        Assert.assertTrue(router.findCommandForRequest(new Request(reqString)) instanceof CoffeeResponse);
    }

    @Test
    public void getTeaRequestReturnsInstanceOfTeaResponse() {
        String reqString = "GET /tea HTTP/1.1\r\n\r\n";
        Assert.assertTrue(router.findCommandForRequest(new Request(reqString)) instanceof TeaPartyResponse);
    }

    @Test
    public void getLogsRequestReturnsLogResponseInstance() {
        String reqString = "GET /logs HTTP/1.1\r\n\r\n";
        Assert.assertTrue(router.findCommandForRequest(new Request(reqString)) instanceof LogResponse);
    }

    @Test
    public void getRedirectRequestReturnsRdirectResponseInstance() {
        String reqString = "GET /redirect HTTP/1.1\r\n\r\n";
        Assert.assertTrue(router.findCommandForRequest(new Request(reqString)) instanceof RedirectResponse);
    }

    @Test
    public void getCookieRequestReturnsCookieResponseInstance() {
        String reqString = "GET /cookie HTTP/1.1\r\n\r\n";
        Assert.assertTrue(router.findCommandForRequest(new Request(reqString)) instanceof CookieResponse);
    }

    @Test
    public void getEatCookieRequestReturnsEatCookieResponseInstance() {
        String reqString = "GET /eat_cookie HTTP/1.1\r\n\r\n";
        Assert.assertTrue(router.findCommandForRequest(new Request(reqString)) instanceof EatCookieResponse);
    }

    @Test
    public void testHeadRequestReturnInstanceOfHeadResponse() {
        String reqString = "HEAD / HTTP/1.1\r\n\r\n";
        Assert.assertTrue(router.findCommandForRequest(new Request(reqString)) instanceof HeadResponse);
    }

    @Test
    public void testOptionsResponseReturnInstanceOfOptionsResponse() {
        String reqString = "OPTIONS / HTTP/1.1\r\n\r\n";
        Assert.assertTrue(router.findCommandForRequest(new Request(reqString)) instanceof OptionsResponse);
    }

    @Test
    public void testPutRequestReturnsInstanceOfPutResponse() {
        String reqString = "PUT /file1 HTTP/1.1\r\n\r\n";
        Assert.assertTrue(router.findCommandForRequest(new Request(reqString)) instanceof PutResponse);
    }

    @Test
    public void testPostRequestReturnsPostResponseInstance() {
        String reqString = "POST /file1 HTTP/1.1\r\n\r\n";
        Assert.assertTrue(router.findCommandForRequest(new Request(reqString)) instanceof PostResponse);
    }

    @Test
    public void testDeleteRequestReturnsDeleteResponseInstance() {
        String reqString = "DELETE /file1 HTTP/1.1\r\n\r\n";
        Assert.assertTrue(router.findCommandForRequest(new Request(reqString)) instanceof DeleteResponse);
    }

    @Test
    public void getBogusRequestReturnsDefaultResponseInstance() {
        String reqString = "BOGUS /file1 HTTP/1.1\r\n\r\n";
        Assert.assertTrue(router.findCommandForRequest(new Request(reqString)) instanceof DefaultResponse);
    }
}