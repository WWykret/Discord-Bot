package discord.bot.commands;

import net.dv8tion.jda.api.events.ReadyEvent;

public class ReactOnReadyCommand extends BotCommand {
    @Override
    public void onReady(ReadyEvent event) {
        super.onReady(event);
        System.out.println("API is ready!");
    }
}
