package discord.bot.commands.memecommands.memeapi.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record MemesListData(
    int count,
    MemeData[] memes
) {}
