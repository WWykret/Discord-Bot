package discord.bot.commands;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.HashMap;
import java.util.Map;

import discord.bot.CommandPermissions;
import discord.bot.CommandPermissions.Permission;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class ReminderCommand extends BotCommand {
    private final int maxRemindersPerUser = 5;
    private Map<User, Integer> reminderCounder = new HashMap<>();

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        String msg = event.getMessage().getContentRaw();
        if (msg.indexOf(commandPrefix + "remind ") == 0) {
            if (!CommandPermissions.hasPermission(event.getGuild(), event.getMember(), Permission.USER)) {
                CommandPermissions.sendLackOfAccessMsg(event.getChannel());
                return;
            }

            String[] commandArgs = msg.split("\\s+", 4);
            if (commandArgs.length < 4) return;

            tryToSetReminder(event.getChannel(), event.getAuthor(), commandArgs[1], commandArgs[2], commandArgs[3]);
        }
    }

    private void tryToSetReminder(MessageChannel channel, User user, String timeAsString, String timeUnitStr, String message) {
        Long timeAmount;
        try {
            timeAmount = Long.parseLong(timeAsString);
        } catch (NumberFormatException ex) {
            timeAmount = null;
        }
        if (timeAmount == null) {
            channel.sendMessage("Ilosc czasu musi byc liczba").queue();
            return;
        }

        TemporalUnit unit = switch (timeUnitStr.toLowerCase()) {
            case "s" -> ChronoUnit.SECONDS;
            case "min" -> ChronoUnit.MINUTES;
            case "h" -> ChronoUnit.HOURS;
            case "d" -> ChronoUnit.DAYS;
            default -> null;
        };
        if (unit == null) {
            channel.sendMessage("Dostepne jednostki czasu: 's', 'min', 'h', 'd'").queue();
            return;
        }

        setReminder(channel, user, timeAmount, unit, message);
    }

    private void setReminder(MessageChannel channel, User user, long time, TemporalUnit unit, String message) {
        int currentReminders = reminderCounder.getOrDefault(user, 0);
        if (currentReminders >= maxRemindersPerUser) {
            channel.sendMessageFormat("maksymalnie %d przypomnien na uzytkownika", maxRemindersPerUser).queue();
            return;
        }

        reminderCounder.put(user, currentReminders + 1);
        channel.sendMessage("Ustawiono przypomnienie").queue();
        new Thread(() -> remind(channel, user, time, unit, message)).start();
    }

    private void remind(MessageChannel channel, User user, long time, TemporalUnit unit, String message) {
        long timeToSleep = Duration.of(time, unit).toMillis();
        try {
            Thread.sleep(timeToSleep);
        } catch (InterruptedException ex) {

        }

        int currentReminders = reminderCounder.getOrDefault(user, 0);
        if (currentReminders > 0) reminderCounder.put(user, currentReminders - 1);

        channel.sendMessageFormat("<@%s> przypominam o:\n%s", user.getId(), message).queue();
    }
}
