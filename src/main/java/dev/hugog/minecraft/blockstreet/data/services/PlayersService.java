package dev.hugog.minecraft.blockstreet.data.services;

import dev.hugog.minecraft.blockstreet.data.dao.InvestmentDao;
import dev.hugog.minecraft.blockstreet.data.dao.PlayerDao;
import dev.hugog.minecraft.blockstreet.data.repositories.Repository;
import dev.hugog.minecraft.blockstreet.data.repositories.implementations.PlayersRepository;

import javax.inject.Inject;
import java.util.*;

public class PlayersService implements Service {

    private final PlayersRepository playersRepository;

    @Inject
    public PlayersService(PlayersRepository playersRepository) {
        this.playersRepository = playersRepository;
    }

    public void addSharesToPlayer(UUID playerId, long companyId, int sharesAmount) {

        PlayerDao playerDao = getOrCreatePlayer(playerId);
        InvestmentDao recentInvestment = new InvestmentDao(companyId, sharesAmount);

        boolean hasInvestedInCompany = playerDao.getInvestments().stream().anyMatch(playerInvestment -> playerInvestment.getCompanyId() == recentInvestment.getCompanyId());

        if (hasInvestedInCompany) {
            playerDao.getInvestments().stream()
                    .filter(playerInvestment -> playerInvestment.getCompanyId() == recentInvestment.getCompanyId())
                    .findFirst()
                    .ifPresent(playerInvestment -> playerInvestment.setSharesAmount(playerInvestment.getSharesAmount() + recentInvestment.getSharesAmount()));

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
                    .build();
        }

    }

    @Override
    public Repository<?, ?> getRepository() {
        return playersRepository;
    }

}
