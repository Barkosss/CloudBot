package Permanager.commands.system;

import Permanager.commands.BaseCommand;
import Permanager.utils.JSONHandler;
import Permanager.utils.LoggerHandler;
import kotlin.Pair;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.json.simple.JSONObject;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class EmbedsCommand implements BaseCommand {
    JSONHandler json = new JSONHandler();
    LoggerHandler logger = new LoggerHandler();

    @Override
    public String getCommandName() {
        return "embeds";
    }

    @Override
    public String getCommandDescription() {
        return "...";
    }

    @Override
    public List<CommandData> registerCommand() {
        SlashCommandData command = Commands.slash(getCommandName(), getCommandDescription())
                .addOptions(
                        new OptionData(OptionType.STRING, "section", "Выберите раздел")
                                .setRequired(true));

        OptionData optionData = command.getOptions().getFirst();
        List<Pair<String, String>> choices = getChoices();
        for (Pair<String, String> choice : choices) {
            optionData.addChoice(choice.getFirst(), choice.getSecond());
        }

        return List.of(command);
    }

    @Override
    public void run(SlashCommandInteractionEvent event) {
        JSONObject section = (JSONObject) json.read("embeds.json", event.getOption("section").getAsString());
        EmbedBuilder embed = new EmbedBuilder();

        if (section.containsKey("title")) {
            embed.setTitle((String) section.get("title"));
        }

        if (section.containsKey("description")) {
            embed.setDescription((String) section.get("description"));
        }

        if (section.containsKey("author")) {
            embed.setAuthor((String) section.get("author"), null, null);
        }

        if (section.containsKey("color")) {
            embed.setColor(Color.decode((String) section.get("color")));
        }

        if (section.containsKey("thumbnail")) {
            embed.setThumbnail((String) section.get("thumbnail"));
        }

        if (section.containsKey("footer")) {
            embed.setFooter((String) section.get("footer"));
        }

        if (section.containsKey("image")) {
            embed.setImage((String) section.get("image"));
        }

        if (section.containsKey("fields")) {
            JSONObject fields = (JSONObject) section.get("fields");

            for (Object key : fields.keySet()) {
                JSONObject fieldObject = (JSONObject) fields.get(key);
                String name = (String) fieldObject.get("name");
                String value = (String) fieldObject.get("value");
                Boolean inline = (Boolean) fieldObject.get("inline");

                embed.addField(name, value, inline);
            }
        }

        event.replyEmbeds(embed.build()).queue();
    }

    private List<Pair<String, String>> getChoices() {
        List<Pair<String, String>> choices = new ArrayList<>();
        try {
            JSONObject jsonObject = json.readJSON("embeds.json");

            JSONObject object;
            for (Object key : jsonObject.keySet()) {
                object = ((JSONObject) jsonObject.get(key));
                if (object.containsKey("name")) {
                    choices.add(new Pair<>(object.get("name").toString(), key.toString()));
                }
            }

            return choices;

        } catch (Exception err) {
            logger.error("Error: " + err);
            return null;
        }
    }
}
