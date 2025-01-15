package Permanager.commands.custom;

import Permanager.commands.BaseCommand;
import Permanager.utils.ValidateService;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.MessageContextInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
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
    public List<CommandData> registerCommand() {
        return List.of(Commands.slash(getCommandName(), getCommandDescription())
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
                        ),
                Commands.message("Reminder create"));
    }

    @Override
    public void run(SlashCommandInteractionEvent event) {
        String action = event.getId().split("_")[0];
        EmbedBuilder embed = new EmbedBuilder();
        switch (action) {

            case "create": {
                String content = Objects.requireNonNull(event.getOption("content")).getAsString();
                String duration = Objects.requireNonNull(event.getOption("duration")).getAsString();

                Optional<LocalDate> validDuration = validate.isValidDate(duration);
                if (validDuration.isEmpty() || validDuration.get().isBefore(LocalDate.now())) {
                    event.replyEmbeds(new EmbedBuilder()
                            .setTitle("Error")
                            .setColor(Color.RED)
                            .build()
                    ).setEphemeral(true).queue();
                    return;
                }

                embed = getCreateEmbed(content, validDuration.get(), 0);
                break;
            }

            case "edit": {
                long index = Objects.requireNonNull(event.getOption("index")).getAsLong();
                String newContent = Objects.requireNonNull(event.getOption("content")).getAsString();
                String newDuration = Objects.requireNonNull(event.getOption("duration")).getAsString();
                embed = getEditEmbed(index, newContent, newDuration);
                break;
            }

            case "remove": {
                //remove(event, event);
                break;
            }

            case "list": {
                //list(event, event);
                break;
            }
        }

        event.replyEmbeds(embed.build()).setEphemeral(true).queue();
    }

    @Override
    public void messageContext(MessageContextInteractionEvent event) {

        TextInput content = TextInput.create("content", "Reminder's content", TextInputStyle.PARAGRAPH)
                .setPlaceholder("Enter content")
                .setMaxLength(300)
                .setRequired(true)
                .setValue(event.getTarget().getContentRaw())
                .build();

        TextInput duration = TextInput.create("duration", "Duration", TextInputStyle.SHORT)
                .setPlaceholder("Enter duration")
                .setMaxLength(64)
                .setRequired(true)
                .build();

        Modal modal = Modal.create("reminder_create", "Create reminder")
                .addComponents(ActionRow.of(content), ActionRow.of(duration))
                .build();

        event.replyModal(modal).queue();
    }

    @Override
    public void modal(ModalInteractionEvent event) {
        String action = event.getModalId().split("_")[1];

        switch (action) {
            case "create": {
                String content = Objects.requireNonNull(event.getValue("content")).getAsString();
                String duration = Objects.requireNonNull(event.getValue("duration")).getAsString();

                Optional<LocalDate> validDuration = validate.isValidDate(duration);
                if (validDuration.isEmpty() || validDuration.get().isBefore(LocalDate.now())) {
                    event.replyEmbeds(new EmbedBuilder()
                            .setTitle("Error")
                            .setColor(Color.RED)
                            .build()
                    ).setEphemeral(true).queue();
                    return;
                }

                EmbedBuilder embed = getCreateEmbed(content, validDuration.get(), 0);

                // ...
                // Добавление напоминания в базу данных
                // ...

                event.replyEmbeds(embed.build()).setEphemeral(true).queue();
                break;
            }

            case "edit": {
                Optional<Long> validReminderId = validate.isValidLong(Objects.requireNonNull(event.getValue("index")).getAsString());
                String newContent = Objects.requireNonNull(event.getValue("content")).getAsString();
                String newDuration = Objects.requireNonNull(event.getValue("duration")).getAsString();

                if (validReminderId.isEmpty()) {
                    // ERROR
                    return;
                }

                EmbedBuilder embed = getEditEmbed(validReminderId.get(), newContent, newDuration);

                // ...
                // Изменение напоминания в базе данных
                // ...

                event.replyEmbeds(embed.build()).setEphemeral(true).queue();
                break;
            }

            case "remove": {
                Optional<Long> validReminderId = validate.isValidLong(Objects.requireNonNull(event.getValue("index")).getAsString());

                if (validReminderId.isEmpty()) {
                    // ERROR
                    return;
                }

                EmbedBuilder embed = getRemoveEmbed(validReminderId.get());

                // ...
                // Удаление напоминания из базы данных
                // ...

                event.replyEmbeds(embed.build()).setEphemeral(true).queue();
                break;
            }

            case "list": {
                EmbedBuilder embed = getListEmbed(event.getIdLong(), Objects.requireNonNull(event.getGuild()).getIdLong());

                // ...
                // Получение напоминаний из базы данных
                // ...

                event.replyEmbeds(embed.build()).setEphemeral(true).queue();
                break;
            }
        }
    }

    private EmbedBuilder getCreateEmbed(String content, LocalDate duration, long index) {

        return new EmbedBuilder()
                .setTitle("Create reminder")
                .addField("Content", String.format("```\n%s\n```", content), false)
                .addField("Duration", String.format("> <t:%s:R>", duration.toEpochDay() * 24 * 60 * 60), false)
                .setColor(Color.decode("#a2d2ff"))
                .setFooter(String.format("Create reminder #%s", index));
    }

    private EmbedBuilder getEditEmbed(long id, String newContent, String newDuration) {
        return new EmbedBuilder();
    }

    private EmbedBuilder getRemoveEmbed(long reminderId) {
        return new EmbedBuilder();
    }

    private EmbedBuilder getListEmbed(long userId, long serverId) {
        return new EmbedBuilder();
    }
}
