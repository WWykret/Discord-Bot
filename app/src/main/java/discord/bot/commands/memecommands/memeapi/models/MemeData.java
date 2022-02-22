package discord.bot.commands.memecommands.memeapi.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record MemeData(
    Integer code,
    String postLink,
    String title,
    String url,
    boolean nsfw,
    boolean spoiler
)
{}
