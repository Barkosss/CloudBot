package Permanager.commands;

import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.MessageContextInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.util.List;

public interface BaseCommand {

    String getCommandName();

    String getCommandDescription();

    List<CommandData> registerCommand();

    void run(SlashCommandInteractionEvent event);

    default void select(StringSelectInteractionEvent event) {}

    default void button(ButtonInteractionEvent event) {}

    default void modal(ModalInteractionEvent event) {}

    default void userContext(UserContextInteractionEvent event) {}

    default void messageContext(MessageContextInteractionEvent event) {}
}
