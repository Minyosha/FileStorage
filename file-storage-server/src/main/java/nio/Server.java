package nio;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Server {
    private ServerSocketChannel server;
    private Selector selector;
    private Path dir = Path.of(System.getProperty("user.home"));
    StringBuilder sb = new StringBuilder();
    String msg = sb.toString().toLowerCase();

    public Server() throws IOException {
        server = ServerSocketChannel.open();
        selector = Selector.open();
        server.bind(new InetSocketAddress(8189));
        server.configureBlocking(false);
        server.register(selector, SelectionKey.OP_ACCEPT);
    }

    public void start() throws IOException {
        while (server.isOpen()) {
            selector.select();
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = keys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                if (key.isAcceptable()) {
                    handleAccept();
                }
                if (key.isReadable()) {
                    handleRead(key);
                }
                iterator.remove();
            }

        }
    }

    private void handleRead(SelectionKey key) throws IOException {
        ByteBuffer buf = ByteBuffer.allocate(1024);
        SocketChannel channel = (SocketChannel) key.channel();
        StringBuilder s = new StringBuilder();
        while (channel.isOpen()) {
            int read = channel.read(buf);
            if (read < 0) {
                channel.close();
                return;
            }
            if (read == 0) {
                break;
            }
            buf.flip();
            while (buf.hasRemaining()) {
                s.append((char) buf.get());
            }
            buf.clear();
        }

        String command = s.toString().trim();
        if (command.startsWith("ls")) {
            String[] list = new File(String.valueOf(dir)).list();
            for (String file : list) {
                channel.write(ByteBuffer.wrap(file.getBytes(UTF_8)));
                channel.write(ByteBuffer.wrap("\r\n".getBytes(UTF_8)));
            }
            channel.write(ByteBuffer.wrap("\r\n->".getBytes(UTF_8)));
        }
        if (command.startsWith("cat")) {
            String[] msg = command.split(" ");
            String fileName = null;

            fileName = msg[1];

            File chosenFile = new File(String.valueOf(dir.resolve(fileName).toFile()));
            if (chosenFile.exists()) {
                List<String> lines = Files.readAllLines(Paths.get(chosenFile.getPath()), UTF_8);
                for (String line : lines) {
                    channel.write(ByteBuffer.wrap(line.getBytes(UTF_8)));
                    channel.write(ByteBuffer.wrap("\r\n".getBytes(UTF_8)));
                }
            } else {
                channel.write(ByteBuffer.wrap("Файл с таким именем не найден\n".getBytes(UTF_8)));
            }
            channel.write(ByteBuffer.wrap("\r\n->".getBytes(UTF_8)));
        }

        if (command.startsWith("cd")) {
            String[] mess = command.split(" ");
            if (mess.length > 2) {
                channel.write(ByteBuffer.wrap("Неверно указана директория\n".getBytes(UTF_8)));
                channel.write(ByteBuffer.wrap("\r\n->".getBytes(UTF_8)));
            } else {
                Path newDir = dir.resolve(mess[1]);
                File d = newDir.toFile();
                if (!(d.isDirectory())) {
                    channel.write(ByteBuffer.wrap("Указаное имя не принадлежит директории\n".getBytes(UTF_8)));
                    channel.write(ByteBuffer.wrap("\r\n->".getBytes(UTF_8)));
                } else {
                    if (d.exists()) {
                        dir = newDir;
                        channel.write(ByteBuffer.wrap(("Вы перешли в директорию " + newDir + " \n").getBytes(UTF_8)));
                        channel.write(ByteBuffer.wrap("\r\n->".getBytes(UTF_8)));
                    } else {
                        channel.write(ByteBuffer.wrap("Не существует данной директории\n".getBytes(UTF_8)));
                        channel.write(ByteBuffer.wrap("\r\n->".getBytes(UTF_8)));
                    }
                }
            }
        }
    }

    private void handleAccept() throws IOException {
        SocketChannel channel = server.accept();
        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_READ);
        channel.write(ByteBuffer.wrap("Connected to server".getBytes(UTF_8)));
    }

}








