package Permanager.commands.custom;

import Permanager.commands.BaseCommand;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.MessageContextInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.util.List;

public class BrickBankCommand implements BaseCommand {
    @Override
    public String getCommandName() {
        return "brickbank";
    }

    @Override
    public String getCommandDescription() {
        return "...";
    }

    @Override
    public List<CommandData> registerCommand() {
        return List.of(
                Commands.slash(getCommandName(), getCommandDescription()),
                Commands.user("BrickBank pay"),
                Commands.message("BrickBank pay")
        );
    }

    @Override
    public void run(SlashCommandInteractionEvent event) {

    }

    @Override
    public void select(StringSelectInteractionEvent event) {
        // ...
    }

    @Override
    public void button(ButtonInteractionEvent event) {
        // ...
    }

    @Override
    public void modal(ModalInteractionEvent event) {
        // ...
    }

    @Override
    public void userContext(UserContextInteractionEvent event) {
        // ...
    }

    @Override
    public void messageContext(MessageContextInteractionEvent event) {
        // ...
    }
}
