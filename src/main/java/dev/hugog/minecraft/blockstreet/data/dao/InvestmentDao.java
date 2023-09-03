package dev.hugog.minecraft.blockstreet.data.dao;

import dev.hugog.minecraft.blockstreet.data.entities.InvestmentEntity;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class InvestmentDao implements Dao<InvestmentEntity> {

    private long companyId;
    @Setter
    private int sharesAmount;


    @Override
    public InvestmentEntity toEntity() {
        final InvestmentEntity entity = new InvestmentEntity();
        entity.setCompanyId(companyId);
        entity.setSharesAmount(sharesAmount);
        return entity;
    }

    @Override
    public Dao<InvestmentEntity> fromEntity(InvestmentEntity entity) {
        return new InvestmentDao(
                entity.getCompanyId(),
                entity.getSharesAmount()
        );
    }

}
