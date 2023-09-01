package dev.hugog.minecraft.blockstreet.data.services;

import dev.hugog.minecraft.blockstreet.data.dao.InvestmentDao;
import dev.hugog.minecraft.blockstreet.data.dao.PlayerDao;
import dev.hugog.minecraft.blockstreet.data.repositories.Repository;
import dev.hugog.minecraft.blockstreet.data.repositories.implementations.PlayersRepository;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.UUID;

public class PlayersService implements Service {

    private final PlayersRepository playersRepository;

    @Inject
    public PlayersService(PlayersRepository playersRepository) {
        this.playersRepository = playersRepository;
    }

    public void registerPlayerInvestment(UUID playerId, long companyId, int sharesAmount) {

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

    public boolean playerExists(UUID playerId) {
        return playersRepository.exists(playerId);
    }

    private PlayerDao getOrCreatePlayer(UUID playerId) {

        if (playerExists(playerId)) {
            return (PlayerDao) playersRepository.getById(playerId)
                    .map(playerEntity -> new PlayerDao().fromEntity(playerEntity))
                    .orElse(null);
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
