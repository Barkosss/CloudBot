const { EmbedBuilder, ButtonBuilder, ButtonStyle, ActionRowBuilder } = require('discord.js');
const timestamp = require("discord-timestamp");

const { Database, LocalStorage, JSONFormatter } = require("moonlifedb");
const adapter = new LocalStorage({ path: 'database' }) // Note #1
const db = new Database(adapter, { useTabulation: new JSONFormatter({ whitespace: "\t" }) })

module.exports.run = async(client) => {
    try {
        const emoji = db.read("system", { key: "emoji" });
        const color = db.read("system", { key: "color" });

        // Reminder System
        if (Object.keys(db.read("reminder", { key: `reminders` })).length) {
            const text = db.read("system", { key: "text.reminder" });

            const now = timestamp(Date.now());
            const reminders = db.read("reminder", { key: `reminders` });

            for(let userID in reminders) {

                for(let index in reminders[userID]) {

                    const content = reminders[userID][index].content;
                    const timer = reminders[userID][index].timer;
                    const lastUpdate = reminders[userID][index].lastUpdate;
                    const createdAt = reminders[userID][index].createdAt;

                    if (timer > now) break;

                    const embed = new EmbedBuilder();
                    embed.setTitle(emoji.reminder + " | Напоминание:");
                    embed.addFields([{ name: `Содержимое:`, value: "```" + content + "```" }]);
                    if (lastUpdate) embed.addFields([{ name: `Последнее изменение:`, value: `- <t:${lastUpdate}:t> (<t:${lastUpdate}:R>)` }]);
                    embed.addFields([{ name: `Дата создания:`, value: `- <t:${createdAt}:f> (<t:${createdAt}:R>)` }]);
                    embed.setColor(color.main);

                    const button = new ActionRowBuilder()
                        .addComponents(
                            new ButtonBuilder()
                                .setStyle(ButtonStyle.Primary)
                                .setCustomId("reminder_create")
                                .setLabel(`| ${text.create.button}`)
                                .setEmoji(emoji.timetable)
                        )

                    const user = await client.users.fetch(userID);
                    user.send({ embeds:[embed], components:[button] }).then(() => {
                        db.edit("reminder", { key: `reminders.${userID}.${index}`, value: undefined });

                        if (!Object.keys(db.read('reminder', { key: `reminders.${userID}` })).length)
                            db.edit('reminder', { key: `reminders.${userID}`, value: undefined });

                        let count = db.read("account", { key: `users.${userID}.userCountReminders` }) - 1;
                        db.edit("account", { key: `users.${userID}.userCountReminders`, value: count });

                    }).catch((error) => {
                        if (error.code === 50007) { // DiscordAPIError: Cannot send messages to this user
                            console.log(`Cannot send message to user ${user.tag}: DMs are closed.`);
                        } else {
                            console.log(`Failed to send DM to user ${user.tag}:`, error);
                        }
                    })
                }
            }
        }

    } catch(error) {
        console.log(error);
    }
};