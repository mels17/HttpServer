package Responses;

import Entities.*;

import java.io.File;

public class FileLinksResponse implements HttpResponseCommand {
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
        return new Response(STATUS_CODES.OK, HeaderDetails.STANDARD_HEADER, body, HeaderDetails.HTML_CONTENT_TYPE);
    }
}
