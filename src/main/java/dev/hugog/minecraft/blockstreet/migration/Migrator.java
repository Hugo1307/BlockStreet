package dev.hugog.minecraft.blockstreet.migration;

public interface Migrator {

    void migrate();

    void archiveOldData();

    String getOldDataVersion();

}
