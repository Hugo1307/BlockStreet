package dev.hugog.minecraft.blockstreet.data.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import dev.hugog.minecraft.blockstreet.data.dao.NotificationPreference;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlayerEntity implements DataEntity {

    private String uniqueId;
    private String name;
    private List<InvestmentEntity> investments;
    private Set<NotificationPreference> notificationPreferences;

}
