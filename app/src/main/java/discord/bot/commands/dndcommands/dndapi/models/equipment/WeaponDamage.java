package discord.bot.commands.dndcommands.dndapi.models.equipment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import discord.bot.commands.dndcommands.dndapi.models.APIReference;

@JsonIgnoreProperties(ignoreUnknown = true)
public record WeaponDamage(
    @JsonProperty("damage_dice") String damageDice,
    @JsonProperty("damage_type") APIReference damageType
) {}