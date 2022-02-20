package discord.bot.commands.dndcommands.dndapi.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record APIReference(
    String index,
    String name,
    String url
) {}