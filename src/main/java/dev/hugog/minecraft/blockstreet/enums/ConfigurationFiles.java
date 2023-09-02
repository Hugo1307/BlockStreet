package dev.hugog.minecraft.blockstreet.enums;

import lombok.Getter;

@Getter
public enum ConfigurationFiles {

    CONFIG("config.yml"),
    MESSAGES("messages.yml"),
    SIGNS("signs.yml");

    private final String fileName;

    ConfigurationFiles(String fileName) {
        this.fileName = fileName;
    }

}
