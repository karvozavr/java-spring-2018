package ru.spbau.mit.karvozavr.md5_filehash;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5CheckSumSingleThreadTest {

    @Test
    public void md5SingleFile() throws IOException {
        String fileName = "src/test/testfiles/testfile.txt";
        String fileHash = DigestUtils.md5Hex(Files.newInputStream(Paths.get(fileName)));
        assertThat(MD5CheckSumSingleThread.md5CheckSum(fileName), is(fileHash));
    }

    @Test
    public void md5Directory() throws IOException {
        String dirName = "src/test/testfiles/Directory";
        String file1 = dirName + "/file1.txt";
        String file2 = dirName + "/Inner/file2.txt";
        String file1Hash = DigestUtils.md5Hex(Files.newInputStream(Paths.get(file1)));
        String file2Hash = DigestUtils.md5Hex(Files.newInputStream(Paths.get(file2)));
        String tempHash = DigestUtils.md5Hex("Inner" + file2Hash);
        String resultHash = DigestUtils.md5Hex("Directory" + file1Hash + tempHash);
        assertThat(MD5CheckSumSingleThread.md5CheckSum(dirName), is(resultHash));
    }
}