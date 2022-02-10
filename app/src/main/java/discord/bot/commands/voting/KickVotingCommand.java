package discord.bot.commands.voting;

import java.util.concurrent.TimeUnit;

import discord.bot.Emojis;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.requests.RestAction;

public class KickVotingCommand extends VotingCommand {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String msg = event.getMessage().getContentRaw();

        if (msg.indexOf(commandPrefix + "vote kick ") == 0) {
            var toKick = event.getMessage().getMentionedMembers().stream().findAny().orElse(null);
            if (toKick != null) {
                long votingTime = 10;
                MessageChannel channel = event.getChannel();
                createMessageAndPerformActios(channel, toKick, votingTime);
            }
        }
    }

    private void createMessageAndPerformActios(MessageChannel channel, Member toKick, long votingTime) {
        createKickVotingMsg(channel, toKick, votingTime).queue(
                m -> {
                    addEmojisToVoting(m).queue();
                    isVotingSuccess(m).queueAfter(votingTime, TimeUnit.SECONDS,
                            r -> displayResultsAndKickIfNessescary(channel, r, toKick));
                });
    }

    private void displayResultsAndKickIfNessescary(MessageChannel channel, boolean shouldBeKicked, Member toKick) {
        if (shouldBeKicked) {
            toKick.kick("Przykro mi, bot byles zbyt SUS").queue();
            channel.sendMessage("Koniec glosowania... " + Emojis.ALBANIAN_WAR_CRIMES).queue();
        } else {
            channel.sendMessage("Nastepnym razem... " + Emojis.ALBANIAN_WAR_CRIMES).queue();
        }
    }

    private RestAction<Message> createKickVotingMsg(MessageChannel channel, Member memberToKick,
            long howManySecsForVoting) {
        return channel
                .sendMessageFormat("Kickujemy %s? (%d s na glosowanie)", memberToKick.getNickname(),
                        howManySecsForVoting)
                .map(msgA -> msgA.getIdLong())
                .flatMap(id -> channel.retrieveMessageById(id));
    }
}
