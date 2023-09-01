package dev.hugog.minecraft.blockstreet.data.entities;

import lombok.Data;

@Data
public class InvestmentEntity implements DataEntity {

    private long companyId;
    private int sharesAmount;

}