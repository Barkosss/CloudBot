const { EmbedBuilder, ButtonBuilder, ActionRowBuilder, ModalBuilder, TextInputBuilder } = require('discord.js');
const timestamp = require("discord-timestamp");

const { Database, LocalStorage, JSONFormatter } = require("moonlifedb");
const adapter = new LocalStorage({ path: 'database' }) // Note #1
const db = new Database(adapter, { useTabulation: new JSONFormatter({ whitespace: "\t" }) })

module.exports.run = async(client, interaction) => {
    try {
        const emoji = db.read("system", { key: "emoji" });
        const color = db.read("system", { key: "color" });
        const text = db.read("system", { key: "text" });

        if (interaction.isButton()) {
            const section = interaction.customId.split("_")[1];

            switch(section) {
                // Верификация
                case "verify": {
                    const action = interaction.customId.split("_")[2];

                    switch(action) {

                        case "start": {

                            break;
                        }
                    }
                    break;
                }

                // Регистрация
                case "registration": {
                    const action = interaction.customId.split("_")[2];

                    switch(action) {

                        // Создать заявку
                        case "createOrder": {
                            const modal = new ModalBuilder({
                                customId: "auth_registration_createOrder",
                                title: "Создание заявки"
                            });

                            const userAge = new TextInputBuilder({
                                customId: "age",
                                label: "Укажите возраст",
                                style: "SHORT",
                                maxLength: 3,
                                required: true
                            });

                            const userExp = new TextInputBuilder({
                                customId: "exp",
                                label: "Расскажите о своём RP опыте",
                                style: "PARAGRAPH",
                                maxLength: 1024,
                                required: true
                            });

                            const userSteamId = new TextInputBuilder({
                                customId: "steamId",
                                label: "Укажите ваш Steam ID",
                                style: "SHORT",
                                minLength: 19,
                                maxLength: 22,
                                required: true
                            });

                            const userAgeRow = new ActionRowBuilder().addComponents(userAge);
                            const userExpRow = new ActionRowBuilder().addComponents(userExp);
                            const userSteamIdRow = new ActionRowBuilder().addComponents(userSteamId);
                            modal.addComponents(userAgeRow, userExpRow, userSteamIdRow);
                            await interaction.showModal(modal);
                            break;
                        }

                        // Одобрить заявку
                        case "acceptedOrder": {

                            break;
                        }

                        // Отклонить заявку
                        case "rejectOrder": {

                            break;
                        }

                        // Заблокировать пользователя
                        case "blockUser": {

                            break;
                        }

                        //  Добавить комментарий к заявке
                        case "addComments": {

                            break;
                        }

                        // Читать комментарии к заявке
                        case "readComments": {

                            break;
                        }

                        // Создать дискуссию с пользователем
                        case "createDiscussion": {

                            break;
                        }

                        // Присоединиться к дискуссиии с пользователем
                        case "joinDiscussion": {

                            break;
                        }
                    }
                    break;
                }
            }

            return;
        }

        if (interaction.isModalSubmit()) {
            const action = interaction.customId.split("_")[2];

            switch(action) {

                case "createOrder": {

                    const userAge = interaction.fields.getTextInputValue("age");
                    const userExp = interaction.fields.getTextInputValue("exp");
                    const userSteamId = interaction.fields.getTextInputValue("steamId");

                    // ...

                    db.edit("account", { key: ``, value: {
                            "userAge": userAge,
                            "userExp": userExp,
                            "userSteamId": userSteamId
                        }, newline: true
                    }).catch(error => console.log(error));

                    const embed = new EmbedBuilder()
                    embed.setTitle(emoji.registration + "| Подтверждение данных:");
                    embed.setDescription(text.registration.acceptedUserOrder);
                    embed.addFields([
                        { name: `Возраст:`, value: "```" + userAge + "```" },
                        { name: `Опыт в Role-Play`, value: "```" + userExp + "```" },
                        { name: `Steam ID:`, value: "```" + userSteamId + "```" },
                        //{ name: ``, value: `` },
                        //{ name: ``, value: `` },
                    ]);
                    embed.setFooter({ text: `После подтверждения изменить данные уже не получится` });
                    embed.setColor(color.main);

                    const button = new ActionRowBuilder()
                        .addComponents(
                            new ButtonBuilder()
                                .setStyle("SUCCESS")
                                .setCustomId("auth_registration_acceptedUserOrder")
                                .setLabel("| Подтвердить")
                                .setEmoji(emoji.accepted)
                        )
                        .addComponents(
                            new ButtonBuilder()
                                .setStyle("SUCCESS")
                                .setCustomId("auth_registration_acceptedUserOrder")
                                .setLabel("| Подтвердить")
                                .setEmoji(emoji.reject)
                        )

                    await interaction.reply({ embeds:[embed], components:[button], ephemeral: true });
                    break;
                }
            }
            return;
        }

        if (interaction.isSelectMenu()) {


            return;
        }


    } catch (error) {
        console.log(error);
    }
}