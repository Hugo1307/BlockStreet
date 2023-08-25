package dev.hugog.minecraft.blockstreet.data.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class CompanyEntity implements DataEntity {

    @JsonProperty("Id")
    private int id;
    @JsonProperty("Name")
    private String name;
    @JsonProperty("Description")
    private String description;
    @JsonProperty("PricePerStock")
    private int pricePerStock;
    @JsonProperty("Risk")
    private int risk;
    @JsonProperty("TotalStocks")
    private int totalStocks;
    @JsonProperty("AvailableStocks")
    private int availableStocks;
    @JsonProperty("Historic")
    private List<String> historic;

}
