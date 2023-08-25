package dev.hugog.minecraft.blockstreet.data.repositories.implementations;

import dev.hugog.minecraft.blockstreet.data.entities.CompaniesEntity;
import dev.hugog.minecraft.blockstreet.data.entities.CompanyEntity;
import dev.hugog.minecraft.blockstreet.data.repositories.RepositoryImpl;
import dev.hugog.minecraft.blockstreet.data.sources.DataSource;
import dev.hugog.minecraft.blockstreet.data.sources.yml.implementations.CompaniesYml;
import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.Optional;

@Log4j2
public class CompaniesRepository extends RepositoryImpl {

    private final DataSource<CompaniesEntity> dataSource;

    public CompaniesRepository(DataSource<CompaniesEntity> dataSource) {
        super(dataSource);
        this.dataSource = dataSource;
    }

    public Optional<CompanyEntity> getCompanyById(int companyId) {

        if (!verifyDataSource()) {
            log.warn("Data Source invalid at CompaniesRepository.");
            return Optional.empty();
        }

        CompaniesEntity companiesEntity = dataSource.load(CompaniesEntity.class);
        return companiesEntity.getCompanies().stream().filter(company -> company.getId() == companyId).findFirst();

    }

    public void updateCompany(CompanyEntity companyToUpdate) {

        if (!verifyDataSource()) {
            log.warn("Data Source invalid at CompaniesRepository.");
            return;
        }

        CompaniesEntity companiesEntity = dataSource.load(CompaniesEntity.class);
        List<CompanyEntity> allCompanies = companiesEntity.getCompanies();

        allCompanies.replaceAll(company -> company.getId() == companyToUpdate.getId() ? companyToUpdate : company);

        companiesEntity.setCompanies(allCompanies);
        dataSource.save(companiesEntity);

    }

    @Override
    public boolean verifyDataSource() {
        return dataSource instanceof CompaniesYml;
    }

}
