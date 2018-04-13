package ru.spbau.mit.karvozavr.md5_filehash;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class MD5CheckSumSingleThread {

    public static String md5CheckSum(String fileName) throws IOException {
        Path file = Paths.get(fileName);
        try {
            return md5hex(file);
        } catch (RuntimeException e) {
            if (e.getCause() instanceof IOException) {
                throw (IOException) e.getCause();
            } else {
                throw e;
            }
        }
    }

    private static String md5hex(Path file) {
        System.out.println(file);
        try {
            if (Files.isDirectory(file)) {
                return DigestUtils.md5Hex(file.getFileName() + Files.list(file)
                    .map(MD5CheckSumSingleThread::md5hex)
                    .collect(Collectors.joining()));
            } else {
                return DigestUtils.md5Hex(Files.newInputStream(file));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
