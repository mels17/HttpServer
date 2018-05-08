package Services;

import Entities.RegExHashMap;
import Responses.HttpResponseCommand;
import httpServer.HttpServer;

import java.io.*;
import java.net.Socket;

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

        HttpServer.logs.add(requestString.split("\r\n")[0]);

        RegExHashMap<String, HttpResponseCommand> routeMap = HttpServer.getRouteMap();
        StringBuilder sb = routeMap.get(requestString).process(requestString);
        if (sb != null) {
            response.write(sb.toString());
            sb.setLength(0);
            response.flush();
        }

    }

    public static OutputStream getOutputStream() {
        return out;
    }




}
