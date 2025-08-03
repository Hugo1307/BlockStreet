package dev.hugog.minecraft.blockstreet.utils;

import com.google.inject.Inject;
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
	private final String shares;
	private final String totalSharesValue;
	private final String companyStatus;
	private final String companyStatusBankrupt;
	private final String companyStatusTrading;

	private final String playerNotFound;
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
	private final String cannotBuyBankruptCompany;
	private final String cannotDeleteNotOwnedCompany;
	private final String cannotOwnMoreThanMaxShares;

	private final String invalidCmd;
	private final String interestRate;
	private final String updatedInterestRate;
	private final String interestRateTimeLeft;
	private final String companyStocksCrashed;
	private final String newVersionAvailable;
	private final String unknownCommand;

	private final String buyCommandDescription;
	private final String buyCommandUsage;
	private final String sellCommandDescription;
	private final String sellCommandUsage;
	private final String companyCommandDescription;
	private final String companyCommandUsage;
	private final String companiesCommandDescription;
	private final String companiesCommandUsage;
	private final String portfolioCommandDescription;
	private final String portfolioCommandUsage;
	private final String infoCommandDescription;
	private final String infoCommandUsage;
	private final String reloadCommandDescription;
	private final String reloadCommandUsage;
	private final String helpCommandDescription;
	private final String helpCommandUsage;
	private final String createCompanyCommandDescription;
	private final String createCompanyCommandUsage;
	private final String deleteCompanyCommandDescription;
	private final String deleteCompanyCommandUsage;

	private final String uiPreviousPage;
	private final String uiNextPage;
	private final String uiNavigateBack;
	private final String uiPortfolioTitle;
	private final String uiInvestmentItemShares;
	private final String uiInvestmentItemCurrentValue;
	private final String uiInvestmentItemSellOne;
	private final String uiInvestmentItemSellAll;
	private final String uiCompaniesTitle;
	private final String uiCompanyItemStatus;
	private final String uiCompanyItemSharePrice;
	private final String uiCompanyItemRisk;
	private final String uiCompanyItemMarketCap;
	private final String uiCompanyItemAllTimeVariation;
	private final String uiCompanyItemLastVariation;
	private final String uiCompanyItemBuyOneShare;
	private final String uiCompanyItemBuyTenShares;
	private final String uiCompanyItemBuyHundredShares;
	private final String uiCheckPortfolioButtonTitle;
	private final String uiCheckPortfolioButtonDescription;
	private final String uiPortfolioSummaryItemTitle;
	private final String uiPortfolioSummaryItemCompanies;
	private final String uiPortfolioSummaryItemShares;
	private final String uiPortfolioSummaryItemTotalValue;

	@Inject
	public Messages(BlockStreet plugin) {

		final ConfigAccessor messagesConfig = new ConfigAccessor(plugin, ConfigurationFiles.MESSAGES.getFileName());

		this.pluginPrefix = messagesConfig.getConfig().getString("pluginPrefix");
		this.pluginHeader = messagesConfig.getConfig().getString("pluginHeader");
		this.pluginFooter = messagesConfig.getConfig().getString("pluginFooter");
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
		this.shares = messagesConfig.getConfig().getString("shares");
		this.totalSharesValue = messagesConfig.getConfig().getString("totalSharesValue");
		this.companyStatus = messagesConfig.getConfig().getString("companyStatus");
		this.companyStatusTrading = messagesConfig.getConfig().getString("companyStatusTrading");
		this.companyStatusBankrupt = messagesConfig.getConfig().getString("companyStatusBankrupt");

		this.playerNotFound = messagesConfig.getConfig().getString("playerNotFound");
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
		this.cannotBuyBankruptCompany = messagesConfig.getConfig().getString("cannotBuyBankruptCompany");
		this.cannotDeleteNotOwnedCompany = messagesConfig.getConfig().getString("cannotDeleteNotOwnedCompany");
		this.cannotOwnMoreThanMaxShares = messagesConfig.getConfig().getString("cannotOwnMoreThanMaxShares");

		this.invalidCmd = messagesConfig.getConfig().getString("invalidCmd");
		this.interestRate = messagesConfig.getConfig().getString("interestRate");
		this.updatedInterestRate = messagesConfig.getConfig().getString("updatedInterestRate");
		this.playerAnyActions = messagesConfig.getConfig().getString("playerAnyActions");
		this.interestRateTimeLeft = messagesConfig.getConfig().getString("interestRateTimeLeft");
		this.companyStocksCrashed = messagesConfig.getConfig().getString("companyStocksCrashed");
		this.newVersionAvailable = messagesConfig.getConfig().getString("newVersionAvailable");
		this.unknownCommand = messagesConfig.getConfig().getString("unknownCommand");

		this.buyCommandDescription = messagesConfig.getConfig().getString("buyCommandDescription");
		this.buyCommandUsage = messagesConfig.getConfig().getString("buyCommandUsage");
		this.sellCommandDescription = messagesConfig.getConfig().getString("sellCommandDescription");
		this.sellCommandUsage = messagesConfig.getConfig().getString("sellCommandUsage");
		this.companyCommandDescription = messagesConfig.getConfig().getString("companyCommandDescription");
		this.companyCommandUsage = messagesConfig.getConfig().getString("companyCommandUsage");
		this.companiesCommandDescription = messagesConfig.getConfig().getString("companiesCommandDescription");
		this.companiesCommandUsage = messagesConfig.getConfig().getString("companiesCommandUsage");
		this.portfolioCommandDescription = messagesConfig.getConfig().getString("portfolioCommandDescription");
		this.portfolioCommandUsage = messagesConfig.getConfig().getString("portfolioCommandUsage");
		this.infoCommandDescription = messagesConfig.getConfig().getString("infoCommandDescription");
		this.infoCommandUsage = messagesConfig.getConfig().getString("infoCommandUsage");
		this.reloadCommandDescription = messagesConfig.getConfig().getString("reloadCommandDescription");
		this.reloadCommandUsage = messagesConfig.getConfig().getString("reloadCommandUsage");
		this.helpCommandDescription = messagesConfig.getConfig().getString("helpCommandDescription");
		this.helpCommandUsage = messagesConfig.getConfig().getString("helpCommandUsage");
		this.createCompanyCommandDescription = messagesConfig.getConfig().getString("createCompanyCommandDescription");
		this.createCompanyCommandUsage = messagesConfig.getConfig().getString("createCompanyCommandUsage");
		this.deleteCompanyCommandDescription = messagesConfig.getConfig().getString("deleteCompanyCommandDescription");
		this.deleteCompanyCommandUsage = messagesConfig.getConfig().getString("deleteCompanyCommandUsage");

		this.uiPreviousPage = messagesConfig.getConfig().getString("uiPreviousPage");
		this.uiNextPage = messagesConfig.getConfig().getString("uiNextPage");
		this.uiNavigateBack = messagesConfig.getConfig().getString("uiNavigateBack");
		this.uiPortfolioTitle = messagesConfig.getConfig().getString("uiPortfolioTitle");
		this.uiInvestmentItemShares = messagesConfig.getConfig().getString("uiInvestmentItemShares");
		this.uiInvestmentItemCurrentValue = messagesConfig.getConfig().getString("uiInvestmentItemCurrentValue");
		this.uiInvestmentItemSellOne = messagesConfig.getConfig().getString("uiInvestmentItemSellOne");
		this.uiInvestmentItemSellAll = messagesConfig.getConfig().getString("uiInvestmentItemSellAll");
		this.uiCompaniesTitle = messagesConfig.getConfig().getString("uiCompaniesTitle");
		this.uiCompanyItemStatus = messagesConfig.getConfig().getString("uiCompanyItemStatus");
		this.uiCompanyItemSharePrice = messagesConfig.getConfig().getString("uiCompanyItemSharePrice");
		this.uiCompanyItemRisk = messagesConfig.getConfig().getString("uiCompanyItemRisk");
		this.uiCompanyItemMarketCap = messagesConfig.getConfig().getString("uiCompanyItemMarketCap");
		this.uiCompanyItemAllTimeVariation = messagesConfig.getConfig().getString("uiCompanyItemAllTimeVariation");
		this.uiCompanyItemLastVariation = messagesConfig.getConfig().getString("uiCompanyItemLastVariation");
		this.uiCompanyItemBuyOneShare = messagesConfig.getConfig().getString("uiCompanyItemBuyOneShare");
		this.uiCompanyItemBuyTenShares = messagesConfig.getConfig().getString("uiCompanyItemBuyTenShares");
		this.uiCompanyItemBuyHundredShares = messagesConfig.getConfig().getString("uiCompanyItemBuyHundredShares");
		this.uiCheckPortfolioButtonTitle = messagesConfig.getConfig().getString("uiCheckPortfolioButtonTitle");
		this.uiCheckPortfolioButtonDescription = messagesConfig.getConfig().getString("uiCheckPortfolioButtonDescription");
		this.uiPortfolioSummaryItemTitle = messagesConfig.getConfig().getString("uiPortfolioSummaryItemTitle");
		this.uiPortfolioSummaryItemCompanies = messagesConfig.getConfig().getString("uiPortfolioSummaryItemCompanies");
		this.uiPortfolioSummaryItemShares = messagesConfig.getConfig().getString("uiPortfolioSummaryItemShares");
		this.uiPortfolioSummaryItemTotalValue = messagesConfig.getConfig().getString("uiPortfolioSummaryItemTotalValue");

	}

}
