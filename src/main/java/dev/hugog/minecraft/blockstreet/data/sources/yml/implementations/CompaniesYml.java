package dev.hugog.minecraft.blockstreet.data.sources.yml.implementations;

import dev.hugog.minecraft.blockstreet.data.entities.CompaniesEntity;
import dev.hugog.minecraft.blockstreet.data.sources.yml.YmlDataSourceImpl;
import dev.hugog.minecraft.blockstreet.enums.DataFilePath;

import java.io.File;

public class CompaniesYml extends YmlDataSourceImpl<CompaniesEntity> {

    public CompaniesYml(File pluginDataFolder) {
        super(DataFilePath.COMPANIES.getFileName(), pluginDataFolder);
    }

}
