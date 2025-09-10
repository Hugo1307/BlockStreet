package dev.hugog.minecraft.blockstreet.data.dao;

import dev.hugog.minecraft.blockstreet.data.entities.PlayerEntity;
import dev.hugog.minecraft.blockstreet.enums.NotificationType;
import lombok.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class PlayerDao implements Dao<PlayerEntity> {

    private String uniqueId;
    private String name;
    private List<InvestmentDao> investments;
    private Set<NotificationType> blockedNotifications;

    @Override
    public PlayerEntity toEntity() {
        PlayerEntity entity = new PlayerEntity();

        entity.setUniqueId(uniqueId);
        entity.setName(name);
        entity.setInvestments(investments.stream().map(Dao::toEntity).collect(Collectors.toList()));
        entity.setBlockedNotifications(blockedNotifications != null ? blockedNotifications : new HashSet<>());

        return entity;
    }

    @Override
    public Dao<PlayerEntity> fromEntity(PlayerEntity entity) {
        return new PlayerDao(
                entity.getUniqueId(),
                entity.getName(),
                entity.getInvestments().stream().map(investment -> new InvestmentDao(
                                investment.getCompanyId(),
                                investment.getSharesAmount(),
                                investment.getAverageBuyPrice() != null ? investment.getAverageBuyPrice() : 0.0))
                        .collect(Collectors.toList()),
                entity.getBlockedNotifications() != null ? entity.getBlockedNotifications() : new HashSet<>()
        );
    }

}
