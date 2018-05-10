package Responses;

import Entities.*;
import Services.HttpRequestHandler;
import Services.RequestParser;

import java.io.FileInputStream;
import java.io.IOException;

public class ImageResponse implements HttpResponseCommand {
    @Override
    public Response process(Request request) {
        String responseHeader = new Response(STATUS_CODES.OK, HeaderDetails.STANDARD_HEADER, "",
                HeaderDetails.IMAGE_CONTENT_TYPE + RequestParser.getFileExtension(request.get_path())).get_header();
        readFromImageAndWriteToOutputStream(request.get_path(), responseHeader.getBytes());

        return null;
    }

    private void readFromImageAndWriteToOutputStream(String filename, byte[] responseHeaderBytes) {
        try {
            HttpRequestHandler.getOutputStream().write(responseHeaderBytes);
            FileInputStream fis = new FileInputStream(Constants.PUBLIC_DIR_PATH + filename);
            int i;
            while ((i = fis.read()) > -1)
                HttpRequestHandler.getOutputStream().write(i);

            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
