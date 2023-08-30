package dev.hugog.minecraft.blockstreet.data.sources.yml.implementations;

import dev.hugog.minecraft.blockstreet.data.entities.PlayerEntity;
import dev.hugog.minecraft.blockstreet.data.sources.yml.YmlDataSourceImpl;

import java.io.File;

public class PlayersYml extends YmlDataSourceImpl<PlayerEntity> {

    public PlayersYml(File pluginDataFolder) {
        super(new File(pluginDataFolder.getAbsolutePath() + "/players"));
    }

}
