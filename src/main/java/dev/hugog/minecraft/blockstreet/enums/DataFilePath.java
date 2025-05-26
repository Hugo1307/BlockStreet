package dev.hugog.minecraft.blockstreet.enums;

import lombok.Getter;

import java.io.File;

@Getter
public enum DataFilePath {

    COMPANIES("companies"),
    PLAYERS("players"),
    SIGNS("signs");

    private final String dataPath;

    DataFilePath(String dataPath) {
        this.dataPath = dataPath;
    }

    public String getFullPathById(String id) {
        return new File(dataPath, id + ".yml").getPath();
    }

}
