package dev.hugog.minecraft.blockstreet.data.dao;

import dev.hugog.minecraft.blockstreet.data.entities.SignEntity;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class SignDao implements Dao<SignEntity> {

    private int id;
    private long companyId;
    private LocationDao location;

    @Override
    public SignEntity toEntity() {

        SignEntity entity = new SignEntity();

        entity.setId(id);
        entity.setCompanyId(companyId);
        entity.setLocation(location.toEntity());

        return entity;

    }

    @Override
    public Dao<SignEntity> fromEntity(SignEntity entity) {
        return new SignDao(
                entity.getId(),
                entity.getCompanyId(),
                (LocationDao) new LocationDao().fromEntity(entity.getLocation())
        );
    }

}
