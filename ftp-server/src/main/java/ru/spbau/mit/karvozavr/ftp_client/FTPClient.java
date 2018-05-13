package ru.spbau.mit.karvozavr.ftp_client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Scanner;

/**
 * Simple FTP client.
 */
public class FTPClient {

    private InetSocketAddress serverAddress;
    private Charset encoder = Charset.forName("UTF-8");

    public FTPClient(InetSocketAddress serverAddress) {
        this.serverAddress = serverAddress;
    }

    /**
     * List directories and files in given directory.
     *
     * @param directory directory to list
     * @return response
     * @throws IOException in case of IO error
     */
    public String list(String directory) throws IOException {
        var channel = SocketChannel.open();
        channel.connect(serverAddress);
        channel.write(encode("1 "));
        channel.write(encode(directory));

        var scanner = new Scanner(channel);
        scanner.useDelimiter("\\Z");
        return scanner.next();
    }

    /**
     * Get file from server.
     *
     * @param filename file name
     * @return response
     * @throws IOException in case of IO error
     */
    public String get(String filename) throws IOException {
        try (var channel = SocketChannel.open()) {
            channel.connect(serverAddress);
            channel.write(encode("2 "));
            channel.write(encode(filename));

            var scanner = new Scanner(channel);
            scanner.useDelimiter("\\Z");
            return scanner.next();
        }
    }

    /**
     * Encode UTF-8 string to ByteBuffer.
     *
     * @param s string to encode
     * @return encoded string
     */
    private ByteBuffer encode(String s) {
        return encoder.encode(s);
    }
}
