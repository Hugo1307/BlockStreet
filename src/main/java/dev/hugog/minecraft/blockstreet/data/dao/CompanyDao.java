package dev.hugog.minecraft.blockstreet.data.dao;

import dev.hugog.minecraft.blockstreet.data.entities.CompanyEntity;
import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompanyDao implements Dao<CompanyEntity> {

    private int id;
    private String name;
    private String description;
    private double initialSharePrice;
    @Setter private double currentSharePrice;
    private int risk;
    private int totalShares;
    @Setter private int availableShares;
    private List<Double> historic;

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
        entity.setHistoric(historic);

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
                entity.getHistoric()
        );

    }

}
