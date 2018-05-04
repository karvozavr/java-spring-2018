package ru.spbau.mit.karvozavr.ftp_server;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Scanner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class FTPServerTest {

    private ByteBuffer encode(String s) {
        var encoder = Charset.forName("UTF-8");
        return encoder.encode(s);
    }

    @Test
    public void testGet() throws IOException {
        var server = startFtpServer();
        var channel = SocketChannel.open();
        channel.configureBlocking(true);
        channel.connect(server.getAddress());
        channel.write(encode("2 file.txt"));
        var scanner = new Scanner(channel);
        scanner.useDelimiter("\\Z");
        assertThat(scanner.next(), is(String.format("46 File 1 contains.%n12345 rabbit gone for a walk.")));
        server.shutdown();
    }

    @Test
    public void testList() throws IOException {
        var server = startFtpServer();
        var channel = SocketChannel.open();
        var scanner = new Scanner(channel);
        scanner.useDelimiter("\\Z");
        channel.configureBlocking(true);
        channel.connect(server.getAddress());
        channel.write(encode("1 " + File.separator));

        assertThat(scanner.next(), is("2 Dir 1 true file.txt false"));
        server.shutdown();
    }

    private static FTPServer startFtpServer() throws IOException {
        var server = FTPServer.withRootDirectory(String.join(File.separator,
            System.getProperty("user.dir"), "src", "test", "resources", "testdir"));
        var thread = new Thread(server);
        thread.start();
        return server;
    }
}