package dev.hugog.minecraft.blockstreet.enums;

import lombok.Getter;

import java.io.File;

@Getter
public enum DataFilePath {

    COMPANIES(File.separator + "companies"),
    PLAYERS(File.separator + "players"),
    SIGNS(File.separator + "signs");

    private final String dataPath;

    DataFilePath(String dataPath) {
        this.dataPath = dataPath;
    }

    public String getFullPath(String fileName) {
        return dataPath + File.separator + fileName;
    }

    public String getFullPathById(String id) {
        return dataPath + File.separator + id + ".yml";
    }

}
