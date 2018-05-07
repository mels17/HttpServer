import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileOperations {

    public static void overWriteFile(String filename, String content) throws IOException {
        BufferedWriter out = new BufferedWriter(new FileWriter(Constants.PUBLIC_DIR_PATH + filename));
        out.write(content);
        out.close();
    }

    public static String getTextFileContents(String filename) throws IOException {
        String path = Constants.PUBLIC_DIR_PATH + filename;
        return new String(Files.readAllBytes(Paths.get(path)), "UTF-8");
    }

    public static void deleteFile(String filename) {
        File file = new File(Constants.PUBLIC_DIR_PATH + filename);
        if (file.exists() && !file.isDirectory()) file.delete();
    }

    public static byte[] getPartialContentFromFile(ByteRange byteRange, String filename) {
        Long contentLength = byteRange.get_end() - byteRange.get_start() + 1;
        byte[] content = new byte[Math.toIntExact(contentLength)];
        File file = new File(Constants.PUBLIC_DIR_PATH + filename);
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(file, "r");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            raf.seek(byteRange.get_start());
            raf.readFully(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }
}
