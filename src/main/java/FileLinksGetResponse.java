import java.io.File;
public class FileLinksGetResponse implements HttpResponseCommand {
    @Override
    public StringBuilder process(String request) {
        File _directory = new File("/Users/malavika.vasudevan/IdeaProjects/HttpServer/public");;
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
}
