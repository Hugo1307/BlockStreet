package dev.hugog.minecraft.blockstreet.data.services;

import dev.hugog.minecraft.blockstreet.data.dao.CompanyDao;
import dev.hugog.minecraft.blockstreet.data.dao.InvestmentDao;
import dev.hugog.minecraft.blockstreet.data.dao.PlayerDao;
import dev.hugog.minecraft.blockstreet.data.repositories.Repository;
import dev.hugog.minecraft.blockstreet.data.repositories.implementations.PlayersRepository;
import dev.hugog.minecraft.blockstreet.enums.NotificationType;
import org.bukkit.Bukkit;

import javax.inject.Inject;
import java.util.*;

public class PlayersService implements Service {

    private final PlayersRepository playersRepository;

    @Inject
    public PlayersService(PlayersRepository playersRepository) {
        this.playersRepository = playersRepository;
    }

    public void addSharesToPlayer(UUID playerId, CompanyDao company, int sharesAmount) {

        PlayerDao playerDao = getOrCreatePlayer(playerId);
        InvestmentDao recentInvestment = new InvestmentDao(company.getId(), sharesAmount, company.getCurrentSharePrice());

        boolean hasInvestedInCompany = playerDao.getInvestments().stream().anyMatch(playerInvestment -> playerInvestment.getCompanyId() == recentInvestment.getCompanyId());

        if (hasInvestedInCompany) {
            playerDao.getInvestments().stream()
                    .filter(playerInvestment -> playerInvestment.getCompanyId() == recentInvestment.getCompanyId())
                    .findFirst()
                    .ifPresent(playerInvestment -> {
                        playerInvestment.setSharesAmount(playerInvestment.getSharesAmount() + recentInvestment.getSharesAmount());
                        playerInvestment.setAverageBuyPrice(calculateNewAverageBuyPrice(playerInvestment, recentInvestment));
                    });
        } else {
            playerDao.getInvestments().add(recentInvestment);
        }

        playersRepository.save(playerDao.toEntity());

    }

    public void removeSharesFromPlayer(UUID playerId, long companyId, int sharesAmount) {

        PlayerDao playerDao = getOrCreatePlayer(playerId);

        playerDao.getInvestments().stream()
                .filter(playerInvestment -> playerInvestment.getCompanyId() == companyId)
                .findFirst()
                .ifPresent(playerInvestment -> {
                    if (playerInvestment.getSharesAmount() <= sharesAmount) {
                        playerDao.getInvestments().removeIf(existingInvestment -> existingInvestment.getCompanyId() == companyId);
                    } else {
                        playerInvestment.setSharesAmount(playerInvestment.getSharesAmount() - sharesAmount);
                    }

                });

        playersRepository.save(playerDao.toEntity());

    }

    public boolean hasSharesInCompany(UUID playerId, long companyId, int sharesAmount) {

        PlayerDao playerDao = getOrCreatePlayer(playerId);

        return playerDao.getInvestments().stream()
                .anyMatch(investment -> investment.getCompanyId() == companyId && investment.getSharesAmount() >= sharesAmount);

    }

    public long getTotalPlayerSharesCount(UUID playerId) {
        return getOrCreatePlayer(playerId).getInvestments().stream()
                .mapToLong(InvestmentDao::getSharesAmount)
                .sum();
    }

    public double getTotalPortfolioValue(UUID playerId, CompaniesService companiesService) {
        return getOrCreatePlayer(playerId).getInvestments().stream()
                .mapToDouble(investment -> companiesService.getCompanyInvestmentValue(investment.getCompanyId(), investment.getSharesAmount()))
                .sum();
    }

    public List<InvestmentDao> getInvestments(UUID playerId) {
        return Collections.unmodifiableList(getOrCreatePlayer(playerId).getInvestments());
    }

    public Optional<InvestmentDao> getInvestmentInCompany(UUID playerId, long companyId) {
        PlayerDao playerDao = getOrCreatePlayer(playerId);
        return playerDao.getInvestments().stream()
                .filter(investment -> investment.getCompanyId() == companyId)
                .findFirst();
    }

    public boolean hasAnyInvestments(UUID playerId) {
        return !getInvestments(playerId).isEmpty();
    }

    /**
     * Cleans up investments for a player by removing shares from companies that no longer exist.
     *
     * @param playerId          the unique identifier of the player
     * @param existingCompanies a list containing all existing companies
     */
    public void cleanUpInvestments(UUID playerId, List<CompanyDao> existingCompanies) {
        if (!playerExists(playerId)) {
            return; // No need to clean up if the player does not exist
        }
        getInvestments(playerId).stream()
                .filter(investment -> existingCompanies.stream().noneMatch(company -> company.getId() == investment.getCompanyId()))
                .forEach(investment -> removeSharesFromPlayer(playerId, investment.getCompanyId(), investment.getSharesAmount()));
    }

    /**
     * Cleans up investments for all online players by removing shares from companies that no longer exist.
     *
     * @param existingCompanies a list containing all existing companies
     */
    public void cleanUpInvestmentsForOnlinePlayers(List<CompanyDao> existingCompanies) {
        Bukkit.getOnlinePlayers().forEach(player -> {
            UUID playerId = player.getUniqueId();
            cleanUpInvestments(playerId, existingCompanies);
        });
    }

    /**
     * Checks if a player has notifications enabled. If the setting is null, it defaults to true.
     *
     * @param playerId the unique identifier of the player
     * @return true if notifications are enabled or the setting is null, false otherwise
     */
    public boolean hasNotificationEnabled(UUID playerId, NotificationType notificationType) {
        PlayerDao playerDao = getOrCreatePlayer(playerId);
        return playerDao.getBlockedNotifications().stream()
                .noneMatch(blockedType -> blockedType == notificationType);
    }

    /**
     * Toggles the notification setting for a specific type of notification for a player.
     *
     * @param playerId         the unique identifier of the player to toggle the notification for
     * @param notificationType the type of notification to toggle
     */
    public void toggleNotification(UUID playerId, NotificationType notificationType) {
        PlayerDao playerDao = getOrCreatePlayer(playerId);
        if (hasNotificationEnabled(playerId, notificationType)) {
            playerDao.getBlockedNotifications().add(notificationType);
        } else {
            playerDao.getBlockedNotifications().removeIf(blockedType -> blockedType == notificationType);
        }
        playersRepository.save(playerDao.toEntity());
    }

    private double calculateNewAverageBuyPrice(InvestmentDao existingInvestment, InvestmentDao newInvestment) {
        if (existingInvestment.getAverageBuyPrice() <= 0.0) {
            return newInvestment.getAverageBuyPrice();
        }
        double existingTotalValue = existingInvestment.getSharesAmount() * existingInvestment.getAverageBuyPrice();
        double newTotalValue = newInvestment.getSharesAmount() * newInvestment.getAverageBuyPrice();
        int totalShares = existingInvestment.getSharesAmount() + newInvestment.getSharesAmount();
        return totalShares > 0 ? (existingTotalValue + newTotalValue) / totalShares : 0.0;
    }

    private boolean playerExists(UUID playerId) {
        return playersRepository.exists(playerId);
    }

    private PlayerDao getOrCreatePlayer(UUID playerId) {
        if (playerExists(playerId)) {
            return (PlayerDao) playersRepository.getById(playerId)
                    .map(playerEntity -> new PlayerDao().fromEntity(playerEntity))
                    .orElseThrow();
        } else {
            return PlayerDao.builder()
                    .name("Unknown")
                    .uniqueId(playerId.toString())
                    .investments(new ArrayList<>())
                    .blockedNotifications(new HashSet<>())
                    .build();
        }
    }

    @Override
    public Repository<?, ?> getRepository() {
        return playersRepository;
    }

}
