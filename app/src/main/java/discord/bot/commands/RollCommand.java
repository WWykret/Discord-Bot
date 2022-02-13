package discord.bot.commands;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import discord.bot.CommandPermissions;
import discord.bot.CommandPermissions.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class RollCommand extends BotCommand {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        String msg = event.getMessage().getContentDisplay();

        if (msg.toLowerCase().indexOf(commandPrefix + "roll ") == 0) {
            if (!CommandPermissions.hasPermission(event.getGuild(), event.getMember(), Permission.USER)) {
                CommandPermissions.sendLackOfAccessMsg(event.getChannel());
                return;
            }
            
            String rollMsg = getRollMsg(msg.substring(6).toLowerCase());
            event.getChannel().sendMessage(rollMsg).queue();
        }
    }

    private String getRollMsg(String commandParams) {
        commandParams = commandParams.replaceAll("\\s+", "");

        if (commandParams.toLowerCase().startsWith("d"))
            commandParams = "1" + commandParams;

        DiceRollInfo diceInfo = new DiceRollInfo(commandParams);

        if (!diceInfo.isFormatCorrect())
            return "Zly format, sproboj\n!roll [1-200]d[1-1000]";

        if (diceInfo.howManyDice() < 1 || diceInfo.howManyDice() > 200)
            return "Mozna kulac tylko 1-200 kostek";

        if (diceInfo.dieType() < 1 || diceInfo.dieType() > 1000)
            return "Wspierane tylko d1 - d1000";

        int[] results = IntStream.range(0, diceInfo.howManyDice())
                .map(i -> new Random().nextInt(diceInfo.dieType()) + 1).toArray();

        return "Wyniki: \n" + Arrays.stream(results).mapToObj(Integer::toString).collect(Collectors.joining(", "))
                + "\nw sumie: " + Arrays.stream(results).sum();
    }
}

class DiceRollInfo {
    private final int howManyDice;
    private final int dieType;
    private boolean isFormatCorrect = true;

    public DiceRollInfo(String rawDiceString) {
        if (rawDiceString.lastIndexOf("d") != rawDiceString.indexOf("d"))
            isFormatCorrect = false;
        String[] parts = rawDiceString.split("d");

        Integer howManyDiceInteger = null;
        Integer dieTypeInteger = null;
        try {
            howManyDiceInteger = toIntOrNull(parts[0]);
            dieTypeInteger = toIntOrNull(parts[1]);
        } catch (IndexOutOfBoundsException ex) {
            isFormatCorrect = false;
        }

        howManyDice = howManyDiceInteger != null ? howManyDiceInteger : 0;
        dieType = dieTypeInteger != null ? dieTypeInteger : 0;

        isFormatCorrect = isFormatCorrect && dieTypeInteger != null && howManyDiceInteger != null;
    }

    public int howManyDice() {
        return howManyDice;
    }

    public int dieType() {
        return dieType;
    }

    public boolean isFormatCorrect() {
        return isFormatCorrect;
    }

    private Integer toIntOrNull(String numAsString) {
        try {
            return Integer.parseInt(numAsString);
        } catch (NumberFormatException ex) {
            return null;
        }
    }
}