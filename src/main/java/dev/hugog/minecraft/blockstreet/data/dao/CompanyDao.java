package dev.hugog.minecraft.blockstreet.data.dao;

import dev.hugog.minecraft.blockstreet.data.entities.CompanyEntity;
import dev.hugog.minecraft.blockstreet.utils.SizedStack;
import lombok.*;

import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class CompanyDao implements Dao<CompanyEntity> {

    @Setter
    private int id;
    private String name;
    private String description;
    private double initialSharePrice;
    @Setter
    private double currentSharePrice;
    private int risk;
    private int totalShares;
    @Setter
    private int availableShares;
    private SizedStack<QuoteDao> historic;

    @Override
    public CompanyEntity toEntity() {

        CompanyEntity entity = new CompanyEntity();

        entity.setId(id);
        entity.setName(name);
        entity.setDescription(description);
        entity.setInitialSharePrice(initialSharePrice);
        entity.setCurrentSharePrice(currentSharePrice);
        entity.setRisk(risk);
        entity.setTotalShares(totalShares);
        entity.setAvailableShares(availableShares);
        entity.setHistoric(historic.stream().map(Dao::toEntity).collect(Collectors.toList()));

        return entity;

    }

    @Override
    public Dao<CompanyEntity> fromEntity(CompanyEntity entity) {

        return new CompanyDao(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getInitialSharePrice(),
                entity.getCurrentSharePrice(),
                entity.getRisk(),
                entity.getTotalShares(),
                entity.getAvailableShares(),
                new SizedStack<QuoteDao>(500).fromList(
                        entity.getHistoric().stream()
                                .map(quote -> (QuoteDao) new QuoteDao().fromEntity(quote))
                                .collect(Collectors.toList())
                )
        );

    }

    public boolean isBankrupt() {
        return this.getCurrentSharePrice() == 0.0;
    }

}
