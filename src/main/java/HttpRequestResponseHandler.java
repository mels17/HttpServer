import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HttpRequestResponseHandler implements Runnable{
    private File _directory;
    private Socket _client;

    public HttpRequestResponseHandler(Socket clientConnectionSocket, File directory) {
        _client = clientConnectionSocket;
        _directory = directory;
    }

    private String[] getListOfFiles() {
        return _directory.list();
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

        // check if the request contains get

        BufferedWriter response = new BufferedWriter(new OutputStreamWriter(_client.getOutputStream()));

    }

    private static void constructResponseHeader(int statusCode, StringBuilder sb) {
        sb.append("HTTP/1.1 200 OK\r\n");
        sb.append("Date:" + getTimeAndDate() + "\r\n");
        sb.append("Server:localhost\r\n");
        sb.append("Content-Type: text/html\r\n");
        sb.append("Connection: Closed\r\n\r\n");
    }

    private static String getTimeAndDate() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
        return sdf.format(date);
    }
}
