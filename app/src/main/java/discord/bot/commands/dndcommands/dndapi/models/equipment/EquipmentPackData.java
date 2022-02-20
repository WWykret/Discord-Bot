package discord.bot.commands.dndcommands.dndapi.models.equipment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import discord.bot.commands.dndcommands.dndapi.models.APIReference;
import discord.bot.commands.dndcommands.dndapi.models.Cost;

@JsonIgnoreProperties(ignoreUnknown = true)
public record EquipmentPackData(
    String index,
    String name,
    @JsonProperty("equipment_category") APIReference equipmentCategory,
    @JsonProperty("gear_category") APIReference gearCategory,
    Cost cost,
    APIReference[] contents,
    String url
) implements EquipmentData {}