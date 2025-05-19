package dev.hugog.minecraft.blockstreet.commands.validators;

import dev.hugog.minecraft.dev_command.arguments.parsers.CommandArgumentParser;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Optional;

/**
 * Custom {@link CommandArgumentParser} implementation for positive integers.
 */
@EqualsAndHashCode(callSuper = false)
@ToString
public class PositiveIntegerArgumentParser extends CommandArgumentParser<Integer> {

    public PositiveIntegerArgumentParser(String argument) {
        super(argument);
    }

    @Override
    public boolean isValid() {
        return this.getArgument().matches("[+]?\\d+");
    }

    @Override
    public Optional<Integer> parse() {
        if (!isValid()) {
            return Optional.empty();
        }
        return Optional.of(Integer.parseInt(this.getArgument()));
    }

}
