package Permanager.commands.utils;

import Permanager.commands.BaseCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.awt.*;

public class PingCommand implements BaseCommand {


    @Override
    public String getCommandName() {
        return "ping";
    }

    @Override
    public String getCommandDescription() {
        return "Bot's ping";
    }

    @Override
    public SlashCommandData registerCommand() {
        return Commands.slash(getCommandName(), getCommandDescription());
    }

    @Override
    public void run(SlashCommandInteractionEvent event) {
        long time = System.currentTimeMillis();

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(String.format("Ping %s:", event.getJDA().getSelfUser().getName()));
        embed.addField("Latency:", String.format("`%s`ms", System.currentTimeMillis() - time), false);
        embed.addField("Gateway latency: ", String.format("`%s`ms", event.getJDA().getGatewayPing()), false);
        embed.addField("Uptime: ", String.format("<t:%s:R>", "00000"), false);
        embed.setThumbnail(event.getJDA().getSelfUser().getAvatarUrl());
        embed.setColor(Color.decode("#5564f2"));

        event.replyEmbeds(embed.build()).setEphemeral(true).queue();
    }
}
