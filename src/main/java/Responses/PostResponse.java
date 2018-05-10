package Responses;

import Entities.Constants;
import Entities.HeaderDetails;
import Entities.Request;
import Entities.Response;
import Services.RequestParser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class PostResponse implements HttpResponseCommand {
    @Override
    public Response process(Request request) {
        if (request.get_body().equals("")) return new Response(405, HeaderDetails.STANDARD_HEADER, "Method Not Allowed",
                HeaderDetails.TEXT_CONTENT_TYPE);
        if (request.get_path().equals("/cat-form")) {
            String fileName = request.get_body().split("=")[0];
            try {
                File file = new File(Constants.PUBLIC_DIR_PATH + request.get_path() + "/" + fileName);
                BufferedWriter output = new BufferedWriter(new FileWriter(file));
                output.write(request.get_body());
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
