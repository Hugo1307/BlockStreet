package hugog.blockstreet.commands;

import com.google.inject.Inject;
import hugog.blockstreet.Main;
import hugog.blockstreet.update.UpdateChecker;
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

