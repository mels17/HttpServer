package Responses;

import Entities.Constants;
import Entities.HeaderDetails;
import Entities.Request;
import Entities.Response;

import java.io.File;

public class FileLinksGetResponse implements HttpResponseCommand {
    @Override
    public Response process(Request request) {
        File _directory = new File(Constants.PUBLIC_DIR_PATH);

        String body = "<!DOCTYPE html>"
                + "<html>"
                + "<head>"
                + "<title> File Links </title>"
                + "</head>"
                + "<body>";

        for (String file : _directory.list()) {
            String path = '/' + file;
            body += "<a href=" + path + ">" + file + "</a>";
        }

        body += "</body>" + "</html>";
        return new Response(200, "Standard", body, HeaderDetails.HTML_CONTENT_TYPE);
    }
}
