package dev.hugog.minecraft.blockstreet.schedulers;

import dev.hugog.minecraft.blockstreet.BlockStreet;
import dev.hugog.minecraft.blockstreet.data.dao.CompanyDao;
import dev.hugog.minecraft.blockstreet.data.dao.QuoteDao;
import dev.hugog.minecraft.blockstreet.data.services.CompaniesService;
import dev.hugog.minecraft.blockstreet.data.services.SignsService;
import dev.hugog.minecraft.blockstreet.utils.Messages;
import dev.hugog.minecraft.blockstreet.utils.random.StocksRandomizer;
import org.bukkit.scheduler.BukkitRunnable;

import javax.inject.Inject;
import java.text.MessageFormat;
import java.util.List;

public class InterestRateScheduler extends BukkitRunnable {

    private final BlockStreet plugin;
    private final CompaniesService companiesService;
    private final SignsService signsService;
    private final Messages messages;

    @Inject
    public InterestRateScheduler(BlockStreet plugin, CompaniesService companiesService, SignsService signsService, Messages messages) {
        this.plugin = plugin;
        this.companiesService = companiesService;
        this.signsService = signsService;
        this.messages = messages;
    }

    @Override
    public void run() {

        // Broadcast message with the info that the interest rate has been updated
        plugin.getServer().broadcastMessage(messages.getPluginPrefix() + messages.getUpdatedInterestRate());

        List<CompanyDao> allCompanies = companiesService.getAllCompanies();
        for (CompanyDao company : allCompanies) {

            // Do not process companies that are bankrupt
            if (company.isBankrupt()) {
                continue;
            }

            StocksRandomizer stocksRandomizer = new StocksRandomizer(company.getRisk(), company.getInitialSharePrice());
            double newSharesQuote = stocksRandomizer.getRandomQuote(company.getCurrentSharePrice());
            double newSharePrice = stocksRandomizer.getRandomStockValue(company.getCurrentSharePrice(), newSharesQuote);

            // Check if the company stocks should crash
            if (shouldCrash(company.getRisk(), stocksRandomizer, newSharePrice)) {
                newSharePrice = 0;
            }

            // Update the company's share price with the new randomized value
            companiesService.updateCompanySharesValue(company.getId(), newSharePrice);

            // Save the new quote in the company's historic data
            QuoteDao quoteDao = new QuoteDao(newSharesQuote, newSharePrice, System.currentTimeMillis());
            companiesService.updateCompanyHistoric(company.getId(), quoteDao);

            // If the company just went bankrupt, we can broadcast a message
            if (newSharePrice <= 0) {
                plugin.getServer().broadcastMessage(messages.getPluginPrefix() + MessageFormat.format(messages.getCompanyStocksCrashed(), company.getName()));
            }

            // Update the signs for the company
            plugin.getServer().getScheduler().runTask(plugin, () -> signsService.updateBukkitSignsByCompany(company.getId()));

        }

    }

    /**
     * Checks if the company stocks should crash based on the risk, the new share price and a random chance.
     *
     * @param companyRisk      the risk of the company
     * @param stocksRandomizer the stocks randomizer used to generate the new share price
     * @param newSharePrice    the new share price of the company
     * @return true if the stocks should crash, false otherwise
     */
    private boolean shouldCrash(int companyRisk, StocksRandomizer stocksRandomizer, double newSharePrice) {
        double dangerZonePercentage = plugin.getConfig().getDouble("BlockStreet.StockCrash.Aggressiveness", 0.0);
        boolean shouldCrash = Math.random() < 0.3 * (Math.pow(1 + 0.35 * dangerZonePercentage, companyRisk)) - 0.35;
        return shouldCrash && stocksRandomizer.canCrash(newSharePrice);
    }

}
