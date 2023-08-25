package dev.hugog.minecraft.blockstreet.dependencyinjection;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import dev.hugog.minecraft.blockstreet.BlockStreet;
import dev.hugog.minecraft.blockstreet.commands.CmdDependencyInjector;
import dev.hugog.minecraft.blockstreet.data.repositories.UpdatesRepository;
import dev.hugog.minecraft.blockstreet.data.sources.api.PluginReleaseAPIDataSource;
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

        this.bind(UpdatesRepository.class).toInstance(new UpdatesRepository(new PluginReleaseAPIDataSource(), plugin));

        this.bind(CmdDependencyInjector.class);

        this.bind(PlayerJoinListener.class);

    }

}
