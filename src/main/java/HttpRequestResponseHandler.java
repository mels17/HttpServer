import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HttpRequestResponseHandler implements Runnable {
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

    private static StringBuilder appendWelcomeString(StringBuilder sb, String name) {
        return sb.append("Hello " + name + "!\r\n\n");
    }


    public void run() {
        System.out.println("Thread started with name:" + Thread.currentThread().getName());
        try {
            readRequest();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
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
        request = new InputStreamReader(_client.getInputStream());
        response = new OutputStreamWriter(_client.getOutputStream());

        BufferedReader in = new BufferedReader(request);

        String req = getRequestHeader(in);

        StringBuilder sb = new StringBuilder();
        if (isGetLogRequest(req)) {
            sb = getResponse(401, "Unauthorized", getParams(req));
        } else if(isRedirectGet(req)) {
            sb = constructRedirectResponse(req);
        } else if (isGeneralRequest(req)) {
            sb = getResponse(200, "OK", getParams(req));
        } else if(isGetRequest(req)) {
            sb = getResponseForTextFile(req);
        } else if(isPatchRequest(req)) {
            System.out.println("HERE");
            sb = getPatchResponse(req);
        } else if(isValidRequest(req)) {
            sb = getResponse(200, "OK", getParams(req));
        } else if(isHeadRequest(req)) {
            sb = getHeadResponse(req);
        } else if(isOptionsRequest(req)) {
            sb = constructOptionsResponse(req);
        }
        response.write(sb.toString());
        sb.setLength(0);
        response.flush();
    }

    private StringBuilder getPatchResponse(String req) {
        StringBuilder sb = new StringBuilder();
        sb.append("HTTP/1.1 " + 204 + " " +  "No Content" + "\r\n\r\n");
        String filename = getPath(req).substring(1);
        System.out.println(filename);
        try {
            overWriteFile(filename, getDataFromRequest(req));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb;
    }

    private String getDataFromRequest(String req) {
        return req.split("\r\n\r\n")[1];
    }

    private void overWriteFile(String filename, String content) throws IOException {
        BufferedWriter out = new BufferedWriter(new FileWriter("/Users/malavika.vasudevan/IdeaProjects/HttpServer/public/" + filename));
        out.write(content);
        out.close();
    }

    private StringBuilder getHeadResponse(String req) {
        StringBuilder sb = new StringBuilder();
        if(getPath(req).equals("/")) {
            sb = constructGeneralResponse(sb, 200, "OK");
            System.out.println(sb.toString());
        } else {
            sb = constructGeneralResponse(sb, 404, "Not Found");
        }
        return sb;
    }

    private static StringBuilder constructRedirectResponse(String req) {
        StringBuilder sb = new StringBuilder();
        sb.append("HTTP/1.1 " + 302 + " " +  "Found" + "\r\n");
        sb.append("Location: /\r\n\r\n");
        sb.append("Date:" + getTimeAndDate() + "\r\n");
        sb.append("Server:localhost\r\n");
        sb.append("Content-Type: text/html\r\n");
        sb.append("Connection: Closed\r\n\n");
        return sb;
    }

    private static StringBuilder constructOptionsResponse(String req) {
        StringBuilder sb = new StringBuilder();
        String path = getPath(req);
        sb.append("HTTP/1.1 " + 200 + " " +  "OK" + "\r\n");
        if(path.equals("/method_options")) {
            sb.append("Allow: GET, HEAD, POST, OPTIONS, PUT\r\n\r\n");
        } else if(path.equals("/method_options2")) {
            sb.append("Allow:GET, OPTIONS, HEAD\r\n\r\n");
        }
        sb.append("Date:" + getTimeAndDate() + "\r\n");
        sb.append("Server:localhost\r\n");
        sb.append("Content-Type: text/html\r\n");
        sb.append("Connection: Closed\r\n\n");
        return sb;
    }

    private static StringBuilder getResponseForTextFile(String req) {
        StringBuilder sb = new StringBuilder();
        String filename = getPath(req).substring(1);
        sb.append("HTTP/1.1 " + 200 + " " +  "OK" + "\r\n\r\n");
        try {
            sb.append(getTextFileContents(filename) + "\r\n");
        } catch (IOException e) {
            sb.append("File Not Found.");
            e.printStackTrace();
        }
        sb.append("Date:" + getTimeAndDate() + "\r\n");
        sb.append("Server:localhost\r\n");
        sb.append("Content-Type: text/html\r\n");
        sb.append("Connection: Closed\r\n\n");
        return sb;
    }
    private static String getTextFileContents(String filename) throws IOException {
        String path = "/Users/malavika.vasudevan/IdeaProjects/HttpServer/public/" + filename;
        return new String(Files.readAllBytes(Paths.get(path)), "UTF-8");
    }

    private static StringBuilder getResponse(int statusCode, String status, String  name) {
        return getListOfFiles(appendWelcomeString(constructGeneralResponse(new StringBuilder(), statusCode, status), name));
    }

    private static StringBuilder constructGeneralResponse(StringBuilder sb, int statusCode, String status) {
        sb.append("HTTP/1.1 " + statusCode + " " +  status + "\r\n\r\n");
        sb.append("Date:" + getTimeAndDate() + "\r\n");
        sb.append("Server:localhost\r\n");
        sb.append("Content-Type: text/html\r\n");
        sb.append("Connection: Closed\r\n\n");

        return sb;
    }

    private String getRequestHeader(BufferedReader request) throws IOException {
        StringBuilder r = new StringBuilder();
        int inputChar;
        do {
            inputChar = request.read();
            r.append(Character.toChars(inputChar));
        }
        while (request.ready());

        System.out.println(r);

        return r.toString();
    }

    private boolean isRedirectGet(String request) {
        return isGetRequest(request) &&  request.split("\\s")[1].equals("/redirect");
    }

    private boolean isGetRequest(String request) {
        return request.startsWith(Constants.GET_REQUEST);
    }

    private boolean isGeneralRequest(String request) {
        return isGetRequest(request) && request.split("\\s")[1].equals("/");
    }

    private boolean isGetLogRequest(String request) {
        return isGetRequest(request) &&  request.split("\\s")[1].equals("/logs");
    }

    private static String getPath(String request) {
        return request.split("\\s")[1];
    }

    private String getParams(String request) {
        String path = request.split("\n")[0].split("\\s")[1];
        if (path.contains("=") && path.contains("&")){
            return path.split("=")[1].split("&")[0];
        }
        return path.contains("=") ? path.split("=")[1] : "World";
    }

    private static String getTimeAndDate() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
        return sdf.format(date);
    }

    private boolean isHeadRequest(String request) {
        return request.startsWith(Constants.HEAD_REQUEST);
    }

    private boolean isOptionsRequest(String request) {
        return request.startsWith(Constants.OPTIONS_REQUEST);
    }

    private boolean isValidRequest(String request) {
        return request.startsWith(Constants.PUT_REQUEST) || request.startsWith(Constants.POST_REQUEST);
    }

    private boolean isPatchRequest(String request) {
        return request.startsWith(Constants.PATCH_REQUEST);
    }
}
