package discord.bot.commands.voting;

import java.util.Set;
import java.util.stream.Collectors;

import discord.bot.BotUtils;
import discord.bot.Emojis;
import discord.bot.commands.BotCommand;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.requests.RestAction;

public class VotingCommand extends BotCommand {
    protected RestAction<Void> addEmojisToVoting(Message votingMsg) {
        return votingMsg.addReaction(Emojis.THUMBS_UP.toString())
                .and(votingMsg.addReaction(Emojis.THUMBS_DOWN.toString()));
    }

    protected RestAction<Boolean> isVotingSuccess(Message msg) {
        return BotUtils.updateMessage(msg)
                .map(message -> isVotingSuccessInternal(message));
    }

    private boolean isVotingSuccessInternal(Message message) {
        var acceptables = Set.of(Emojis.THUMBS_UP.toString(), Emojis.THUMBS_DOWN.toString());
        var allReactions = message.getReactions().stream()
                .filter(r -> r.getReactionEmote().isEmoji())
                .filter(r -> acceptables.contains(r.getReactionEmote().getEmoji()))
                .collect(Collectors.toMap(r -> r.getReactionEmote().getEmoji(), r -> r.getCount()));

        return allReactions.get(Emojis.THUMBS_UP.toString()) > allReactions.get(Emojis.THUMBS_DOWN.toString());

    }

}
