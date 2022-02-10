package discord.bot;

public enum Emojis {
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