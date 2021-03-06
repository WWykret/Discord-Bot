package discord.bot.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import discord.bot.CommandPermissions;
import discord.bot.HelpSupport;
import discord.bot.CommandPermissions.Permission;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.requests.RestAction;

public class SnipeCommand extends BotCommand implements HelpSupport{

    private Map<Pair<Guild,MessageChannel>, Long> lastDeleted = new HashMap<>();
    
    private int cachedMessages = 50;
    private Map<Pair<Guild,MessageChannel>, List<Message>> storedMsgs = new HashMap<>();

    @Override
    public void onMessageDelete(MessageDeleteEvent event) {
        Guild guild = event.getGuild();
        MessageChannel channel = event.getChannel();
        long id = event.getMessageIdLong();

        var msgsIndex = new Pair<>(guild, channel);
        lastDeleted.put(msgsIndex, id);
    }

    public void onMessageReceived(MessageReceivedEvent event) {

        String msg = event.getMessage().getContentDisplay();

        if (msg.toLowerCase().equals(commandPrefix + "snipe")) {
            if (!CommandPermissions.hasPermission(event.getGuild(), event.getMember(), Permission.USER)) {
                CommandPermissions.sendLackOfAccessMsg(event.getChannel());
                return;
            }

            Message deletedMsg = getDeletedMsg(event.getGuild(), event.getChannel());
            if (deletedMsg != null) {
                resendDeletedMessage(event.getChannel(), deletedMsg).queue();
            } else {
                event.getChannel().sendMessage("Nie ma zadnej usunietej wiadomosci").queue();
            }
        } else {
            saveMsg(event.getGuild(), event.getChannel(), event.getMessage());
        }
    }

    private void saveMsg(Guild guild, MessageChannel channel, Message message) {
        var msgsIndex = new Pair<>(guild, channel);
        List<Message> storedChannelMsgs = storedMsgs.getOrDefault(msgsIndex, new ArrayList<Message>());
        if (storedChannelMsgs.size() < cachedMessages) {
            storedChannelMsgs.add(message);
        } else {
            Collections.rotate(storedChannelMsgs, -1);
            storedChannelMsgs.set(storedChannelMsgs.size() - 1, message);
        }
        storedMsgs.put(msgsIndex, storedChannelMsgs);
    }

    private Message getDeletedMsg(Guild guild, MessageChannel channel) {
        var msgsIndex = new Pair<>(guild, channel);
        List<Message> storedMsg = storedMsgs.getOrDefault(msgsIndex, null);
        long deletedMsgId = lastDeleted.getOrDefault(msgsIndex, -1L);
        if (storedMsg == null || deletedMsgId == -1L) return null;

        return storedMsg.stream().filter(m -> m.getIdLong() == deletedMsgId).findAny().orElse(null);
    }

    private RestAction<Message> resendDeletedMessage(MessageChannel channel, Message deletedMsg){
        var embedBuilder = new EmbedBuilder();

        embedBuilder.setAuthor(deletedMsg.getAuthor().getName(), null, deletedMsg.getAuthor().getAvatarUrl());

        embedBuilder.addField("Usunieta wiadomosc: ", deletedMsg.getContentRaw(), false);

        embedBuilder.setTimestamp(deletedMsg.getTimeCreated());

        embedBuilder.setColor(0xff0000);

        return channel.sendMessageEmbeds(embedBuilder.build());
    }

    @Override
    public String commandName() {
        return "snipe";
    }

    @Override
    public String commandUsage() {
        return ".snipe";
    }

    @Override
    public String helpMessage() {
        return "Pokazuje ostatnia usunieta wiadomosc na kanale.";
    }
}

record Pair<T,U>(T item1, U item2) {
}