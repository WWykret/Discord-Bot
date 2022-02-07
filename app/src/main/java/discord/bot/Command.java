package discord.bot;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageReference;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.restaction.MessageAction;


enum Emojis {
    THUMBS_UP("\uD83D\uDC4D"),
    THUMBS_DOWN("\uD83D\uDC4E");

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
                    .sendMessage("kocham jave kocham jave kocham jave <:javaSocialCredits:901211051369054208>").queue();
        }

        if (msg.replace(" ", "").contains("31")) {
            event.getChannel()
                    .sendMessage("wykryto i usunieto nielegalny numer <:ZbrodnieWojenneWAlbanii:823195443059752981>").queue();
            event.getMessage().delete().queue();
        }

        if (msg.indexOf("!vote kick") == 0) {
            var toKick = event.getMessage().getMentionedMembers().stream().findAny().orElse(null);
            if (toKick != null) {
                event.getChannel().sendMessage("Kickujemy " + toKick.getNickname() + "? (30s na glosowanie)").
                queue(message -> {
                        message.addReaction(Emojis.THUMBS_UP.toString()).queue();
                        message.addReaction(Emojis.THUMBS_DOWN.toString()).queue();
                        new Thread(() -> countKickVotes(30, message.getChannel(), message.getIdLong(), toKick)).start();
                    }
                );
            }
        }
    }

    private void countKickVotes(float timeInSecs, MessageChannel channel, long msgId, Member toKick) {
        try {
            Thread.sleep((int)(1000 * timeInSecs));
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        var message = channel.retrieveMessageById(msgId);
        message.queue(msg ->
            {
                var acceptables = Set.of(Emojis.THUMBS_UP.toString(), Emojis.THUMBS_DOWN.toString());
                Map<String, Integer> allReactions = msg.getReactions().stream()
                .filter(r -> r.getReactionEmote().isEmoji())
                .filter(r -> acceptables.contains(r.getReactionEmote().getEmoji()))
                .collect(Collectors.toMap(r -> r.getReactionEmote().getEmoji(), r -> r.getCount()));
                
                if (allReactions.get(Emojis.THUMBS_UP.toString()) > allReactions.get(Emojis.THUMBS_DOWN.toString())) {
                    channel.sendMessage("Koniec glosowania... <:ZbrodnieWojenneWAlbanii:823195443059752981>").queue();
                    toKick.kick("Przykro mi, bot byles zbyt SUS").queue();
                } else {
                    channel.sendMessage("Nastepnym razem... <:ZbrodnieWojenneWAlbanii:823195443059752981>").queue();
                }
            }
        );
    }

    public void onMessageUpdate(MessageUpdateEvent event) {
        if (event.getMessage().getContentDisplay().replace(" ", "").contains("31")) {
            event.getChannel()
                    .sendMessage("wykryto i usunieto nielegalny numer <:ZbrodnieWojenneWAlbanii:823195443059752981>").queue();
            event.getMessage().delete().queue();
        }
    }

    @Override
    public void onReady(ReadyEvent event) {
        System.out.println("API is ready!");
    }
}
