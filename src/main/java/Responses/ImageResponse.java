package Responses;

import Entities.Constants;
import Services.HttpRequestHandler;
import Services.RequestParser;
import Services.ResponseConstructor;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class ImageResponse implements HttpResponseCommand {
    @Override
    public StringBuilder process(String request) {
        String extension = RequestParser.getFileExtension(request);
        String responseHeader = ResponseConstructor.constructImageHeader(extension);

        readFromImageAndWriteToOutputStream(RequestParser.getPath(request), responseHeader.getBytes());

        return null;
    }

    private void readFromImageAndWriteToOutputStream(String filename,  byte[] responseHeaderBytes) {
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
