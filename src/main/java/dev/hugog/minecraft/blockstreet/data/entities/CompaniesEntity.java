package dev.hugog.minecraft.blockstreet.data.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class CompaniesEntity implements DataEntity {
    @JsonProperty("Companies")
    private List<CompanyEntity> companies;
}
