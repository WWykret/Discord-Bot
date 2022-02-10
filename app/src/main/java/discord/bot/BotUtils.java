package discord.bot;

import java.util.function.Supplier;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.requests.RestAction;

public class BotUtils {
    public static RestAction<Message> updateMessage(Message msg) {
        return msg.getChannel().retrieveMessageById(msg.getIdLong());
    }

    public static void queueWithDelay(Supplier<RestAction<?>> action, long delay) {
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
}
