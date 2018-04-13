package ru.spbau.mit.karvozavr.md5_filehash;

import org.apache.commons.codec.digest.DigestUtils;

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

public class MD5CheckSumFJP {

    public static String md5CheckSum(String fileName) throws ExecutionException, InterruptedException {
        ForkJoinPool forkJoinPool = new ForkJoinPool(4);
        ForkJoinTask<String> result = forkJoinPool.submit(new RecursiveHash(Paths.get(fileName)));
        return result.get();
    }

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
