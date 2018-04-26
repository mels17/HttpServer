import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class HttpRequestResponseHandler implements Runnable{
    private File _directory;
    private Socket _client;

    public HttpRequestResponseHandler(Socket clientConnectionSocket, File directory) {
        _client = clientConnectionSocket;
        _directory = directory;
    }

    private String getListOfFiles() {
        return String.join("\n", _directory.list());
    }


    public void run() {
        System.out.println("Thread started with name:" + Thread.currentThread().getName());
        try {
            readRequest();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return;
    }

    private void readRequest() throws IOException {
        BufferedReader request = new BufferedReader(new InputStreamReader(_client.getInputStream()));
        BufferedWriter response = new BufferedWriter(new OutputStreamWriter(_client.getOutputStream()));
        if (isGetRequest(request)){
            StringBuilder sb = constructResponseHeader(200);
            response.write(sb.toString());
            response.write(getListOfFiles());
            sb.setLength(0);
            response.flush();
        }
    }

    private static StringBuilder constructResponseHeader(int statusCode) {
        StringBuilder sb = new StringBuilder();
        sb.append("HTTP/1.1 200 OK\r\n");
        sb.append("Date:" + getTimeAndDate() + "\r\n");
        sb.append("Server:localhost\r\n");
        sb.append("Content-Type: text/html\r\n");
        sb.append("Connection: Closed\r\n\r\n");

        return sb;
    }

    private String getRequestHeader(BufferedReader request) throws IOException {
        String temp = ".";
        String requestHeader = "";
        while(!temp.equals("")) {
            temp = request.readLine();
            requestHeader += temp + "\n";
        }
        return requestHeader;
    }

    private boolean isGetRequest(BufferedReader request) throws IOException {
        return getRequestHeader(request).split("\n")[0].contains("GET");
    }

    private static String getTimeAndDate() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
        return sdf.format(date);
    }
}
