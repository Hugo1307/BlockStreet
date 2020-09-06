package hugog.blockstreet;

import hugog.blockstreet.others.ConfigAccessor;
import hugog.blockstreet.others.InterestRate;
import hugog.blockstreet.update.AutoUpdate;
import hugog.blockstreet.commands.CmdImplementer;
import hugog.blockstreet.events.PJoinEvent;
import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.parser.ParseException;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.io.*;
import java.util.*;

public class Main extends JavaPlugin {

    public ConfigAccessor playerReg;
    public ConfigAccessor messagesConfig;
    public Economy economy = null;

    @Override
    public void onEnable() {

        setupEconomy();

        registerCommands();

        configureConfig();
        
        configureMessages();

        configureCompaniesReg();

        InterestRate.startTimer();
        
        try {
			AutoUpdate.checkForUpdates();
		} catch (ParseException e) {
			System.out.println(ChatColor.DARK_RED + "[BlockStreet] Unable to search for new versions.");
		}
        
        Bukkit.getServer().getPluginManager().registerEvents(new PJoinEvent(this), this);
           	
        System.out.println("[BlockStreet] Plugin successfully enabled!");
        
    }

    @Override
    public void onDisable() {
        System.out.println("[BlockStreet] Plugin successfully disabled!");
    }

    private void registerCommands(){
    	getCommand("invest").setExecutor(new CmdImplementer(this));
    }

    private void configureConfig(){
    	
        if(!new File(getDataFolder(), "config.yml").exists()){
            saveDefaultConfig();
        }

        FileConfiguration config = getConfig();

        config.options().copyDefaults(true);

        config.addDefault("BlockStreet.Timer", 30);
        config.addDefault("BlockStreet.Leverage", (double) 0);
        config.addDefault("BlockStreet.Warnings.Active", true);
        config.addDefault("BlockStreet.Warnings.First", 10);
        config.addDefault("BlockStreet.Warnings.Second", 20);
        config.addDefault("BlockStreet.Updates.Reminder", true);
        
        saveConfig();

    }
    
    private void configureCompaniesReg() {
    	
		ConfigAccessor companiesReg = new ConfigAccessor(this, "companies.yml");
	    List<String> stocksHistoric = new ArrayList<String>();
        
	    if (!new File(getDataFolder(), "companies.yml").exists()) {
	    	 companiesReg.saveConfig();
	     }
	    
	   	 stocksHistoric.add("+ " + 2.3 + "%");
	     stocksHistoric.add("+ " + 4.8 + "%");
	     stocksHistoric.add("- " + 2.4 + "%");
	     stocksHistoric.add("- " + 0.9 + "%");
	     stocksHistoric.add("+ " + 1.1 + "%");
	     
	     companiesReg.getConfig().options().copyDefaults(true);
    	
	     companiesReg.getConfig().addDefault("Companies.1.Name", "Blocks Inc");
	     companiesReg.getConfig().addDefault("Companies.1.Price", 1253.0);
	     companiesReg.getConfig().addDefault("Companies.1.Risk", 4);
	     companiesReg.getConfig().addDefault("Companies.1.AvailableStocks", -1);
	     companiesReg.getConfig().addDefault("Companies.1.Historic", stocksHistoric);

	     companiesReg.getConfig().addDefault("Companies.2.Name", "iSteve");
	     companiesReg.getConfig().addDefault("Companies.2.Price", 989.0);
	     companiesReg.getConfig().addDefault("Companies.2.Risk", 3);
	     companiesReg.getConfig().addDefault("Companies.2.AvailableStocks", -1);
	     companiesReg.getConfig().addDefault("Companies.2.Historic", stocksHistoric);

	     companiesReg.getConfig().addDefault("Companies.3.Name", "MineCoins");
	     companiesReg.getConfig().addDefault("Companies.3.Price", 645.0);
	     companiesReg.getConfig().addDefault("Companies.3.Risk", 1);
	     companiesReg.getConfig().addDefault("Companies.3.AvailableStocks", -1);
	     companiesReg.getConfig().addDefault("Companies.3.Historic", stocksHistoric);
        
	     companiesReg.saveConfig();
	     
    }
    
    private void configureMessages() {
    	
    	this.messagesConfig = new ConfigAccessor(this, "messages.yml");
    	
    	if(!new File(getDataFolder(), "messages.yml").exists()){
            messagesConfig.saveDefaultConfig();
        }
    	
    	messagesConfig.getConfig().options().copyDefaults(true);
    	
    	messagesConfig.getConfig().addDefault("pluginReload", "Plugin's config successfully reloaded.");
    	
    	messagesConfig.getConfig().addDefault("menuMainCmd", "Show all plugin's commands.");
    	messagesConfig.getConfig().addDefault("menuCompaniesCmd", "List all companies.");
    	messagesConfig.getConfig().addDefault("menuCompanyCmd", "Show details of a company.");
    	messagesConfig.getConfig().addDefault("menuBuyCmd", "Buys stocks from company with the specified Id.");
    	messagesConfig.getConfig().addDefault("menuSellCmd", "Sells stocks from company with the specified Id.");
    	messagesConfig.getConfig().addDefault("menuActionsCmd", "List all your stocks.");
    	messagesConfig.getConfig().addDefault("menuCreateCompanyCmd", "Creates a new company.");
    	messagesConfig.getConfig().addDefault("menuReloadCmd", "Reload plugin's configuration.");
    	messagesConfig.getConfig().addDefault("menuTimeCmd", "Show time left to stocks update.");
    	
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
    	messagesConfig.getConfig().addDefault("insufficientActions", "This company doesn't have enough stocks to sell.");
    	messagesConfig.getConfig().addDefault("playerNoActions", "You don't have enough stocks.");
    	messagesConfig.getConfig().addDefault("playerAnyActions", "You don't have any stocks.");
    	messagesConfig.getConfig().addDefault("buyActionsCmd", "Buy an amount of stocks from this company.");
    	messagesConfig.getConfig().addDefault("sellActionsCmd", "Sell an amount of stocks from this company.");
    	messagesConfig.getConfig().addDefault("boughtActions", "You have bought {0} stocks.");
    	messagesConfig.getConfig().addDefault("soldActions", "You have sold {0} stocks for {1}.");
    	messagesConfig.getConfig().addDefault("createdCompany", "You created the company {0}.");
    	messagesConfig.getConfig().addDefault("deletedCompany", "You deleted the company {0}.");
    	messagesConfig.getConfig().addDefault("unknownCommand", "Unknown command.");
    	
    	messagesConfig.getConfig().addDefault("invalidCmd", "Invalid Command! Try again.");
    	messagesConfig.getConfig().addDefault("interestRate", "Commercial stocks will be updated within {0} minutes.");
    	messagesConfig.getConfig().addDefault("updatedInterestRate", "All commercial stocks have been updated.");
    	messagesConfig.getConfig().addDefault("interestRateTimeLeft", "{0} minutes left to commercial stocks update.");
    	
    	messagesConfig.getConfig().addDefault("newVersionAvailable", "Good news! New version of BlockStreet available.");
    	
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
