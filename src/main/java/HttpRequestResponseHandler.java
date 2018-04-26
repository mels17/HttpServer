import java.io.*;
import java.net.Socket;

public class HttpRequestResponseHandler implements Runnable{
    private String _directory;
    Socket _client;

    public HttpRequestResponseHandler(Socket clientConnectionSocket, String directory) {
        _client = clientConnectionSocket;
        _directory = directory;
    }

    @Override
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

    }
}
