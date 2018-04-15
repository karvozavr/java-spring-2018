package ru.spbau.mit.karvozavr.ftp_server;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        var server = new FTPServer();
        server.run();
    }
}
