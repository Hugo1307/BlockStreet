package hugog.blockstreet;

import hugog.blockstreet.others.ConfigAccessor;
import hugog.blockstreet.others.Interest_Rate;
import hugog.blockstreet.commands.CmdImplementer;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.io.*;
import java.util.*;

public class Main extends JavaPlugin {

    public ConfigAccessor playerReg;
    public ConfigAccessor messagesConfig;
    public Economy economy = null;

    @Override
    public void onEnable() {
        System.out.println("[BlockStreet] Plugin successfully enabled!");
        Interest_Rate Interest_Rate = new Interest_Rate(this);

        setupEconomy();

        registerCommands();

        configureConfig();
        
        configureMessages();

        configurePlayerReg();

        Interest_Rate.startTimer();
    }

    @Override
    public void onDisable() {
        System.out.println("[BlockStreet] Plugin successfully disabled!");
    }

    private void configurePlayerReg(){

        playerReg = new ConfigAccessor(this, "players.yml");

        playerReg.saveDefaultConfig();

    }

    private void registerCommands(){
    	getCommand("invest").setExecutor(new CmdImplementer(this));
    }

    private void configureConfig(){
        if(!new File(getDataFolder(), "config.yml").exists()){
            saveDefaultConfig();
        }

        FileConfiguration config = getConfig();
        List<String> Variations = new ArrayList<String>();

        Variations.add("+ " + 2.3 + "%");
        Variations.add("+ " + 4.8 + "%");
        Variations.add("- " + 2.4 + "%");
        Variations.add("- " + 0.9 + "%");
        Variations.add("+ " + 1.1 + "%");

        config.options().copyDefaults(true);

        config.addDefault("BlockStreet.Permissions.AllowAllUsers", true);
        config.addDefault("BlockStreet.Interest.Time", 30);
        config.addDefault("BlockStreet.Warnings.Active", true);
        config.addDefault("BlockStreet.Warnings.First", 10);
        config.addDefault("BlockStreet.Warnings.Second", 20);
        
        config.addDefault("BlockStreet.Companies.Count", 5);

        config.addDefault("BlockStreet.Companies.1.Name", "Blocks Inc");
        config.addDefault("BlockStreet.Companies.1.Price", 1253.0);
        config.addDefault("BlockStreet.Companies.1.Risk", 4);
        config.addDefault("BlockStreet.Companies.1.AvailableActions", -1);
        config.addDefault("BlockStreet.Companies.1.Variations", Variations);

        config.addDefault("BlockStreet.Companies.2.Name", "iSteve");
        config.addDefault("BlockStreet.Companies.2.Price", 989.0);
        config.addDefault("BlockStreet.Companies.2.Risk", 3);
        config.addDefault("BlockStreet.Companies.2.AvailableActions", -1);
        config.addDefault("BlockStreet.Companies.2.Variations", Variations);

        config.addDefault("BlockStreet.Companies.3.Name", "MineCoins");
        config.addDefault("BlockStreet.Companies.3.Price", 645.0);
        config.addDefault("BlockStreet.Companies.3.Risk", 1);
        config.addDefault("BlockStreet.Companies.3.AvailableActions", -1);
        config.addDefault("BlockStreet.Companies.3.Variations", Variations);

        config.addDefault("BlockStreet.Companies.4.Name", "MineApple");
        config.addDefault("BlockStreet.Companies.4.Price", 398.0);
        config.addDefault("BlockStreet.Companies.4.Risk", 3);
        config.addDefault("BlockStreet.Companies.4.AvailableActions", -1);
        config.addDefault("BlockStreet.Companies.4.Variations", Variations);

        config.addDefault("BlockStreet.Companies.5.Name", "bitCube");
        config.addDefault("BlockStreet.Companies.5.Price", 125.0);
        config.addDefault("BlockStreet.Companies.5.Risk", 2);
        config.addDefault("BlockStreet.Companies.5.AvailableActions", -1);
        config.addDefault("BlockStreet.Companies.5.Variations", Variations);

        saveConfig();

    }
    
    private void configureMessages() {
    	
    	messagesConfig = new ConfigAccessor(this, "messages.yml");
    	
    	if(!new File(getDataFolder(), "messages.yml").exists()){
            messagesConfig.saveDefaultConfig();
        }
    	
    	messagesConfig.getConfig().options().copyDefaults(true);
    	
        messagesConfig.getConfig().addDefault("pluginReload", "Plugin's config successfully reloaded.");
    	
    	messagesConfig.getConfig().addDefault("menuMainCmd", "Show all plugin's commands.");
    	messagesConfig.getConfig().addDefault("menuCompaniesCmd", "List all companies.");
    	messagesConfig.getConfig().addDefault("menuCompanyCmd", "Show details of a company.");
    	messagesConfig.getConfig().addDefault("menuBuyCmd", "Buys actions from company with the specified Id.");
    	messagesConfig.getConfig().addDefault("menuSellCmd", "Sells actions from company with the specified Id.");
    	messagesConfig.getConfig().addDefault("menuActionsCmd", "List all your actions.");
    	messagesConfig.getConfig().addDefault("menuCreateCompanyCmd", "Creates a new company.");
    	messagesConfig.getConfig().addDefault("menuReloadCmd", "Reload plugin's configuration.");
    	
    	messagesConfig.getConfig().addDefault("listNextPage", "For next page.");
    	messagesConfig.getConfig().addDefault("id", "Id");
    	messagesConfig.getConfig().addDefault("price", "Price");
    	messagesConfig.getConfig().addDefault("risk", "Level of risk");
    	messagesConfig.getConfig().addDefault("availableActions", "Available Actions");
    	messagesConfig.getConfig().addDefault("actionHistoric", "Historic");
    	messagesConfig.getConfig().addDefault("details", "Details");
    	
    	messagesConfig.getConfig().addDefault("noPermission", "You don't have enough permissions.");
    	messagesConfig.getConfig().addDefault("nonExistantPage", "This page does not exists.");
    	messagesConfig.getConfig().addDefault("missingArguments", "There are missing arguments.");
    	messagesConfig.getConfig().addDefault("wrongArguments", "There are wrong arguments.");
    	messagesConfig.getConfig().addDefault("invalidCompany", "Invalid company.");
    	messagesConfig.getConfig().addDefault("companyAlreadyExists", "This company already exists.");
    	messagesConfig.getConfig().addDefault("insufficientMoney", "You don't have enough money.");
    	messagesConfig.getConfig().addDefault("insufficientActions", "This company doesn't have enough actions to sell.");
    	messagesConfig.getConfig().addDefault("playerNoActions", "You don't have enough actions.");
        messagesConfig.getConfig().addDefault("playerAnyActions", "You don't have any actions.");
    	messagesConfig.getConfig().addDefault("buyActionsCmd", "Buys an amount of actions from this company.");
    	messagesConfig.getConfig().addDefault("sellActionsCmd", "Sells an amount of actions from this company.");
    	messagesConfig.getConfig().addDefault("boughtActions", "You have bought {0} actions.");
    	messagesConfig.getConfig().addDefault("soldActions", "You have sold {0} actions for {1}.");
    	messagesConfig.getConfig().addDefault("createdCompany", "You, sucessfully, created the company {0}.");
    	
    	messagesConfig.getConfig().addDefault("invalidCmd", "Invalid Command! Try again.");
    	messagesConfig.getConfig().addDefault("interestRate", "You'll receive your interest rates within {0} minutes.");
    	messagesConfig.getConfig().addDefault("updatedInterestRate", "All actions have been updated.");
        
    	messagesConfig.saveConfig();
    	
    }

    private boolean setupEconomy()
    {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(Economy.class);
        if (economyProvider != null)
        {
            economy = (Economy)economyProvider.getProvider();
        }
        else
        {
            System.out.print("[" + getDescription().getName() + "][WARNING] NO ECONOMY SYSTEM FOUND! PLUGIN DISABLED.");
            getServer().getPluginManager().disablePlugin(this);
        }
        return economy != null;
    }
}
