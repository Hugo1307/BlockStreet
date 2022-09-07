package hugog.blockstreet.dependencyinjection;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import hugog.blockstreet.Main;
import hugog.blockstreet.commands.CmdDependencyInjector;
import hugog.blockstreet.commands.CmdImplementer;
import hugog.blockstreet.listeners.PlayerJoin;

public class BasicBinderModule extends AbstractModule {

    private final Main plugin;

    public BasicBinderModule(Main plugin) {
        this.plugin = plugin;
    }

    public Injector createInjector() {
        return Guice.createInjector(this);
    }

    @Override
    protected void configure() {

        this.bind(Main.class).toInstance(plugin);

        this.bind(CmdDependencyInjector.class);
        this.bind(CmdImplementer.class);

        this.bind(PlayerJoin.class);

    }

}
