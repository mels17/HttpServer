package Responses;

import Entities.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class PostResponse implements HttpResponseCommand {
    @Override
    public Response process(Request request) {
        if (request.get_body().equals("")) return new Response(STATUS_CODES.NOT_ALLOWED, HeaderDetails.STANDARD_HEADER, "Method Not Allowed",
                HeaderDetails.TEXT_CONTENT_TYPE);
        if (request.get_path().equals("/cat-form")) {
            String fileName = request.get_body().split("=")[0];
            try {
                File file = new File(Constants.PUBLIC_DIR_PATH + request.get_path() + "/" + fileName);
                BufferedWriter output = new BufferedWriter(new FileWriter(file));
                output.write(request.get_body());
                output.flush();
                output.close();
                return new Response(STATUS_CODES.CREATED, "Location: /cat-form/" + fileName + "\r\n",
                        "Write to file complete", HeaderDetails.TEXT_CONTENT_TYPE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new Response(STATUS_CODES.OK, HeaderDetails.STANDARD_HEADER, "Write Incomplete",
                HeaderDetails.TEXT_CONTENT_TYPE);
    }
}
