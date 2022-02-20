package discord.bot.commands.dndcommands.dndapi.models.equipment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import discord.bot.commands.dndcommands.dndapi.models.APIReference;

@JsonIgnoreProperties(ignoreUnknown = true)
public record EquipmentPackContent(
    APIReference item,
    int quantity
) {}
