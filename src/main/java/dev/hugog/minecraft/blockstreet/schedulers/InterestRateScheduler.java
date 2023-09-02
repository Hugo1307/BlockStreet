package dev.hugog.minecraft.blockstreet.schedulers;

import dev.hugog.minecraft.blockstreet.data.dao.CompanyDao;
import dev.hugog.minecraft.blockstreet.data.services.CompaniesService;
import dev.hugog.minecraft.blockstreet.utils.Messages;
import dev.hugog.minecraft.blockstreet.utils.random.StocksRandomizer;
import org.bukkit.Server;
import org.bukkit.scheduler.BukkitRunnable;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class InterestRateScheduler extends BukkitRunnable {

    private final Server server;
    private final CompaniesService companiesService;
    private final Messages messages;

    @Inject
    public InterestRateScheduler(Server server, CompaniesService companiesService, Messages messages) {
        this.server = server;
        this.companiesService = companiesService;
        this.messages = messages;
    }

    @Override
    public void run() {

        List<CompanyDao> allCompanies = companiesService.getAllCompanies();

        for (CompanyDao company : allCompanies) {

            StocksRandomizer stocksRandomizer = new StocksRandomizer(company.getRisk(), company.getInitialSharePrice());
            double newSharesQuote = stocksRandomizer.getRandomQuote(company.getCurrentSharePrice());
            double newSharePrice = stocksRandomizer.getRandomStockValue(company.getCurrentSharePrice(), newSharesQuote);

            companiesService.updateCompanyHistoric(company.getId(), newSharesQuote);
            companiesService.updateCompanySharesValue(company.getId(), newSharePrice);

        }

        server.broadcastMessage(messages.getPluginPrefix() + messages.getUpdatedInterestRate());

    }

}
