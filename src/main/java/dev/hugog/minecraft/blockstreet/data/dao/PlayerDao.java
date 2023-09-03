package dev.hugog.minecraft.blockstreet.data.dao;

import dev.hugog.minecraft.blockstreet.data.entities.PlayerEntity;
import lombok.*;

import java.util.List;
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

    @Override
    public PlayerEntity toEntity() {

        final PlayerEntity entity = new PlayerEntity();

        entity.setUniqueId(uniqueId);
        entity.setName(name);
        entity.setInvestments(investments.stream().map(Dao::toEntity).collect(Collectors.toList()));

        return entity;

    }

    @Override
    public Dao<PlayerEntity> fromEntity(PlayerEntity entity) {
        return new PlayerDao(
                entity.getUniqueId(),
                entity.getName(),
                entity.getInvestments().stream().map(investment -> new InvestmentDao(investment.getCompanyId(), investment.getSharesAmount())).collect(Collectors.toList())
        );
    }

}
