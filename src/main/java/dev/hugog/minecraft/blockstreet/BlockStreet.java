package dev.hugog.minecraft.blockstreet;

import com.google.inject.Injector;
import dev.hugog.minecraft.blockstreet.api.services.AutoUpdateService;
import dev.hugog.minecraft.blockstreet.data.services.CompaniesService;
import dev.hugog.minecraft.blockstreet.data.services.PlayersService;
import dev.hugog.minecraft.blockstreet.data.services.SignsService;
import dev.hugog.minecraft.blockstreet.dependencyinjection.BasicBinderModule;
import dev.hugog.minecraft.blockstreet.enums.ConfigurationFiles;
import dev.hugog.minecraft.blockstreet.enums.DataFilePath;
import dev.hugog.minecraft.blockstreet.listeners.PlayerJoinListener;
import dev.hugog.minecraft.blockstreet.listeners.SignsListener;
import dev.hugog.minecraft.blockstreet.utils.ConfigAccessor;
import dev.hugog.minecraft.blockstreet.utils.Messages;
import dev.hugog.minecraft.blockstreet.schedulers.InterestRateScheduler;
import dev.hugog.minecraft.dev_command.DevCommand;
import dev.hugog.minecraft.dev_command.commands.executors.DevCommandExecutor;
import dev.hugog.minecraft.dev_command.commands.handler.CommandHandler;
import dev.hugog.minecraft.dev_command.dependencies.DependencyHandler;
import dev.hugog.minecraft.dev_command.integration.Integration;
import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Objects;
import java.util.logging.Level;

public class BlockStreet extends JavaPlugin {

    public ConfigAccessor messagesConfig;

    @Getter private Economy economy;
    @Getter private static BlockStreet instance;
    private BukkitTask interestRateTask;

    private Integration pluginDevCommandsIntegration;

    // Listeners
    @Inject
    private PlayerJoinListener playerJoinListener;
    @Inject
    private SignsListener signsListener;

    // Services
    @Inject private AutoUpdateService autoUpdateService;
    @Inject private CompaniesService companiesService;
    @Inject private PlayersService playersService;
    @Inject private SignsService signsService;

    // Utils
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

        initializeCompaniesData();
        initializePlayersData();
        initializeSignsData();

        registerSchedulers();

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

        autoUpdateService.isUpdateAvailable().thenAcceptAsync((isUpdateAvailable) -> {
            if (isUpdateAvailable) {
                getLogger().warning("An update is available! Download it at: https://www.spigotmc.org/resources/blockstreet.75791/");
            } else {
                getLogger().info("You are using the latest version of BlockStreet!");
            }
        });

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

        dependencyHandler.registerDependency(pluginDevCommandsIntegration, this);
        dependencyHandler.registerDependency(pluginDevCommandsIntegration, messages);
        dependencyHandler.registerDependency(pluginDevCommandsIntegration, getLogger());

        dependencyHandler.registerDependency(pluginDevCommandsIntegration, economy);

        dependencyHandler.registerDependency(pluginDevCommandsIntegration, autoUpdateService);
        dependencyHandler.registerDependency(pluginDevCommandsIntegration, companiesService);
        dependencyHandler.registerDependency(pluginDevCommandsIntegration, playersService);

    }

    private void configureConfig(){
        if(!new File(getDataFolder(), ConfigurationFiles.CONFIG.getFileName()).exists())
        	this.saveDefaultConfig();
    }
    
    private void initializeCompaniesData() {

        File companiesDirectory = new File(getDataFolder(), DataFilePath.COMPANIES.getDataPath());
        if (!companiesDirectory.exists()) {
            if (companiesDirectory.mkdir()) {
                File defaultCompanyFile = new File(getDataFolder(), DataFilePath.COMPANIES.getFullPathById("0"));
                try {
                    byte[] buffer = Objects.requireNonNull(getResource("companies/0.yml")).readAllBytes();

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

    private void initializePlayersData() {

        File playersDirectory = new File(getDataFolder(), DataFilePath.PLAYERS.getDataPath());
        if (!playersDirectory.exists()) {
            if (!playersDirectory.mkdir()) {
                getLogger().warning("Unable to create players directory.");
            }
        }

    }

    private void initializeSignsData() {

        File signsDirectory = new File(getDataFolder(), DataFilePath.SIGNS.getDataPath());
        if (!signsDirectory.exists()) {
            if (!signsDirectory.mkdir()) {
                getLogger().warning("Unable to create signs directory.");
            }
        }

    }


    private void configureMessages() {
    	this.messagesConfig = new ConfigAccessor(this, ConfigurationFiles.MESSAGES.getFileName());
		if(!new File(getDataFolder(), ConfigurationFiles.MESSAGES.getFileName()).exists())
			messagesConfig.saveDefaultConfig();
    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(playerJoinListener, this);
        getServer().getPluginManager().registerEvents(signsListener, this);
    }

    public void registerSchedulers() {
        int interestTime = getConfig().getInt("BlockStreet.InterestInterval"); // In minutes
        interestRateTask = new InterestRateScheduler(this, companiesService, signsService, messages)
                .runTaskTimerAsynchronously(this, 20L*10, 20L*60*interestTime);
    }

    public void stopSchedulers() {
        interestRateTask.cancel();
        // signCheckerTask.cancel();
    }

    private void setupEconomy() {

        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(Economy.class);

        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        } else {
            getLogger().log(Level.SEVERE, "[BlockStreet] Vault not found. Disabling plugin.");
            getServer().getPluginManager().disablePlugin(this);
        }

    }

}