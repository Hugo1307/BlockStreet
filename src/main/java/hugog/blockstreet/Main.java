package hugog.blockstreet;

import com.google.inject.Inject;
import com.google.inject.Injector;
import hugog.blockstreet.commands.CmdImplementer;
import hugog.blockstreet.dependencyinjection.BasicBinderModule;
import hugog.blockstreet.enums.ConfigurationFiles;
import hugog.blockstreet.listeners.PlayerJoin;
import hugog.blockstreet.listeners.SignHandler;
import hugog.blockstreet.others.ConfigAccessor;
import hugog.blockstreet.runnables.InterestRateRunnable;
import hugog.blockstreet.runnables.SignCheckerRunnable;
import hugog.blockstreet.update.UpdateChecker;
import lombok.Getter;
import lombok.Setter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.util.Objects;

public class Main extends JavaPlugin {

    @Getter
    private static Main instance;

    @Inject
    private CmdImplementer cmdImplementer;

    @Inject
    private UpdateChecker updateChecker;

    @Inject
    private PlayerJoin playerJoin;

    public ConfigAccessor messagesConfig;
    @Getter private Economy economy;

    private BukkitTask interestRateTask, signCheckerTask;

    @Getter
    private boolean inTestMode = false;

    public Main() {
        super();
    }

    public Main(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file, Boolean inTestMode) {
        super(loader, description, dataFolder, file);
        this.inTestMode = inTestMode;
    }

    @Override
    public void onEnable() {

    	instance = this;

        configureDI();

        if (!inTestMode)
            setupEconomy();

        registerCommands();
        configureConfig();
        configureMessages();
        configureCompaniesReg();
        registerRunnables();
        checkForUpdates();
        registerEvents();

        getLogger().info("Plugin successfully enabled.");
        
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin successfully disabled!");
    }

    private void registerCommands(){
    	Objects.requireNonNull(getCommand("invest")).setExecutor(cmdImplementer);
    }

    private void configureConfig(){
        if(!new File(getDataFolder(), ConfigurationFiles.CONFIG.getFileName()).exists())
        	this.saveDefaultConfig();
    }
    
    private void configureCompaniesReg() {
		ConfigAccessor companiesReg = new ConfigAccessor(this, ConfigurationFiles.COMPANIES.getFileName());
		if (!new File(getDataFolder(), ConfigurationFiles.COMPANIES.getFileName()).exists())
			companiesReg.saveDefaultConfig();
    }
    
    private void configureMessages() {
    	this.messagesConfig = new ConfigAccessor(this, ConfigurationFiles.MESSAGES.getFileName());
		if(!new File(getDataFolder(), ConfigurationFiles.MESSAGES.getFileName()).exists())
			messagesConfig.saveDefaultConfig();
    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(playerJoin, this);
        getServer().getPluginManager().registerEvents(new SignHandler(), this);
    }

    public void registerRunnables() {
        int interestTime = Main.getInstance().getConfig().getInt("BlockStreet.Timer");
        interestRateTask = new InterestRateRunnable().runTaskTimerAsynchronously(this, 20L*10, 20L*60);
        signCheckerTask = new SignCheckerRunnable().runTaskTimer(this, 20L*10, 20L*60*interestTime);
    }

    public void stopRunnables() {
        interestRateTask.cancel();
        signCheckerTask.cancel();
    }

    public void checkForUpdates() {
        boolean updateAvailable = updateChecker.checkForUpdates();
        if (updateAvailable)
            getLogger().info("New update available. Please update to " + updateChecker.getLastVersion().toString() + "!");
    }

    private void configureDI() {
        BasicBinderModule module = new BasicBinderModule(this);
        Injector injector = module.createInjector();
        injector.injectMembers(this);
    }

    private void setupEconomy() {

        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(Economy.class);

        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        } else {
            getLogger().severe("Vault dependency not found. Disabling plugin...");
            getServer().getPluginManager().disablePlugin(this);
        }

    }

}
