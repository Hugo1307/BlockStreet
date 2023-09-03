package dev.hugog.minecraft.blockstreet.data.sources.yml.implementations;

import dev.hugog.minecraft.blockstreet.data.entities.SignEntity;
import dev.hugog.minecraft.blockstreet.data.sources.yml.YmlDataSourceImpl;

import java.io.File;

public class SignsYml extends YmlDataSourceImpl<SignEntity> {

    public SignsYml(File pluginDataFolder) {
        super(pluginDataFolder);
    }

}
