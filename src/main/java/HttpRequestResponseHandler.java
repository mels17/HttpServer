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

    private static ByteRange getContentRange(String req) {
        String[] reqContent = req.split("\r\n");
        ByteRange br = new ByteRange();
        for (String line: reqContent) {
            if(line.startsWith("Range")){
                String[] range = line.split("\\s")[2].split("/")[0].split("-");
                br.setStart(Integer.parseInt(range[0]));
                br.setEnd(Integer.parseInt(range[1]));
            }
        }
        return br;
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
            sb = getPatchResponse(req);
        } else if(isPutRequest(req)) {
            sb = getPutResponse(req);
        } else if(isPostRequest(req)){
            sb = getPostResponse(req);
        } else if(isHeadRequest(req)) {
            sb = getHeadResponse(req);
        } else if(isOptionsRequest(req)) {
            sb = constructOptionsResponse(req);
        } else if(isDeleteRequest(req)) {
            sb = getDeleteResponse(req);
        }
        response.write(sb.toString());
        sb.setLength(0);
        response.flush();
    }

    private StringBuilder getPutResponse(String req) {
        String filename = getPath(req);
        if(!filename.equals("/")) {
//            if(!filename.contains(".")) filename += ".txt";
            String content = getDataFromRequest(req);
            try {
                overWriteFile(filename, content);
            } catch (IOException e) {
                return constructGeneralResponse(new StringBuilder(), 404, "Not Found");
            }
        }
        return getResponse(200, "OK", getParams(req));
    }

    private StringBuilder getDeleteResponse(String req) {
        String path = getPath(req);
        deleteFile(path);

        StringBuilder sb = new StringBuilder();
        sb.append("HTTP/1.1 " + 200 + " " +  "OK" + "\r\n");

        return sb;
    }

    private void deleteFile(String filename) {
        String filePath = "/Users/malavika.vasudevan/IdeaProjects/HttpServer/public";
//        if(!filename.contains(".")) filename += ".txt";
        File file = new File(filePath + filename);
        if(file.exists() && !file.isDirectory()) file.delete();
    }


    private static StringBuilder getPostResponse(String request) {
        String path = getPath(request);
        String filePath = "/Users/malavika.vasudevan/IdeaProjects/HttpServer/public/";
        String content;
        if(path.equals("/cat-form")) {
            filePath += path.substring(1);
            content = getDataFromRequest(request);
            String fileName = content.split("=")[0];
            try {
                File file = new File(filePath + "/" + fileName);
                BufferedWriter output = new BufferedWriter(new FileWriter(file));
                output.write(content);
                output.flush();
                output.close();
                return getPostResponseHeaderWithRedirect(fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return getResponse(200, "OK", getParams(request));
    }

    private static StringBuilder getPostResponseHeaderWithRedirect(String newFile) {
        StringBuilder sb = new StringBuilder();
        sb.append("HTTP/1.1 " + 201 + " " +  "Created" + "\r\n");
        sb.append("Location: /cat-form/" + newFile + "\r\n\r\n");

        return sb;
    }

    private StringBuilder getPatchResponse(String req) {
        StringBuilder sb = new StringBuilder();
        sb.append("HTTP/1.1 " + 204 + " " +  "No Content" + "\r\n\r\n");
        String filename = getPath(req).substring(1);
        try {
            overWriteFile(filename, getDataFromRequest(req));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb;
    }

    private static String getDataFromRequest(String req) {
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
        String filename = getPath(req).substring(1);
        StringBuilder sb = new StringBuilder();

        if(containsContentRangeInRequestHeader(req)) {
            sb = getPartialResponseHeader(sb, req, filename);
        } else {
            sb.append("HTTP/1.1 " + 200 + " " +  "OK" + "\r\n\r\n");
        }

        try {
            sb.append(getTextFileContents(filename) + "\r\n");
        } catch (IOException e) {
            sb = new StringBuilder();
            sb.append("HTTP/1.1 " + 404 + " " +  "Not Found" + "\r\n\r\n");
            sb.append("File Not Found.");
            e.printStackTrace();
        }
        sb.append("Date:" + getTimeAndDate() + "\r\n");
        sb.append("Server:localhost\r\n");
        sb.append("Content-Type: text/html\r\n");
        sb.append("Connection: Closed\r\n\n");
        return sb;
    }

    private static StringBuilder getPartialResponseHeader(StringBuilder sb, String request, String filename) {
        ByteRange br = getContentRange(request);
        sb.append("HTTP/1.1 " + 206 + " " +  "Partial Content" + "\r\n");
        sb.append("Accept-Ranges: bytes");
        sb.append("Content-Range: bytes " + br.get_start() + "-" + br.get_end() + "\r\n\r\n");

        return sb;
    }

    private static String getTextFileContents(String filename) throws IOException {
//        if(!filename.contains(".")) {
//            filename += ".txt";
//        }
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

    private static String getParams(String request) {
        String path = request.split("\n")[0].split("\\s")[1];
        if (path.contains("=") && path.contains("&")){
            return path.split("=")[1].split("&")[0];
        }
        return path.contains("=") ? path.split("=")[1] : "World";
    }

//    private static ByteRange getByteRangeParams(String request) {
//        ByteRange br = new ByteRange();
//        String path = getPath(request);
//        String[] params = path.split("&");
//        br.setStart(Integer.parseInt(params[0].split("=")[1]));
//        br.setEnd(Integer.parseInt(params[1].split("=")[1]));
//        return br;
//    }

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

    private boolean isPostRequest(String request) {
        return request.startsWith(Constants.POST_REQUEST);
    }

    private boolean isPutRequest(String request) {
        return request.startsWith(Constants.PUT_REQUEST);
    }

    private static boolean containsContentRangeInRequestHeader(String request) {
        return request.contains("Range");
    }
    private boolean isPatchRequest(String request) {
        return request.startsWith(Constants.PATCH_REQUEST);
    }

    private boolean isDeleteRequest(String req) {
        return req.startsWith(Constants.DELETE_REQUEST);
    }
}
