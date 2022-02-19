package discord.bot.commands.dndapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SpellData(
    String index,
    String name,
    String[] desc,
    @JsonProperty("higher_level") String[] higherLevel,
    String range,
    String[] components,
    String material,
    boolean ritual,
    String duration,
    boolean concentration,
    @JsonProperty("casting_time") String castingTime,
    int level,
    @JsonProperty("attack_type") String attackType,
    Damage damage,
    APIReference[] classes,
    APIReference[] subclasses,
    String url
) {}


record Damage(
    @JsonProperty("damage_type") DamageType dmgType,
    @JsonProperty("damage_at_slot_level") DamageAtSlotLevel dmgAtSlotLevels,
    @JsonProperty("damage_at_character_level") DamageAtCharacterLevel dmgAtCharacterLevels
) {}

@JsonIgnoreProperties(ignoreUnknown = true)
record DamageType(
    String index,
    String name
) {}

@JsonIgnoreProperties(ignoreUnknown = true)
record DamageAtSlotLevel(
    @JsonProperty("1") String lvl1Dmg,
    @JsonProperty("2") String lvl2Dmg,
    @JsonProperty("3") String lvl3Dmg,
    @JsonProperty("4") String lvl4Dmg,
    @JsonProperty("5") String lvl5Dmg,
    @JsonProperty("6") String lvl6Dmg,
    @JsonProperty("7") String lvl7Dmg,
    @JsonProperty("8") String lvl8Dmg,
    @JsonProperty("9") String lvl9Dmg
) {
    public String damage() {
        
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
record DamageAtCharacterLevel(
    @JsonProperty("1") String lvl1Dmg,
    @JsonProperty("2") String lvl2Dmg,
    @JsonProperty("3") String lvl3Dmg,
    @JsonProperty("4") String lvl4Dmg,
    @JsonProperty("5") String lvl5Dmg,
    @JsonProperty("6") String lvl6Dmg,
    @JsonProperty("7") String lvl7Dmg,
    @JsonProperty("8") String lvl8Dmg,
    @JsonProperty("9") String lvl9Dmg,
    @JsonProperty("10") String lvl10Dmg,
    @JsonProperty("11") String lvl11Dmg,
    @JsonProperty("12") String lvl12Dmg,
    @JsonProperty("13") String lvl13Dmg,
    @JsonProperty("14") String lvl14Dmg,
    @JsonProperty("15") String lvl15Dmg,
    @JsonProperty("16") String lvl16Dmg,
    @JsonProperty("17") String lvl17Dmg,
    @JsonProperty("18") String lvl18Dmg,
    @JsonProperty("19") String lvl19Dmg,
    @JsonProperty("20") String lvl20Dmg
) {}

@JsonIgnoreProperties(ignoreUnknown = true)
record APIReference(
    String index,
    String name,
    String url
) {}

