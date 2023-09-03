package dev.hugog.minecraft.blockstreet.commands.validators;

import dev.hugog.minecraft.dev_command.validators.CommandArgument;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Custom {@link CommandArgument} implementation for positive integers.
 */
@EqualsAndHashCode(callSuper = false)
@ToString
public class CompanyRiskArgument extends CommandArgument<Integer> {

    public CompanyRiskArgument(String argument) {
        super(argument);
    }

    @Override
    public boolean isValid() {
        return this.getArgument().matches("[0-5]");
    }

    @Override
    public Integer parse() {
        return Integer.parseInt(this.getArgument());
    }

}
