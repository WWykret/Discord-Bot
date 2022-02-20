package discord.bot.commands.dndcommands.dndapi.models.equipment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ArmorClass(
    int base,
    @JsonProperty("dex_bonus") boolean dexBonus,
    @JsonProperty("max_bonus") Integer maxBonus
) {}