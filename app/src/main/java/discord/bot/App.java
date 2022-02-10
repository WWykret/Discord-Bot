package discord.bot;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.security.auth.login.LoginException;

import discord.bot.commands.*;
import discord.bot.commands.voting.KickVotingCommand;
import net.dv8tion.jda.api.*;

public class App
{
    public static void main(String[] args) throws LoginException, InterruptedException
    {
        String token;
        try {
            token = Files.readString(Paths.get("token"));
        } catch (IOException e) {
            token = "";
        }

        //Registered commands       
        Object[] registeredCommands = {
            new ILoveJavaCommand(),
            new DeleteIllegalNumberCommand(),
            new KickVotingCommand(),
            new RollCommand(),
            new ReactOnReadyCommand()
        };

        // Note: It is important to register your ReadyListener before building
        JDA jda = JDABuilder.createDefault(token)
            .addEventListeners(registeredCommands)
            .build();

        // optionally block until JDA is ready
        jda.awaitReady();
    }
}