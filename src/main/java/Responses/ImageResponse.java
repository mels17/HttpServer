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

        try {
            HttpRequestHandler.out.write(responseHeader.getBytes());
            FileInputStream fis = new FileInputStream(Constants.PUBLIC_DIR_PATH + RequestParser.getPath(request));
            int i;
            while ((i = fis.read()) > -1)
                HttpRequestHandler.out.write(i);

            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
