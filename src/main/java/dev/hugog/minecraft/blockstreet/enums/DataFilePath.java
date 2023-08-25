package dev.hugog.minecraft.blockstreet.enums;

import lombok.Getter;

@Getter
public enum DataFilePath {

    COMPANIES("companies.yml"),
    PLAYERS("players.yml"),
    SIGNS("signs.yml");

    private final String fileName;

    DataFilePath(String fileName) {
        this.fileName = fileName;
    }


}
