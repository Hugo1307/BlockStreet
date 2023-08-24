package dev.hugog.minecraft.blockstreet.runnables;

import dev.hugog.minecraft.blockstreet.others.CompanySign;
import dev.hugog.minecraft.blockstreet.others.CompanySignsContainer;
import org.bukkit.scheduler.BukkitRunnable;

public class SignCheckerRunnable extends BukkitRunnable {

    @Override
    public void run() {
        for (CompanySign companySign : CompanySignsContainer.getSigns())
            companySign.update();
    }

}
