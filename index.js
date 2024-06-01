const { Client, Intents, Collection } = require('discord.js')

const client = new Client(
    {
        intents: [
            Intents.FLAGS.GUILDS,
            Intents.FLAGS.GUILD_MESSAGES,
            Intents.FLAGS.MESSAGE_CONTENT,
            Intents.FLAGS.GUILD_MEMBERS,
            Intents.FLAGS.GUILD_PRESENCES,
            Intents.FLAGS.GUILD_VOICE_STATES,
            Intents.FLAGS.DIRECT_MESSAGES
        ],
        partials: [
            "CHANNEL",
            "GUILD_MEMBER",
            "MESSAGE",
            "USER"
        ]
    }
);

const config = require('./config.json');
const fs = require('fs');
const timestamp = require('discord-timestamp');
const chalk = require('chalk');

// Datebase
const { Database, LocalStorage, JSONFormatter, Snowflake } = require('moonlifedb');
const db = new Database(new LocalStorage({ path: 'database' }), { useTabulation: new JSONFormatter({ whitespace: "\t" }) })


client.commands = new Collection();
const commands = fs.readdirSync(__dirname).filter(file => file.endsWith('.js'))
var isConnected = false;
client.login(config.token);

client.once('ready', async() => {
    try {

    } catch(error) {
        // ...
    }
});


client.on('interactionCreate', async(client, interaction) => {
    try {

    } catch(error) {
        // ...
    }
});