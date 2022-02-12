package discord.bot;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.security.auth.login.LoginException;

import discord.bot.commands.*;
import discord.bot.commands.voting.KickVotingCommand;
import net.dv8tion.jda.api.*;

public class App
{
    private static List<Object> registeredCommands = new ArrayList<>();

    private static JDA jda;

    private static String token;

    public static void main(String[] args) throws LoginException, InterruptedException
    {
        try {
            token = Files.readString(Paths.get("token"));
        } catch (IOException e) {
            token = "";
        }

        registerCommand(new ILoveJavaCommand());
        // registerCommand(new DeleteIllegalNumberCommand());
        registerCommand(new KickVotingCommand());
        registerCommand(new RollCommand());
        registerCommand(new SnipeCommand());
        registerCommand(new ReactOnReadyCommand());

        buildJDA();
    }

    public static void buildJDA() throws LoginException, InterruptedException {
        // Note: It is important to register your ReadyListener before building
        jda = JDABuilder.createDefault(token)
            .addEventListeners(registeredCommands.toArray())
            .build();

        // optionally block until JDA is ready
        jda.awaitReady();
    }

    public static void registerCommand(Object commandToRegister) {
        if (getRegisteredInstance(commandToRegister) == null) registeredCommands.add(commandToRegister);
    }

    public static void unregisterCommand(Object commandToRegister) {
        Object registeredInstance = getRegisteredInstance(commandToRegister);
        if (registeredInstance != null) registeredCommands.remove(registeredInstance);
    }

    private static Object getRegisteredInstance(Object commandToRegister) {
        return registeredCommands.stream().filter(o -> o.getClass().equals(commandToRegister.getClass())).findAny().orElse(null);
    }
}