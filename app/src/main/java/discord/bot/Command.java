package discord.bot;

import java.util.Arrays;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.RestAction;

enum Emojis {
    THUMBS_UP("\uD83D\uDC4D"),
    THUMBS_DOWN("\uD83D\uDC4E"),
    ALBANIAN_WAR_CRIMES("<:ZbrodnieWojenneWAlbanii:823195443059752981>"),
    JAVA_SOCIAL_CREDITS("<:javaSocialCredits:901211051369054208>");

    private final String code;

    private Emojis(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return code;
    }
}

public class Command extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String msg = event.getMessage().getContentDisplay();

        if (msg.contains("java") && !event.getAuthor().isBot()) {
            event.getChannel()
                    .sendMessage("kocham jave kocham jave kocham jave " + Emojis.JAVA_SOCIAL_CREDITS).queue();
        }

        if (msg.replace(" ", "").contains("31") && !event.getAuthor().isBot()) {
            event.getChannel()
                    .sendMessage("wykryto i usunieto nielegalny numer " + Emojis.ALBANIAN_WAR_CRIMES).queue();
            event.getMessage().delete().queue();
        }

        if (msg.indexOf("!vote kick") == 0) {
            var toKick = event.getMessage().getMentionedMembers().stream().findAny().orElse(null);
            if (toKick != null) {
                long votingTime = 30;
                MessageChannel channel = event.getChannel();
                createKickVotingMsg(channel, toKick, votingTime)
                        .queue(m -> {
                            addEmojisToVoting(m).queue();
                            queueWithDelay(() -> countVotesAndKickIfNessescary(channel, m, toKick), votingTime);
                        });
            }
        }

        if (msg.indexOf("!roll") == 0) {
            String diceString = msg.substring(5).strip();
            if (diceString.toLowerCase().startsWith("d")) diceString = "1" + diceString;
            String[] dice = diceString.split("d|D");
            if (dice.length == 2) {
                int howManyDice = 0;
                int dieKind = 0;
                try {
                    howManyDice = Integer.parseInt(dice[0]);
                    dieKind = Integer.parseInt(dice[1]);
                } catch (NumberFormatException ex) {
                    event.getChannel().sendMessage("Zly format, sproboj\n!roll [1-200]d[1-1000]").queue();
                    return;
                }
                if (howManyDice < 1 || howManyDice > 200) {
                    event.getChannel().sendMessage("Mozna kulac tylko 1-200 kostek").queue();
                    return;
                } else if (dieKind < 1 || dieKind > 1000) {
                    event.getChannel().sendMessage("Wspierane tylko d1 - d1000").queue();
                    return;
                }
                int[] results = new int[howManyDice];
                for (int i = 0; i < howManyDice; i++) {
                    results[i] = new Random().nextInt(dieKind) + 1;
                }
                StringBuilder msgToReturn = new StringBuilder("Wyniki: \n");
                for (int i = 0; i < howManyDice; i++) msgToReturn.append(results[i] + ", ");
                msgToReturn.append("\nw sumie: ")
                .append(IntStream.of(results).sum());
                event.getChannel().sendMessage(msgToReturn.toString()).queue();
            }
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

    private RestAction<Void> addEmojisToVoting(Message votingMsg) {
        return votingMsg.addReaction(Emojis.THUMBS_UP.toString())
                .and(votingMsg.addReaction(Emojis.THUMBS_DOWN.toString()));
    }

    private RestAction<Message> countVotesAndKickIfNessescary(MessageChannel channel, Message msg, Member toKick) {
        return updateMessage(msg)
                .flatMap(message -> {

                    boolean shouldBeKicked = isVotingSuccess(message);

                    if (shouldBeKicked) {
                        toKick.kick("Przykro mi, bot byles zbyt SUS").queue();
                        return channel.sendMessage("Koniec glosowania... " + Emojis.ALBANIAN_WAR_CRIMES);
                    }

                    return channel.sendMessage("Nastepnym razem... " + Emojis.ALBANIAN_WAR_CRIMES);
                });
    }

    private boolean isVotingSuccess(Message message) {
        var acceptables = Set.of(Emojis.THUMBS_UP.toString(), Emojis.THUMBS_DOWN.toString());
        var allReactions = message.getReactions().stream()
                .filter(r -> r.getReactionEmote().isEmoji())
                .filter(r -> acceptables.contains(r.getReactionEmote().getEmoji()))
                .collect(Collectors.toMap(r -> r.getReactionEmote().getEmoji(), r -> r.getCount()));

        return allReactions.get(Emojis.THUMBS_UP.toString()) > allReactions.get(Emojis.THUMBS_DOWN.toString());
    }

    private void queueWithDelay(Supplier<RestAction<?>> action, long delay) {
        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000 * delay);
                    action.get().queue();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void onMessageUpdate(MessageUpdateEvent event) {
        if (event.getMessage().getContentDisplay().replace(" ", "").contains("31")) {
            event.getChannel()
                    .sendMessage("wykryto i usunieto nielegalny numer <:ZbrodnieWojenneWAlbanii:823195443059752981>")
                    .queue();
            event.getMessage().delete().queue();
        }
    }

    @Override
    public void onReady(ReadyEvent event) {
        System.out.println("API is ready!");
    }

    private RestAction<Message> updateMessage(Message msg) {
        return msg.getChannel().retrieveMessageById(msg.getIdLong());
    }
}
