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
    COMPANY_BANKRUPT("notificationCompanyBankruptName"),
    /**
     * Notification for when a new company is created by a player.
     */
    COMPANY_CREATED("notificationCompanyCreatedName");

    private final String messageKey;
}
