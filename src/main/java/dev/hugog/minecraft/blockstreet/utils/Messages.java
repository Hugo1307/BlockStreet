package dev.hugog.minecraft.blockstreet.utils;

import com.google.inject.Inject;
import dev.hugog.minecraft.blockstreet.BlockStreet;
import dev.hugog.minecraft.blockstreet.enums.ConfigurationFiles;
import lombok.Getter;

@Getter
public class Messages {

    private final ConfigAccessor messagesConfig;

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
    private String menuDeleteCompanyCmd;
    private String menuReloadCmd;
    private String menuTimeCmd;
    private String buyActionsCmd;
    private String sellActionsCmd;
    private String listNextPage;
    private String id;
    private String price;
    private String risk;
    private String availableActions;
    private String actionHistoric;
    private String details;
    private String shares;
    private String totalSharesValue;
    private String companyStatus;
    private String companyStatusBankrupt;
    private String companyStatusTrading;

    private String playerNotFound;
    private String playerOnlyCommand;
    private String noPermission;
    private String nonExistentPage;
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
    private String deletedCompany;
    private String cannotBuyBankruptCompany;
    private String cannotDeleteNotOwnedCompany;
    private String cannotOwnMoreThanMaxShares;

    private String invalidCmd;
    private String interestRate;
    private String updatedInterestRate;
    private String interestRateTimeLeft;
    private String companyStocksCrashed;
    private String newVersionAvailable;
    private String unknownCommand;

    private String buyCommandDescription;
    private String buyCommandUsage;
    private String sellCommandDescription;
    private String sellCommandUsage;
    private String companyCommandDescription;
    private String companyCommandUsage;
    private String companiesCommandDescription;
    private String companiesCommandUsage;
    private String portfolioCommandDescription;
    private String portfolioCommandUsage;
    private String infoCommandDescription;
    private String infoCommandUsage;
    private String reloadCommandDescription;
    private String reloadCommandUsage;
    private String helpCommandDescription;
    private String helpCommandUsage;
    private String createCompanyCommandDescription;
    private String createCompanyCommandUsage;
    private String deleteCompanyCommandDescription;
    private String deleteCompanyCommandUsage;

    private String uiPreviousPage;
    private String uiNextPage;
    private String uiNavigateBack;
    private String uiPortfolioTitle;
    private String uiInvestmentItemShares;
    private String uiInvestmentItemCurrentValue;
    private String uiInvestmentItemSellOne;
    private String uiInvestmentItemSellAll;
    private String uiCompaniesTitle;
    private String uiCompanyItemStatus;
    private String uiCompanyItemSharePrice;
    private String uiCompanyItemRisk;
    private String uiCompanyItemMarketCap;
    private String uiCompanyItemAllTimeVariation;
    private String uiCompanyItemLastVariation;
    private String uiCompanyItemBuyOneShare;
    private String uiCompanyItemBuyTenShares;
    private String uiCompanyItemBuyHundredShares;
    private String uiCheckPortfolioButtonTitle;
    private String uiCheckPortfolioButtonDescription;
    private String uiPortfolioSummaryItemTitle;
    private String uiPortfolioSummaryItemCompanies;
    private String uiPortfolioSummaryItemShares;
    private String uiPortfolioSummaryItemTotalValue;

    @Inject
    public Messages(BlockStreet plugin) {
        this.messagesConfig = new ConfigAccessor(plugin, ConfigurationFiles.MESSAGES.getFileName());
        initializeMessages();
    }

    public void reload() {
        this.messagesConfig.reloadConfig();
        initializeMessages();
    }

    private void initializeMessages() {
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
