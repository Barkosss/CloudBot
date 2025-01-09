package Permanager.commands.custom;

import Permanager.commands.BaseCommand;
import Permanager.utils.ValidateService;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

import java.awt.*;
import java.time.LocalDate;
import java.util.Optional;

public class ReminderCommand implements BaseCommand {
    ValidateService validate = new ValidateService();

    @Override
    public String getCommandName() {
        return "reminder";
    }

    @Override
    public String getCommandDescription() {
        return "Reminder system";
    }

    @Override
    public SlashCommandData registerCommand() {
        return Commands.slash(getCommandName(), getCommandDescription())
                .addSubcommands(
                        new SubcommandData("create", "...")
                                .addOption(OptionType.STRING, "duration", "...", true)
                                .addOption(OptionType.STRING, "content", "...", true)
                )
                .addSubcommands(
                        new SubcommandData("edit", "...")
                                .addOption(OptionType.STRING, "index", "...", true)
                                .addOption(OptionType.STRING, "new_duration", "...")
                                .addOption(OptionType.STRING, "new_content", "...")
                )
                .addSubcommands(
                        new SubcommandData("remove", "...")
                                .addOption(OptionType.STRING, "index", "...", true)
                )
                .addSubcommands(
                        new SubcommandData("list", "...")
                );
    }

    @Override
    public void run(SlashCommandInteractionEvent event) {

        switch (event.getSubcommandName()) {

            case "create": {
                create(event);
                break;
            }

            case "edit": {
                edit(event);
                break;
            }

            case "remove": {
                remove(event);
                break;
            }

            case "list": {
                list(event);
                break;
            }
        }
    }

    private void create(SlashCommandInteractionEvent event) {
        String duration = event.getOption("duration").getAsString();
        String content = event.getOption("content").getAsString();
        Optional<LocalDate> validDuration = validate.isValidDate(duration);

        if (validDuration.isEmpty() || validDuration.get().isBefore(LocalDate.now())) {
            EmbedBuilder embed = new EmbedBuilder();

            event.replyEmbeds(embed.build()).setEphemeral(true).queue();
            return;
        }
        long index = 1;

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Create reminder");
        embed.addField("Duration", "", false);
        embed.addField("Content", String.format("```\n%s\n```", content), false);
        embed.setColor(Color.decode(""));
        embed.setFooter(String.format("Create reminder #%s", index));

        event.replyEmbeds(embed.build()).setEphemeral(true).queue();
    }

    private void edit(SlashCommandInteractionEvent event) {

        EmbedBuilder embed = new EmbedBuilder();

        event.replyEmbeds(embed.build()).setEphemeral(true).queue();
    }

    private void remove(SlashCommandInteractionEvent event) {

        EmbedBuilder embed = new EmbedBuilder();

        event.replyEmbeds(embed.build()).setEphemeral(true).queue();
    }

    private void list(SlashCommandInteractionEvent event) {

        EmbedBuilder embed = new EmbedBuilder();

        event.replyEmbeds(embed.build()).setEphemeral(true).queue();
    }
}
