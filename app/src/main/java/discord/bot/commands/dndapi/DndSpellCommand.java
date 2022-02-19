package discord.bot.commands.dndapi;


import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import discord.bot.CommandPermissions;
import discord.bot.CommandPermissions.Permission;
import discord.bot.commands.BotCommand;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class DndSpellCommand extends BotCommand {

    private static final String API_ENDPOINT = "https://www.dnd5eapi.co/api/";

    public void onMessageReceived(MessageReceivedEvent event) {

        String msg = event.getMessage().getContentDisplay();

        if (msg.toLowerCase().equals(commandPrefix + "spell")) {
            if (!CommandPermissions.hasPermission(event.getGuild(), event.getMember(), Permission.USER)) {
                CommandPermissions.sendLackOfAccessMsg(event.getChannel());
                return;
            }
            
            var jsonMapper = new ObjectMapper();
            try {
                String testString = getCallResultAsString();
                SpellData testClassInstance = jsonMapper.readValue(testString, SpellData.class);
                event.getChannel().sendMessage(testClassInstance.toString()).queue();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private String getCallResultAsString() throws Exception{
        URL url = new URL(API_ENDPOINT + "spells/acid-arrow");
        // HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        var result = new StringBuilder();
        InputStream stream = url.openStream();
        int byteRead = stream.read();
        while(byteRead != -1) {
            result.append((char) byteRead);
            byteRead = stream.read();
        }
        return result.toString();
    }
}