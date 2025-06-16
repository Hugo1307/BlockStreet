package dev.hugog.minecraft.blockstreet.commands.validators;

import dev.hugog.minecraft.dev_command.arguments.parsers.CommandArgumentParser;

import java.util.Optional;

public class SharePriceArgumentParser extends CommandArgumentParser<Double> {
    public SharePriceArgumentParser(String argument) {
        super(argument);
    }

    @Override
    public boolean isValid() {
        return !this.getArgument().isEmpty() && !this.getArgument().isBlank() && this.getArgument().matches("[-+]?\\d*(\\.\\d+)?");
    }

    @Override
    public Optional<Double> parse() {
        return !this.isValid() ? Optional.empty() : Optional.of(Double.parseDouble(this.getArgument()));
    }

}
