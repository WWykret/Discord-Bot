package discord.bot.commands.dndcommands;


import java.util.Arrays;
import java.util.stream.Collectors;

import discord.bot.CommandPermissions;
import discord.bot.CommandPermissions.Permission;
import discord.bot.commands.BotCommand;
import discord.bot.commands.dndcommands.dndapi.APIController;
import discord.bot.commands.dndcommands.dndapi.models.APIReference;
import discord.bot.commands.dndcommands.dndapi.models.equipment.ArmorData;
import discord.bot.commands.dndcommands.dndapi.models.equipment.EquipmentPackContent;
import discord.bot.commands.dndcommands.dndapi.models.equipment.EquipmentPackData;
import discord.bot.commands.dndcommands.dndapi.models.equipment.GearData;
import discord.bot.commands.dndcommands.dndapi.models.equipment.WeaponDamage;
import discord.bot.commands.dndcommands.dndapi.models.equipment.WeaponData;
import discord.bot.commands.dndcommands.dndapi.models.equipment.WeaponRange;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class DndGearCommand extends BotCommand {

    public void onMessageReceived(MessageReceivedEvent event) {

        String msg = event.getMessage().getContentDisplay();

        if (msg.toLowerCase().indexOf(commandPrefix + "gear") == 0) {
            if (!CommandPermissions.hasPermission(event.getGuild(), event.getMember(), Permission.USER)) {
                CommandPermissions.sendLackOfAccessMsg(event.getChannel());
                return;
            }
            
            String[] args = msg.split("\\s+", 2);
            if (args.length != 2) {
                event.getChannel().sendMessage("Musisz podac nazwe zaklecia").queue();
                return;
            }

            String gearIndex = APIController.getIndexFromName(args[1]);
            var gearData = APIController.getEquipmentWithIndex(gearIndex);

            if (gearData == null) {
                event.getChannel().sendMessage("Nie znaleziono przedmiotu o takiej nazwie").queue();
                return;
            }

            MessageEmbed gearInfoEmbed = null;

            if (gearData instanceof WeaponData) gearInfoEmbed = WeaponEmbedGenerator.generateEmbed((WeaponData) gearData);
            else if (gearData instanceof ArmorData) gearInfoEmbed = ArmorEmbedGenerator.generateEmbed((ArmorData) gearData);
            else if (gearData instanceof GearData) gearInfoEmbed = GearEmbedGenerator.generateEmbed((GearData) gearData);
            else if (gearData instanceof EquipmentPackData) 
                gearInfoEmbed = EquipmentPackEmbedGenerator.generateEmbed((EquipmentPackData) gearData);

            if (gearInfoEmbed != null) event.getChannel().sendMessageEmbeds(gearInfoEmbed).queue();
        }
    }
}

class WeaponEmbedGenerator {
    
    public static MessageEmbed generateEmbed(WeaponData weaponData) {
        var builder = new EmbedBuilder();

        builder.setTitle(weaponData.name());
        builder.setColor(0xdd0000);

        setRangeAndCategoryInfo(builder, weaponData);
        setBasicDamageInfo(builder, weaponData);
        setWeaponPropertiesInfo(builder, weaponData);
        
        builder.addField("Cost", weaponData.cost().toString(), false);

        return builder.build();
    }

    private static void setRangeAndCategoryInfo(EmbedBuilder builder, WeaponData weaponData) {
        String rangeAsString = weaponData.weaponRange();
        var rangeAsObject = weaponData.range();
        String category = weaponData.weaponCategory();

        if (rangeAsString == null || rangeAsObject == null || category == null) return;

        StringBuilder rangeAndCategoryMsg = new StringBuilder();

        rangeAndCategoryMsg.append(category + ", ");

        rangeAndCategoryMsg.append(rangeAsString + " ");
        rangeAndCategoryMsg.append(getRangeString(rangeAsObject));

        builder.addField("Type", rangeAndCategoryMsg.toString(), false);
    }

    private static void setBasicDamageInfo(EmbedBuilder builder, WeaponData weaponData) {
        var damageInfo = weaponData.damage();

        if (damageInfo == null) return;
        
        builder.addField("Damage", getDamageString(damageInfo), false);
    }

    private static void setWeaponPropertiesInfo (EmbedBuilder builder, WeaponData weaponData) {
        var properties = Arrays.stream(weaponData.properties()).map(APIReference::name).toList();
        var twoHandedDmg = weaponData.twoHandedDamage();
        var thrownRange = weaponData.throwRange();


        if (properties == null) return;

        var propertiesInfo = new StringBuilder();
        for (String property : properties) {
            propertiesInfo.append(property);
            
            String propertyAdditionalInfo = switch(property) {
                case "Versatile" -> " - " + getDamageString(twoHandedDmg);
                case "Thrown" -> " " + getRangeString(thrownRange);
                default -> "";
            };

            propertiesInfo.append(propertyAdditionalInfo);
            propertiesInfo.append("\n");
        }

        builder.addField("Properties", propertiesInfo.toString(), false);
    }

    private static String getDamageString(WeaponDamage damage) {
        if (damage == null) return "";        
        
        return damage.damageDice() + " (" + damage.damageType().name() + ")";
    }

    private static String getRangeString(WeaponRange range) {
        if (range == null) return "";        
        
        var rangeBuilder = new StringBuilder();

        rangeBuilder.append("(" + range.normalRange() + " ft");
        
        Integer longRange = range.longRange();
        if (longRange != null) rangeBuilder.append(" / " + longRange + " ft");

        rangeBuilder.append(")");
        return rangeBuilder.toString();
    }
}

class ArmorEmbedGenerator {
    
    public static MessageEmbed generateEmbed(ArmorData armorData) {
        var builder = new EmbedBuilder();

        builder.setTitle(armorData.name());
        builder.setColor(0x444444);
        
        setArmorCategoryInfo(armorData, builder);
        setArmorClassInfo(armorData, builder);
        setStealthInfo(armorData, builder);
        setMinStrengthInfo(armorData, builder);

        builder.addField("Cost", armorData.cost().toString(), false);

        return builder.build();
    }

    private static void setArmorCategoryInfo(ArmorData armorData, EmbedBuilder builder) {
        String category = armorData.armorCategory();

        if (category == null) return;

        builder.addField("Armor Type", category, false);
    }

    private static void setArmorClassInfo(ArmorData armorData, EmbedBuilder builder) {
        var ac = armorData.armorClass();

        if (ac == null) return;

        var acInfoBuilder = new StringBuilder();
        
        acInfoBuilder.append(ac.base());

        if (ac.dexBonus()) acInfoBuilder.append(" + DEX");
        if (ac.maxBonus() != null) acInfoBuilder.append(" (max " + (ac.base() + ac.maxBonus()) + ")");

        builder.addField("Armor Class", acInfoBuilder.toString(), false);
    }

    private static void setStealthInfo(ArmorData armorData, EmbedBuilder builder) {
        boolean stealthDisadvantage = armorData.stealthDisadvantage();

        builder.addField("Gives Stealth Disadvantage", stealthDisadvantage ? "Yes" : "No", false);
    }

    private static void setMinStrengthInfo(ArmorData armorData, EmbedBuilder builder) {
        int minStr = armorData.minStrength();

        if (minStr <= 0) return;

        builder.addField("Minimum Strength Needed", String.valueOf(minStr), false);
    }
}

class GearEmbedGenerator {
    
    public static MessageEmbed generateEmbed(GearData gearData) {
        var builder = new EmbedBuilder();

        builder.setTitle(gearData.name());
        builder.setColor(0xba8200);

        setWeightInfo(gearData, builder);

        builder.addField("Cost", gearData.cost().toString(), false);

        return builder.build();
    }

    private static void setWeightInfo(GearData gearData, EmbedBuilder builder) {
        int weight = gearData.weight();

        builder.addField("Weight", String.valueOf(weight), false);
    }
}

class EquipmentPackEmbedGenerator {
    
    public static MessageEmbed generateEmbed(EquipmentPackData equipmentPackData) {
        var builder = new EmbedBuilder();

        builder.setTitle(equipmentPackData.name());
        builder.setColor(0xf159ff);

        setContentsInfo(equipmentPackData, builder);

        builder.addField("Cost", equipmentPackData.cost().toString(), false);

        return builder.build();
    }

    private static void setContentsInfo(EquipmentPackData gearData, EmbedBuilder builder) {
        var contents = gearData.contents();

        if (contents == null) return;

        var contentsInfoBuilder = new StringBuilder();

        for (EquipmentPackContent content : contents) {
            contentsInfoBuilder.append(content.item().name());
            int quantity = content.quantity();
            if (quantity > 1) contentsInfoBuilder.append(" x" + quantity);
            contentsInfoBuilder.append("\n");
        }

        builder.addField("Contents", contentsInfoBuilder.toString(), false);
    }
}