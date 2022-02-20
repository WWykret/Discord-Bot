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
    String url 
) implements EquipmentData {}


@JsonIgnoreProperties(ignoreUnknown = true)
record WeaponDamage(
    @JsonProperty("damage_dice") String damageDice,
    @JsonProperty("damage_type") APIReference damageType
) {}

@JsonIgnoreProperties(ignoreUnknown = true)
record WeaponRange(
    @JsonProperty("normal") Integer normalRange,
    @JsonProperty("long") Integer longRange
) {}