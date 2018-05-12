package ru.spbau.mit.karvozavr.ftp_client.ui.util;

import java.io.Serializable;

/**
 * File info data class.
 */
public class FileInfo implements Serializable {
    private String name;
    private boolean isDirectory;

    /**
     * Creates new FileInfo instance.
     * @param name name of a file
     * @param isDirectory is file a directory
     */
    public FileInfo(String name, boolean isDirectory) {
        this.name = name;
        this.isDirectory = isDirectory;
    }

    public String getName() {
        return name;
    }

    public boolean getIsDirectory() {
        return isDirectory;
    }

    @Override
    public String toString() {
        return String.format("Name: %s, Dir: %b", name, isDirectory);
    }
}
