package Services;

import Entities.RegExHashMap;
import Entities.Request;
import Entities.Response;
import Responses.DefaultResponse;
import Responses.HttpResponseCommand;
import httpServer.HttpServer;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;

public class HttpRequestHandler implements Runnable {
    private static File _directory;
    private Socket _client;
    InputStreamReader request;
    OutputStreamWriter response;
    private static OutputStream out;

    public HttpRequestHandler(Socket clientConnectionSocket, File directory) {
        _client = clientConnectionSocket;
        _directory = directory;
    }

    public void run() {
        System.out.println("Thread started with name:" + Thread.currentThread().getName());
        try {
            readRequest();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
            System.out.println("Thread closed.");
        }
    }

    private void closeConnection() {
        try {
            request.close();
            response.close();
            _client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readRequest() throws IOException {
        out = _client.getOutputStream();
        request = new InputStreamReader(_client.getInputStream());
        response = new OutputStreamWriter(out);
        BufferedReader in = new BufferedReader(request);

        String requestString = RequestParser.getRequestString(in);
        Request requestObj = new Request(requestString);

        HttpServer.logs.add(requestString.split("\r\n")[0]);

//        RegExHashMap<String, HttpResponseCommand> routeMap = HttpServer.getRouteMap();
        HashMap<String, HttpResponseCommand> routeMap = HttpServer.getGeneralRequestRouter();
        Response responseObj = routeMap.getOrDefault(requestObj.get_requestType(), new DefaultResponse()).process(requestObj);
        if (responseObj != null) {
            response.write(responseObj.get_header() + responseObj.get_body());
            response.flush();
        }

    }

    public static OutputStream getOutputStream() {
        return out;
    }


}
