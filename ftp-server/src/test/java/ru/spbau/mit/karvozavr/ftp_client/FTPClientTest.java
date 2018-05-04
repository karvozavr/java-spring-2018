package ru.spbau.mit.karvozavr.ftp_client;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.spbau.mit.karvozavr.ftp_server.FTPServer;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class FTPClientTest {

    private FTPServer server;
    private InetSocketAddress serverAddress = new InetSocketAddress(22334);

    @Before
    public void before() throws IOException {
        server = startFtpServer();
    }

    @After
    public void after() {
        server.shutdown();
        server = null;
    }

    @Test
    public void list() throws IOException {
        var client = new FTPClient(serverAddress);
        String result = client.list(File.separator);
        String expected = "2 Dir 1 true file.txt false";
        assertThat(result, is(expected));
    }

    @Test
    public void get() throws IOException {
        var client = new FTPClient(serverAddress);
        String result = client.get("file.txt");
        String expected = String.format("46 File 1 contains.%n12345 rabbit gone for a walk.");
        assertThat(result, is(expected));
    }

    private static FTPServer startFtpServer() throws IOException {
        var server = FTPServer.withRootDirectory(String.join(File.separator,
            System.getProperty("user.dir"), "src", "test", "resources", "testdir"));
        var thread = new Thread(server);
        thread.start();
        return server;
    }
}