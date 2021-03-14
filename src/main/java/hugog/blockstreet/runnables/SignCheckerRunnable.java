package hugog.blockstreet.runnables;

import hugog.blockstreet.others.CompanySign;
import hugog.blockstreet.others.CompanySignsContainer;
import org.bukkit.scheduler.BukkitRunnable;

public class SignCheckerRunnable extends BukkitRunnable {

    @Override
    public void run() {
        for (CompanySign companySign : CompanySignsContainer.getSigns())
            companySign.update();
    }

}
