package discord.bot.commands.memecommands.memeapi;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import discord.bot.commands.memecommands.memeapi.models.MemeData;
import discord.bot.commands.memecommands.memeapi.models.MemesListData;

public class MemeAPIController {
    private static final String API_URL = "https://meme-api.herokuapp.com/gimme";

    public static MemeData getRandomMeme(String subreddit) {
        var jsonMapper = new ObjectMapper();
        
        String jsonString = getMemeJsonString(subreddit);

        if (jsonString == null) return null;

        try {
            MemesListData memesList = jsonMapper.readValue(jsonString, MemesListData.class);
            return memesList.memes()[0];
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private static String getMemeJsonString(String subreddit) {
        try {
            String memeApiUrl = API_URL;
            if (subreddit != null) memeApiUrl += "/" + subreddit;
            memeApiUrl += "/1";

            URL url = new URL(memeApiUrl);
            var result = new StringBuilder();
            BufferedReader urlStream = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));
            String line = null;
            while((line = urlStream.readLine()) != null) {
                result.append(line);
            }
            return result.toString();
        } catch (IOException e) {
            return null;
        }
    }
}
