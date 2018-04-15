package ru.spbau.mit.karvozavr.ftp_server;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        if (args.length >= 1) {
            var server = FTPServer.withRootDirectory(args[0]);
            server.run();
        } else {
            System.err.println("Not enough arguments. Provide server root directory.");
        }
    }
}
