const { EmbedBuilder, ButtonBuilder, ActionRowBuilder } = require('discord.js');
const timestamp = require("discord-timestamp");

const { Database, LocalStorage, JSONFormatter } = require("moonlifedb");
const adapter = new LocalStorage({ path: 'database' }) // Note #1
const db = new Database(adapter, { useTabulation: new JSONFormatter({ whitespace: "\t" }) })

module.exports.run = async(client, interaction) => {
    try {
        const emoji = db.read("system", { key: "emoji" });
        const color = db.read("system", { key: "color" });

        const section = interaction.options.getString("section");
        switch(section) {

            case "verify": {
                const embed = new EmbedBuilder();
                embed.setTitle(emoji.verify + " | Верификация пользователя");
                embed.setDescription("Чтобы подать заявку на вступление, необходимо для начала пройти верификацию");
                embed.setColor(color.green);

                const button = new ActionRowBuilder()
                    .addComponents(
                        new ButtonBuilder()
                            .setStyle("SUCCESS")
                            .setCustomId("auth_verify_start")
                            .setLabel("| Верифицироваться")
                            .setEmoji(emoji.verify)
                    )

                await interaction.channel.send({ embeds:[embed], components:[button] });
                await interaction.reply({ content: `Success!`, ephemeral: true });
                break;
            }

            case "registration": {
                const embed = new EmbedBuilder();
                embed.setTitle(emoji.registration + " | Регистрация");
                embed.setDescription("Чтобы получить полноценный доступ, необходимо пройти регистрацию. Она состоит их пяти простых вопросов.");
                embed.addFields("График рассмотрения заявок:", "С <t:1722409200:t> до <t:1722438000:t>", false);
                embed.setColor(color.green);

                const button = new ActionRowBuilder()
                    .addComponents(
                        new ButtonBuilder()
                            .setStyle("SUCCESS")
                            .setCustomId("auth_registration_createOrder")
                            .setLabel("| Подать заявку")
                            .setEmoji(emoji.registration)
                    )

                await interaction.channel.send({ embeds:[embed], components:[button] });
                await interaction.reply({ content: `Success!`, ephemeral: true });
                break;
            }

            case "status": {
                const embed = new EmbedBuilder();
                embed.setTitle(emoji.status + " | Активность сервера");
                embed.setDescription("");
                embed.addFields([
                    { name: "Название сервера:", value: "```" + db.read("system", { key: "text.serverStatus.serverName" }) + "```" },
                    { name: "Запустил:", value: "`Никто`" },
                    { name: "Пароль:", value: "`Отсутствует`" },
                    { name: "Дополнительная информация:", value: "```" + "Отсутствует" + "```" },
                    { name: "Последнее действие:", value: "<t:" + timestamp(Date.now()) + ":R>"  }
                ])
                embed.setColor(color.green);

                const button = new ActionRowBuilder()
                    .addComponents(
                        new ButtonBuilder()
                            .setStyle("SUCCESS")
                            .setCustomId("statusserver_openServer")
                            .setLabel("| Открыть Сервер")
                            .setEmoji(emoji.open)
                    )
                    .addComponents(
                        new ButtonBuilder()
                            .setStyle("SECONDARY")
                            .setCustomId("statusserver_getTiming")
                            .setLabel("| Расписание")
                            .setEmoji(emoji.timetable)
                    )

                const buttonAnnounce = new ActionRowBuilder()
                    .addComponents(
                        new ButtonBuilder()
                            .setStyle("PRIMARY")
                            .setCustomId("statusserver_setAnnouncement")
                            .setLabel("| Получать уведомления")
                            .setEmoji(emoji.bell)
                    )

                await interaction.channel.send({ embeds:[embed], components:[button, buttonAnnounce] });
                await interaction.reply({ content: `Success!`, ephemeral: true });
                break;
            }
        }

    } catch(err) {
        console.log(err);
    }
}