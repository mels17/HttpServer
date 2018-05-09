package Responses;

import Entities.Constants;
import Entities.HeaderDetails;
import Entities.Response;
import Services.HttpRequestHandler;
import Services.RequestParser;

import java.io.FileInputStream;
import java.io.IOException;

public class ImageResponse implements HttpResponseCommand {
    @Override
    public Response process(String request) {
        String responseHeader = new Response(200, HeaderDetails.STANDARD_HEADER, "",
                HeaderDetails.IMAGE_CONTENT_TYPE + RequestParser.getFileExtension(request)).get_header();
        readFromImageAndWriteToOutputStream(RequestParser.getPath(request), responseHeader.getBytes());

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
