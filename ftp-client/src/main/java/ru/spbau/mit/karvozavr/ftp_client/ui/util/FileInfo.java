package ru.spbau.mit.karvozavr.ftp_client.ui.util;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public class FileInfo implements Serializable {
    private String name;
    private boolean isDirectory;

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
