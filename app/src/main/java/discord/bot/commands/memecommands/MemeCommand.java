package discord.bot.commands.memecommands;

import discord.bot.CommandPermissions;
import discord.bot.HelpSupport;
import discord.bot.CommandPermissions.Permission;
import discord.bot.commands.BotCommand;
import discord.bot.commands.memecommands.memeapi.MemeAPIController;
import discord.bot.commands.memecommands.memeapi.models.MemeData;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class MemeCommand extends BotCommand implements HelpSupport {
    public void onMessageReceived(MessageReceivedEvent event) {

        String msg = event.getMessage().getContentDisplay().toLowerCase().strip();

        if (msg.indexOf(commandPrefix + "meme") == 0) {
            if (!CommandPermissions.hasPermission(event.getGuild(), event.getMember(), Permission.USER)) {
                CommandPermissions.sendLackOfAccessMsg(event.getChannel());
                return;
            }

            String[] args = msg.split("\\s+");
            MemeData memeData = null;

            if (args.length == 1) memeData = MemeAPIController.getRandomMeme(null);
            else if (args.length == 2) memeData = MemeAPIController.getRandomMeme(args[1]);

            MessageEmbed memeEmbed = null;
            if (memeData != null) memeEmbed = MemeEmbedBuilder.generateMemeEmbed(memeData);
            if (memeEmbed != null) event.getChannel().sendMessageEmbeds(memeEmbed).queue();
        }
    }

    @Override
    public String commandName() {
        return "meme";
    }

    @Override
    public String commandUsage() {
        return ".meme {subreddit}";
    }

    @Override
    public String helpMessage() {
        return "Zwraca losowy mem z reddita, jesli podano to z konkretnego subreddita.";
    }
}

class MemeEmbedBuilder {
    public static MessageEmbed generateMemeEmbed(MemeData memeData) {
        var builder = new EmbedBuilder();

        builder.setTitle(memeData.title(), memeData.postLink());

        builder.setImage(memeData.url());

        return builder.build();
    }
}
