package Permanager.commands.utils;

import Permanager.commands.BaseCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.awt.*;

public class UserInfoCommand implements BaseCommand {
    @Override
    public String getCommandName() {
        return "userinfo";
    }

    @Override
    public String getCommandDescription() {
        return "Information about the user";
    }

    @Override
    public SlashCommandData registerCommand() {
        return Commands.slash(getCommandName(), getCommandDescription())
                .addOption(OptionType.USER, "member", "Target member");
    }

    @Override
    public void run(SlashCommandInteractionEvent event) {
        User targetUser;
        if (event.getOption("member") != null) {
            targetUser = event.getOption("member").getAsUser();
        } else {
            targetUser = event.getUser();
        }

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("User Information: " + targetUser.getName());
        embed.setThumbnail(targetUser.getEffectiveAvatarUrl());
        embed.setFooter("User id: " + targetUser.getId());
        embed.setColor(Color.decode("#5564f2"));

        event.replyEmbeds(embed.build()).queue();
    }
}
