package Permanager;

import Permanager.commands.BaseCommand;
import Permanager.system.AuditSystem;
import Permanager.utils.JSONHandler;
import Permanager.utils.LoggerHandler;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.MessageContextInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import net.dv8tion.jda.internal.utils.JDALogger;
import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CommandManager extends ListenerAdapter {
    JSONHandler jsonHandler = new JSONHandler();
    LoggerHandler logger = new LoggerHandler();

    private final Map<String, BaseCommand> commands = new HashMap<>();

    public CommandManager() {
        try {
            String token = String.valueOf(jsonHandler.read("config.json", "token"));

            JDA jda = JDABuilder.createDefault(token)
                    .enableIntents(GatewayIntent.GUILD_MEMBERS,
                            GatewayIntent.GUILD_MESSAGES,
                            GatewayIntent.DIRECT_MESSAGES,
                            GatewayIntent.MESSAGE_CONTENT)
                    .addEventListeners(this).addEventListeners(new AuditSystem()).build();
            jda.getPresence().setActivity(Activity.customStatus("A Java bot"));
            JDALogger.setFallbackLoggerEnabled(false);

            Reflections reflections = new Reflections("Permanager.commands");
            // Получаем множеством всех классов, которые реализовывают интерфейс BaseCommand
            Set<Class<? extends BaseCommand>> subclasses = reflections.getSubTypesOf(BaseCommand.class);
            CommandListUpdateAction JDACommands = jda.updateCommands();

            String commandName;
            BaseCommand instanceClass;
            // Проходимся по каждому классу
            for (Class<? extends BaseCommand> subclass : subclasses) {
                // Создаём экземпляр класса
                instanceClass = subclass.getConstructor().newInstance();

                commandName = instanceClass.getCommandName();

                // Если название команды пустое, то пропускаем ход
                if (commandName == null) {
                    continue;
                }
                commandName = commandName.toLowerCase();

                // Проверка, нет ли команд с таким именем в мапе
                if (!commands.containsKey(commandName)) {
                    // Добавляем класс в хэшмап, ключ - название команды, значение - экземпляр класса
                    commands.put(commandName, instanceClass);
                    // Добавляем команду в отображение
                    List<CommandData> commands = instanceClass.registerCommand();
                    for (CommandData command : commands) {
                        JDACommands = JDACommands.addCommands(command);
                    }

                } else {
                    String errMessage = String.format("There was a duplication of the command - %s", commandName);
                    logger.error(errMessage, true);
                    System.exit(0);
                }
            }
            JDACommands.queue();

        } catch (Exception err) {
            logger.error(String.format("Command loader: %s", err));
        }
    }

    @Override
    public void onReady(ReadyEvent event) {
        System.out.printf("Bot %s is ready!%n", event.getJDA().getSelfUser().getAsTag());
    }

    @Override
    public void onGenericInteractionCreate(@NotNull GenericInteractionCreateEvent interaction) {
        switch (interaction) {
            case SlashCommandInteractionEvent slashInteractionEvent -> {
                // Слэш
                String commandName = slashInteractionEvent.getName();
                BaseCommand command = commands.get(commandName);
                command.run(slashInteractionEvent);
            }
            case ButtonInteractionEvent buttonInteractionEvent -> {
                // Кнопка
                String commandName = buttonInteractionEvent.getComponentId().split("_")[0];
                BaseCommand command = commands.get(commandName);
                command.button(buttonInteractionEvent);
            }
            case StringSelectInteractionEvent stringSelectEvent -> {
                // Меню выбора (Строки)
                String commandName = stringSelectEvent.getComponentId().split("_")[0];
                BaseCommand command = commands.get(commandName);
                command.select(stringSelectEvent);
            }
            case ModalInteractionEvent modalInteractionEvent -> {
                // Диалоговое окно (Modal)
                String commandName = modalInteractionEvent.getModalId().split("_")[0];
                BaseCommand command = commands.get(commandName);
                command.modal(modalInteractionEvent);
            }
            case UserContextInteractionEvent userContextInteractionEvent -> {
                // User Context
                String commandName = userContextInteractionEvent.getFullCommandName().split(" ")[0].toLowerCase();
                BaseCommand command = commands.get(commandName);
                command.userContext(userContextInteractionEvent);
            }
            case MessageContextInteractionEvent messageContextInteractionEvent -> {
                // Message Context
                String commandName = messageContextInteractionEvent.getFullCommandName().split(" ")[0].toLowerCase();
                BaseCommand command = commands.get(commandName);
                command.messageContext(messageContextInteractionEvent);
            }
            default -> throw new IllegalStateException("Unexpected value: " + interaction);
        }
    }
}