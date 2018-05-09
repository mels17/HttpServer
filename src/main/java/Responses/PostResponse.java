package Responses;

import Entities.Constants;
import Entities.HeaderDetails;
import Entities.Response;
import Services.RequestParser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class PostResponse implements HttpResponseCommand {
    @Override
    public Response process(String request) {
        String path = RequestParser.getPath(request);
        String content = RequestParser.getDataFromRequest(request);
        if (content.equals("")) return new Response(405, HeaderDetails.STANDARD_HEADER, "Method Not Allowed",
                HeaderDetails.TEXT_CONTENT_TYPE);
        if (path.equals("/cat-form")) {
            String fileName = content.split("=")[0];
            try {
                File file = new File(Constants.PUBLIC_DIR_PATH + path + "/" + fileName);
                BufferedWriter output = new BufferedWriter(new FileWriter(file));
                output.write(content);
                output.flush();
                output.close();
                return new Response(201, "Location: /cat-form/" + fileName + "\r\n",
                        "Write to file complete", HeaderDetails.TEXT_CONTENT_TYPE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new Response(200, HeaderDetails.STANDARD_HEADER, "Write Incomplete",
                HeaderDetails.TEXT_CONTENT_TYPE);
    }
}
