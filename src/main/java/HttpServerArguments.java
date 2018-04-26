public class HttpServerArguments {
    private int _port;
    private String _directory;

    public HttpServerArguments(int port, String directory) {
        _port = port;
        _directory = directory;
    }

    public static HttpServerArguments createServerArguments(String[] args) {
        if (args.length < 4) return new HttpServerArguments(Constants.PORT, Constants.DIRECTORY);

        int port = Constants.PORT;
        String directory = Constants.DIRECTORY;
        for (int i = 0; i < args.length; i++) {
            if (args[i]== null) continue;
            if (args[i].equals("-p")) port = Integer.parseInt(args[i+1]);
            if (args[i].equals("-d")) directory = args[i+1];
        }
        return new HttpServerArguments(port, directory);
    }

    public int getPort() {
        return _port;
    }

    public String getDirectory() {
        return _directory;
    }
}
