package dev.hugog.minecraft.blockstreet.dependencyinjection;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import dev.hugog.minecraft.blockstreet.BlockStreet;
import dev.hugog.minecraft.blockstreet.data.repositories.implementations.CompaniesRepository;
import dev.hugog.minecraft.blockstreet.data.repositories.implementations.PlayersRepository;
import dev.hugog.minecraft.blockstreet.data.repositories.implementations.SignsRepository;
import dev.hugog.minecraft.blockstreet.data.sources.yml.implementations.CompaniesYml;
import dev.hugog.minecraft.blockstreet.data.sources.yml.implementations.PlayersYml;
import dev.hugog.minecraft.blockstreet.data.sources.yml.implementations.SignsYml;
import dev.hugog.minecraft.blockstreet.listeners.PlayerJoinListener;
import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;

import java.io.File;
import java.util.logging.Logger;

public class BasicBinderModule extends AbstractModule {

    private final BlockStreet plugin;

    public BasicBinderModule(BlockStreet plugin) {
        this.plugin = plugin;
    }

    public Injector createInjector() {
        return Guice.createInjector(this);
    }

    @Override
    protected void configure() {

        this.bind(BlockStreet.class).toInstance(plugin);

        this.bind(Server.class).toInstance(plugin.getServer());
        this.bind(PluginDescriptionFile.class).toInstance(plugin.getDescription());

        this.bind(Logger.class).annotatedWith(Names.named("bukkitLogger")).toInstance(plugin.getLogger());
        this.bind(File.class).annotatedWith(Names.named("pluginDataDirectory")).toInstance(plugin.getDataFolder());
        this.bind(FileConfiguration.class).annotatedWith(Names.named("pluginConfig")).toInstance(YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "config.yml")));

        // TODO: Change bindings depending on the data source being used
        this.bind(CompaniesRepository.class).toInstance(new CompaniesRepository(new CompaniesYml(plugin.getDataFolder())));
        this.bind(PlayersRepository.class).toInstance(new PlayersRepository(new PlayersYml(plugin.getDataFolder())));
        this.bind(SignsRepository.class).toInstance(new SignsRepository(new SignsYml(plugin.getDataFolder())));

        this.bind(PlayerJoinListener.class);

    }

}
