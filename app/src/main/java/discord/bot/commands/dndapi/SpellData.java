package discord.bot.commands.dndapi;

import com.fasterxml.jackson.annotation.JsonAlias;
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
    @JsonProperty("damage_at_character_level") DamageAtLevel dmgAtCharacterLevel,
    @JsonProperty("damage_at_slot_level") DamageAtLevel dmgAtSpellslotLevel
) {}

@JsonIgnoreProperties(ignoreUnknown = true)
record DamageType(
    String index,
    String name
) {}

@JsonIgnoreProperties(ignoreUnknown = true)
record DamageAtLevel(
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
) {
    public String damage(int level) {
        return switch(level) {
            case 1 -> lvl1Dmg;
            case 2 -> lvl2Dmg;
            case 3 -> lvl3Dmg;
            case 4 -> lvl4Dmg;
            case 5 -> lvl5Dmg;
            case 6 -> lvl6Dmg;
            case 7 -> lvl7Dmg;
            case 8 -> lvl8Dmg;
            case 9 -> lvl9Dmg;
            case 10 -> lvl10Dmg;
            case 11 -> lvl11Dmg;
            case 12 -> lvl12Dmg;
            case 13 -> lvl13Dmg;
            case 14 -> lvl14Dmg;
            case 15 -> lvl15Dmg;
            case 16 -> lvl16Dmg;
            case 17 -> lvl17Dmg;
            case 18 -> lvl18Dmg;
            case 19 -> lvl19Dmg;
            case 20 -> lvl20Dmg;
            default -> null;
        };
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
record APIReference(
    String index,
    String name,
    String url
) {}

