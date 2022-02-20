package discord.bot.commands.dndcommands;


import java.util.Arrays;
import java.util.stream.Collectors;

import discord.bot.CommandPermissions;
import discord.bot.CommandPermissions.Permission;
import discord.bot.commands.BotCommand;
import discord.bot.commands.dndcommands.dndapi.APIController;
import discord.bot.commands.dndcommands.dndapi.models.APIReference;
import discord.bot.commands.dndcommands.dndapi.models.spells.SpellData;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class DndSpellCommand extends BotCommand {

    public void onMessageReceived(MessageReceivedEvent event) {

        String msg = event.getMessage().getContentDisplay();

        if (msg.toLowerCase().indexOf(commandPrefix + "spell") == 0) {
            if (!CommandPermissions.hasPermission(event.getGuild(), event.getMember(), Permission.USER)) {
                CommandPermissions.sendLackOfAccessMsg(event.getChannel());
                return;
            }
            
            String[] args = msg.split("\\s+", 2);
            if (args.length != 2) {
                event.getChannel().sendMessage("Musisz podac nazwe zaklecia").queue();
                return;
            }

            String spellIndex = getSpellIndex(args[1]);
            var spellData = APIController.getSpellDataWithIndex(spellIndex);

            if (spellData == null) {
                event.getChannel().sendMessage("Nie znaleziono zaklecia o takiej nazwie").queue();
                return;
            }

            MessageEmbed spellInfoEmbed = SpellEmbedGenerator.generateSpellEmbed(spellData);
            if (spellInfoEmbed != null) event.getChannel().sendMessageEmbeds(spellInfoEmbed).queue();
        }
    }

    private String getSpellIndex(String spellName) {
        spellName = spellName.toLowerCase();
        spellName = spellName.replaceAll("\\s+", " "); //replace multipla spaces
        spellName = spellName.replace(" ", "-");
        return spellName;
    }
}

class SpellEmbedGenerator {
    
    public static MessageEmbed generateSpellEmbed(SpellData spellData) {
        var builder = new EmbedBuilder();

        builder.setTitle(spellData.name());
        builder.setColor(0x00dd22);

        setDescription(spellData, builder);
        setHigherLevelsInfo(spellData, builder);
        setRangeInfo(spellData, builder);
        setComponentsInfo(spellData, builder);
        setMaterialInfo(spellData, builder);
        setRitualInfo(spellData, builder);
        setDurationInfo(spellData, builder);
        setConcentrationInfo(spellData, builder);
        setCastingTimeInfo(spellData, builder);
        setSpellLevelInfo(spellData, builder);
        setClassesInfo(spellData, builder);

        return builder.build();
    }

    private static void setDescription(SpellData spellData, EmbedBuilder builder) {
        var descriptionsParagraphs = spellData.desc();
        
        var descBuilder = new StringBuilder();

        for (String paragraph : descriptionsParagraphs) {
            descBuilder.append(paragraph);
            descBuilder.append("\n");
        }

        builder.setDescription(descBuilder.toString());
    }

    private static void setHigherLevelsInfo(SpellData spellData, EmbedBuilder builder) {
        var higherLevelsParagraphs = spellData.higherLevel();

        if (higherLevelsParagraphs == null) return;

        var higherLevelsBuilder = new StringBuilder();

        for (String paragraph : higherLevelsParagraphs) {
            higherLevelsBuilder.append(paragraph);
            higherLevelsBuilder.append("\n");
        }

        builder.addField("At higher levels", higherLevelsBuilder.toString(), false);
    }

    private static void setRangeInfo(SpellData spellData, EmbedBuilder builder) {
        String range = spellData.range();

        if (range == null) return;

        builder.addField("Range", range, false);
    }

    private static void setComponentsInfo(SpellData spellData, EmbedBuilder builder) {
        var components = spellData.components();

        if (components == null) return;

        builder.addField("Components", Arrays.stream(components).collect(Collectors.joining(", ")), false);
    }

    private static void setMaterialInfo(SpellData spellData, EmbedBuilder builder) {
        String matrerial = spellData.material();

        if (matrerial == null) return;

        builder.addField("Material", matrerial, false);
    }

    private static void setRitualInfo(SpellData spellData, EmbedBuilder builder) {
        builder.addField("Is Ritual", spellData.ritual() ? "Yes" : "No", false);
    }

    private static void setDurationInfo(SpellData spellData, EmbedBuilder builder) {
        String duration = spellData.duration();

        if (duration == null) return;

        builder.addField("Duration", duration, false);
    }

    private static void setConcentrationInfo(SpellData spellData, EmbedBuilder builder) {
        builder.addField("Needs Concentration", spellData.concentration() ? "Yes" : "No", false);
    }

    private static void setCastingTimeInfo(SpellData spellData, EmbedBuilder builder) {
        String castTime = spellData.castingTime();

        if (castTime == null) return;

        builder.addField("Casting Time", castTime, false);
    }

    private static void setSpellLevelInfo(SpellData spellData, EmbedBuilder builder) {
        int spellLevel = spellData.level();
        String spellLevelStr = String.valueOf(spellLevel) + (spellLevel == 0 ? " (Cantrip)" : "");
        builder.addField("Spell level", spellLevelStr, false);
    }

    private static void setClassesInfo(SpellData spellData, EmbedBuilder builder) {
        APIReference[] classes = spellData.classes();
        
        if (classes == null) return;
        
        builder.addField("Classes", Arrays.stream(classes).map(APIReference::name).collect(Collectors.joining(", ")), false);
    }

}