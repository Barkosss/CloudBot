const { SlashCommandBuilder, ContextMenuCommandBuilder } = require("@discordjs/builders");
const config = require("./config.json");
const { REST } = require("@discordjs/rest");
const { Routes } = require("discord-api-types/v10");

const commands = [

    new SlashCommandBuilder()
        .setName("embed")
        .setDMPermission(false)
        .setDescription("Отправить системный эмбед")
        .addStringOption((option) =>
            option
                .setName("section")
                .setDescription("Выберите раздел")
                .addChoices(
                    { name: "Верификация", value: "verify" },
                    { name: "Регистрация", value: "registration" },
                    { name: "Статус сервера", value: "status" },
                )
                .setRequired(true)
        ),

    new SlashCommandBuilder()
        .setName("reminder")
        .setDMPermission(false)
        .setDescription("Управление напоминаниями")
        .addSubcommand(subCommand =>
            subCommand
                .setName("create")
                .setDescription("Создать напоминание")
                .addStringOption(option => option.setName("duration").setDescription("Укажите длительность").setMinLength(2).setRequired(true))
                .addStringOption(option => option.setName("content").setDescription("Укажите содержание").setRequired(true))
        )
        .addSubcommand(subCommand =>
            subCommand
                .setName("edit")
                .setDescription("Редактировать напоминание")
                .addNumberOption(option => option.setName("index").setDescription("Укажите индекс напоминания").setMinValue(1).setRequired(true))
                .addStringOption(option => option.setName("duration").setDescription("Укажите новую длительность").setMinLength(2))
                .addStringOption(option => option.setName("content").setDescription("Укажите новое содержание"))
        )
        .addSubcommand(subCommand =>
            subCommand
                .setName("remove")
                .setDescription("Удалить напоминание")
                .addNumberOption(option => option.setName("index").setDescription("Укажите индекс напоминания").setMinValue(1).setRequired(true))
        )
        .addSubcommand(subCommand =>
            subCommand
                .setName("list")
                .setDescription("Посмотреть активные напоминания")
        )

].map(command => command.toJSON());

const rest = new REST({ version: "10" }).setToken(config.token);
(async() => {
    try {
        console.log("Reloading");

        await rest.put(
            Routes.applicationCommands("1226072189871063063"),
            {
                body: commands
            }
        );

        console.log("Reloaded");

    } catch(error) {
        console.log(error);
    }
})();