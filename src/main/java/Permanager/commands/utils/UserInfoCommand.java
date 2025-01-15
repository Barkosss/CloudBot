package Permanager.commands.utils;

import Permanager.commands.BaseCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.awt.*;
import java.util.List;

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
    public List<CommandData> registerCommand() {
        return List.of(Commands.slash(getCommandName(), getCommandDescription())
                .addOption(OptionType.USER, "member", "Target member"));
    }

    @Override
    public void run(SlashCommandInteractionEvent event) {
        User targetUser = event.getOption("member") != null ? event.getOption("member").getAsUser() : event.getUser();
        event.replyEmbeds(sendEmbed(targetUser).build()).queue();
    }

    @Override
    public void userContext(UserContextInteractionEvent event) {
        User targetUser = event.getTarget();
        event.replyEmbeds(sendEmbed(targetUser).build()).queue();
    }

    private EmbedBuilder sendEmbed(User targetUser) {
        return new EmbedBuilder()
                .setTitle("User Information: " + targetUser.getName())
                .setThumbnail(targetUser.getEffectiveAvatarUrl())
                .setFooter("User id: " + targetUser.getId())
                .setColor(Color.decode("#5564f2"));
    }
}
