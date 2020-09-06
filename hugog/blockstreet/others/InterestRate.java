package hugog.blockstreet.others;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class InterestRate{
	
    public static int minutesCounter = 0;
    private static final JavaPlugin plugin = (JavaPlugin) Bukkit.getPluginManager().getPlugin("BlockStreet");
    
    public static void startTimer(){
    	
    	Messages messages = new Messages(new ConfigAccessor(plugin, "messages.yml"));
    	ConfigAccessor companiesReg = new ConfigAccessor(plugin, "companies.yml");
    	int interestTime = plugin.getConfig().getInt("BlockStreet.Timer");
    	
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {

            public void run(){
                minutesCounter++;

                /*
                 * Broadcasts warnings to the server when X minutes left to interest rate update.
                 */
                
                if (plugin.getConfig().getBoolean("BlockStreet.Warnings.Active")){
                	
                    if (minutesCounter == plugin.getConfig().getInt("BlockStreet.Warnings.First") && plugin.getConfig().getInt("BlockStreet.Warnings.First") != 0)
                        Bukkit.broadcastMessage(messages.getPluginPrefix() + MessageFormat.format(messages.getInterestRate().replace("'", "''"), 
                        		(interestTime - plugin.getConfig().getInt("BlockStreet.Warnings.First"))));
                    
                    if (minutesCounter == plugin.getConfig().getInt("BlockStreet.Warnings.Second") && plugin.getConfig().getInt("BlockStreet.Warnings.Second") != 0)
                        Bukkit.broadcastMessage(messages.getPluginPrefix() + MessageFormat.format(messages.getInterestRate().replace("'", "''"), 
                        		(interestTime - plugin.getConfig().getInt("BlockStreet.Warnings.Second"))));
                    
                }

                if (minutesCounter == interestTime){
                	
                	int companiesCount = 0, randomSignal = -1;
                	double[] randomFeesByRisk = new double[5];
                	double multiplier = 0, leverage = 0;
                    String signal;
                    List<String> feesHistoric = new ArrayList<String>();                    
                	
                	if (companiesReg.getConfig().get("Companies") != null) 
                		companiesCount = companiesReg.getConfig().getConfigurationSection("Companies").getKeys(false).size();
                	              	              	
                	leverage = new ConfigAccessor(plugin, "config.yml").getConfig().getDouble("BlockStreet.Leverage");
                	
                    Bukkit.broadcastMessage(messages.getPluginPrefix() + messages.getUpdatedInterestRate());
                	minutesCounter = 0;                                      
                    
                    for (int companyId = 1; companyId <= companiesCount; companyId++) {

                    	Company currentCompany = new Company(companiesReg, companyId);
                    	
                        randomSignal = (int)(Math.random() * 2);
                        randomFeesByRisk[0] = Math.round((Math.random() * 1) * 100.0) / 100.0 + leverage;
                        randomFeesByRisk[1] = Math.round((Math.random() * 2.5) * 100.0) / 100.0 + leverage;
                        randomFeesByRisk[2] = Math.round((Math.random() * 4) * 100.0) / 100.0 + leverage;
                        randomFeesByRisk[3] = Math.round((Math.random() * 7) * 100.0) / 100.0 + leverage;
                        randomFeesByRisk[4] = Math.round((Math.random() * 10) * 100.0) / 100.0 + leverage;

                        feesHistoric = currentCompany.getCompanyHistoric();
                        feesHistoric.remove(feesHistoric.size() - 1);

                        signal = randomSignal == 0 ? "-" : "+";                        

                        for (int i = 0; i < randomFeesByRisk.length; i++) {
                        	
                        	if (i+1 == currentCompany.getRisk()) {
                        		feesHistoric.add(0, signal + " " + randomFeesByRisk[i] + " %");
                        		multiplier = randomSignal == 0 ? 1 - randomFeesByRisk[i]/100 : 1 + randomFeesByRisk[i]/100; 
                        	}
                        	                       	
                        }
                        
                        currentCompany.setCompanyHistoric(feesHistoric);                      
                        currentCompany.setStocksPrice(currentCompany.getStocksPrice() * multiplier);                                         
                        currentCompany.saveToYML();

                    }

                }

            }
            
        }, 20 * 60, 20 * 60); 

    }

}
