package ru.spbau.mit.karvozavr.ftp_server;

import org.junit.Test;

import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

public class FTPServerTest {

    @Test
    public void testSmoke() throws IOException, InterruptedException {
        var server = new FTPServer(FTPServer.defaultConfiguration(Paths.get(System.getProperty("user-dir"))));

        var thread = new Thread(server);
        thread.start();

        var channel = SocketChannel.open();
        channel.configureBlocking(true);
        channel.connect(server.getAddress());
        var encoder = Charset.forName("UTF-8");
        channel.write(encoder.encode(CharBuffer.wrap("1 filename.txt")));

        TimeUnit.SECONDS.sleep(5);
    }
}