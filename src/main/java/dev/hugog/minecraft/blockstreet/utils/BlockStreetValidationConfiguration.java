package dev.hugog.minecraft.blockstreet.utils;

import dev.hugog.minecraft.dev_command.commands.BukkitDevCommand;
import dev.hugog.minecraft.dev_command.validation.IAutoValidationConfiguration;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BlockStreetValidationConfiguration implements IAutoValidationConfiguration {

  private final Messages messages;

  @Override
  public String getNoPermissionMessage(BukkitDevCommand bukkitDevCommand) {
    return messages.getPluginPrefix() + messages.getNoPermission();
  }

  @Override
  public String getInvalidArgumentsMessage(BukkitDevCommand bukkitDevCommand) {
    return messages.getPluginPrefix() + messages.getWrongArguments();
  }

  @Override
  public String getInvalidSenderMessage(BukkitDevCommand bukkitDevCommand) {
    return messages.getPluginPrefix() + messages.getPlayerOnlyCommand();
  }

}
