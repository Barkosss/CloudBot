package Permanager.commands.custom;

import Permanager.commands.BaseCommand;
import Permanager.utils.JSONHandler;
import Permanager.utils.LoggerHandler;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.MessageContextInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

import java.util.List;
import java.util.Objects;

public class BrickBankCommand implements BaseCommand {
    JSONHandler json = new JSONHandler();
    LoggerHandler logger = new LoggerHandler();

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
                Commands.slash(getCommandName(), getCommandDescription())
                        .addSubcommands(
                                new SubcommandData("main", "...")
                        )
                        .addSubcommands(
                                new SubcommandData("pay", "...")
                                        .addOption(OptionType.USER, "member", "...", true)
                                        .addOption(OptionType.NUMBER, "amount", "...", true)
                                        .addOption(OptionType.STRING, "comments", "...", false)
                        ),
                Commands.user("BrickBank pay"),
                Commands.message("BrickBank pay")
        );
    }

    @Override
    public void run(SlashCommandInteractionEvent event) {
        String action = event.getSubcommandName();

        switch (action) {

            case "main": {
                event.replyEmbeds(getMainEmbed().build()).queue();
                break;
            }

            case "pay": {
                User user = Objects.requireNonNull(event.getOption("user")).getAsUser();
                long amount = Objects.requireNonNull(event.getOption("amount")).getAsLong();
                String comment = Objects.requireNonNull(event.getOption("comments")).getAsString();

                if (amount <= 0 || user.getIdLong() < amount) {

                    return;
                }

                if (comment.length() > 1024) {
                    comment = comment.substring(0, 1024);
                }

                event.replyEmbeds(getPayEmbed(user, amount, comment).build()).queue();
                break;
            }

            case null: {
                break;
            }
            default: {
                event.replyEmbeds(getErrorEmbed().build()).queue();
                break;
            }
        }
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
        Member member = event.getTargetMember();
        assert member != null;
        event.reply(String.format("User: %s", member.getUser().getName())).queue();
    }

    @Override
    public void messageContext(MessageContextInteractionEvent event) {
        Member member = event.getTarget().getMember();
        assert member != null;
        event.reply(String.format("User: %s", member.getUser().getName())).queue();
    }

    private EmbedBuilder getMainEmbed() {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("");

        return embed;
    }

    private EmbedBuilder getPayEmbed(User user, long amount, String comment) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("");
        embed.addField("Target user:", "> " + user, false);
        embed.addField("Amount:", "`" + amount + "`", false);
        if (!comment.isEmpty()) {
            embed.addField("Comments:", "```" + comment + "```", false);
        }

        return embed;
    }

    private EmbedBuilder getErrorEmbed() {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("");

        return embed;
    }
}