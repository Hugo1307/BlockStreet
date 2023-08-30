package dev.hugog.minecraft.blockstreet.data.entities;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InvestmentEntity implements DataEntity {

    private long companyId;
    private int sharesAmount;

}