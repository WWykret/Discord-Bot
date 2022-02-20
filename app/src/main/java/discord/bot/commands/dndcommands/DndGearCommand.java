package discord.bot.commands.dndcommands;


import java.util.Arrays;
import java.util.stream.Collectors;

import discord.bot.CommandPermissions;
import discord.bot.CommandPermissions.Permission;
import discord.bot.commands.BotCommand;
import discord.bot.commands.dndcommands.dndapi.APIController;
import discord.bot.commands.dndcommands.dndapi.models.APIReference;
import discord.bot.commands.dndcommands.dndapi.models.equipment.ArmorData;
import discord.bot.commands.dndcommands.dndapi.models.equipment.EquipmentPackData;
import discord.bot.commands.dndcommands.dndapi.models.equipment.GearData;
import discord.bot.commands.dndcommands.dndapi.models.equipment.WeaponData;
import discord.bot.commands.dndcommands.dndapi.models.spells.SpellData;
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

            // MessageEmbed spellInfoEmbed = SpellEmbedGenerator.generateSpellEmbed(spellData);
            // if (spellInfoEmbed != null) event.getChannel().sendMessageEmbeds(spellInfoEmbed).queue();
            if (gearData instanceof WeaponData) System.out.println("WEAPON");
            else if (gearData instanceof ArmorData) System.out.println("ARMOR");
            else if (gearData instanceof GearData) System.out.println("GEAR");
            else if (gearData instanceof EquipmentPackData) System.out.println("EQP");
        }
    }
}