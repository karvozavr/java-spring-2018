package ru.spbau.mit.karvozavr.md5_filehash;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * Main class for MD5 time measurement.
 */
public class Main {

    private static void compareCheckSums(String fileName) throws IOException, ExecutionException, InterruptedException {
        long startTime = System.currentTimeMillis();
        MD5CheckSumSingleThread.md5CheckSum(fileName);
        long endTime = System.currentTimeMillis();
        long singleThread = endTime - startTime;

        startTime = System.currentTimeMillis();
        MD5CheckSumFJP.md5CheckSum(fileName);
        endTime = System.currentTimeMillis();

        long fjp = endTime - startTime;

        if (fjp <= singleThread) {
            System.out.println("Fork join pool wins!");
        } else {
            System.out.println("Single thread hash wins!");
        }
    }

    public static void main(String[] args) {
        if (args.length > 0) {
            try {
                compareCheckSums(args[0]);
            } catch (Exception e) {
                System.out.println("Fatal error. Google it.");
                e.printStackTrace();
            }
        }
    }
}
