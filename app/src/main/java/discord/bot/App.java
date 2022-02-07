package discord.bot;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.events.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;

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

        // Note: It is important to register your ReadyListener before building
        JDA jda = JDABuilder.createDefault(token)
            .addEventListeners(new Command())
            .build();

        // optionally block until JDA is ready
        jda.awaitReady();
    }
}