# Discord Java Bot

Simple java bot for discord supporting various functionality

## Table of contents
* [General info](#general-info)
* [Implementation ideas](#implementation-ideas)
* [Run](#run)
* [Technologies](#technologies)
* [Currently avilable commands](#currently-avilable-commands)
* [Help command convention](#help-command-convention)
* [Status](#status)

## General info

This bot allows reacts on various commands and events on discord server.

This application was created mainly as fun project for my personal discord server, so messages sent by bot and emoticons are
mainly written in polish and specific to my server. Feel free to copy and modify this bot as you see fit.

## Implementation ideas

The application consists of core that connects to discord and multiple indepentent commands that can be easily enabled or disabled
if need be. My main goal was to create a bot that is easily costumizable and easy to extend by new functionalities. 

## Run
To run this bot you need to have gradle and Java 17 installed.

To get started with my program, navigate to a directory where you want to use the project, then clone it with:
```bash
git clone https://github.com/WWykret/Discord-Bot.git
```
or
```bash
git clone git@github.com:WWykret/Discord-Bot.git
```
If you don't want to use git clone for whatever reason, you can manually download it, and move the folder somewhere convenient.


Next step is to add enviroment variable `TOKEN` to value generated from [Discord Developer Portal](https://discordapp.com/developers/applications/)
You can do it with command
```bash
export TOKEN=<token from Discord Developer Portal>
```

Finnaly, open up your terminal, and go to the correct directory. Then build the program
```bash
gradle stage
```

And run it
```bash
java -jar app/build/libs/app-1.0-all.jar
```

## Technologies

The program was written in Java using Java Discord API for communication with discord.

It additionally uses
- [D&D 5e API](https://www.dnd5eapi.co/) for getting information about D&D items and spells
- [Meme API](https://github.com/D3vd/Meme_Api) for getting images from reddit
- Jackson JSON processor for managing json

## Currently avilable commands
List bellow includes all commands that are avilable in current version of the application

- `Delete Illegal Number` - deletes all messages that contain number `31`
- `Hangman` - allows server users to play hangman game with bot (list of words in file `hangman-words`)
- `Help` - displays help informations about other commands (Comands need to be configured to be seen by help; Should be loaded after all commands that support help)
- `I Love Java` - everytime there is `java` keyword in messsage bot says that it loves java
- `Reminder` - allows users to set reminders that bot will keep track of
- `React On Ready` - after connecting to discord bot displays on console that it is ready (Should be loaded last)
- `Roll` - allows users to roll a different kind of dice
- `Snipe` - allows users to display last deleted message on the channel
- `Kick Voting` - allows moderation to create voting whether or not kick someone from the server
- `Meme` - displays random meme from reddit
- `Spell` - retrives info about spells from D&D 5e API
- `Gear` - retrives info about equipment from D&D 5e API

## Help command convention
Messages generated with `Help` command follow the following convention
- `[a|b|c]` - exacly one of items `a` or `b` or `c` 
- `[1-50]` - exacly one integer from range `0` to `50`
- `{a|b|c}` - exacly one of items `a` or `b` or `c` or nothing
- `{1-50}` - exacly one integer from range `0` to `50` or nothing
- `<something>` - whatever is written inside `<>`
- `{something}` - whatever is written inside `{}` or nothing

## Status

I am currently working on this project.