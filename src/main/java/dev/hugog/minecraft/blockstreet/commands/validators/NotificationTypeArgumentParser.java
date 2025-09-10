package dev.hugog.minecraft.blockstreet.commands.validators;

import dev.hugog.minecraft.blockstreet.enums.NotificationType;
import dev.hugog.minecraft.dev_command.arguments.CommandArgument;
import dev.hugog.minecraft.dev_command.arguments.parsers.CommandArgumentParser;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Optional;

/**
 * Custom {@link CommandArgument} implementation for NotificationTypes.
 */
@EqualsAndHashCode(callSuper = false)
@ToString
public class NotificationTypeArgumentParser extends CommandArgumentParser<NotificationType> {

    public NotificationTypeArgumentParser(String argument) {
        super(argument);
    }

    @Override
    public boolean isValid() {
        try {
            NotificationType.valueOf(this.getArgument());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public Optional<NotificationType> parse() {
        if (!isValid()) {
            return Optional.empty();
        }
        return Optional.of(NotificationType.valueOf(this.getArgument()));
    }

}
