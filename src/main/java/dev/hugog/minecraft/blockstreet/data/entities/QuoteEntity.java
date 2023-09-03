package dev.hugog.minecraft.blockstreet.data.entities;

import lombok.Data;

@Data
public class QuoteEntity implements DataEntity {

    private double variation;
    private double price;
    private long timestamp;

}
