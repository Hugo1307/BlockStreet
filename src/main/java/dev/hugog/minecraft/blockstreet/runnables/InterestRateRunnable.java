package dev.hugog.minecraft.blockstreet.runnables;

import dev.hugog.minecraft.blockstreet.BlockStreet;
import dev.hugog.minecraft.blockstreet.others.Company;
import dev.hugog.minecraft.blockstreet.others.ConfigAccessor;
import dev.hugog.minecraft.blockstreet.others.Messages;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.MessageFormat;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class InterestRateRunnable extends BukkitRunnable {

    @Getter
    private static int minutesCounter = 0;

    private final Messages messages = new Messages();
    private final ConfigAccessor companiesReg = new ConfigAccessor(BlockStreet.getInstance(), "companies.yml");
    private final int interestTime = BlockStreet.getInstance().getConfig().getInt("BlockStreet.Timer");

    @Override
    public void run() {

        boolean areWarningsEnabled = BlockStreet.getInstance().getConfig().getBoolean("BlockStreet.Warnings.Enabled");
        int timeTillInterests = interestTime - ++minutesCounter;

        if (areWarningsEnabled){

            int firstWarningTime = BlockStreet.getInstance().getConfig().getInt("BlockStreet.Warnings.First");
            int secondWarningTime = BlockStreet.getInstance().getConfig().getInt("BlockStreet.Warnings.Second");

            if (timeTillInterests == firstWarningTime && firstWarningTime != 0) {
                Bukkit.broadcastMessage(messages.getPluginPrefix() + MessageFormat.format(messages.getInterestRate().replace("'", "''"), timeTillInterests));
            }

            if (timeTillInterests == secondWarningTime && secondWarningTime != 0) {
                Bukkit.broadcastMessage(messages.getPluginPrefix() + MessageFormat.format(messages.getInterestRate().replace("'", "''"), timeTillInterests));
            }

        }

        if (timeTillInterests <= 0) {

            int randomSignal;
            double[] randomFeesByRisk = new double[5];
            double multiplier = 0;
            String signal;
            List<String> feesHistoric;

            if (companiesReg.getConfig().get("Companies") == null) return;

            Set<Integer> allCompaniesIds = companiesReg.getConfig().getConfigurationSection("Companies").getKeys(false).stream().map(Integer::parseInt).collect(Collectors.toSet());
            int leverage = new ConfigAccessor(BlockStreet.getInstance(), "config.yml").getConfig().getInt("BlockStreet.Leverage");

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

            BlockStreet.getInstance().getServer().broadcastMessage(messages.getPluginPrefix() + messages.getUpdatedInterestRate());
            minutesCounter = 0;

        }

    }

}
