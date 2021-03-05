package hugog.blockstreet.others;

import hugog.blockstreet.Main;
import hugog.blockstreet.enums.ConfigurationFiles;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class CompanySign implements Savable<CompanySign> {

    private int id;
    private int companyId;
    private Location location;

    public CompanySign(int id, int companyId, Location location) {
        this.id = id;
        this.location = location;
    }

    public CompanySign(int companyId, Location location) {

        ConfigAccessor signsReg = new ConfigAccessor(Main.getInstance(), ConfigurationFiles.SIGNS.getFileName());
        Optional<ConfigurationSection> signsSection = Optional.ofNullable(signsReg.getConfig().getConfigurationSection("Signs"));
        AtomicInteger nextId = new AtomicInteger();

        signsSection.ifPresent(configurationSection -> nextId.set(configurationSection.getKeys(false).size() + 1));

        this.id = nextId.get();
        this.location = location;
        this.companyId = companyId;

    }

    public CompanySign(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public Location getLocation() {
        return location;
    }

    public int getCompanyId() {
        return companyId;
    }

    public Sign toBukkitSign() {
        if (this.getLocation().getBlock().getState() instanceof Sign)
            return (Sign) this.getLocation().getBlock().getState();
        return null;
    }

    @Override
    public void save() {

        ConfigAccessor signsReg = new ConfigAccessor(Main.getInstance(), ConfigurationFiles.SIGNS.getFileName());

        signsReg.getConfig().set("Signs." + this.id + ".CompanyId", this.companyId);
        signsReg.getConfig().set("Signs." + this.id + ".Location", this.location);
        signsReg.saveConfig();

    }

    @Override
    public CompanySign load() {

        ConfigAccessor signsReg = new ConfigAccessor(Main.getInstance(), ConfigurationFiles.SIGNS.getFileName());

        signsReg.reloadConfig();

        this.location = signsReg.getConfig().getLocation("Signs." + this.id + ".Location");
        this.companyId = signsReg.getConfig().getInt("Signs." + this.id + ".CompanyId");

        return this;
    }

    public void update() {

        Sign sign = this.toBukkitSign();
        Company signCompany = new Company(this.companyId).load();

        if (!CompanyContainer.companyExists(this.companyId)) return;

        if (sign == null) return;

        sign.setLine(0, ChatColor.BOLD + "" + ChatColor.GREEN + "BlockStreet");
        sign.setLine(1, ChatColor.YELLOW + signCompany.getName());
        sign.setLine(2, ChatColor.GREEN + "Stocks: " + ChatColor.GRAY + signCompany.getStocksPrice() + "$");
        sign.setLine(3, ChatColor.GRAY + "Click for details");

        sign.update(true);

    }

}
