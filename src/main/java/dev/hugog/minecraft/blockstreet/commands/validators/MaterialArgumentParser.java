package dev.hugog.minecraft.blockstreet.commands.validators;

import dev.hugog.minecraft.dev_command.arguments.parsers.CommandArgumentParser;
import org.bukkit.Material;

import java.util.Optional;

/**
 * Custom {@link CommandArgumentParser} implementation to match bukkit Materials.
 */
public class MaterialArgumentParser extends CommandArgumentParser<Material> {

    public MaterialArgumentParser(String argument) {
        super(argument);
    }

    @Override
    public boolean isValid() {
        return Material.matchMaterial(this.getArgument()) != null;
    }

    @Override
    public Optional<Material> parse() {
        if (!isValid()) {
            return Optional.empty();
        }
        return Optional.ofNullable(Material.matchMaterial(this.getArgument()));
    }

}
