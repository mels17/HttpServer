package Responses;

import Entities.Constants;
import Services.ResponseConstructor;

import java.io.File;

public class FileLinksGetResponse implements HttpResponseCommand {
    @Override
    public StringBuilder process(String request) {
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
        return new ResponseConstructor(200, body,
                "Standard", "text/html")
                .getResponse();
    }
}
