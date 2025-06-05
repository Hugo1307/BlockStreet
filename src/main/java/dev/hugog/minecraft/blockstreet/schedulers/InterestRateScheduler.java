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

            double dangerZonePercentage = plugin.getConfig().getDouble("BlockStreet.StockCrash.DangerZonePercentage", 0.0);

            StocksRandomizer stocksRandomizer = new StocksRandomizer(company.getRisk(), company.getInitialSharePrice(), dangerZonePercentage);
            double newSharesQuote = stocksRandomizer.getRandomQuote(company.getCurrentSharePrice());
            double newSharePrice = stocksRandomizer.getRandomStockValue(company.getCurrentSharePrice(), newSharesQuote);

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

}
