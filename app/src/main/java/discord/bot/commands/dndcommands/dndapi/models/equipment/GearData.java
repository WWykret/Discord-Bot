package discord.bot.commands.dndcommands.dndapi.models.equipment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import discord.bot.commands.dndcommands.dndapi.models.APIReference;
import discord.bot.commands.dndcommands.dndapi.models.Cost;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GearData(
    String index,
    String name,
    @JsonProperty("equipment_category") APIReference eqipmentCategory,
    @JsonProperty("gear_category") APIReference gearCategory,
    int weight,
    Cost cost,
    String url
) {}