package dev.hugog.minecraft.blockstreet.data.services;

import dev.hugog.minecraft.blockstreet.data.dao.CompanyDao;
import dev.hugog.minecraft.blockstreet.data.dao.LocationDao;
import dev.hugog.minecraft.blockstreet.data.dao.SignDao;
import dev.hugog.minecraft.blockstreet.data.entities.SignEntity;
import dev.hugog.minecraft.blockstreet.data.repositories.Repository;
import dev.hugog.minecraft.blockstreet.data.repositories.implementations.SignsRepository;
import dev.hugog.minecraft.blockstreet.utils.FormattingUtils;
import dev.hugog.minecraft.blockstreet.utils.VisualizationUtils;
import lombok.extern.log4j.Log4j2;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Sign;

import javax.inject.Inject;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
public class SignsService implements Service {

    public final SignsRepository repository;
    public final CompaniesService companiesService;

    @Inject
    public SignsService(SignsRepository repository, CompaniesService companiesService) {
        this.repository = repository;
        this.companiesService = companiesService;
    }

    public List<SignDao> getAllSigns() {

        return repository.getAllIds().stream()
                .map(repository::getById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(signEntity -> (SignDao) new SignDao().fromEntity(signEntity))
                .collect(Collectors.toList());

    }

    public SignDao createSign(long companyId, Location signLocation) {

        SignDao signDao = SignDao.builder()
                .id(repository.getNextId().intValue())
                .companyId(companyId)
                .location(LocationDao.fromBukkitLocation(signLocation))
                .build();

        repository.save(signDao.toEntity());

        return signDao;

    }

    public void deleteSign(int signId) {
        repository.getById(signId).ifPresent(signEntity -> repository.delete(signEntity.getId()));
    }

    public SignDao getSignByLocation(Location location) {

        return getAllSigns().stream()
                .filter(signDao -> signDao.getLocation().toBukkitLocation().equals(location))
                .findFirst()
                .orElse(null);

    }

    public Sign getBukkitSignFromId(int id) {

        if (!repository.exists(id)) return null;

        Optional<SignEntity> signEntityOptional = repository.getById(id);

        if (signEntityOptional.isEmpty()) return null;

        SignDao signDao = (SignDao) new SignDao().fromEntity(signEntityOptional.get());
        Location signLocation = signDao.getLocation().toBukkitLocation();

        if (signLocation.getBlock().getState() instanceof Sign) {
            return (Sign) signLocation.getBlock().getState();
        }

        return null;

    }

    public void updateBukkitSignsById(int signId) {

        SignEntity signEntity = repository.getById(signId).orElse(null);

        if (signEntity == null) return;

        Sign bukkitSign = getBukkitSignFromId(signId);

        if (bukkitSign == null) return;

        if (companiesService.companyExists(signEntity.getCompanyId())) {

            CompanyDao companyInSign = companiesService.getCompanyDaoById(signEntity.getCompanyId());
            updateSign(bukkitSign, companyInSign);

        }

    }

    public void updateBukkitSignsByCompany(long companyId) {

        repository.getSignsByCompanyId(companyId).stream()
                .map(signEntity -> getBukkitSignFromId(signEntity.getId()))
                .filter(Objects::nonNull)
                .forEach(bukkitSign -> {

                    if (companiesService.companyExists(companyId)) {

                        CompanyDao companyInSign = companiesService.getCompanyDaoById(companyId);
                        updateSign(bukkitSign, companyInSign);

                    }

                });

    }

    private void updateSign(Sign sign, CompanyDao company) {
        double lastVariation = !company.getHistoric().isEmpty() ? company.getHistoric().peek().getVariation() * 100 : 0.0;
        sign.setLine(0, ChatColor.BOLD + "" + ChatColor.GREEN + "BlockStreet");
        sign.setLine(1, ChatColor.YELLOW + company.getName());
        sign.setLine(2, ChatColor.GREEN + "Value: " + ChatColor.GRAY + FormattingUtils.formatDouble(company.getCurrentSharePrice()));
        sign.setLine(3, VisualizationUtils.formatCompanyVariation(lastVariation));
        sign.setEditable(false);
        sign.update();
    }

    @Override
    public Repository<?, ?> getRepository() {
        return this.repository;
    }

}
