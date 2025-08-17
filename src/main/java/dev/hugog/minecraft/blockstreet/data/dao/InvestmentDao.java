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
    @Setter
    private double averageBuyPrice;

    /**
     * Get the variation of the investment in percentage.
     *
     * <p>Used to calculate how much the investment has changed (in percentage) since the shares were bought.
     *
     * @param currentSharePrice the current share price of the company
     * @return the percentage variation of the investment
     */
    public double getInvestmentVariation(double currentSharePrice) {
        return (currentSharePrice - averageBuyPrice) / averageBuyPrice * 100;
    }

    @Override
    public InvestmentEntity toEntity() {
        final InvestmentEntity entity = new InvestmentEntity();
        entity.setCompanyId(companyId);
        entity.setSharesAmount(sharesAmount);
        entity.setAverageBuyPrice(averageBuyPrice);
        return entity;
    }

    @Override
    public Dao<InvestmentEntity> fromEntity(InvestmentEntity entity) {
        return new InvestmentDao(
                entity.getCompanyId(),
                entity.getSharesAmount(),
                entity.getAverageBuyPrice() != null ? entity.getAverageBuyPrice() : 0.0
        );
    }

}
