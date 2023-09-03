package dev.hugog.minecraft.blockstreet.data.dao;

import dev.hugog.minecraft.blockstreet.data.entities.LocationEntity;
import lombok.*;
import org.bukkit.Location;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class LocationDao implements Dao<LocationEntity> {

    private String world;
    private double x;
    private double y;
    private double z;

    public static LocationDao fromBukkitLocation(Location location) {

        if (location.getWorld() == null) {
            throw new IllegalArgumentException("Location must have a world");
        }

        return new LocationDao(
                location.getWorld().getName(),
                location.getX(),
                location.getY(),
                location.getZ()
        );

    }

    public Location toBukkitLocation() {

        return new Location(
                org.bukkit.Bukkit.getWorld(world),
                x,
                y,
                z
        );

    }

    @Override
    public LocationEntity toEntity() {

        final LocationEntity entity = new LocationEntity();

        entity.setWorld(world);
        entity.setX(x);
        entity.setY(y);
        entity.setZ(z);

        return entity;

    }

    @Override
    public Dao<LocationEntity> fromEntity(LocationEntity entity) {
        return new LocationDao(
                entity.getWorld(),
                entity.getX(),
                entity.getY(),
                entity.getZ()
        );
    }

}
