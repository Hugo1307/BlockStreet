package hugog.blockstreet.events;

import hugog.blockstreet.Main;
import hugog.blockstreet.others.Company;
import hugog.blockstreet.others.CompanyContainer;
import hugog.blockstreet.others.CompanySign;
import hugog.blockstreet.others.CompanySignsContainer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.List;

public class SignHandlerEvent implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {

            Block clickedBlock = event.getClickedBlock();

            if (clickedBlock == null) return;

            for (CompanySign companySign : CompanySignsContainer.getSigns()) {

                if (companySign.getLocation().equals(event.getClickedBlock().getLocation())) {

                    event.getPlayer().performCommand("invest company " + companySign.getCompanyId());

                }

            }

        }

    }

    @EventHandler
    public void onSignChanged(SignChangeEvent event) {

        List<String> lines = Arrays.asList(event.getLines());

        if (lines.get(0) == null) return;

        if (lines.get(0).equalsIgnoreCase("[BlockStreet]")) {

            List<Company> allCompanies = CompanyContainer.getCompanies();

            for (Company company : allCompanies) {

                if (company.getName().equalsIgnoreCase(lines.get(1))) {

                    Sign sign = (Sign)event.getBlock().getState();
                    CompanySign companySign = new CompanySign(company.getId(), sign.getLocation());

                    Bukkit.getScheduler().runTaskLater(Main.getInstance(), ()->{
                        companySign.update();
                        companySign.save();
                    }, 20L);

                }

            }

        }

    }

}
