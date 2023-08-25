package dev.hugog.minecraft.blockstreet.others;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class Investor {

	private String name;
	private List<Investment> investments;
	
	private final JavaPlugin plugin = (JavaPlugin) Bukkit.getPluginManager().getPlugin("BlockStreet");
	private final ConfigAccessor ymlReg = new ConfigAccessor(plugin, "players.yml");
	
	public Investor(String name) {

		this.name = name;
		this.investments = new ArrayList<Investment>();
		
		ymlReg.reloadConfig();
		
		if (ymlReg.getConfig().get("Players." + name + ".Companies") != null) {
			
			ymlReg.getConfig().getConfigurationSection("Players." + name + ".Companies").getKeys(false).stream().forEach(companyId -> {
				
				int stocksAmount = ymlReg.getConfig().getInt("Players." + name + ".Companies." + companyId + ".Amount");
				this.investments.add(new Investment(Integer.parseInt(companyId), stocksAmount));
							
			});

        }
		
	}

	//Getters & Setters
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Investment> getInvestments() {
		return this.investments;
	}
	
	public Investment getInvestment(int companyId) {
		
		for (Investment investment : this.investments) 
			if (investment.getId() == companyId) return investment;				
		
		return null;
		
	}

	public void setInvestments(List<Investment> investments) {
		this.investments = investments;
	}
	
	// Other methods
	
	public void saveToYml() {
		
		ymlReg.reloadConfig();
		
		for (Investment investment : this.investments) {
			
			if (investment.getStocksAmount() > 0) {
				ymlReg.getConfig().set("Players." + name + ".Companies." + investment.getId() + ".Amount", investment.getStocksAmount());
				ymlReg.saveConfig();
			}else {
				ymlReg.getConfig().set("Players." + name + ".Companies." + investment.getId(), null);
				ymlReg.saveConfig();
			}
			
		}		
		
	}
	
	public void addInvestment(Investment investment) {

		this.investments.remove(investment);
		
		this.investments.add(investment);
				
	}
	
	public void removeInvestment(Investment investment) {
		this.investments.remove(investment);
	}
	
	public boolean contains(int investmentId) {
		
		for (Investment investment : this.investments) 
			if (investment.getId() == investmentId) return true;
		
		return false;
		
	}
	
}
