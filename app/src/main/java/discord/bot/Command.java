package discord.bot;

import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Command extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String msg = event.getMessage().getContentDisplay();

        if (msg.contains("java") && !event.getAuthor().isBot()) {
            event.getChannel()
                    .sendMessage("kocham jave kocham jave kocham jave <:javaSocialCredits:901211051369054208>").queue();
        }
    }

    @Override
    public void onReady(ReadyEvent event) {
        System.out.println("API is ready!");
    }
}
