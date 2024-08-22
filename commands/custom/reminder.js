const { EmbedBuilder, ButtonBuilder, SelectMenuBuilder, ActionRowBuilder, ModalBuilder, TextInputBuilder, TextInputStyle } = require('discord.js');
const timestamp = require("discord-timestamp");

const { Database, LocalStorage, JSONFormatter } = require("moonlifedb");
const adapter = new LocalStorage({ path: 'database' }) // Note #1
const db = new Database(adapter, { useTabulation: new JSONFormatter({ whitespace: "\t" }) })

// Convert String to Number (Timestamp)
function convertTime(times) {

    let timer = 0;
    if (times.split(' ').length === 2) { // Если время указано в формате - ЧЧ:ММ:СС ДД:ММ:ГГ (примерно)
        const Data = new Date();
        const date = (times.split(' ')[0]).split('.');
        const year = date[2]; const month = date[1]; const day = date[0];
        const time = (times.split(' ')[1]).split(':');
        const hour = time[0]; const minute = time[1];

        if ((1 > month || month > 12) || (1 > day || day > 31) || (0 > hour || hour > 24) || (0 > minute || minute > 60)) {
            return -1;
        }

        if (year < Data.getFullYear() || month-1 < Data.getMonth() || day < Data.getDate()) {
            return -2;
        }

        timer = timestamp(new Date(year, month - 1, day, hour, minute));

    } else if (times.split(' ').length === 1) {

        const map = {
            1000: ['s', 'sec', 'secs', 'second', 'seconds', 'с', 'сек', 'секунда', 'секунду', 'секунды', 'секунд'],
            60000: ['m', 'min', 'minute', 'minutes', 'м', 'мин', 'минута', 'минуту', 'минуты', 'минут'],
            3600000: ['h', 'hour', 'hours', 'ч', 'час', 'часа', 'часов'],
            86400000: ['d', 'day', 'days', 'д', 'день', 'дня', 'дней'],
            604800000: ['w', 'week', 'weeks', 'н', 'нед', 'неделя', 'недели', 'недель', 'неделю'],
            2592000000: ['mo', 'mos', 'month', 'months', 'мес', 'месяц', 'месяца', 'месяцев'],
            31536000000: ['y', 'year', 'years', 'г', 'год', 'года', 'лет']
        }

        const numbers = times.match(/\d+/g);
        const words = times.match(/\D+/g);

        let index = 0;
        words.forEach(item => {
            for (let key in map) {
                let value = map[key];
                if (value.includes(item)) {
                    timer = timer + key * (numbers[index]);
                    break;
                }
            }
            index++;
        });

        timer = timestamp(Date.now()) + Math.ceil(timer / 1000);
    } else {
        return -3;
    }

    return timer;
}

// Error checking
async function checkError(timer){
    if (timer === -1) {
        await interaction.reply({ content: text.error.notCorrectFormatData, ephemeral: true });
    } else if (timer === -2) {
        await interaction.reply({ content: text.error.notFutureDate, ephemeral: true });
    } else if (timer === -3) {
        await interaction.reply({ content: text.error.notCorrectFormat, ephemeral: true });
    }
}

module.exports.run = async(client, interaction) => {
    try {
        const text = db.read("system", { key: `text.reminder` });
        const emoji = db.read("system", { key: `emoji` });
        const color = db.read("system", { key: `color` });

        if (interaction.isButton()) {
            const modal = new ModalBuilder({
                customId: "reminder_create",
                title: "Создание напоминания"
            });

            const content = new TextInputBuilder({
                customId: "content",
                label: "Укажите содержимое",
                style: TextInputStyle.Paragraph,
                maxLength: 256,
                required: true,
            })

            const duration = new TextInputBuilder({
                customId: "duration",
                label: "Укажите время напоминания",
                style: TextInputStyle.Short,
                minLength: 2,
                required: true,
            })

            const contentRow = new ActionRowBuilder().addComponents(content);
            const durationRow = new ActionRowBuilder().addComponents(duration);
            modal.addComponents(contentRow, durationRow);
            await interaction.showModal(modal);
            return;
        }

        const section = (interaction.customId) ? (interaction.customId.split('_')[1]) : (interaction.options._subcommand);

        switch(section) {

            // Create reminder
            case "create": {
                const times = ((interaction.options) ? (interaction.options.getString("duration")) : (interaction.fields.getTextInputValue("duration"))).replace(/^\s\s*/, '').replace(/\s\s*$/, '');
                const content = ((interaction.options) ? (interaction.options.getString("content")) : (interaction.fields.getTextInputValue("content")));
                const timer = convertTime(times);

                // Error checking
                if (timer < 0) {
                    return checkError(timer);
                }

                const count = ((db.check("account", { key: `users.${interaction.user.id}.userCountReminders` })) ? (db.read("account", { key: `users.${interaction.user.id}.userCountReminders` })) : (0)) + 1;
                console.log(count);
                console.log(interaction.user.id);
                db.edit("account", { key: `users.${interaction.user.id}.userCountReminders`, value: count, newline: true }).catch(err => console.log(err));
                db.edit("reminder", {
                    key: `reminders.${interaction.user.id}.#${count}`, value: {
                        "content": content,
                        "timer": timer,
                        "createdAt": timestamp(Date.now()),
                        "lastUpdate": false
                    }, newline: true
                }).catch(err => console.log(err));

                const embed = new EmbedBuilder();
                embed.setTitle(emoji.reminder + ` | Напоминание #${count}:`)
                embed.addFields([
                    { name: `Дата:`, value: `> <t:${timer}:f> (<t:${timer}:R>)` },
                    { name: `Содержание:`, value: "```" + content + "```" },
                ])
                embed.setFooter({ text: `Действие: Создание` })
                embed.setColor(color.main)

                await interaction.reply({ embeds:[embed], ephemeral: true });
                break;
            }

            // Edit reminder by index
            case "edit": {
                const index = interaction.options.getNumber("index");
                const data = db.read("reminder", { key: `reminders.${interaction.user.id}.#${index}` });
                const newContent = interaction.options.getString("content");
                const newTimes = interaction.options.getString("duration");

                if (!newContent && !newTimes) {
                    return await interaction.reply({ content: text.error.notEditData, ephemeral: true });
                }

                // If a new date is specified
                let timer;
                if (newTimes) {
                    timer = convertTime(newTimes.replace(/^\s\s*/, '').replace(/\s\s*$/, ''));

                    // Error checking
                    if (timer < 0) {
                        return checkError(timer);
                    }

                    db.edit("reminder", { key: `reminders.${interaction.user.id}.#${index}.timer`, value: timer }).catch(err => console.log(err));
                }

                // If a new content is specified
                if (newContent !== data.content) {
                    db.edit("reminder", { key: `reminders.${interaction.user.id}.#${index}.content`, value: newContent }).catch(err => console.log(err));
                }

                db.edit("reminder", { key: `reminders.${interaction.user.id}.#${index}.lastUpdate`, value: timestamp(Date.now()) }).catch(err => console.log(err));

                const embed = new EmbedBuilder();
                embed.setTitle(emoji.reminder + ` | Напоминание изменено #${index}:`);
                if (timer && timer !== data.timer) {
                    embed.addFields([{ name: "Старое время:", value: `> <t:${data.timer}:f> (<t:${data.timer}:R>)` }]);
                    embed.addFields([{ name: "Новое время:", value: `> <t:${timer}:f> (<t:${timer}:R>)` }]);
                }
                if (newContent && newContent !== data.content) {
                    embed.addFields([{ name: "Старое содержимое:", value: "```" + data.content + "```" }]);
                    embed.addFields([{ name: "Новое содержимое:", value: "```" + newContent + "```" }]);
                }
                embed.setFooter({ text: `Действие: Изменение` });
                embed.setColor(color.main);

                await interaction.reply({ embeds:[embed], ephemeral: true });
                break;
            }

            // remove reminder by index
            case "remove": {
                const index = interaction.options.getNumber("index");

                if (!db.read("account", { key: `users.${interaction.user.id}.userCountReminders` })) {
                    return await interaction.reply({ content: text.error.notFound, ephemeral: true });
                }

                const count = db.read("account", { key: `users.${interaction.user.id}.userCountReminders` }) - 1;
                db.edit("account", { key: `users.${interaction.user.id}.userCountReminders`, value: count }).catch(err => console.log(err));
                db.edit("reminder", { key: `reminders.${interaction.user.id}.#${index}`, value: undefined }).catch(err => console.log(err));

                const embed = new EmbedBuilder();
                embed.setTitle(emoji.reminder + " | Список напоминаний:");
                embed.setDescription(text.remove.description);
                embed.setFooter({ text: `Действие: Удаление` });
                embed.setColor(color.main)

                await interaction.reply({ embeds:[embed], ephemeral: true });
                break;
            }

            // Viewing a list of reminders
            case "list": {
                const data = db.read("reminder", { key: `reminders.${interaction.user.id}` });

                const embed = new EmbedBuilder();
                embed.setTitle(emoji.reminder + " | Список напоминаний:");
                if (Object.keys(data).length) {

                    for(let index in data) {
                        const content = data[index].content;
                        const timer = data[index].timer;

                        embed.addFields([
                            { name: `${index} Напоминание:`, value: `- **Дата:**\n> <t:${timer}:f> (<t:${timer}:R>)\n- **Содержимое:** \`\`\`${content}\`\`\`` }
                        ])
                    }

                } else {
                    embed.setDescription("```Напоминания отсутствуют```")
                }
                embed.setFooter({ text: `Действие: Просмотр` });
                embed.setColor(color.main);

                await interaction.reply({ embeds:[embed], ephemeral: true });
                break;
            }
        }

    } catch(error) {
        console.log(error);
    }
}