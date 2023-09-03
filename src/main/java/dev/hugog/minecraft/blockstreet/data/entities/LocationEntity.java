package dev.hugog.minecraft.blockstreet.data.entities;

import lombok.Data;

@Data
public class LocationEntity implements DataEntity {

    private String world;
    private double x;
    private double y;
    private double z;

}
