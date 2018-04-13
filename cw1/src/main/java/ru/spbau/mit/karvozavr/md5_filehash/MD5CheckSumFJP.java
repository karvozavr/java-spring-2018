package ru.spbau.mit.karvozavr.md5_filehash;

import org.apache.commons.codec.digest.DigestUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

/**
 * Util class to calculate MD5 hash using ForkJoinPool.
 */
public class MD5CheckSumFJP {

    /**
     * Calculates MD5 hash.
     *
     * @param fileName name of file or directory
     * @return hex MD5 hash
     * @throws IOException          if failed to perform file operations
     * @throws InterruptedException if ForkJoin pool throws it
     */
    @NotNull
    public static String md5CheckSum(@NotNull String fileName) throws ExecutionException, InterruptedException {
        ForkJoinPool forkJoinPool = new ForkJoinPool(4);
        ForkJoinTask<String> result = forkJoinPool.submit(new RecursiveHash(Paths.get(fileName)));
        return result.get();
    }

    /**
     * Recursive task for calculating hash.
     */
    private static class RecursiveHash extends RecursiveTask<String> {

        private final Path file;

        RecursiveHash(Path fileName) {
            this.file = fileName;
        }

        @Override
        protected String compute() {
            try {
                if (Files.isDirectory(file)) {

                    List<RecursiveHash> tasks = new ArrayList<>();

                    Files.list(file)
                        .forEach(path -> tasks.add(new RecursiveHash(path)));
                    tasks.forEach(ForkJoinTask::fork);

                    StringBuffer result = new StringBuffer();
                    result.append(file.getFileName());
                    tasks.forEach(task -> result.append(task.join()));

                    return DigestUtils.md5Hex(result.toString());
                } else {
                    return DigestUtils.md5Hex(Files.newInputStream(file));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
