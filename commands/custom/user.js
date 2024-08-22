const { EmbedBuilder, ButtonBuilder, SelectMenuBuilder, ActionRowBuilder, ModalBuilder, TextInputBuilder, TextInputStyle } = require('discord.js');
const timestamp = require("discord-timestamp");

const { Database, LocalStorage, JSONFormatter } = require("moonlifedb");
const adapter = new LocalStorage({ path: 'database' }) // Note #1
const db = new Database(adapter, { useTabulation: new JSONFormatter({ whitespace: "\t" }) })

module.exports.run = async(client, interaction) => {

    try {
        const user = interaction.options.getMember("member");
        const bio = (db.check("account", { key: `users.${interaction.user.id}.bio` })) ? (db.read("account", { key: `users.${interaction.user.id}.bio` })) : ("Отсутствует");

        const embed = new EmbedBuilder();
        embed.setTitle(emoji.user + " | Информация об " + user.username);
        embed.setDescription("О себе: ```" + bio + "```");
        embed.addFields([
            { name: `Имя польвазотеля:`, value: user.username },
            { name: `Статус`, value: `` },
            { name: `Присоединился`, value: `` },
            { name: `Дата регистрации`, value: `` },
        ]);
        embed.setColor(color.main);
        embed.setFooter({ text: `User ID: ${user.id}`, iconURL: user.avatar });


        const button = new ActionRowBuilder()
            .addComponents(
                new ButtonBuilder()

            )

        await interaction.reply({ embeds:[embed], components:[button], ephemeral: true });

    } catch(error) {
        console.log(error);
    }
}