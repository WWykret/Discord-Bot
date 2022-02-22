package discord.bot.commands.dndcommands.dndapi;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import discord.bot.commands.dndcommands.dndapi.models.equipment.ArmorData;
import discord.bot.commands.dndcommands.dndapi.models.equipment.BasicEquipmentData;
import discord.bot.commands.dndcommands.dndapi.models.equipment.EquipmentData;
import discord.bot.commands.dndcommands.dndapi.models.equipment.EquipmentPackData;
import discord.bot.commands.dndcommands.dndapi.models.equipment.GearData;
import discord.bot.commands.dndcommands.dndapi.models.equipment.WeaponData;
import discord.bot.commands.dndcommands.dndapi.models.spells.SpellData;

public class DnDAPIController {
    private static final String API_ENDPOINT = "https://www.dnd5eapi.co/api/";

    public static SpellData getSpellDataWithIndex(String spellIndex) {
        var jsonMapper = new ObjectMapper();
        
        String jsonString = getItemOrSpellJsonString("spells/" + spellIndex);

        if (jsonString == null) return null;

        try {
            return jsonMapper.readValue(jsonString, SpellData.class);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static EquipmentData getEquipmentWithIndex(String equipmentIndex) {
        String resourcePath = "equipment/" + equipmentIndex;

        var jsonMapper = new ObjectMapper();
        
        String jsonString = getItemOrSpellJsonString(resourcePath);

        if (jsonString == null) return null;

        try {
            BasicEquipmentData basicData = jsonMapper.readValue(jsonString, BasicEquipmentData.class);
            String equipmentType = basicData.equipmentCategory().name();
            return switch(equipmentType) {
                case "Weapon" -> jsonMapper.readValue(jsonString, WeaponData.class);
                case "Armor" -> jsonMapper.readValue(jsonString, ArmorData.class);
                case "Adventuring Gear" ->
                    basicData.gearCategory().name().equals("Equipment Packs") ?
                    jsonMapper.readValue(jsonString, EquipmentPackData.class) : jsonMapper.readValue(jsonString, GearData.class);
                default -> null;
            };
        } catch (JsonProcessingException | NullPointerException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private static String getItemOrSpellJsonString(String resource) {
        try {
            URL url = new URL(API_ENDPOINT + resource);
            var result = new StringBuilder();
            InputStream stream = url.openStream();
            int byteRead = stream.read();
            while(byteRead != -1) {
                result.append((char) byteRead);
                byteRead = stream.read();
            }
            return result.toString();
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getIndexFromName(String name) {
        name = name.strip();
        name = name.toLowerCase();
        name = name.replaceAll("\\s+", " "); //replace multiple spaces
        name = name.replace(" ", "-");
        return name;
    }
}
