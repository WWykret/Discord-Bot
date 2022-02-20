package discord.bot.commands.dndcommands.dndapi.models.equipment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record WeaponRange(
    @JsonProperty("normal") Integer normalRange,
    @JsonProperty("long") Integer longRange
) {}