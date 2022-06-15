package nio;

import java.io.IOException;

public class StartClient {
    public static void main(String[] args) throws IOException {
        Client nioClient = new Client();
        nioClient.start();
    }
}
