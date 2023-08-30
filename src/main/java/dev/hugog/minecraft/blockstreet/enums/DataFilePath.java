package dev.hugog.minecraft.blockstreet.enums;

import lombok.Getter;

@Getter
public enum DataFilePath {

    COMPANIES("/companies"),
    PLAYERS("/players"),
    SIGNS("/signs");

    private final String dataPath;

    DataFilePath(String dataPath) {
        this.dataPath = dataPath;
    }

    public String getFullPath(String fileName) {
        return dataPath + "/" + fileName;
    }

    public String getFullPathById(String id) {
        return dataPath + "/" + id + ".yml";
    }

}
