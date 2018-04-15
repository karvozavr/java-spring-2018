package ru.spbau.mit.karvozavr.ftp_server;

import org.junit.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class FTPServerTest {

    private ByteBuffer encode(String s) {
        var encoder = Charset.forName("UTF-8");
        return encoder.encode(s);
    }

    @Test
    public void testSmoke() throws IOException, InterruptedException {
        var server = startFtpServer();
        var channel = SocketChannel.open();
        channel.configureBlocking(true);
        channel.connect(server.getAddress());
        channel.write(encode("2 file.txt"));
        var scanner = new Scanner(channel);
        scanner.useDelimiter("\\Z");
        System.out.println(scanner.next());
    }

    @Test
    public void testFFF() throws IOException{
        FileChannel channel = FileChannel.open(Paths.get(System.getProperty("user.dir") + "/src/test/resources/testdir/file.txt"));
        var ch2 = Channels.newChannel(System.out);
        ch2.write(encode("Lol kek"));
        channel.transferTo(0, 2048, ch2);
    }

    @Test
    public void testList() throws IOException, InterruptedException {
        var server = startFtpServer();
        var channel = SocketChannel.open();
        var scanner = new Scanner(channel);
        scanner.useDelimiter("\\z");
        channel.configureBlocking(true);
        channel.connect(server.getAddress());
        channel.write(encode("1 /"));

        assertThat(scanner.next(), is("2 file.txt false Dir 1 true"));
    }

    private FTPServer startFtpServer() throws IOException, InterruptedException {
        var server = new FTPServer(FTPServer.defaultConfiguration(Paths.get(System.getProperty("user.dir") + "/src/test/resources/testdir")));
        var thread = new Thread(server);
        thread.start();
        return server;
    }
}