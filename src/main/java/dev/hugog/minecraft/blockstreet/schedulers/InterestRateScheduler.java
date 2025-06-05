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

        // Read stock crash configuration
        boolean crashEnabled = plugin.getConfig().getBoolean("StockCrash.Enabled", true); // Default to true if not found
        double dangerZonePercentage = plugin.getConfig().getDouble("StockCrash.DangerZonePercentage", 0.3); // Default to 0.3
        double crashChance = plugin.getConfig().getDouble("StockCrash.CrashChance", 0.1); // Default to 0.1

        List<CompanyDao> allCompanies = companiesService.getAllCompanies();

        for (CompanyDao company : allCompanies) {

            // Get full CompanyDao object. The one from getAllCompanies might be sufficient,
            // but if it's a light version, refetch or ensure it's complete.
            // Assuming the CompanyDao from getAllCompanies() is complete enough for isBankrupt() and initialSharePrice.
            // If not, an explicit call like:
            // CompanyDao currentCompanyDao = companiesService.getCompanyDaoById(company.getId());
            // if (currentCompanyDao == null) continue;
            // For now, we'll assume `company` is sufficient.

            if (company.isBankrupt()) {
                // If company is bankrupt, its price is 0 and should not change.
                // We might still need to update signs for it if its state just changed.
                // However, the current logic updates signs based on the main company object,
                // so if price is 0, signs should reflect that eventually.
                // We also need to ensure that processPotentialStockCrash is not called again.
                // The check within processPotentialStockCrash for isBankrupt() handles this.

                // It's important to update signs even for bankrupt companies to reflect their status
                final CompanyDao companyToUpdateSigns = company; // Make effectively final for lambda
                plugin.getServer().getScheduler().runTask(plugin, () -> {
                    signsService.updateBukkitSignsByCompany(companyToUpdateSigns.getId());
                });
                continue; // Skip price randomizing and crash processing for already bankrupt stock
            }

            StocksRandomizer stocksRandomizer = new StocksRandomizer(company.getRisk(), company.getInitialSharePrice());
            double newSharesQuote = stocksRandomizer.getRandomQuote(company.getCurrentSharePrice());
            double newSharePrice = stocksRandomizer.getRandomStockValue(company.getCurrentSharePrice(), newSharesQuote);

            // Update the company's share price with the new randomized value
            companiesService.updateCompanySharesValue(company.getId(), newSharePrice);

            // Now, process potential stock crash based on the updated price
            // The company DAO needs to be refreshed here to get the just-updated price
            CompanyDao updatedCompanyDao = companiesService.getCompanyDaoById(company.getId());
            if (updatedCompanyDao != null) {
                 // Store the price *before* potential crash, in case we want to message about it
                double priceBeforeCrashCheck = updatedCompanyDao.getCurrentSharePrice();
                companiesService.processPotentialStockCrash(updatedCompanyDao.getId(), crashEnabled, dangerZonePercentage, crashChance);

                // Refresh DAO again to see if a crash occurred
                CompanyDao companyAfterCrashCheck = companiesService.getCompanyDaoById(updatedCompanyDao.getId());
                if (companyAfterCrashCheck != null && companyAfterCrashCheck.isBankrupt() && priceBeforeCrashCheck > 0) {
                    // Stock just crashed in this cycle
                    plugin.getServer().broadcastMessage(messages.getPluginPrefix() + "Stock " + companyAfterCrashCheck.getName() + " has crashed and is now worthless!");
                }
            }


            // Update historic data and signs regardless of crash (price will be 0 if crashed)
            // Ensure we use the most up-to-date company DAO for quote history
            CompanyDao finalCompanyState = companiesService.getCompanyDaoById(company.getId());
            if (finalCompanyState != null) {
                QuoteDao quoteDao = new QuoteDao(newSharesQuote, finalCompanyState.getCurrentSharePrice(), System.currentTimeMillis());
                companiesService.updateCompanyHistoric(finalCompanyState.getId(), quoteDao);

                // Update signs in the main thread
                plugin.getServer().getScheduler().runTask(plugin, () -> {
                    signsService.updateBukkitSignsByCompany(finalCompanyState.getId());
                });
            }

        }

        plugin.getServer().broadcastMessage(messages.getPluginPrefix() + messages.getUpdatedInterestRate());

    }

}
