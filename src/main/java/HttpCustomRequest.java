import java.net.Socket;

public class HttpCustomRequest implements Runnable{
    Socket _clientSocket;

    public HttpCustomRequest(Socket clientConnectionSocket, String directory) {
        _clientSocket = clientConnectionSocket;
    }

    @Override
    public void run() {

    }
}
