package discord.bot.commands;

import discord.bot.Emojis;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class DeleteIllegalNumberCommand extends BotCommand {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();

        boolean containsForbiddenNumber = message.replaceAll("\\s+", "").contains("31");
        boolean sentByHuman = !event.getAuthor().isBot();
        if (containsForbiddenNumber && sentByHuman) {
            event.getChannel()
                    .sendMessageFormat("wykryto i usunieto nielegalny numer %s", Emojis.ALBANIAN_WAR_CRIMES).queue();
            event.getMessage().delete().queue();
        }
    }
}
