package hugog.blockstreet.others;

import hugog.blockstreet.Main;
import hugog.blockstreet.enums.ConfigurationFiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class CompanyContainer {

    public static List<Company> getCompanies() {

        ConfigAccessor companiesReg = new ConfigAccessor(Main.getInstance(), ConfigurationFiles.COMPANIES.getFileName());
        Set<Integer> companiesIds = Objects.requireNonNull(companiesReg.getConfig().getConfigurationSection("Companies"))
                .getKeys(false).stream().map(Integer::parseInt).collect(Collectors.toSet());
        List<Company> companiesToReturn = new ArrayList<>();

        for (int companyId : companiesIds)
            companiesToReturn.add(new Company(companyId).load());
        return companiesToReturn;

    }

    public static boolean companyExists(int id) {
        ConfigAccessor companiesReg = new ConfigAccessor(Main.getInstance(), ConfigurationFiles.COMPANIES.getFileName());
        companiesReg.reloadConfig();
        return companiesReg.getConfig().get("Companies." + id) != null;
    }

}
