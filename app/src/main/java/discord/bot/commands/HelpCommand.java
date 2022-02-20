package discord.bot.commands;

import java.util.ArrayList;
import java.util.List;

import discord.bot.HelpSupport;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class HelpCommand extends BotCommand {

    private final String commandKeyword = "help";
    private List<HelpSupport> avilableCommands = new ArrayList<HelpSupport>();
    public HelpCommand(List<Object> registeredCommands) {
        for (Object command : registeredCommands) {
            if (command instanceof HelpSupport) avilableCommands.add((HelpSupport) command);
        }
    }
    
    public void onMessageReceived(MessageReceivedEvent event) {

        String msg = event.getMessage().getContentDisplay().toLowerCase().strip();

        if (msg.equals(commandPrefix + commandKeyword)) {
            String message = getHelpMessage();
            event.getChannel().sendMessage(message).queue();
        } else if (msg.indexOf(commandPrefix + commandKeyword) == 0) {
            String[] args = msg.split("\\s+", 2);

            HelpSupport commandHelp = getCommandWithName(args[1]);
            if (commandHelp == null) {
                event.getChannel().sendMessageFormat("Brak wsparcia `.help` dla komendy `%s`", args[1]).queue();
                return;
            }

            String commandHelpAsString = "`" + commandHelp.commandUsage() + "`\n\n" + commandHelp.helpMessage();
            event.getChannel().sendMessage(commandHelpAsString).queue();
        }
    }

    private String getHelpMessage() {
        var messageBuilder = new StringBuilder();
        messageBuilder.append("Oto dostepne komendy:\n");
        for (HelpSupport command : avilableCommands) {
            messageBuilder.append("`" + commandPrefix + command.commandName() + "`");
            messageBuilder.append("\n");
        }
        messageBuilder.append("Zeby dowiedziec sie wiecej o komendzie, uzyj `" + commandPrefix + commandKeyword + " <nazwa komendy>`");
        return messageBuilder.toString();
    }

    private HelpSupport getCommandWithName(String commandName) {
        for (HelpSupport command : avilableCommands) {
            if (command.commandName().equals(commandName)) return command;
        }
        return null;
    }
}
