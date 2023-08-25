package dev.hugog.minecraft.blockstreet.commands;

import com.google.inject.Inject;
import dev.hugog.minecraft.blockstreet.BlockStreet;
import lombok.Getter;

@Getter
public class CmdDependencyInjector {

    private final BlockStreet blockStreet;

    @Inject
    public CmdDependencyInjector(BlockStreet blockStreet) {
        this.blockStreet = blockStreet;
    }

}

