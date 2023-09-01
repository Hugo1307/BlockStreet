package dev.hugog.minecraft.blockstreet.data.services;

import dev.hugog.minecraft.blockstreet.data.dao.CompanyDao;
import dev.hugog.minecraft.blockstreet.data.entities.CompanyEntity;
import dev.hugog.minecraft.blockstreet.data.repositories.Repository;
import dev.hugog.minecraft.blockstreet.data.repositories.implementations.CompaniesRepository;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CompaniesService implements Service {

    private final CompaniesRepository companiesRepository;

    @Inject
    public CompaniesService(CompaniesRepository companiesRepository) {
        this.companiesRepository = companiesRepository;
    }

    public boolean hasEnoughShares(long companyId, int sharesAmount) {

        if (!companyExists(companyId)) {
            return false;
        }

        // If the company has unlimited stocks it will always be available.
        // Companies with unlimited stocks are marked with "-1" as available stocks.
        return companiesRepository.getById(companyId)
                .map(company -> company.getAvailableShares() >= sharesAmount || company.getAvailableShares() == -1)
                .orElse(false);

    }

    public Integer getCompanyInvestmentValue(long companyId, int sharesAmount) {

        if (!companyExists(companyId)) {
            return null;
        }

        return companiesRepository.getById(companyId).map(company -> company.getSharePrice() * sharesAmount).orElse(null);

    }

    public void removeSharesFromCompany(long companyId, int amountOfSharesToRemove) {

        CompanyDao companyDao = (CompanyDao) companiesRepository.getById(companyId)
                .map(companyEntity -> new CompanyDao().fromEntity(companyEntity))
                .orElse(null);

        if (companyDao == null) {
            return;
        }

        // If the company has unlimited stocks, we don't allow it to have less.
        if (companyDao.getAvailableShares() == -1) {
            return;
        }

        // If, by removing the shares, the company would have negative shares, we don't allow it.
        if (companyDao.getAvailableShares() - amountOfSharesToRemove < 0) {
            return;
        }

        companyDao.setAvailableShares(companyDao.getAvailableShares() - amountOfSharesToRemove);
        companiesRepository.save(companyDao.toEntity());

    }

    public void addSharesToCompany(long companyId, int amountOfSharesToAdd) {

        CompanyDao companyDao = (CompanyDao) companiesRepository.getById(companyId)
                .map(companyEntity -> new CompanyDao().fromEntity(companyEntity))
                .orElse(null);

        if (companyDao == null) {
            return;
        }

        // If the company has unlimited stocks, we don't allow it to have more.
        if (companyDao.getAvailableShares() == -1) {
            return;
        }

        // If, by adding the shares, the company would have more shares than it has available, we don't allow it.
        if (companyDao.getTotalShares() < companyDao.getAvailableShares() + amountOfSharesToAdd) {
            return;
        }

        companyDao.setAvailableShares(companyDao.getAvailableShares() + amountOfSharesToAdd);
        companiesRepository.save(companyDao.toEntity());

    }

    public List<CompanyEntity> getCompaniesByIdInterval(int startingId, int endingId) {

        List<Long> allowedIds = companiesRepository.getAllIds().stream()
                .filter(id -> id >= startingId && id <= endingId)
                .collect(Collectors.toList());

        return allowedIds.stream()
                .map(companiesRepository::getById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

    }

    public CompanyDao getCompanyDaoById(long companyId) {
        return (CompanyDao) companiesRepository.getById(companyId)
                .map(companyEntity -> new CompanyDao().fromEntity(companyEntity))
                .orElse(null);
    }

    public boolean companyExists(long companyId) {
        return companiesRepository.exists(companyId) && companiesRepository.getById(companyId).isPresent();
    }

    @Override
    public Repository<?, ?> getRepository() {
        return companiesRepository;
    }

}