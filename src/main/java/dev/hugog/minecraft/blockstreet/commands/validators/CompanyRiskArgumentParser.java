package dev.hugog.minecraft.blockstreet.commands.validators;

import dev.hugog.minecraft.dev_command.arguments.CommandArgument;
import dev.hugog.minecraft.dev_command.arguments.parsers.CommandArgumentParser;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Optional;

/**
 * Custom {@link CommandArgument} implementation for positive integers.
 */
@EqualsAndHashCode(callSuper = false)
@ToString
public class CompanyRiskArgumentParser extends CommandArgumentParser<Integer> {

    public CompanyRiskArgumentParser(String argument) {
        super(argument);
    }

    @Override
    public boolean isValid() {
        return this.getArgument().matches("[1-5]");
    }

    @Override
    public Optional<Integer> parse() {
        if (!isValid()) {
            return Optional.empty();
        }
        return Optional.of(Integer.parseInt(this.getArgument()));
    }

}
