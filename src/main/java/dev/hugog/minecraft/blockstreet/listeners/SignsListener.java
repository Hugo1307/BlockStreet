package dev.hugog.minecraft.blockstreet.listeners;

import dev.hugog.minecraft.blockstreet.BlockStreet;
import dev.hugog.minecraft.blockstreet.data.dao.CompanyDao;
import dev.hugog.minecraft.blockstreet.data.dao.SignDao;
import dev.hugog.minecraft.blockstreet.data.services.CompaniesService;
import dev.hugog.minecraft.blockstreet.data.services.SignsService;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;

public class SignsListener implements Listener {

    private final BlockStreet plugin;
    private final SignsService signsService;
    private final CompaniesService companiesService;

    @Inject
    public SignsListener(BlockStreet plugin, SignsService signsService, CompaniesService companiesService) {
        this.plugin = plugin;
        this.signsService = signsService;
        this.companiesService = companiesService;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {

            Block clickedBlock = event.getClickedBlock();

            if (clickedBlock == null) return;

            SignDao signAtLocation = signsService.getSignByLocation(clickedBlock.getLocation());

            if (signAtLocation == null) return;

            event.getPlayer().performCommand("invest company " + signAtLocation.getCompanyId());

        }

    }

    @EventHandler
    public void onSignChanged(SignChangeEvent event) {

        List<String> lines = Arrays.asList(event.getLines());

        if (lines.get(0) == null) return;

        if (lines.get(0).equalsIgnoreCase("[BlockStreet]")) {

            List<CompanyDao> allCompanies = companiesService.getAllCompanies();

            for (CompanyDao company : allCompanies) {

                if (company.getName().equalsIgnoreCase(lines.get(1))) {

                    Sign sign = (Sign) event.getBlock().getState();

                    SignDao signDao = signsService.createSign(company.getId(), sign.getLocation());

                    plugin.getServer().getScheduler().runTaskLater(BlockStreet.getInstance(), () -> {
                        signsService.updateBukkitSignsById(signDao.getId());
                    }, 20L);

                }

            }

        }

    }

    @EventHandler
    public void onSignDestroyed(BlockBreakEvent event) {

        Block destroyedBlock = event.getBlock();

        if (destroyedBlock.getState() instanceof Sign) {

            Sign sign = (Sign) event.getBlock().getState();

            if (sign.getLines()[0].contains("BlockStreet")) {

                SignDao signDao = signsService.getSignByLocation(destroyedBlock.getLocation());

                if (signDao != null) {
                    signsService.deleteSign(signDao.getId());
                }

            }

        }

    }

}
