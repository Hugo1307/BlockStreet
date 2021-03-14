package hugog.blockstreet.runnables;

import hugog.blockstreet.Main;
import hugog.blockstreet.others.Company;
import hugog.blockstreet.others.ConfigAccessor;
import hugog.blockstreet.others.Messages;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.MessageFormat;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class InterestRateRunnable extends BukkitRunnable {
	
    private static int minutesCounter = 0;

    private final Messages messages = new Messages(new ConfigAccessor(Main.getInstance(), "messages.yml"));
    private final ConfigAccessor companiesReg = new ConfigAccessor(Main.getInstance(), "companies.yml");
    private final int interestTime = Main.getInstance().getConfig().getInt("BlockStreet.Timer");

    @Override
    public void run() {

        minutesCounter++;

        boolean areWarningsEnabled = Main.getInstance().getConfig().getBoolean("BlockStreet.Warnings.Enabled");
        int timeTillInterests = interestTime - minutesCounter;

        if (areWarningsEnabled){

            int firstWarningTime = Main.getInstance().getConfig().getInt("BlockStreet.Warnings.First");
            int secondWarningTime = Main.getInstance().getConfig().getInt("BlockStreet.Warnings.Second");

            if (timeTillInterests == firstWarningTime && firstWarningTime != 0) {
                Bukkit.broadcastMessage(messages.getPluginPrefix() + MessageFormat.format(messages.getInterestRate().replace("'", "''"), timeTillInterests));
            }

            if (timeTillInterests == secondWarningTime && secondWarningTime != 0) {
                Bukkit.broadcastMessage(messages.getPluginPrefix() + MessageFormat.format(messages.getInterestRate().replace("'", "''"), timeTillInterests));
            }

        }

        if (timeTillInterests == 0){

            int randomSignal;
            double[] randomFeesByRisk = new double[5];
            double multiplier = 0;
            String signal;
            List<String> feesHistoric;

            if (companiesReg.getConfig().get("Companies") == null) return;

            Set<Integer> allCompaniesIds = companiesReg.getConfig().getConfigurationSection("Companies").getKeys(false).stream().map(Integer::parseInt).collect(Collectors.toSet());
            int leverage = new ConfigAccessor(Main.getInstance(), "config.yml").getConfig().getInt("BlockStreet.Leverage");

            Bukkit.broadcastMessage(messages.getPluginPrefix() + messages.getUpdatedInterestRate());
            minutesCounter = 0;

            for (int companyId : allCompaniesIds) {

                Company currentCompany = new Company(companyId).load();

                randomSignal = (int) (Math.random() * 10);
                randomFeesByRisk[0] = Math.round((Math.random() * 1) * 100.0) / 100.0;
                randomFeesByRisk[1] = Math.round((Math.random() * 2.5) * 100.0) / 100.0;
                randomFeesByRisk[2] = Math.round((Math.random() * 4) * 100.0) / 100.0;
                randomFeesByRisk[3] = Math.round((Math.random() * 7) * 100.0) / 100.0;
                randomFeesByRisk[4] = Math.round((Math.random() * 10) * 100.0) / 100.0;

                feesHistoric = currentCompany.getCompanyHistoric();
                feesHistoric.remove(feesHistoric.size() - 1);

                if (leverage >= 0 && leverage < 10) {

                    if (randomSignal > leverage) signal = "-";
                    else signal = "+";

                }else {
                    randomSignal = (int) (Math.random() * 2);
                    signal = randomSignal == 0 ? "-" : "+";
                }

                for (int i = 0; i < randomFeesByRisk.length; i++) {

                    if (i+1 == currentCompany.getRisk()) {
                        feesHistoric.add(0, signal + " " + randomFeesByRisk[i] + " %");
                        multiplier = signal.equals("-") ? 1 - randomFeesByRisk[i]/100 : 1 + randomFeesByRisk[i]/100;
                    }

                }

                currentCompany.setCompanyHistoric(feesHistoric);
                currentCompany.setStocksPrice(currentCompany.getStocksPrice() * multiplier);
                currentCompany.save();

            }

        }

    }

    public static int getMinutesCounter() {
        return minutesCounter;
    }

}
