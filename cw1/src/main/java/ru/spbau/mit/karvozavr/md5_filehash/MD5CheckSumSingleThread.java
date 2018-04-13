package ru.spbau.mit.karvozavr.md5_filehash;

import org.apache.commons.codec.digest.DigestUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

/**
 * Util class to calculate MD5 hash in single thread.
 */
public class MD5CheckSumSingleThread {

    /**
     * Calculates MD5 hash.
     *
     * @param fileName name of file or directory
     * @return hex MD5 hash
     * @throws IOException if failed to perform file operations
     */
    @NotNull
    public static String md5CheckSum(@NotNull String fileName) throws IOException {
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

    /**
     * Calculates MD5 hash.
     *
     * @param file file or directory
     * @return hex MD5 hash
     */
    @NotNull
    private static String md5hex(@NotNull Path file) {
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
