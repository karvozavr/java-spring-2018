package ru.spbau.mit.karvozavr.ftp_client;

import ru.spbau.mit.karvozavr.ftp_client.ui.util.FileInfo;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Simple FTP client.
 */
public class FTPClient {

    private InetSocketAddress serverAddress;
    private static final int transferSize = 4098;

    public FTPClient(InetSocketAddress serverAddress) {
        this.serverAddress = serverAddress;
    }

    /**
     * List directories and files in given directory.
     *
     * @param directory directory to list
     * @return list of dirs and files
     * @throws IOException in case of IO error
     */
    public List<FileInfo> list(String directory) throws IOException {
        SocketChannel channel = SocketChannel.open();
        channel.connect(serverAddress);
        channel.write(encode("1 "));
        channel.write(encode(directory));

        Scanner scanner = new Scanner(channel);

        ArrayList<FileInfo> result = new ArrayList<>();
        int size = scanner.nextInt();
        for (int i = 0; i < size; i++) {
            result.add(new FileInfo(scanner.next(), scanner.nextBoolean()));
        }

        return result;
    }

    /**
     * Get file from server and save it.
     *
     * @param filename file name
     * @throws IOException in case of IO error
     */
    public void save(String filename, File path) throws IOException {
        SocketChannel channel = SocketChannel.open();
        channel.connect(serverAddress);
        channel.write(encode("2 "));
        channel.write(encode(filename));
        Scanner scanner = new Scanner(channel);

        long fileSize = scanner.nextLong();
        if (fileSize == 0) {
            return;
        }

        RandomAccessFile file = new RandomAccessFile(path.toString(), "rw");
        FileChannel fileChannel = file.getChannel();
        long bytesTransferred = 0;
        while (fileSize - bytesTransferred > 0) {
            bytesTransferred += fileChannel.transferFrom(channel, bytesTransferred, transferSize);
            System.out.println(String.format("%d of %d", bytesTransferred, fileSize));
        }
    }

    /**
     * Encode UTF-8 string to ByteBuffer.
     *
     * @param s string to encode
     * @return encoded string
     */
    private ByteBuffer encode(String s) {
        Charset encoder = Charset.forName("UTF-8");
        return encoder.encode(s);
    }
}
