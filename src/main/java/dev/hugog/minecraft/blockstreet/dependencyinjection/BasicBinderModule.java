package dev.hugog.minecraft.blockstreet.dependencyinjection;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import dev.hugog.minecraft.blockstreet.BlockStreet;
import dev.hugog.minecraft.blockstreet.data.repositories.implementations.CompaniesRepository;
import dev.hugog.minecraft.blockstreet.data.repositories.implementations.PlayersRepository;
import dev.hugog.minecraft.blockstreet.data.repositories.implementations.UpdatesRepository;
import dev.hugog.minecraft.blockstreet.api.PluginReleaseAPIData;
import dev.hugog.minecraft.blockstreet.data.sources.yml.implementations.CompaniesYml;
import dev.hugog.minecraft.blockstreet.data.sources.yml.implementations.PlayersYml;
import dev.hugog.minecraft.blockstreet.listeners.PlayerJoinListener;

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

        // TODO: Change bindings depending on the data source being used
        this.bind(UpdatesRepository.class).toInstance(new UpdatesRepository(new PluginReleaseAPIData(), plugin));
        this.bind(CompaniesRepository.class).toInstance(new CompaniesRepository(new CompaniesYml(plugin.getDataFolder())));
        this.bind(PlayersRepository.class).toInstance(new PlayersRepository(new PlayersYml(plugin.getDataFolder())));

        this.bind(PlayerJoinListener.class);

    }

}
