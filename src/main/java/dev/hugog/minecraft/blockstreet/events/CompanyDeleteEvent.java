package dev.hugog.minecraft.blockstreet.events;

import dev.hugog.minecraft.blockstreet.data.dao.CompanyDao;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
@RequiredArgsConstructor
public class CompanyDeleteEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private final CompanyDao company;

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    @NonNull
    public HandlerList getHandlers() {
        return handlers;
    }
}
