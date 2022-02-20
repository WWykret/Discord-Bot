package discord.bot.commands.dndcommands.dndapi.models.equipment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import discord.bot.commands.dndcommands.dndapi.models.APIReference;

@JsonIgnoreProperties(ignoreUnknown = true)
public record BasicEquipmentData (
    String index,
    String name,
    @JsonProperty("equipment_category") APIReference equipmentCategory,
    @JsonProperty("gear_category") APIReference gearCategory
) implements EquipmentData {}
