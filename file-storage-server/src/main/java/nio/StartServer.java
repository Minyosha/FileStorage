package nio;

import java.io.IOException;
import java.nio.file.Path;

public class StartServer {
    public static void main(String[] args) throws IOException {
        Server nioServer = new Server();
        nioServer.start();
    }
}



