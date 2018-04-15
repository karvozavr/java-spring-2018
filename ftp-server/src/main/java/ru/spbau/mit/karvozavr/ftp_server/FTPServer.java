package ru.spbau.mit.karvozavr.ftp_server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class FTPServer implements Runnable {

    private ExecutorService queryHandlerPool;
    private ServerSocketChannel serverSocket;
    private FTPServerConfiguration config;

    public FTPServer(FTPServerConfiguration config) throws IOException {
        this.config = config;
        if (!Files.isDirectory(config.serverRootDirectory)) {
            throw new IllegalArgumentException("Not a directory.");
        }
        queryHandlerPool = Executors.newFixedThreadPool(config.connectionsNumber);

        serverSocket = ServerSocketChannel.open();
        serverSocket.configureBlocking(true);
        serverSocket.bind(new InetSocketAddress(0));
    }


    public static class FTPServerConfiguration {
        public final int connectionsNumber;
        public final long transferSize;
        public final String encoding;
        public final Path serverRootDirectory;

        public FTPServerConfiguration(int connectionsNumber, long transferSize, String encoding, Path serverDirectory) {
            this.connectionsNumber = connectionsNumber;
            this.transferSize = transferSize;
            this.encoding = encoding;
            this.serverRootDirectory = serverDirectory;
        }
    }

    public static FTPServerConfiguration defaultConfiguration(Path serverRootDirectory) {
        return new FTPServerConfiguration(
            16,
            4096,
            "UTF-8",
            serverRootDirectory
        );
    }

    public SocketAddress getAddress() throws IOException {
        if (serverSocket != null) {
            return serverSocket.getLocalAddress();
        } else {
            throw new IllegalStateException();
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                try {
                    var clientSocket = serverSocket.accept();
                    System.out.println("Gotcha");
                    queryHandlerPool.submit(new QueryHandler(clientSocket));
                } catch (IOException e) {
                    e.printStackTrace();
                    System.err.println("Failed to process connection.");
                }
            }
        } finally {
            close();
        }
    }

    private void close() {
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Failed to close socket.");
            }

            serverSocket = null;
        }
    }

    private class QueryHandler implements Runnable {

        SocketChannel channel;
        Scanner reader;

        public QueryHandler(SocketChannel receiverChannel) throws IOException {
            this.channel = receiverChannel;
            receiverChannel.configureBlocking(true);
            this.reader = new Scanner(receiverChannel, config.encoding);
        }

        @Override
        public void run() {
            int type = reader.nextInt();
            String content = reader.nextLine();
            try {
                switch (type) {
                    case 1:
                        sendGetResponse(content, channel);
                        break;
                    case 2:
                        sendListResponse(content, channel);
                        break;
                    default:
                        channel.write(encode("Unknown query type."));
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Failed to process query.");
            }
        }
    }

    private void sendListResponse(String dirName, SocketChannel receiverChannel) throws IOException {
        var dir = config.serverRootDirectory.resolve(dirName);
        String response;

        if (!Files.isDirectory(dir)) {
            response = "0";
        } else {
            try {
                int[] size = {0};
                String files = Files.list(dir)
                    .peek(path -> ++size[0])
                    .map(path -> path.getFileName() + " " + Boolean.toString(Files.isDirectory(path)))
                    .collect(Collectors.joining(" "));

                response = String.format("%d %s", size[0], files);
            } catch (IOException e) {
                System.err.println("Directory does not exist.");
                response = "0";
            }
        }

        receiverChannel.write(encode(response));
    }

    private void sendGetResponse(String fileName, SocketChannel receiverChannel) throws IOException {
        Path file = config.serverRootDirectory.resolve(fileName);
        if (!Files.isRegularFile(file)) {
            receiverChannel.write(encode("0"));
        } else {
            long size = Files.size(file);
            receiverChannel.write(encode(String.valueOf(size)));

            var fileChannel = FileChannel.open(file);
            while (size > 0) {
                size -= fileChannel.transferTo(fileChannel.position(), config.transferSize, receiverChannel);
            }
        }
    }

    private ByteBuffer encode(String s) {
        var encoder = Charset.forName(config.encoding);
        return encoder.encode(s);
    }
}