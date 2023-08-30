package dev.hugog.minecraft.blockstreet.data.entities;

import lombok.Data;

import java.util.List;

@Data
public class PlayerEntity implements DataEntity {

    private String uniqueId;
    private String name;
    private List<InvestmentEntity> investments;

}
