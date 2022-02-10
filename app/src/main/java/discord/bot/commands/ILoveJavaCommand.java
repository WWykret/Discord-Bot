package discord.bot.commands;

import discord.bot.Emojis;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class ILoveJavaCommand extends BotCommand {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();

        boolean containsKeyword = message.toLowerCase().contains("java");
        boolean sentByHuman = !event.getAuthor().isBot();
        if (containsKeyword && sentByHuman) {
            event.getChannel()
                    .sendMessage("kocham jave kocham jave kocham jave " + Emojis.JAVA_SOCIAL_CREDITS).queue();
        }
    }
}
