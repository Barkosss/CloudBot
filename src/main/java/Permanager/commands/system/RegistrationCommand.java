package Permanager.commands.system;

import Permanager.commands.BaseCommand;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.util.List;

public class RegistrationCommand implements BaseCommand {

    @Override
    public String getCommandName() {
        return null;
    }

    @Override
    public String getCommandDescription() {
        return null;
    }

    @Override
    public List<CommandData> registerCommand() {
        return List.of();
    }

    @Override
    public void run(SlashCommandInteractionEvent event) {

    }

    @Override
    public void select(StringSelectInteractionEvent event) {
        BaseCommand.super.select(event);
    }

    @Override
    public void button(ButtonInteractionEvent event) {
        BaseCommand.super.button(event);
    }

    @Override
    public void modal(ModalInteractionEvent event) {
        BaseCommand.super.modal(event);
    }
}
