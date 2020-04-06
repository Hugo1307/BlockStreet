package hugog.blockstreet.others;

import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;
import org.bukkit.util.ChatPaginator;

import net.milkbowl.vault.chat.Chat;

public class Messages {
	
	private String pluginPrefix;
	private String pluginHeader;
	private String pluginFooter;
	private String pluginReload;
	private String menuMainCmd;
	private String menuCompaniesCmd;
	private String menuCompanyCmd;
	private String menuBuyCmd;
	private String menuSellCmd;
	private String menuActionsCmd;
	private String menuCreateCompanyCmd;
	private String menuReloadCmd;
	private String buyActionsCmd;
	private String sellActionsCmd;
	private String listNextPage;
	private String id;
	private String price;
	private String risk;
	private String availableActions;
	private String actionHistoric;
	private String details;
	private String noPermission;
	private String nonExistantPage;
	private String missingArguments;
	private String wrongArguments;
	private String invalidCompany;
	private String companyAlreadyExists;
	private String insufficientMoney;
	private String insufficientActions;
	private String playerNoActions;
	private String playerAnyActions;
	private String boughtActions;
	private String soldActions;
	private String createdCompany;
	private String invalidCmd;
	private String interestRate;
	private String updatedInterestRate;

	public Messages(ConfigAccessor messagesConfig) {
		
		this.pluginPrefix = ChatColor.BOLD + "" + ChatColor.GREEN + "BlockStreet" + ChatColor.GOLD + ChatColor.BOLD + " > " + ChatColor.RESET  + ChatColor.RESET + ChatColor.GRAY;
		this.pluginHeader = ChatColor.GREEN + "" + ChatColor.STRIKETHROUGH + "                                                                                " 
		+ "\n" + ChatColor.RESET + ChatColor.GREEN + "                               " + 
				ChatColor.BOLD + "BlockStreet";
		
		this.pluginFooter = ChatColor.GREEN + "" + ChatColor.STRIKETHROUGH + "                                                                                ";
		this.pluginReload = messagesConfig.getConfig().getString("pluginReload");
		this.menuMainCmd = messagesConfig.getConfig().getString("menuMainCmd");
		this.menuCompaniesCmd = messagesConfig.getConfig().getString("menuCompaniesCmd");
		this.menuCompanyCmd = messagesConfig.getConfig().getString("menuCompanyCmd");
		this.menuBuyCmd = messagesConfig.getConfig().getString("menuBuyCmd");
		this.menuSellCmd = messagesConfig.getConfig().getString("menuSellCmd");
		this.menuActionsCmd = messagesConfig.getConfig().getString("menuActionsCmd");
		this.menuCreateCompanyCmd = messagesConfig.getConfig().getString("menuCreateCompanyCmd");
		this.menuReloadCmd = messagesConfig.getConfig().getString("menuReloadCmd");
		this.buyActionsCmd = messagesConfig.getConfig().getString("buyActionsCmd");
		this.sellActionsCmd = messagesConfig.getConfig().getString("sellActionsCmd");
		this.listNextPage = messagesConfig.getConfig().getString("listNextPage");
		this.id = messagesConfig.getConfig().getString("id");
		this.price = messagesConfig.getConfig().getString("price");
		this.risk = messagesConfig.getConfig().getString("risk");
		this.availableActions = messagesConfig.getConfig().getString("availableActions");
		this.actionHistoric = messagesConfig.getConfig().getString("actionHistoric");
		this.details = messagesConfig.getConfig().getString("details");
		this.noPermission = messagesConfig.getConfig().getString("noPermission");
		this.nonExistantPage = messagesConfig.getConfig().getString("nonExistantPage");
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
		this.invalidCmd = messagesConfig.getConfig().getString("invalidCmd");
		this.interestRate = messagesConfig.getConfig().getString("interestRate");
		this.updatedInterestRate = messagesConfig.getConfig().getString("updatedInterestRate");
		this.playerAnyActions = messagesConfig.getConfig().getString("playerAnyActions");
	}
	
	public String getPluginPrefix() {
		return pluginPrefix;
	}

	public String getPluginHeader() {
		return pluginHeader;
	}

	public String getPluginFooter() {
		return pluginFooter;
	}

	public String getPluginReload() {
		return pluginReload;
	}

	public String getMenuMainCmd() {
		return menuMainCmd;
	}

	public String getMenuCompaniesCmd() {
		return menuCompaniesCmd;
	}

	public String getMenuCompanyCmd() {
		return menuCompanyCmd;
	}

	public String getMenuBuyCmd() {
		return menuBuyCmd;
	}

	public String getMenuSellCmd() {
		return menuSellCmd;
	}

	public String getMenuActionsCmd() {
		return menuActionsCmd;
	}

	public String getMenuReloadCmd() {
		return menuReloadCmd;
	}

	public String getBuyActionsCmd() {
		return buyActionsCmd;
	}
	
	public String getSellActionsCmd() {
		return sellActionsCmd;
	}

	public String getListNextPage() {
		return listNextPage;
	}

	public String getId() {
		return id;
	}

	public String getPrice() {
		return price;
	}

	public String getRisk() {
		return risk;
	}

	public String getAvailableActions() {
		return availableActions;
	}

	public String getActionHistoric() {
		return actionHistoric;
	}
	
	public String getDetails() {
		return details;
	}
	
	public String getNoPermission() {
		return noPermission;
	}

	public String getNonExistantPage() {
		return nonExistantPage;
	}

	public String getMissingArguments() {
		return missingArguments;
	}

	public String getWrongArguments() {
		return wrongArguments;
	}

	public String getInvalidCompany() {
		return invalidCompany;
	}

	public String getCompanyAlreadyExists() {
		return companyAlreadyExists;
	}

	public String getInsufficientMoney() {
		return insufficientMoney;
	}

	public String getInsufficientActions() {
		return insufficientActions;
	}

	public String getPlayerNoActions() {
		return playerNoActions;
	}

	public String getBoughtActions() {
		return boughtActions;
	}
	
	public String getSoldActions() {
		return soldActions;
	}

	public String getInvalidCmd() {
		return invalidCmd;
	}
	
	public String getInterestRate() {
		return interestRate;
		
	}

	public String getMenuCreateCompanyCmd() {
		return menuCreateCompanyCmd;
	}

	public String getCreatedCompany() {
		return createdCompany;
	}

	public String getUpdatedInterestRate() {
		return updatedInterestRate;
	}
	
	public String getPlayerAnyActions(){
		return playerAnyActions;
	}
}
