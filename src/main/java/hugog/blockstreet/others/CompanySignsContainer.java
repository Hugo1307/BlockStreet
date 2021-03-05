package hugog.blockstreet.others;

import hugog.blockstreet.Main;
import hugog.blockstreet.enums.ConfigurationFiles;

import java.util.*;
import java.util.stream.Collectors;

public abstract class CompanySignsContainer {

    public static List<CompanySign> getSigns() {

        ConfigAccessor companiesReg = new ConfigAccessor(Main.getInstance(), ConfigurationFiles.SIGNS.getFileName());
        List<CompanySign> companiesSignsToReturn = new ArrayList<>();

        if (companiesReg.getConfig().getConfigurationSection("Signs") == null) return Collections.emptyList();

        Set<Integer> companiesSignsIds = Objects.requireNonNull(companiesReg.getConfig().getConfigurationSection("Signs"))
                .getKeys(false).stream().map(Integer::parseInt).collect(Collectors.toSet());

        for (int companySignId : companiesSignsIds)
            companiesSignsToReturn.add(new CompanySign(companySignId).load());
        return companiesSignsToReturn;

    }

}
