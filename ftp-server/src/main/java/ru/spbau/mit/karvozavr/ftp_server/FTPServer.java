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
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * Simple FTP server class.
 */
public class FTPServer implements Runnable {

    private ExecutorService queryHandlerPool;
    private ServerSocketChannel serverSocket;
    private FTPServerConfiguration config;

    /**
     * Creates new FTP server instance with given root directory and default configuration.
     * @param dir root server directory
     * @return new FTP server instance
     * @throws IOException in case of IO error
     */
    public static FTPServer withRootDirectory(String dir) throws IOException {
        return new FTPServer(defaultConfiguration(Paths.get(dir)));
    }

    /**
     * Creates new FTP server instance with given configuration.
     * @param config configuration
     * @return new FTP server instance
     * @throws IOException in case of IO error
     */
    public static FTPServer withConfiguration(FTPServerConfiguration config) throws IOException {
        return new FTPServer(config);
    }

    /**
     * Returns server socket address.
     * @return server socket address
     * @throws IOException in case of IO error
     */
    public SocketAddress getAddress() throws IOException {
        if (serverSocket != null) {
            return serverSocket.getLocalAddress();
        } else {
            throw new IllegalStateException();
        }
    }

    /**
     * Starts the server.
     */
    @Override
    public void run() {
        try {
            while (true) {
                try {
                    var clientSocket = serverSocket.accept();
                    queryHandlerPool.submit(new QueryHandler(clientSocket));
                } catch (IOException e) {
                    e.printStackTrace();
                    System.err.println("Failed to handle connection.");
                }
            }
        } finally {
            close();
        }
    }

    /**
     * FTP server configuration data class.
     */
    public static class FTPServerConfiguration {
        private int connectionsNumber;
        private long transferSize;
        private String encoding;
        private Path serverRootDirectory;

        public FTPServerConfiguration(int connectionsNumber, long transferSize, String encoding, Path serverDirectory) {
            this.connectionsNumber = connectionsNumber;
            this.transferSize = transferSize;
            this.encoding = encoding;
            this.serverRootDirectory = serverDirectory;
        }
    }

    /**
     * Constructs new FTP server instance with given configuration.
     * @param config configuration
     * @throws IOException in case of IO error
     */
    private FTPServer(FTPServerConfiguration config) throws IOException {
        this.config = config;
        if (!Files.isDirectory(config.serverRootDirectory)) {
            throw new IllegalArgumentException("Not a directory.");
        }
        queryHandlerPool = Executors.newFixedThreadPool(config.connectionsNumber);

        serverSocket = ServerSocketChannel.open();
        serverSocket.configureBlocking(true);
        serverSocket.bind(new InetSocketAddress(0));
    }

    /**
     * Shutdown server.
     */
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

    /**
     * Returns default server configuration with given root directory.
     * @param serverRootDirectory root directory
     * @return default server configuration
     */
    private static FTPServerConfiguration defaultConfiguration(Path serverRootDirectory) {
        return new FTPServerConfiguration(
            16,
            4096,
            "UTF-8",
            serverRootDirectory
        );
    }

    /**
     * Query handler task.
     */
    private class QueryHandler implements Runnable {

        SocketChannel receiverChannel;
        Scanner scanner;

        public QueryHandler(SocketChannel receiverChannel) throws IOException {
            this.receiverChannel = receiverChannel;
            receiverChannel.configureBlocking(true);
            this.scanner = new Scanner(receiverChannel, config.encoding);
            scanner.useDelimiter("\\z");
        }

        @Override
        public void run() {
            var query = scanner.next();
            var tokens = query.split(" ", 2);
            int type = -1;
            String content = "";
            if (tokens.length == 2) {
                type = Integer.parseInt(tokens[0]);
                content = tokens[1];
                content = content.trim();
            }

            if (content.equals("/"))
                content = "";

            try {
                switch (type) {
                    case 1:
                        sendListResponse(content, receiverChannel);
                        break;
                    case 2:
                        sendGetResponse(content, receiverChannel);
                        break;
                    default:
                        receiverChannel.write(encode("Unknown query type."));
                        break;
                }

                receiverChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Failed to handle the query.");
            }

            scanner.close();
        }
    }

    /**
     * Sends 'List' query response.
     * @param dirName query directory
     * @param receiverChannel receiver
     * @throws IOException in case of IO error
     */
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

    /**
     * Sends 'Get' query response.
     * @param fileName query file name
     * @param receiverChannel receiver
     * @throws IOException in case of IO error
     */
    private void sendGetResponse(String fileName, SocketChannel receiverChannel) throws IOException {
        Path file = config.serverRootDirectory.resolve(fileName);
        if (!Files.isRegularFile(file)) {
            receiverChannel.write(encode("0"));
        } else {
            long size = Files.size(file);
            FileChannel fileChannel = null;

            try {
                fileChannel = FileChannel.open(file);
            } catch (IOException e) {
                receiverChannel.write(encode("0"));
                return;
            }

            receiverChannel.write(encode(size + " "));
            long bytesTransferred = 0;
            while (size - bytesTransferred > 0) {
                bytesTransferred += fileChannel.transferTo(bytesTransferred, config.transferSize, receiverChannel);
            }
            fileChannel.close();
        }
    }

    /**
     * Encodes message to bytes.
     * @param message message to encode
     * @return encoded message
     */
    private ByteBuffer encode(String message) {
        var encoder = Charset.forName(config.encoding);
        return encoder.encode(message);
    }
}
