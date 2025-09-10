package dev.hugog.minecraft.blockstreet.data.entities;

import dev.hugog.minecraft.blockstreet.enums.NotificationType;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class PlayerEntity implements DataEntity {

    private String uniqueId;
    private String name;
    private List<InvestmentEntity> investments;
    private Set<NotificationType> blockedNotifications;

}
