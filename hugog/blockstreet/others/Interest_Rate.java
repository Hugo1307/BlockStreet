package hugog.blockstreet.others;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import hugog.blockstreet.Main;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class Interest_Rate{

    int minutesCounter = 0;
    private Main Main;
    public Interest_Rate(Main plugin)
    {
        this.Main = plugin;
    }

    public void startTimer(){
    	
    	Messages messages = new Messages(Main.messagesConfig);
    	List<float[]> companiesFees = new ArrayList<float[]>();
    	int interestTime = Main.getConfig().getInt("BlockStreet.Interest.Time");
    	
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Main, new Runnable() {

            public void run(){
                minutesCounter++;

                if (Main.getConfig().getBoolean("BlockStreet.Warnings.Active")){
                    if (minutesCounter == Main.getConfig().getInt("BlockStreet.Warnings.First")){
                        Bukkit.broadcastMessage(messages.getPluginPrefix() + MessageFormat.format(messages.getInterestRate().replace("'", "''"), 
                        		(interestTime - Main.getConfig().getInt("BlockStreet.Warnings.First"))));
                    }
                    if (minutesCounter == Main.getConfig().getInt("BlockStreet.Warnings.Second")){
                        Bukkit.broadcastMessage(messages.getPluginPrefix() + MessageFormat.format(messages.getInterestRate().replace("'", "''"), 
                        		(interestTime - Main.getConfig().getInt("BlockStreet.Warnings.Second"))));
                    }
                }

                if (minutesCounter == interestTime){
                    minutesCounter = 0;
                    
                    Bukkit.broadcastMessage(messages.getPluginPrefix() + messages.getUpdatedInterestRate());
                    
                    int companiesCount = Main.getConfig().getInt("BlockStreet.Companies.Count");

                    List<String> Historic = new ArrayList<String>();

                    int randomSignal = -1;
                    float randomFee_R1 = 0, randomFee_R2 = 0, randomFee_R3 = 0, randomFee_R4 = 0, randomFee_R5 = 0;
                    
                    companiesFees.clear();
                    
                    for (int company = 1; company <= companiesCount; company++) {

                        randomSignal = (int)(Math.random() * 2);
                        randomFee_R1 = (float) (Math.round((Math.random() * 1) * 100.0) / 100.0);
                        randomFee_R2 = (float) (Math.round((Math.random() * 2.5) * 100.0) / 100.0);
                        randomFee_R3 = (float) (Math.round((Math.random() * 4) * 100.0) / 100.0);
                        randomFee_R4 = (float) (Math.round((Math.random() * 7) * 100.0) / 100.0);
                        randomFee_R5 = (float) (Math.round((Math.random() * 10) * 100.0) / 100.0);
                        String signal = "";

                        Historic = Main.getConfig().getStringList("BlockStreet.Companies." + company +".Variations");

                        Historic.remove(Historic.size() - 1);

                        if (randomSignal == 0){
                            signal = "-";
                        }else if (randomSignal == 1){
                            signal = "+";
                        }

                        switch (Main.getConfig().getInt("BlockStreet.Companies." + company + ".Risk")){
                            case 1:
                                Historic.add(0, signal + " " + randomFee_R1 + " %");
                                companiesFees.add(new float[] {randomSignal, randomFee_R1});
                                break;
                            case 2:
                                Historic.add(0, signal + " " + randomFee_R2 + " %");
                                companiesFees.add(new float[] {randomSignal, randomFee_R2});
                                break;
                            case 3:
                                Historic.add(0, signal + " " + randomFee_R3 + " %");
                                companiesFees.add(new float[] {randomSignal, randomFee_R3});
                                break;
                            case 4:
                                Historic.add(0, signal + " " + randomFee_R4 + " %");
                                companiesFees.add(new float[] {randomSignal, randomFee_R4});
                                break;
                            case 5:
                                Historic.add(0, signal + " " + randomFee_R5 + " %");
                                companiesFees.add(new float[] {randomSignal, randomFee_R5});
                                break;
                            default:
                                break;
                        }

                        Main.getConfig().set("BlockStreet.Companies." + company +".Variations", Historic);
                        Main.saveConfig();

                    }

                    for (Player onlinePlayer : Bukkit.getServer().getOnlinePlayers()){
                        for (int companyNumber = 1; companyNumber <= companiesCount; companyNumber++) {
                            if (Main.playerReg.getConfig().get("Players." + onlinePlayer.getName() + ".Companies." + companyNumber + ".Amount") != null){

                                int actionsValue = Main.playerReg.getConfig().getInt("Players." + onlinePlayer.getName() + ".Companies." + companyNumber + ".Value");
                                int companyRisk = Main.getConfig().getInt("BlockStreet.Companies." + companyNumber + ".Risk");

                                if (companiesFees.get(companyNumber - 1)[0] == 0){

                                	actionsValue -= actionsValue * companiesFees.get(companyNumber - 1)[1] / 100;

                                }else if (companiesFees.get(companyNumber - 1)[0] == 1){
                                    
                                	actionsValue += actionsValue * companiesFees.get(companyNumber - 1)[1] / 100;
                                	
                                }

                                Main.playerReg.getConfig().set("Players." + onlinePlayer.getName() + ".Companies." + companyNumber + ".Value", actionsValue);
                                Main.playerReg.saveConfig();
                                Main.playerReg.reloadConfig();

                            }
                        }
                    }

                }

            }
        },(long) (20 * 60), (long) (20 * 60)); // 20 ticks == 1 sec

    }

}
