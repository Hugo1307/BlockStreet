package dev.hugog.minecraft.blockstreet.utils;

import lombok.NoArgsConstructor;
import org.bukkit.ChatColor;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public final class VisualizationUtils {

    public static String formatCompanyVariation(double variation) {
        ChatColor variationColor = variation >= 0 ? ChatColor.GREEN : ChatColor.RED;
        String variationSign = variation >= 0 ? "↑" : "↓";
        return String.format("%s%s %.2f%%", variationColor, variationSign, Math.abs(variation));
    }

}
