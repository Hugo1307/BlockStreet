package hugog.blockstreet.commands;

import hugog.blockstreet.commands.implementation.*;
import lombok.AccessLevel;
import lombok.Getter;

@Getter(AccessLevel.PACKAGE)
public enum CommandRegister implements ICommandRegister {
    BUY_CMD("buy", BuyCommand.class),
    COMPANIES_CMD("companies", CompaniesCommand.class),
    COMPANY_CMD("company", CompanyCommand.class),
    CREATE_COMPANY_CMD("create", CreateCompanyCommand.class),
    DELETE_COMPANY_CMD("delete", DeleteCompanyCommand.class),
    INFO_CMD("info", InfoCommand.class),
    MAIN_CMD("", MainCommand.class),
    RELOAD_CMD("reload", ReloadCommand.class),
    SELL_CMD("sell", SellCommand.class),
    STOCKS_CMD("stocks", StocksCommand.class),
    INTEREST_TIME_CMD("time", InterestTimeCommand.class);

    private final String alias;
    private final Class<? extends PluginCommand> commandExecutor;

    CommandRegister(String alias, Class<? extends PluginCommand> commandExecutor) {
        this.alias = alias;
        this.commandExecutor = commandExecutor;
    }

}
