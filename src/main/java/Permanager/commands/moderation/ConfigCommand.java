package Permanager.commands.moderation;

import Permanager.commands.BaseCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

import java.util.List;

public class ConfigCommand implements BaseCommand {
    @Override
    public String getCommandName() {
        return "config";
    }

    @Override
    public String getCommandDescription() {
        return "...";
    }

    @Override
    public List<CommandData> registerCommand() {
        return List.of(Commands.slash(getCommandName(), getCommandDescription())
                .addSubcommands(
                        new SubcommandData("dashboard", "...")
                )
                .addSubcommands(
                        new SubcommandData("user", "...")
                )
                .addSubcommands(
                        new SubcommandData("group", "...")
                )
                .addSubcommands(
                        new SubcommandData("limit", "...")
                ));
    }

    @Override
    public void run(SlashCommandInteractionEvent event) {

    }
}
