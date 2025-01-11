package Permanager.commands.custom;

import Permanager.commands.BaseCommand;
import Permanager.utils.ValidateService;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.MessageContextInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

import java.awt.*;
import java.time.LocalDate;
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
        String action = event.getId().split("_")[0];
        EmbedBuilder embed = new EmbedBuilder();
        switch (action) {

            case "create": {
                String content = Objects.requireNonNull(event.getOption("content")).getAsString();
                String duration = Objects.requireNonNull(event.getOption("duration")).getAsString();
                embed = create(event.getIdLong(), Objects.requireNonNull(event.getGuild()).getIdLong(), content, duration);
                break;
            }

            case "edit": {
                //edit(event, event);
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
        String action = event.getId().split("_")[0];

        switch (action) {
            case "create": {
                String content = event.getValue("content").getAsString();
                String duration = event.getValue("duration").getAsString();
                EmbedBuilder embed = create(event.getIdLong(), event.getGuild().getIdLong(), content, duration);

                // ...
                // Добавление напоминания в базу данных
                // ...

                event.replyEmbeds(embed.build()).setEphemeral(true).queue();
                break;
            }

            case "edit": {
                String newContent = event.getValue("content").getAsString();
                String newDuration = event.getValue("duration").getAsString();
                EmbedBuilder embed = edit(event.getIdLong(), event.getGuild().getIdLong(), newContent, newDuration);

                // ...
                // Изменение напоминания в базе данных
                // ...

                event.replyEmbeds(embed.build()).setEphemeral(true).queue();
                break;
            }

            case "remove": {
                Optional<Long> validReminderId = validate.isValidLong(event.getValue("index").getAsString());

                if (validReminderId.isEmpty()) {
                    // ERROR
                    return;
                }

                EmbedBuilder embed = remove(event.getIdLong(), event.getGuild().getIdLong(), validReminderId.get());

                // ...
                // Удаление напоминания из базы данных
                // ...

                event.replyEmbeds(embed.build()).setEphemeral(true).queue();
                break;
            }

            case "list": {
                // ...
                EmbedBuilder embed = list(event.getIdLong(), event.getGuild().getIdLong());

                // ...
                // Получение напоминаний из базы данных
                // ...

                event.replyEmbeds(embed.build()).setEphemeral(true).queue();
                break;
            }
        }
    }

    private EmbedBuilder create(long userId, long serverId, String content, String duration) {
        Optional<LocalDate> validDuration = validate.isValidDate(duration);

        if (validDuration.isEmpty() || validDuration.get().isBefore(LocalDate.now())) {
            return new EmbedBuilder();
        }
        long index = 1;

        return new EmbedBuilder()
                .setTitle("Create reminder")
                .addField("Duration", "", false)
                .addField("Content", String.format("```\n%s\n```", content), false)
                .setColor(Color.decode(""))
                .setFooter(String.format("Create reminder #%s", index));
    }

    private EmbedBuilder edit(long userId, long serverId, String newContent, String newDuration) {

        return new EmbedBuilder();
    }

    private EmbedBuilder remove(long userId, long serverId, long reminderId) {

        return new EmbedBuilder();
    }

    private EmbedBuilder list(long userId, long serverId) {

        return new EmbedBuilder();
    }
}
