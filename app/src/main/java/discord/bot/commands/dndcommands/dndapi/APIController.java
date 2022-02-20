package discord.bot.commands.dndcommands.dndapi;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import discord.bot.commands.dndcommands.dndapi.models.spells.SpellData;

public class APIController {
    private static final String API_ENDPOINT = "https://www.dnd5eapi.co/api/spells/";

    public static SpellData getSpellDataWithIndex(String spellIndex) {
        var jsonMapper = new ObjectMapper();
        
        String jsonString = getSpellDataJsonString(spellIndex);

        if (jsonString == null) return null;

        try {
            return jsonMapper.readValue(jsonString, SpellData.class);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private static String getSpellDataJsonString(String spellIndex) {
        try {
            URL url = new URL(API_ENDPOINT + spellIndex);
            var result = new StringBuilder();
            InputStream stream = url.openStream();
            int byteRead = stream.read();
            while(byteRead != -1) {
                result.append((char) byteRead);
                byteRead = stream.read();
            }
            return result.toString();
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
