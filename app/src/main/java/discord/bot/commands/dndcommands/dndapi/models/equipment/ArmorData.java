package discord.bot.commands.dndcommands.dndapi.models.equipment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import discord.bot.commands.dndcommands.dndapi.models.APIReference;
import discord.bot.commands.dndcommands.dndapi.models.Cost;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ArmorData(
    String index,
    String name,
    @JsonProperty("equipment_category") APIReference equipmentCategory,
    @JsonProperty("armor_category") String armorCategory,
    @JsonProperty("armor_class") ArmorClass armorClass,
    @JsonProperty("str_minimum") int minStrength,
    @JsonProperty("stealth_disadvantage") boolean stealthDisadvantage,
    int weight,
    Cost cost,
    String url
) implements EquipmentData {}

@JsonIgnoreProperties(ignoreUnknown = true)
record ArmorClass(
    int base,
    @JsonProperty("dex_bonus") boolean dexBonus,
    @JsonProperty("max_bonus") Integer maxBonus
) {}