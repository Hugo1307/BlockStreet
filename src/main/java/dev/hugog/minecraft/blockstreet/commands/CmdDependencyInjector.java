package dev.hugog.minecraft.blockstreet.commands;

import com.google.inject.Inject;
import dev.hugog.minecraft.blockstreet.Main;
import dev.hugog.minecraft.blockstreet.update.UpdateChecker;
import lombok.Getter;

@Getter
public class CmdDependencyInjector {

    private final Main main;
    private final UpdateChecker updateChecker;

    @Inject
    public CmdDependencyInjector(Main main, UpdateChecker updateChecker) {
        this.main = main;
        this.updateChecker = updateChecker;
    }

}

