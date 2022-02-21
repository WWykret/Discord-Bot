package discord.bot.commands.voting;

import java.util.concurrent.TimeUnit;

import discord.bot.CommandPermissions;
import discord.bot.Emojis;
import discord.bot.HelpSupport;
import discord.bot.CommandPermissions.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.requests.RestAction;

public class KickVotingCommand extends VotingCommand implements HelpSupport {

    private long votingTime = 60L;

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        String msg = event.getMessage().getContentRaw();
        if (msg.indexOf(commandPrefix + "vote kick ") == 0) {
            if (!CommandPermissions.hasPermission(event.getGuild(), event.getMember(), Permission.MODERATION)) {
                CommandPermissions.sendLackOfAccessMsg(event.getChannel());
                return;
            }
            
            var toKickUser = event.getMessage().getMentionedUsers().stream().findAny().orElse(null);
            
            if (toKickUser != null) {
                MessageChannel channel = event.getChannel();
                Guild guild = event.getGuild();
                
                String toKickId = toKickUser.getId();
                Member toKickMember = guild.getMember(toKickUser);
                String toKickNick = toKickMember == null ? toKickUser.getName() : toKickMember.getNickname();
                if (toKickNick == null) toKickNick = toKickUser.getName();

                createMessageAndPerformActios(guild, channel, toKickId, toKickNick, votingTime);
            }
        }
    }

    private void createMessageAndPerformActios(Guild guild, MessageChannel channel, String toKickId, String toKickNick, long votingTime) {
        createKickVotingMsg(channel, toKickNick, votingTime).queue(
                m -> {
                    addEmojisToVoting(m).queue();
                    isVotingSuccess(m).queueAfter(votingTime, TimeUnit.SECONDS,
                            r -> displayResultsAndKickIfNessescary(guild, channel, r, toKickId));
                });
    }

    private void displayResultsAndKickIfNessescary(Guild guild, MessageChannel channel, boolean shouldBeKicked, String toKickId) {
        if (shouldBeKicked) {
            
            guild.kick(toKickId, "Przykro mi, bot byles zbyt SUS").queue();
            channel.sendMessage("Koniec glosowania... " + Emojis.ALBANIAN_WAR_CRIMES).queue();
        } else {
            channel.sendMessage("Nastepnym razem... " + Emojis.ALBANIAN_WAR_CRIMES).queue();
        }
    }

    private RestAction<Message> createKickVotingMsg(MessageChannel channel, String memberToKick,
            long howManySecsForVoting) {
        return channel
                .sendMessageFormat("Kickujemy %s? (%d s na glosowanie)", memberToKick, howManySecsForVoting)
                .map(msgA -> msgA.getIdLong())
                .flatMap(id -> channel.retrieveMessageById(id));
    }

    @Override
    public String commandName() {
        return "vote kick";
    }

    @Override
    public String commandUsage() {
        return ".vote kick @<osoba do kickowania>";
    }

    @Override
    public String helpMessage() {
        return "Powoduje glosowanie, czy zkikowac oznaczana osobe z serwera. Wymaga rangi `bot-moderation`.";
    }
}
