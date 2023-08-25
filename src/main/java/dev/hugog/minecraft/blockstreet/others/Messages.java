package dev.hugog.minecraft.blockstreet.others;

import dev.hugog.minecraft.blockstreet.BlockStreet;
import dev.hugog.minecraft.blockstreet.enums.ConfigurationFiles;
import lombok.Getter;
import org.bukkit.ChatColor;

@Getter
public class Messages {
	
	private final String pluginPrefix;
	private final String pluginHeader;
	private final String pluginFooter;
	private final String pluginReload;
	private final String menuMainCmd;
	private final String menuCompaniesCmd;
	private final String menuCompanyCmd;
	private final String menuBuyCmd;
	private final String menuSellCmd;
	private final String menuActionsCmd;
	private final String menuCreateCompanyCmd;
	private final String menuDeleteCompanyCmd;
	private final String menuReloadCmd;
	private final String menuTimeCmd;
	private final String buyActionsCmd;
	private final String sellActionsCmd;
	private final String listNextPage;
	private final String id;
	private final String price;
	private final String risk;
	private final String availableActions;
	private final String actionHistoric;
	private final String details;
	private final String playerOnlyCommand;
	private final String noPermission;
	private final String nonExistentPage;
	private final String missingArguments;
	private final String wrongArguments;
	private final String invalidCompany;
	private final String companyAlreadyExists;
	private final String insufficientMoney;
	private final String insufficientActions;
	private final String playerNoActions;
	private final String playerAnyActions;
	private final String boughtActions;
	private final String soldActions;
	private final String createdCompany;
	private final String deletedCompany;
	private final String invalidCmd;
	private final String interestRate;
	private final String updatedInterestRate;
	private final String interestRateTimeLeft;
	private final String newVersionAvailable;
	private final String unknownCommand;
	
	public Messages() {

		final ConfigAccessor messagesConfig = new ConfigAccessor(BlockStreet.getInstance(), ConfigurationFiles.MESSAGES.getFileName());

		this.pluginPrefix = ChatColor.BOLD + "" + ChatColor.GREEN + "BlockStreet" + ChatColor.GOLD + ChatColor.BOLD + " > " + ChatColor.RESET  + ChatColor.RESET + ChatColor.GRAY;
		this.pluginHeader = ChatColor.GRAY + "-=-=-=-=-=-=-=-=-=-= [" + ChatColor.GREEN  + "BlockStreet" + ChatColor.GRAY + "] =-=-=-=-=-=-=-=-=-=-";
		this.pluginFooter = ChatColor.GRAY + "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-";
		this.pluginReload = messagesConfig.getConfig().getString("pluginReload");
		this.menuMainCmd = messagesConfig.getConfig().getString("menuMainCmd");
		this.menuCompaniesCmd = messagesConfig.getConfig().getString("menuCompaniesCmd");
		this.menuCompanyCmd = messagesConfig.getConfig().getString("menuCompanyCmd");
		this.menuBuyCmd = messagesConfig.getConfig().getString("menuBuyCmd");
		this.menuSellCmd = messagesConfig.getConfig().getString("menuSellCmd");
		this.menuActionsCmd = messagesConfig.getConfig().getString("menuActionsCmd");
		this.menuCreateCompanyCmd = messagesConfig.getConfig().getString("menuCreateCompanyCmd");
		this.menuDeleteCompanyCmd = messagesConfig.getConfig().getString("menuDeleteCompanyCmd");
		this.menuReloadCmd = messagesConfig.getConfig().getString("menuReloadCmd");
		this.menuTimeCmd = messagesConfig.getConfig().getString("menuTimeCmd");
		this.buyActionsCmd = messagesConfig.getConfig().getString("buyActionsCmd");
		this.sellActionsCmd = messagesConfig.getConfig().getString("sellActionsCmd");
		this.listNextPage = messagesConfig.getConfig().getString("listNextPage");
		this.id = messagesConfig.getConfig().getString("id");
		this.price = messagesConfig.getConfig().getString("price");
		this.risk = messagesConfig.getConfig().getString("risk");
		this.availableActions = messagesConfig.getConfig().getString("availableActions");
		this.actionHistoric = messagesConfig.getConfig().getString("actionHistoric");
		this.details = messagesConfig.getConfig().getString("details");
		this.playerOnlyCommand = messagesConfig.getConfig().getString("playerOnlyCommand");
		this.noPermission = messagesConfig.getConfig().getString("noPermission");
		this.nonExistentPage = messagesConfig.getConfig().getString("nonExistantPage");
		this.missingArguments = messagesConfig.getConfig().getString("missingArguments");
		this.wrongArguments = messagesConfig.getConfig().getString("wrongArguments");
		this.invalidCompany = messagesConfig.getConfig().getString("invalidCompany");
		this.companyAlreadyExists = messagesConfig.getConfig().getString("companyAlreadyExists");
		this.insufficientMoney = messagesConfig.getConfig().getString("insufficientMoney");
		this.insufficientActions = messagesConfig.getConfig().getString("insufficientActions");
		this.playerNoActions = messagesConfig.getConfig().getString("playerNoActions");
		this.boughtActions = messagesConfig.getConfig().getString("boughtActions");
		this.soldActions = messagesConfig.getConfig().getString("soldActions");
		this.createdCompany = messagesConfig.getConfig().getString("createdCompany");
		this.deletedCompany = messagesConfig.getConfig().getString("deletedCompany");
		this.invalidCmd = messagesConfig.getConfig().getString("invalidCmd");
		this.interestRate = messagesConfig.getConfig().getString("interestRate");
		this.updatedInterestRate = messagesConfig.getConfig().getString("updatedInterestRate");
		this.playerAnyActions = messagesConfig.getConfig().getString("playerAnyActions");
		this.interestRateTimeLeft = messagesConfig.getConfig().getString("interestRateTimeLeft");
		this.newVersionAvailable = messagesConfig.getConfig().getString("newVersionAvailable");
		this.unknownCommand = messagesConfig.getConfig().getString("unknownCommand");
		
	}

}