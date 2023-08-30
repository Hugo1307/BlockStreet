package dev.hugog.minecraft.blockstreet;

import com.google.inject.Injector;
import dev.hugog.minecraft.blockstreet.data.repositories.implementations.CompaniesRepository;
import dev.hugog.minecraft.blockstreet.data.repositories.implementations.PlayersRepository;
import dev.hugog.minecraft.blockstreet.data.repositories.implementations.UpdatesRepository;
import dev.hugog.minecraft.blockstreet.dependencyinjection.BasicBinderModule;
import dev.hugog.minecraft.blockstreet.enums.ConfigurationFiles;
import dev.hugog.minecraft.blockstreet.enums.DataFilePath;
import dev.hugog.minecraft.blockstreet.listeners.PlayerJoinListener;
import dev.hugog.minecraft.blockstreet.listeners.SignHandler;
import dev.hugog.minecraft.blockstreet.others.ConfigAccessor;
import dev.hugog.minecraft.blockstreet.others.Messages;
import dev.hugog.minecraft.blockstreet.runnables.InterestRateRunnable;
import dev.hugog.minecraft.blockstreet.runnables.SignCheckerRunnable;
import dev.hugog.minecraft.dev_command.DevCommand;
import dev.hugog.minecraft.dev_command.commands.executors.DevCommandExecutor;
import dev.hugog.minecraft.dev_command.commands.handler.CommandHandler;
import dev.hugog.minecraft.dev_command.dependencies.DependencyHandler;
import dev.hugog.minecraft.dev_command.integration.Integration;
import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.logging.Level;

public class BlockStreet extends JavaPlugin {

    public ConfigAccessor messagesConfig;

    @Getter private Economy economy;
    @Getter private static BlockStreet instance;
    private BukkitTask interestRateTask, signCheckerTask;

    private Integration pluginDevCommandsIntegration;

    // Listeners
    @Inject
    private PlayerJoinListener playerJoinListener;

    // Repositories
    @Inject private UpdatesRepository updatesRepository;
    @Inject private CompaniesRepository companiesRepository;
    @Inject private PlayersRepository playersRepository;

    @Inject
    private Messages messages;


    @Override
    public void onEnable() {

    	instance = this;

        initDependencyInjectionModules();

        setupEconomy();

        initializeDevCommands();
        registerCommandsDependencies();

        registerEvents();

        configureConfig();
        
        configureMessages();

        checkDefaultCompanyRegistry();

        registerRunnables();

        checkForUpdates();
           	
        System.out.println("[BlockStreet] Plugin successfully enabled!");
        
    }

    @Override
    public void onDisable() {
        System.out.println("[BlockStreet] Plugin successfully disabled!");
    }

    private void initDependencyInjectionModules() {
        BasicBinderModule guiceBinderModule = new BasicBinderModule(this);
        Injector injector = guiceBinderModule.createInjector();
        injector.injectMembers(this);
    }

    public void checkForUpdates() {

        if (updatesRepository.isUpdateAvailable()) {
            getLogger().warning("An update is available! Download it at: https://www.spigotmc.org/resources/blockstreet.75791/");
        } else {
            getLogger().info("You are using the latest version of BlockStreet!");
        }

    }

    private void initializeDevCommands() {

        PluginCommand mainPluginCommand = getCommand("invest");
        pluginDevCommandsIntegration = Integration.createFromPlugin(this);

        if (mainPluginCommand != null) {

            DevCommand devCommand = DevCommand.getOrCreateInstance();
            CommandHandler commandHandler = devCommand.getCommandHandler();
            DevCommandExecutor devCommandExecutor = new DevCommandExecutor("invest", pluginDevCommandsIntegration);

            mainPluginCommand.setExecutor(devCommandExecutor);
            commandHandler.initCommandsAutoConfiguration(pluginDevCommandsIntegration);

        }

    }

    private void registerCommandsDependencies() {

        DevCommand devCommand = DevCommand.getOrCreateInstance();
        DependencyHandler dependencyHandler = devCommand.getDependencyHandler();

        dependencyHandler.registerDependency(pluginDevCommandsIntegration, messages);
        dependencyHandler.registerDependency(pluginDevCommandsIntegration, getLogger());

        dependencyHandler.registerDependency(pluginDevCommandsIntegration, economy);

        dependencyHandler.registerDependency(pluginDevCommandsIntegration, updatesRepository);
        dependencyHandler.registerDependency(pluginDevCommandsIntegration, companiesRepository);
        dependencyHandler.registerDependency(pluginDevCommandsIntegration, playersRepository);

    }

    private void configureConfig(){
        if(!new File(getDataFolder(), ConfigurationFiles.CONFIG.getFileName()).exists())
        	this.saveDefaultConfig();
    }
    
    private void checkDefaultCompanyRegistry() {

        File companiesDirectory = new File(getDataFolder(), DataFilePath.COMPANIES.getDataPath());
        if (!companiesDirectory.exists()) {
            if (companiesDirectory.mkdir()) {
                File defaultCompanyFile = new File(getDataFolder(), DataFilePath.COMPANIES.getFullPathById("0"));
                try {
                    byte[] buffer = getResource("companies/0.yml").readAllBytes();

                    OutputStream outStream = Files.newOutputStream(defaultCompanyFile.toPath());
                    outStream.write(buffer);

                    outStream.close();

                } catch (IOException e) {
                    getLogger().warning("Unable to create default company file.");
                    throw new RuntimeException(e);
                }
            } else {
                getLogger().warning("Unable to create companies directory.");
            }
        } else {
            getLogger().info("Companies directory already exists. Skipping default initialization.");
        }

    }
    
    private void configureMessages() {
    	this.messagesConfig = new ConfigAccessor(this, ConfigurationFiles.MESSAGES.getFileName());
		if(!new File(getDataFolder(), ConfigurationFiles.MESSAGES.getFileName()).exists())
			messagesConfig.saveDefaultConfig();
    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(playerJoinListener, this);
        getServer().getPluginManager().registerEvents(new SignHandler(), this);
    }

    public void registerRunnables() {
        int interestTime = BlockStreet.getInstance().getConfig().getInt("BlockStreet.Timer");
        interestRateTask = new InterestRateRunnable().runTaskTimerAsynchronously(this, 20L*10, 20L*60);
        signCheckerTask = new SignCheckerRunnable().runTaskTimer(this, 20L*10, 20L*60*interestTime);
    }

    public void stopRunnables() {
        interestRateTask.cancel();
        signCheckerTask.cancel();
    }

    private boolean setupEconomy()
    {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }else {
            Bukkit.getLogger().log(Level.SEVERE, "[BlockStreet] Vault not found. Disabling plugin.");
            getServer().getPluginManager().disablePlugin(this);
        }
        return economy != null;
    }

}
