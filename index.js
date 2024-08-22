const { Client, GatewayIntentBits, Collection, Partials} = require("discord.js");
const client = new Client({
    intents: [
        GatewayIntentBits.Guilds,
        GatewayIntentBits.MessageContent,
        GatewayIntentBits.DirectMessages,
        GatewayIntentBits.GuildMembers,
        GatewayIntentBits.GuildMessages
    ],
    partials: [
        Partials.Channel,
        Partials.GuildMember,
        Partials.Message,
        Partials.User
    ]
});

const config = require("./config.json");
const fs = require("fs");
const timestamp = require("discord-timestamp");

const { Database, LocalStorage, JSONFormatter } = require("moonlifedb");
const adapter = new LocalStorage({ path: 'database' }) // Note #1
const db = new Database(adapter, { useTabulation: new JSONFormatter({ whitespace: "\t" }) })


client.commands = new Collection();
const utilityCommands = fs.readdirSync(__dirname + "/commands/utility/").filter(file => file.endsWith(".js"));
const moderationCommands = fs.readdirSync(__dirname + "/commands/moderation/").filter(file => file.endsWith(".js"));
const customCommands = fs.readdirSync(__dirname + "/commands/custom/").filter(file => file.endsWith(".js"));
const systemCommands = fs.readdirSync(__dirname + "/system/").filter(file => file.endsWith(".js"));
let isConnected = false;
client.login(config.token).catch(err => console.log(err));


client.on("ready", async() => {
    try {
        for(let file of utilityCommands) {
            const commandName = file.split(".")[0];
            const command = require(`./commands/utility/${commandName}`);
            client.commands.set(commandName, command);
        }
        for(let file of moderationCommands) {
            const commandName = file.split(".")[0];
            const command = require(`./commands/moderation/${commandName}`);
            client.commands.set(commandName, command);
        }
        for(let file of customCommands) {
            const commandName = file.split(".")[0];
            const command = require(`./commands/custom/${commandName}`);
            client.commands.set(commandName, command);
        }
        for(let file of systemCommands) {
            const commandName = file.split(".")[0];
            const command = require(`./system/${commandName}`);
            client.commands.set(commandName, command);
        }

        isConnected = true;
        console.log(`${client.user.username} is ready!`);
        setInterval(() => {
            return client.commands.get("autofill").run(client);
        }, 3000)

    } catch(err) {
        console.log(err);
    }
});


client.on("interactionCreate", async(interaction) => {
    try {

        if (!isConnected) return;
        var command = client.commands.get(interaction.commandName)

        if (interaction.guild && !interaction.customId) command.run(client, interaction);

        else if (interaction.isButton()) {
            command = client.commands.get(interaction.customId.split('_')[0])
            return command.run(client, interaction, 'button', interaction.customId)
        }
        else if (interaction.isStringSelectMenu()) {
            command = client.commands.get(interaction.customId.split('_')[0])
            return command.run(client, interaction, 'selectMenu', interaction.customId)
        }
        else if (interaction.isModalSubmit()) {
            command = client.commands.get(interaction.customId.split('_')[0])
            return command.run(client, interaction, 'modal', interaction.customId)
        } else if (interaction.isUserContextMenuCommand() || interaction.isMessageContextMenuCommand()) {
            command = client.commands.get(interaction.customId.split('_')[0])
            return command.run(client, interaction, 'contextMenu', interaction.customId)
        }

    } catch(err) {
        console.log(err);
    }
})