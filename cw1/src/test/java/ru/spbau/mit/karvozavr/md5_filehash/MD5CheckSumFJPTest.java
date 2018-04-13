package ru.spbau.mit.karvozavr.md5_filehash;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ExecutionException;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class MD5CheckSumFJPTest {

    @Test
    public void md5SingleFile() throws IOException, ExecutionException, InterruptedException {
        String fileName = "src/test/testfiles/testfile.txt";
        String fileHash = DigestUtils.md5Hex(Files.newInputStream(Paths.get(fileName)));
        assertThat(MD5CheckSumFJP.md5CheckSum(fileName), is(fileHash));
    }

    @Test
    public void md5Directory() throws IOException, ExecutionException, InterruptedException {
        String dirName = "src/test/testfiles/Directory";
        String file1 = dirName + "/file1.txt";
        String file2 = dirName + "/Inner/file2.txt";
        String file1Hash = DigestUtils.md5Hex(Files.newInputStream(Paths.get(file1)));
        String file2Hash = DigestUtils.md5Hex(Files.newInputStream(Paths.get(file2)));
        String tempHash = DigestUtils.md5Hex("Inner" + file2Hash);
        String resultHash = DigestUtils.md5Hex("Directory" + file1Hash + tempHash);
        assertThat(MD5CheckSumFJP.md5CheckSum(dirName), is(resultHash));
    }
}