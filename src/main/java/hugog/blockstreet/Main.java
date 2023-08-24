package hugog.blockstreet;

import hugog.blockstreet.enums.ConfigurationFiles;
import hugog.blockstreet.listeners.PlayerJoin;
import hugog.blockstreet.listeners.SignHandler;
import hugog.blockstreet.others.ConfigAccessor;
import hugog.blockstreet.runnables.InterestRateRunnable;
import hugog.blockstreet.runnables.SignCheckerRunnable;
import hugog.blockstreet.update.AutoUpdate;
import me.hgsoft.minecraft.devcommand.DevCommand;
import me.hgsoft.minecraft.devcommand.commands.executors.DevCommandExecutor;
import me.hgsoft.minecraft.devcommand.commands.handler.CommandHandler;
import me.hgsoft.minecraft.devcommand.integration.Integration;
import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.util.logging.Level;

public class Main extends JavaPlugin {

    public ConfigAccessor messagesConfig;
    public Economy economy = null;
    private static Main instance;
    private BukkitTask interestRateTask, signCheckerTask;

    @Override
    public void onEnable() {

    	instance = this;

        setupEconomy();

        registerCommands();

        registerEvents();

        configureConfig();
        
        configureMessages();

        configureCompaniesReg();

        registerRunnables();

        try {
			AutoUpdate.checkForUpdates();
		} catch (ParseException e) {
			System.out.println(ChatColor.DARK_RED + "[BlockStreet] Unable to search for new versions.");
		}
           	
        System.out.println("[BlockStreet] Plugin successfully enabled!");
        
    }

    @Override
    public void onDisable() {
        System.out.println("[BlockStreet] Plugin successfully disabled!");
    }

    private void registerCommands() {

        PluginCommand mainPluginCommand = getCommand("invest");

        if (mainPluginCommand != null) {

            DevCommand devCommand = DevCommand.getOrCreateInstance();
            Integration integration = Integration.createFromPlugin(this);
            CommandHandler commandHandler = devCommand.getCommandHandler();
            DevCommandExecutor devCommandExecutor = new DevCommandExecutor("invest", integration);

            mainPluginCommand.setExecutor(devCommandExecutor);
            commandHandler.initCommandsAutoConfiguration(integration);

        }

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
        getServer().getPluginManager().registerEvents(new PlayerJoin(this), this);
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

	public static Main getInstance() {
		return instance;
	}

}
