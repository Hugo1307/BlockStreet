package dev.hugog.minecraft.blockstreet.data.sources.yml.implementations;

import dev.hugog.minecraft.blockstreet.data.entities.CompanyEntity;
import dev.hugog.minecraft.blockstreet.data.sources.yml.YmlDataSourceImpl;

import java.io.File;

public class CompaniesYml extends YmlDataSourceImpl<CompanyEntity> {

    public CompaniesYml(File pluginDataFolder) {
        super(pluginDataFolder);
    }

}
