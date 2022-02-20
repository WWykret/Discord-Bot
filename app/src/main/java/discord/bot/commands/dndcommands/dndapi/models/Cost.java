package discord.bot.commands.dndcommands.dndapi.models;

public record Cost(
    int quantity,
    String unit
) {
    @Override
    public String toString() {
        return quantity + " " + unit;
    }
}
