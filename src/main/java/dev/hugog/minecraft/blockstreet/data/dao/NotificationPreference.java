package dev.hugog.minecraft.blockstreet.data.dao;

import dev.hugog.minecraft.blockstreet.enums.NotificationType;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class NotificationPreference {
    private NotificationType notificationType;
    private boolean enabled;
}
