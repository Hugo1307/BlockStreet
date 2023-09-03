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

        List<CompanyDao> allCompanies = companiesService.getAllCompanies();

        for (CompanyDao company : allCompanies) {

            StocksRandomizer stocksRandomizer = new StocksRandomizer(company.getRisk(), company.getInitialSharePrice());
            double newSharesQuote = stocksRandomizer.getRandomQuote(company.getCurrentSharePrice());
            double newSharePrice = stocksRandomizer.getRandomStockValue(company.getCurrentSharePrice(), newSharesQuote);

            QuoteDao quoteDao = new QuoteDao(newSharesQuote, newSharePrice, System.currentTimeMillis());

            companiesService.updateCompanyHistoric(company.getId(), quoteDao);
            companiesService.updateCompanySharesValue(company.getId(), newSharePrice);

            plugin.getServer().getScheduler().runTask(plugin, () -> {
                signsService.updateBukkitSignsByCompany(company.getId());
            });


        }

        plugin.getServer().broadcastMessage(messages.getPluginPrefix() + messages.getUpdatedInterestRate());

    }

}
