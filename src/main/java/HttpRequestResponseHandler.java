import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HttpRequestResponseHandler implements Runnable{
    private static File _directory;
    private Socket _client;
    InputStreamReader request;
    OutputStreamWriter response;

    public HttpRequestResponseHandler(Socket clientConnectionSocket, File directory) {
        _client = clientConnectionSocket;
        _directory = directory;
    }

    private static StringBuilder getListOfFiles(StringBuilder sb) {
        return sb.append("Below are the list of files:\r\n").append(String.join("\r\n", _directory.list()));
    }


    public void run() {
        System.out.println("Thread started with name:" + Thread.currentThread().getName());
        try {


            readRequest();



        } catch (IOException e) {
            e.printStackTrace();
        }
finally {
            closeConnection();
        }
//        return;
    }


    private void closeConnection() {
        try {
            request.close();
            response.close();
            _client.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readRequest() throws IOException {
//        if (_client.getInputStream().available() <= 0) {
//            return;
//        }

        request = new InputStreamReader(_client.getInputStream());
        response = new OutputStreamWriter(_client.getOutputStream());

        BufferedReader in = new BufferedReader(request);

        String req = getRequestHeader(in);


        if (isGetRequest(req)){
            StringBuilder sb = getResponse(200);
            response.write(sb.toString());
            sb.setLength(0);
            response.flush();
        }
    }

    private static StringBuilder getResponse(int statusCode) {
        return getListOfFiles(constructResponseHeader(new StringBuilder(), statusCode));
    }

    private static StringBuilder constructResponseHeader(StringBuilder sb, int statusCode) {
        sb.append("HTTP/1.1 200 OK\r\n\r\n");
        sb.append("HTTP/1.1 200 OK\r\n");
        sb.append("Date:" + getTimeAndDate() + "\r\n");
        sb.append("Server:localhost\r\n");
        sb.append("Content-Type: text/html\r\n");
        sb.append("Connection: Closed\r\n\n\n");

        return sb;
    }

    private String getRequestHeader(BufferedReader request) throws IOException {
        String temp = ".";
        String requestHeader = "";
        StringBuilder r = new StringBuilder();
        int inputChar;
//        while(!temp.equals("")) {
//            temp = request.readLine();
//            requestHeader += temp + "\n";
//        }

        do {
            inputChar = request.read();
            r.append(Character.toChars(inputChar));
        }
        while (request.ready());

        System.out.println(r);

        return r.toString();
    }

    private boolean isGetRequest(String request) throws IOException {
//        return request.startsWith("POST");
        return true;
    }

    private static String getTimeAndDate() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
        return sdf.format(date);
    }
}
