package ru.spbau.mit.karvozavr.ftp_client;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.spbau.mit.karvozavr.ftp_client.ui.util.FileInfo;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;

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
        FTPClient client = new FTPClient(serverAddress);
        List<FileInfo> result = client.list(File.separator);
        System.out.println(result);
        assertThat(result.get(0).getName(), is("Dir1"));
        assertThat(result.get(0).getIsDirectory(), is(true));
        assertThat(result.get(1).getName(), is("file.txt"));
        assertThat(result.get(1).getIsDirectory(), is(false));
    }

    @Test
    public void save() throws IOException {
//        FTPClient client = new FTPClient(serverAddress);
//        String result = client.get("file.txt");
//        String expected = String.format("46 File 1 contains.%n12345 rabbit gone for a walk.");
//        assertThat(result, is(expected));
    }

    private static FTPServer startFtpServer() throws IOException {
        FTPServer server = FTPServer.withRootDirectory(String.join(File.separator,
            System.getProperty("user.dir"), "src", "test", "resources", "testdir"));
        new Thread(server).start();
        return server;
    }
}