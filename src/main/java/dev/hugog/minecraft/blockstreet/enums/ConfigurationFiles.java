package dev.hugog.minecraft.blockstreet.enums;

public enum ConfigurationFiles {

    CONFIG("config.yml"), COMPANIES("companies.yml"), MESSAGES("messages.yml"),
    PLAYERS("players.yml"), SIGNS("signs.yml");

    private final String fileName;

    ConfigurationFiles(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

}
