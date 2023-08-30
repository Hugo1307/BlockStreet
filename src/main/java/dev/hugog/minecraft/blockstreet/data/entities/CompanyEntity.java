package dev.hugog.minecraft.blockstreet.data.entities;

import lombok.Data;

import java.util.List;

@Data
public class CompanyEntity implements DataEntity {

    private int id;
    private String name;
    private String description;
    private int sharePrice;
    private int risk;
    private int totalShares;
    private int availableShares;
    private List<String> historic;

}
