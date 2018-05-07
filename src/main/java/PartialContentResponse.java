import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class PartialContentResponse implements HttpResponseCommand{
    @Override
    public StringBuilder process(String request){
        StringBuilder sb = new StringBuilder();

        String path = RequestParser.getPath(request);
        Long bytes = new File("/Users/malavika.vasudevan/IdeaProjects/HttpServer/public/" + path.substring(1)).length();

        Long start = Long.valueOf("-1");
        Long end = Long.valueOf("-1");
        String[] headers = request.split("\r\n");
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
            RandomAccessFile raf = null;
            try {
                raf = new RandomAccessFile(file, "r");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                raf.seek(start);
                raf.readFully(content);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(start == Long.valueOf(0) && end == Long.valueOf(bytes-1)){
                sb.append("HTTP/1.1 " + 416 + " " + "Range Not Satisfiable" + "\r\n");
                sb.append("Content-Range: bytes */" + bytes.toString() + "\r\n");
            } else {
                sb.append("HTTP/1.1 " + 206 + " " + "Partial content" + "\r\n");
                sb.append("Content-Range: bytes " + start.toString() + "-" + end.toString() + "/" + bytes.toString() + "\r\n");
            }

            sb.append("Content-Length: " + Long.toString(contentLength) + "\r\n");
            sb.append("Server:localhost\r\n");
            sb.append("Content-Type: text/plain\r\n");
            sb.append("Connection: Closed\r\n\r\n");
            sb.append(new String(content));
        }

        return sb;
    }
}
