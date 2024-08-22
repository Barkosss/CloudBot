const { EmbedBuilder, ButtonBuilder, SelectMenuBuilder, ActionRowBuilder, ModalBuilder, TextInputBuilder, TextInputStyle } = require('discord.js');
const timestamp = require("discord-timestamp");

const { Database, LocalStorage, JSONFormatter } = require("moonlifedb");
const adapter = new LocalStorage({ path: 'database' }) // Note #1
const db = new Database(adapter, { useTabulation: new JSONFormatter({ whitespace: "\t" }) })

module.exports.run = async(client, interaction) => {

    try {
        const text = db.read("system", { key: `text.kick` });
        const emoji = db.read("system", { key: `emoji` });
        const color = db.read("system", { key: `color` });

        const member = interaction.options.getMember("member");
        const reason = interaction.options.getString("reason");

        const embedUser = new EmbedBuilder();
        embedUser.setTitle(emoji.kick + " | Вас кикнули с сервера");
        embedUser.setDescription("")
        if (reason) embedUser.addFields([{ name: `Причина:`, value: `` }]);
        embedUser.setColor(emoji.main)

        const buttonUser = new ActionRowBuilder()
            .addComponents(
                new ButtonBuilder()
                    .setStyle("SECONDARY")
                    .setLabel(" | Отправлено с сервера " + `"${interaction.guild.name}"`)
                    .setEmoji(emoji.mail)
                    .setDisabled(true)
            )
            .addComponents(
                new ButtonBuilder()
                    .setStyle("LINK")
                    .setLabel(" | Сервер поддержки")
                    .setEmoji(emoji.support)
                    .setURL(text.supportServerURL)
            );

        await member.send({ embeds:[embedUser], components:[buttonUser] });

        await member.kick(reason).then(async() => {

            const embedAdmin = new EmbedBuilder();
            embedAdmin.setTitle(emoji.kick + " | ");
            embedAdmin.setDescription("");
            embedAdmin.setColor(color.main);

            await interaction.reply({ embeds:[embedAdmin], ephemeral: true });

        }).catch(async(err) => {

            await interaction.reply({ content: `Произошла какая-та ошибка: ${err}`, ephemeral: true });
        });

    } catch(error) {
        console.log(error);
    }
}