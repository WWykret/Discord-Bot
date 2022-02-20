package discord.bot.commands.dndcommands.dndapi.models.equipment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import discord.bot.commands.dndcommands.dndapi.models.APIReference;
import discord.bot.commands.dndcommands.dndapi.models.Cost;

@JsonIgnoreProperties(ignoreUnknown = true)
public record WeaponData(
    String index,
    String name,
    @JsonProperty("equipment_category") APIReference equipmentCategory,
    @JsonProperty("weapon_category") String weaponCategory,
    @JsonProperty("weapon_range") String weaponRange,
    @JsonProperty("category_range") String weaponCategoryRange,
    Cost cost,
    WeaponDamage damage,
    @JsonProperty("two_handed_damage") WeaponDamage twoHandedDamage,
    WeaponRange range,
    int weight,
    APIReference[] properties,
    @JsonProperty("throw_range") WeaponRange throwRange,
    String url 
) implements EquipmentData {}