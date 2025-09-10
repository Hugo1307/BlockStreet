package dev.hugog.minecraft.blockstreet;

import com.google.inject.Injector;
import dev.hugog.minecraft.blockstreet.api.services.AutoUpdateService;
import dev.hugog.minecraft.blockstreet.data.repositories.implementations.CompaniesRepository;
import dev.hugog.minecraft.blockstreet.data.services.CompaniesService;
import dev.hugog.minecraft.blockstreet.data.services.PlayersService;
import dev.hugog.minecraft.blockstreet.data.services.SignsService;
import dev.hugog.minecraft.blockstreet.dependencyinjection.BasicBinderModule;
import dev.hugog.minecraft.blockstreet.enums.ConfigurationFiles;
import dev.hugog.minecraft.blockstreet.enums.DataFilePath;
import dev.hugog.minecraft.blockstreet.listeners.PlayerJoinListener;
import dev.hugog.minecraft.blockstreet.listeners.SignsListener;
import dev.hugog.minecraft.blockstreet.migration.MigrationHandler;
import dev.hugog.minecraft.blockstreet.schedulers.InterestRateScheduler;
import dev.hugog.minecraft.blockstreet.ui.GuiManager;
import dev.hugog.minecraft.blockstreet.utils.BlockStreetValidationConfiguration;
import dev.hugog.minecraft.blockstreet.utils.ConfigAccessor;
import dev.hugog.minecraft.blockstreet.utils.Messages;
import dev.hugog.minecraft.dev_command.DevCommand;
import dev.hugog.minecraft.dev_command.commands.executors.DevCommandExecutor;
import dev.hugog.minecraft.dev_command.commands.handler.CommandHandler;
import dev.hugog.minecraft.dev_command.dependencies.DependencyHandler;
import dev.hugog.minecraft.dev_command.integration.Integration;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Objects;
import java.util.OptionalInt;
import java.util.logging.Level;
import javax.inject.Inject;
import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public class BlockStreet extends JavaPlugin {

    @Getter
    private Economy economy;
    private BukkitTask interestRateTask;

    // Listeners
    @Inject
    private PlayerJoinListener playerJoinListener;
    @Inject
    private SignsListener signsListener;

    // Services
    @Inject
    private AutoUpdateService autoUpdateService;
    @Inject
    private CompaniesService companiesService;
    @Inject
    private PlayersService playersService;
    @Inject
    private SignsService signsService;

    // UI
    @Inject
    private GuiManager guiManager;

    // Utils
    @Inject
    private Messages messages;
    @Inject
    private MigrationHandler migrationHandler;

    @Override
    public void onEnable() {

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

        // Check if the plugin needs to migrate data and do it if necessary
        migrationHandler.checkMigrations();

        getLogger().info("Plugin successfully enabled!");

    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin successfully disabled!");
    }

    private void initDependencyInjectionModules() {
        BasicBinderModule guiceBinderModule = new BasicBinderModule(this);
        Injector injector = guiceBinderModule.createInjector();
        injector.injectMembers(this);
    }

    public void checkForUpdates() {
        autoUpdateService.isUpdateAvailable().thenAcceptAsync(isUpdateAvailable -> {
            if (isUpdateAvailable) {
                getLogger().warning("An update is available! Download it at: https://modrinth.com/plugin/blockstreet");
            } else {
                getLogger().info("You are using the latest version of BlockStreet!");
            }
        });
    }

    private void initializeDevCommands() {

        PluginCommand mainPluginCommand = getCommand("invest");
        Integration pluginDevCommandsIntegration = Integration.createFromPlugin(this);

        if (mainPluginCommand != null) {
            DevCommandExecutor commandExecutor = new DevCommandExecutor("invest", pluginDevCommandsIntegration);
            mainPluginCommand.setExecutor(commandExecutor);
            mainPluginCommand.setTabCompleter(commandExecutor);

            DevCommand devCommand = DevCommand.getOrCreateInstance();
            CommandHandler commandHandler = devCommand.getCommandHandler();

            commandHandler.initCommandsAutoConfiguration(pluginDevCommandsIntegration);
            commandHandler.useAutoValidationConfiguration(new BlockStreetValidationConfiguration(messages));
        }

    }

    private void registerCommandsDependencies() {

        DevCommand devCommand = DevCommand.getOrCreateInstance();
        DependencyHandler dependencyHandler = devCommand.getDependencyHandler();
        Integration pluginDevCommandsIntegration = Integration.createFromPlugin(this);

        dependencyHandler.registerDependency(pluginDevCommandsIntegration, this);
        dependencyHandler.registerDependency(pluginDevCommandsIntegration, messages);
        dependencyHandler.registerDependency(pluginDevCommandsIntegration, getLogger());

        dependencyHandler.registerDependency(pluginDevCommandsIntegration, economy);

        dependencyHandler.registerDependency(pluginDevCommandsIntegration, autoUpdateService);
        dependencyHandler.registerDependency(pluginDevCommandsIntegration, companiesService);
        dependencyHandler.registerDependency(pluginDevCommandsIntegration, playersService);

        dependencyHandler.registerDependency(pluginDevCommandsIntegration, guiManager);

    }

    private void configureConfig(){
        if (!new File(getDataFolder(), ConfigurationFiles.CONFIG.getFileName()).exists())
        	this.saveDefaultConfig();
        else {
            this.getConfig().options().copyDefaults(true);
            this.saveConfig();
        }
    }

    private void initializeCompaniesData() {
        File companiesDirectory = new File(getDataFolder(), DataFilePath.COMPANIES.getDataPath());
        File companiesDataFile = new File(getDataFolder(), DataFilePath.COMPANIES.getFullPathById("data"));

        if (companiesDirectory.exists() && companiesDataFile.exists()) {
            return;
        }

        companiesDirectory.mkdirs();

        // Create the default company file by copying from resources
        File defaultCompanyFile = new File(getDataFolder(), DataFilePath.COMPANIES.getFullPathById("0"));
        try (OutputStream outStream = Files.newOutputStream(defaultCompanyFile.toPath())) {
            byte[] defaultCompanyBuffer = Objects.requireNonNull(getResource("companies/0.yml")).readAllBytes();
            outStream.write(defaultCompanyBuffer);
        } catch (IOException e) {
            getLogger().log(Level.WARNING, "Unable to create default company data file.", e);
            return;
        }

        // Create the companies data file and write the nextId in it
        try {
            if (companiesDataFile.createNewFile()) {
                OptionalInt maxId = ((CompaniesRepository) companiesService.getRepository()).getAllIds().stream()
                        .mapToInt(Long::intValue)
                        .max();

                maxId.ifPresent(companyMaxId -> {
                    try {
                        Files.writeString(companiesDataFile.toPath(), String.format("nextId: %s", companyMaxId + 1));
                    } catch (IOException e) {
                        getLogger().log(Level.WARNING, "Unable to write nextId to companies data file.", e);
                    }
                });
                getLogger().info("Companies data file created successfully.");
            }
        } catch (IOException e) {
            getLogger().log(Level.WARNING, "Unable to create companies data file.", e);
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
    	ConfigAccessor messagesConfig = new ConfigAccessor(this, ConfigurationFiles.MESSAGES.getFileName());
		if(!new File(getDataFolder(), ConfigurationFiles.MESSAGES.getFileName()).exists())
			messagesConfig.saveDefaultConfig();
        else {
            messagesConfig.getConfig().options().copyDefaults(true);
            messagesConfig.saveConfig();
        }
    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(playerJoinListener, this);
        getServer().getPluginManager().registerEvents(signsListener, this);
    }

    public void registerSchedulers() {
        int interestTime = getConfig().getInt("BlockStreet.InterestInterval"); // In minutes
        interestRateTask = new InterestRateScheduler(this, companiesService, signsService, playersService, messages)
                .runTaskTimerAsynchronously(this, 20L*10, 20L*interestTime);
    }

    public void stopSchedulers() {
        interestRateTask.cancel();
    }

    private void setupEconomy() {

        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(Economy.class);

        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        } else {
            getLogger().log(Level.SEVERE, "Vault not found. Disabling plugin.");
            getServer().getPluginManager().disablePlugin(this);
        }

    }

}