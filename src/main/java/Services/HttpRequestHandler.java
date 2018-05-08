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
    public static OutputStream out;

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

        //* matt
        /****
         *
         * LOOK HERE
         */

//        map { paths, objects }
//        map { paths, "objects that implement Responses.HttpResponseCommand" }
//
//
//        HashMap<String, Responses.HttpResponseCommand> mn = new Map();
//        new Map( "/trea", new Responses.TeaPartyResponse());
//
//        command = map("/tea") => TeaPartyCommand
//                map(null, new ErrorCommand);
//
//
//        command.process();
//
//
//        do(while)
//            commandString = reader.read(request);
//            handler = map.get(commandString);
//            reponse = handler.process();
//            return reponse;

        HttpServer.logs.add(requestString.split("\r\n")[0]);

        RegExHashMap<String, HttpResponseCommand> routeMap = HttpServer.initializeRegexMap();
        StringBuilder sb;
        sb = routeMap.get(requestString).process(requestString);


        if (sb != null) {
            response.write(sb.toString());
            sb.setLength(0);
            response.flush();
        }

    }




}
