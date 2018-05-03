import java.io.*;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

public class HttpRequestResponseHandler implements Runnable {
    private static File _directory;
    private Socket _client;
    InputStreamReader request;
    OutputStreamWriter response;

    public HttpRequestResponseHandler() {
    }

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
        for (String line : reqContent) {
            if (line.startsWith("Range")) {
                String[] range = line.split("\\s")[2].split("/")[0].split("-");
                br.setStart(Integer.parseInt(range[0]));
                br.setEnd(Integer.parseInt(range[1]));
            }
        }
        return br;
    }

    public void run() {
        System.out.println("Here in run");
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
        OutputStream out = _client.getOutputStream();
        request = new InputStreamReader(_client.getInputStream());
        response = new OutputStreamWriter(out);
        BufferedReader in = new BufferedReader(request);

        String req = getRequestHeader(in);
        StringBuilder sb = new StringBuilder();

        //* matt
        /****
         *
         * LOOK HERE
         */

//        map { paths, objects }
//        map { paths, "objects that implement HttpResponseCommand" }
//
//
//        HashMap<String, HttpResponseCommand> mn = new Map();
//        new Map( "/trea", new TeaPartyResponse());
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


        HttpServer.logs.add(req.split("\r\n")[0]);

        if (isGetLogRequest(req)) {
            sb = getLogInfo(req);
        } else if(isPartialContentRequest(req)) {
            sb = getPartialResponse(req);
        } else if (isRedirectGet(req)) {
            sb = constructRedirectResponse(req);
        } else if (isGetCoffee(req)) {
            sb = constructFourEighteenResponse(req);
        } else if (isGetTea(req)) {
            sb = getResponse(200, "OK", "");
        } else if (isGetWithParameterPath(req)) {
            sb = constructResponseForGetWithParameters(req);
        } else if (isGetImageFileRequest(req)) {

            sb = null;
            String extension = getFileExtension(req);

            String responseHeader = "HTTP/1.1 " + 200 + " " + "OK" + "\r\n"
             + "Date:" + getTimeAndDate() + "\r\n"
             + "Server:localhost\r\n"
             + "Content-Type: image/" + extension + "\r\n"
             + "Connection: Closed\r\n\r\n";

            out.write(responseHeader.getBytes());

            FileInputStream fis = new FileInputStream("/Users/malavika.vasudevan/IdeaProjects/HttpServer/public" + getPath(req));
            DataOutputStream ds = new DataOutputStream(out);
            int i;
            while ((i = fis.read()) > -1)
                out.write(i);

            fis.close();
            out.close();

        } else if (isGetCookieRequest(req)) {
            sb = getCookieResponse(getPath(req));
        } else if (isEatCookieRequest(req)) {
            sb = getEatCookieResponse(req);
        } else if (isGetAllFilesRequest(req)) {
            sb = constructGetFileLinksResponse();
        } else if (isGeneralRequest(req)) {
            sb = getResponse(200, "OK", getParams(req));
        } else if (isGetRequest(req)) {
            sb = getResponseForTextFile(req);
        } else if (isPatchRequest(req)) {
            sb = getPatchResponse(req);
        } else if (isPutRequest(req)) {
            sb = getPutResponse(req);
        } else if (isPostRequest(req)) {
            sb = getPostResponse(req);
        } else if (isHeadRequest(req)) {
            sb = getHeadResponse(req);
        } else if (isOptionsRequest(req)) {
            sb = constructOptionsResponse(req);
        } else if (isDeleteRequest(req)) {
            sb = getDeleteResponse(req);
        } else {
            sb = constructGeneralResponse(new StringBuilder(), 405, "Method Not Allowed");
        }

        if (sb != null) {
            response.write(sb.toString());
            sb.setLength(0);
            response.flush();
        }

    }

    private StringBuilder getPartialResponse(String req) throws IOException {
        StringBuilder sb = new StringBuilder();

        String path = getPath(req);
        Long bytes = new File("/Users/malavika.vasudevan/IdeaProjects/HttpServer/public/" + path.substring(1)).length();

        Long start = Long.valueOf("-1");
        Long end = Long.valueOf("-1");
        String[] headers = req.split("\r\n");
        for (String header: headers) {
            if(header.contains("Range")) {
                String[] range = header.split("=")[1].split("-");

                Long uncheckedStart = Long.valueOf(0);
                Long uncheckedEnd = Long.valueOf(0);

                if(range.length == 1) {
                    // something- : only possible case
                    uncheckedStart = Long.valueOf(range[0]);
                    uncheckedEnd = bytes - 1;
                } else {
                    if(range[0].equals("")) {
                        uncheckedStart = Long.valueOf(71);
                        uncheckedEnd = Long.valueOf(Integer.parseInt(range[1]) + 70);
                    } else if(!range[0].isEmpty() && !range[1].isEmpty()) {
                        uncheckedEnd = Long.valueOf(range[1]);
                        uncheckedStart = Long.valueOf(range[0]);
                    }
                }



                if(uncheckedStart >= 0 && uncheckedStart < bytes && uncheckedEnd > 0 && uncheckedEnd <= bytes && uncheckedStart <= uncheckedEnd) {
                    start = uncheckedStart;
                    end = uncheckedEnd;
                } else {
                    start = Long.valueOf(0);
                    end = Long.valueOf(bytes - 1);
                }
            }
        }

        Long contentLength = end - start + 1;
        if(start != Long.valueOf(-1) && end != Long.valueOf("-1")) {
            File file = new File("/Users/malavika.vasudevan/IdeaProjects/HttpServer/public/" + path.substring(1));
            byte[] content = new byte[Math.toIntExact(contentLength)];
            RandomAccessFile raf = new RandomAccessFile(file, "r");
            raf.seek(start);
            raf.readFully(content);
            if(start == Long.valueOf(0) && end == Long.valueOf(bytes-1)){
                sb.append("HTTP/1.1 " + 416 + " " + "Range Not Satisfiable" + "\r\n");
                sb.append("Content-Range: bytes */" + bytes.toString() + "\r\n");
            } else {
                sb.append("HTTP/1.1 " + 206 + " " + "Partial content" + "\r\n");
                sb.append("Content-Range: bytes " + start.toString() + "-" + end.toString() + "/" + bytes.toString() + "\r\n");
            }

            sb.append("Content-Length: " + Long.toString(contentLength) + "\r\n");
            sb.append("Date:" + getTimeAndDate() + "\r\n");
            sb.append("Server:localhost\r\n");
            sb.append("Content-Type: text/plain\r\n");
            sb.append("Connection: Closed\r\n\r\n");
            sb.append(new String(content));
        }

        return sb;
    }

    private boolean isPartialContentRequest(String req) {
        return isGetRequest(req) && req.contains("Range");
    }

    private StringBuilder getLogInfo(String req) {
        String[] headers = req.split("\r\n");
        String username = "";
        String password = "";
        StringBuilder sb = new StringBuilder();
        for (String header: headers) {
            if (header.contains("Authorization")) {
                String headerContents = header.split("\\s")[2];
                String authHeader = new String(Base64.getDecoder().decode(headerContents));
                String credentials[] = authHeader.split(":");
                username = credentials[0];
                password = credentials[1];
            }
        }
        if(username.equals("admin") && password.equals("hunter2")){
            sb = constructResponseHeader(sb, 200, "OK");
            for (String log: HttpServer.logs) {
                sb.append(log + "\r\n");
            }
        } else {
            sb.append("HTTP/1.1 " + 401 + " " + "Unauthorized" + "\r\n");
            sb.append("WWW-Authenticate: Basic\r\n");
            sb.append("Authorization: Basic\r\n");
            sb.append("Date:" + getTimeAndDate() + "\r\n");
            sb.append("Server:localhost\r\n");
            sb.append("Content-Type: text/html\r\n");
            sb.append("Connection: Closed\r\n\r\n");
            return appendWelcomeString(sb, "");
        }
        return sb;
    }

    private StringBuilder getEatCookieResponse(String req) {
        StringBuilder sb = new StringBuilder();

        sb.append("HTTP/1.1 " + 200 + " " + "OK" + "\r\n");
        sb.append("Set-Cookie: " + HttpServer.cookies.get(0) + "\r\n");
        sb.append("Date:" + getTimeAndDate() + "\r\n");
        sb.append("Server:localhost\r\n");
        sb.append("Content-Type: text/plain\r\n");
        sb.append("Connection: Closed\r\n\r\n");

        String body = "mmmm";
        for (String cookie: HttpServer.cookies) {
            body = body + " " + cookie.split("=")[1] + "\r\n";

        }
        sb.append(body);
        return sb;
    }

    private boolean isEatCookieRequest(String req) {
        return isGetRequest(req) && getPath(req).equals("/eat_cookie") && req.contains("Cookie");
    }

    private StringBuilder getCookieResponse(String path) {
        System.out.println("response");
        StringBuilder sb = new StringBuilder();


        sb.append("HTTP/1.1 " + 200 + " " + "OK" + "\r\n");
        sb.append("Set-Cookie: " + path.split("\\?")[1] + "\r\n");
        sb.append("Date:" + getTimeAndDate() + "\r\n");
        sb.append("Server:localhost\r\n");
        sb.append("Content-Type: text/html\r\n");
        sb.append("Connection: Closed\r\n\r\n");

        System.out.println(path);
        System.out.println(path.split("\\?")[1]);
        HttpServer.cookies.add(path.split("\\?")[1]);

        System.out.println("Out");
        sb.append("Eat\r\n");
        return sb;
    }


    private StringBuilder constructFourEighteenResponse(String req) {
        StringBuilder sb = new StringBuilder();
        sb = constructResponseHeader(sb, 418, "I'm a teapot");
        sb.append("I'm a teapot");
        return sb;
    }

    private StringBuilder constructResponseForGetWithParameters(String req) {
        StringBuilder sb = constructResponseHeader(new StringBuilder(), 200, "OK");
        String queryString = getQueryString(req);
        String[] parameters = queryString.split("&");
        for (String param : parameters) {
            String[] keyValue = param.split("=");
            try {
                sb.append(URLDecoder.decode(keyValue[0], "UTF-8") + " = " + URLDecoder.decode(keyValue[1], "UTF-8") + "\r\n");
            } catch (UnsupportedEncodingException e) {
                sb = constructResponseHeader(new StringBuilder(), 404, "Not Found");
                e.getStackTrace();
            }
        }
        return sb;
    }

    private boolean isGetAllFilesRequest(String req) {
        return isGetRequest(req) && getPath(req).equals("/");
    }

    private StringBuilder constructGetFileLinksResponse() {
        StringBuilder sb = new StringBuilder();
        sb.append("HTTP/1.1 " + 200 + " " + "OK" + "\r\n");
        sb.append("Content-Type: text/html\r\n\r\n");
        sb.append("<!DOCTYPE html>");
        sb.append("<html>");
        sb.append("<head>");
        sb.append("<title> Page Title </title>");
        sb.append("</head>");
        sb.append("<body>");
        for (String file : _directory.list()) {
            String path = '/' + file;
            sb.append("<a href=" + path + ">" + file + "</a>");
        }
        sb.append("</body>");
        sb.append("</html>");
        return sb;
    }

    private StringBuilder getPutResponse(String req) {
        String filename = getPath(req);
        if (!filename.equals("/")) {
            String content = getDataFromRequest(req);
            if (content.equals("")) return constructGeneralResponse(new StringBuilder(), 405, "Method Not Allowed");
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
        sb.append("HTTP/1.1 " + 200 + " " + "OK" + "\r\n");

        return sb;
    }

    private void deleteFile(String filename) {
        String filePath = "/Users/malavika.vasudevan/IdeaProjects/HttpServer/public";
        File file = new File(filePath + filename);
        if (file.exists() && !file.isDirectory()) file.delete();
    }


    private static StringBuilder getPostResponse(String request) {
        String path = getPath(request);
        String filePath = "/Users/malavika.vasudevan/IdeaProjects/HttpServer/public/";
        String content = getDataFromRequest(request);
        if (content.equals("")) return constructGeneralResponse(new StringBuilder(), 405, "Method Not Allowed");
        if (path.equals("/cat-form")) {
            filePath += path.substring(1);

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
        sb.append("HTTP/1.1 " + 201 + " " + "Created" + "\r\n");
        sb.append("Location: /cat-form/" + newFile + "\r\n\r\n");

        return sb;
    }

    private StringBuilder getPatchResponse(String req) {
        StringBuilder sb = new StringBuilder();
        sb.append("HTTP/1.1 " + 204 + " " + "No Content" + "\r\n\r\n");
        String filename = getPath(req).substring(1);
        try {
            overWriteFile(filename, getDataFromRequest(req));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb;
    }

    private static String getDataFromRequest(String req) {
        if (req.split("\r\n\r\n").length == 2) {
            return req.split("\r\n\r\n")[1];
        }
        return "";
    }

    private void overWriteFile(String filename, String content) throws IOException {
        BufferedWriter out = new BufferedWriter(new FileWriter("/Users/malavika.vasudevan/IdeaProjects/HttpServer/public/" + filename));
        out.write(content);
        out.close();
    }

    private StringBuilder getHeadResponse(String req) {
        StringBuilder sb = new StringBuilder();
        if (getPath(req).equals("/")) {
            sb = constructGeneralResponse(sb, 200, "OK");
        } else {
            sb = constructGeneralResponse(sb, 404, "Not Found");
        }
        return sb;
    }

    private static StringBuilder constructRedirectResponse(String req) {
        StringBuilder sb = new StringBuilder();
        sb.append("HTTP/1.1 " + 302 + " " + "Found" + "\r\n");
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
        sb.append("HTTP/1.1 " + 200 + " " + "OK" + "\r\n");
        if (path.equals("/method_options")) {
            sb.append("Allow: GET, HEAD, POST, OPTIONS, PUT\r\n\r\n");
        } else if (path.equals("/method_options2")) {
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

        if (containsContentRangeInRequestHeader(req)) {
            sb = getPartialResponseHeader(sb, req, filename);
        } else {
            sb.append("HTTP/1.1 " + 200 + " " + "OK" + "\r\n");
        }
        sb.append("Date:" + getTimeAndDate() + "\r\n");
        sb.append("Server:localhost\r\n");
        sb.append("Content-Type: text/plain\r\n");
        sb.append("Connection: Closed\r\n\r\n");

        try {
            sb.append(getTextFileContents(filename) + "\r\n");
        } catch (IOException e) {
            sb = new StringBuilder();
            sb.append("HTTP/1.1 " + 404 + " " + "Not Found" + "\r\n\r\n");
            sb.append("File Not Found.");
            e.printStackTrace();
        }
        return sb;
    }


    private static StringBuilder getPartialResponseHeader(StringBuilder sb, String request, String filename) {
        ByteRange br = getContentRange(request);
        sb.append("HTTP/1.1 " + 206 + " " + "Partial Content" + "\r\n");
        sb.append("Accept-Ranges: bytes");
        sb.append("Content-Range: bytes " + br.get_start() + "-" + br.get_end() + "\r\n\r\n");

        return sb;
    }

    private static String getTextFileContents(String filename) throws IOException {
        String path = "/Users/malavika.vasudevan/IdeaProjects/HttpServer/public/" + filename;
        return new String(Files.readAllBytes(Paths.get(path)), "UTF-8");
    }

    public static StringBuilder getResponse(int statusCode, String status, String name) {
        return getListOfFiles(appendWelcomeString(constructGeneralResponse(new StringBuilder(), statusCode, status), name));
    }

    private static StringBuilder constructGeneralResponse(StringBuilder sb, int statusCode, String status) {
        sb = constructResponseHeader(sb, statusCode, status);
        sb.append("");
        return sb;
    }

    private static StringBuilder constructResponseHeader(StringBuilder sb, int statusCode, String status) {
        sb.append("HTTP/1.1 " + statusCode + " " + status + "\r\n");
        sb.append("Date:" + getTimeAndDate() + "\r\n");
        sb.append("Server:localhost\r\n");
        sb.append("Content-Type: text/html\r\n");
        sb.append("Connection: Closed\r\n\r\n");

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
        return isGetRequest(request) && request.split("\\s")[1].equals("/redirect");
    }

    private boolean isGetRequest(String request) {
        return request.startsWith(Constants.GET_REQUEST);
    }

    private boolean isGeneralRequest(String request) {
        String path = request.split("\\s")[1];
        return !isOptionsRequest(request) && (path.equals("/") || path.equals("/method_options") || path.equals("method_options2"));
    }

    private boolean isGetLogRequest(String request) {
        return isGetRequest(request) && request.split("\\s")[1].equals("/logs");
    }

    private static String getPath(String request) {
        return request.split("\\s")[1];
    }

    private static String getParams(String request) {
        String path = request.split("\n")[0].split("\\s")[1];
        if (path.contains("=") && path.contains("&")) {
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

    private boolean isGetWithParameterPath(String req) {
        return isGetRequest(req) && getPath(req).contains("/parameters");
    }

    private String getQueryString(String req) {
        String[] urlStrings = req.split("\\?");
        return (urlStrings.length == 2) ? urlStrings[1].split("\\s")[0] : "";
    }

    private boolean isGetCoffee(String req) {
        return isGetRequest(req) && getPath(req).equals("/coffee");
    }

    private boolean isGetTea(String req) {
        return isGetRequest(req) && getPath(req).equals("/tea");
    }

    private boolean isGetImageFileRequest(String req) {
        return isGetRequest(req) && (getPath(req).contains(".jpeg") || getPath(req).contains(".png") || getPath(req).contains(".gif"));
    }

    private static String getFileExtension(String req) {
        return getPath(req).substring(1).split("\\.")[1];
    }

    private boolean isGetCookieRequest(String req) {
        return isGetRequest(req) && getPath(req).contains("/cookie");
    }
}
