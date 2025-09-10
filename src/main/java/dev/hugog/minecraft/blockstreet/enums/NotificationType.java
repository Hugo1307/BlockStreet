package dev.hugog.minecraft.blockstreet.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enum representing different types of notifications that the plugin can send to players.
 */
@Getter
@RequiredArgsConstructor
public enum NotificationType {
    /**
     * Notification for when the stock prices of companies are updated.
     */
    STOCKS_UPDATE("notificationStocksUpdateName"),
    /**
     * Notification for when a company goes bankrupt.
     */
    COMPANY_BANKRUPT("notificationCompanyBankruptName");

    private final String messageKey;
}
