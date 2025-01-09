package Permanager.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public interface BaseCommand {
    String getCommandName();

    String getCommandDescription();

    SlashCommandData registerCommand();

    void run(SlashCommandInteractionEvent event);
}
