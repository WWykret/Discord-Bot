package discord.bot.commands;

import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class BotCommand extends ListenerAdapter {
    
    protected String commandPrefix = ".";

    @Override
    public void onReady(ReadyEvent event) {
        System.out.println("Command Loaded: " + this.getClass().getName());
    }
}
