package discord.bot.commands;

import discord.bot.CommandPermissions;
import discord.bot.HelpSupport;

import static discord.bot.CommandPermissions.Permission.USER;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class HangmanCommand extends BotCommand implements HelpSupport {

    private static final int maxLives = 10;

    private List<String> allWords = null;
    private Map<MessageChannel, String> currentWord = new HashMap<>();
    private Map<MessageChannel, List<Character>> guessedChars = new HashMap<>();
    private Map<MessageChannel, Integer> lives = new HashMap<>();

    public HangmanCommand() {
        Path wordsPath = Paths.get("app","src","main","resources","hangman-words");
        try {
            allWords = Files.readAllLines(wordsPath).stream().filter(s -> s.length() < 15 && s.length() > 5).toList();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void onMessageReceived(MessageReceivedEvent event) {

        String msg = event.getMessage().getContentDisplay();

        if (msg.toLowerCase().equals(commandPrefix + "hangman start")) {
            if (!CommandPermissions.hasPermission(event.getGuild(), event.getMember(), USER)) {
                CommandPermissions.sendLackOfAccessMsg(event.getChannel());
                return;
            }

            beginHangmanGame(event.getChannel());
            sendStatisticsMessage(event.getChannel());
        } else if (msg.toLowerCase().indexOf(commandPrefix + "hangman guess ") == 0) {
            if (!CommandPermissions.hasPermission(event.getGuild(), event.getMember(), USER)) {
                CommandPermissions.sendLackOfAccessMsg(event.getChannel());
                return;
            }

            String[] args = msg.split("\\s+");
            if (args.length != 3) return;

            MessageChannel channel = event.getChannel();
            makeAGuess(channel, args[2]);
            sendStatisticsMessage(channel);
            checkIfLost(channel);
            checkIfWon(channel);
        }
    }

    private void checkIfLost(MessageChannel channel) {
        String wordToGuess = currentWord.getOrDefault(channel, null);
        int currLives = lives.getOrDefault(channel, -1);
        
        if (wordToGuess == null) return;

        if (currLives <= 0) {
            channel.sendMessageFormat("Nie tym razem! Szukane slowo: `%s`", wordToGuess).queue();
            resetGame(channel);
        }
    }

    private void checkIfWon (MessageChannel channel) {
        String wordToGuess = currentWord.getOrDefault(channel, null);
        List<Character> charsSoFar = guessedChars.getOrDefault(channel, null);
        int currLives = lives.getOrDefault(channel, -1);

        if (wordToGuess == null) return;

        boolean won = true;
        for (char letter : wordToGuess.toCharArray()) {
            if (!charsSoFar.contains(letter)) won = false;
        }

        if (won) {
            channel.sendMessageFormat("Brawo, zgadles! Pozostale zycia: `%d`", currLives).queue();
            resetGame(channel);
        }
    }

    private void resetGame(MessageChannel channel) {
        currentWord.put(channel, null);
        guessedChars.put(channel, null);
        lives.put(channel, -1);
    }

    private void makeAGuess(MessageChannel channel, String guess) {
        String wordToGuess = currentWord.getOrDefault(channel, null);

        if (wordToGuess == null) return;

        if (guess.length() == 1) guessChar(channel, guess);
        else guessWord(channel, guess); 
    }

    private void guessChar(MessageChannel channel, String guess) {
        String wordToGuess = currentWord.getOrDefault(channel, null);
        List<Character> charsSoFar = guessedChars.getOrDefault(channel, null);
        int currLives = lives.getOrDefault(channel, -1);

        char charToGuess = guess.toLowerCase().charAt(0);

        if (!Character.isLetter(charToGuess) || charsSoFar.contains(charToGuess)) return;

        charsSoFar.add(charToGuess);
        if (wordToGuess.toLowerCase().indexOf(charToGuess) == -1) lives.put(channel, currLives - 1);
    }

    private void guessWord(MessageChannel channel, String guess) {
        String wordToGuess = currentWord.getOrDefault(channel, null);
        List<Character> charsSoFar = guessedChars.getOrDefault(channel, null);
        int currLives = lives.getOrDefault(channel, -1);

        if (!wordToGuess.toLowerCase().equals(guess.toLowerCase())) {
            lives.put(channel, currLives - 1);
            return;
        }

        for (char letter : wordToGuess.toLowerCase().toCharArray()) {
            if (!charsSoFar.contains(letter)) charsSoFar.add(letter);
        }
    }

    private void beginHangmanGame(MessageChannel channel) {
        if (allWords == null) return;

        String selectedWord = allWords.get(new Random().nextInt(allWords.size()));
        
        currentWord.put(channel, selectedWord);
        guessedChars.put(channel, new ArrayList<>());
        lives.put(channel, maxLives);
    }

    private void sendStatisticsMessage(MessageChannel channel) {
        String wordToGuess = currentWord.getOrDefault(channel, null);
        List<Character> charsSoFar = guessedChars.getOrDefault(channel, null);
        int currLives = lives.getOrDefault(channel, -1);

        if (wordToGuess == null) {
            channel.sendMessage("Nie zaczeto jeszcze zadnej gry").queue();
            return;
        }

        String wordToDisplay = encodeString(wordToGuess, charsSoFar);
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.addField("Slowo:", wordToDisplay, true);
        String charsAsString = charsSoFar.stream().map(c -> c.toString()).collect(Collectors.joining(", "));
        embedBuilder.addField("Probowane litery:", charsAsString, false);
        embedBuilder.addField("Pozostale zycia:", String.valueOf(currLives), true);

        channel.sendMessageEmbeds(embedBuilder.build()).queue();
    }

    private String encodeString(String toEncode, List<Character> knownChars) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < toEncode.length(); i++) {
            char currentChar = toEncode.charAt(i);
            if (knownChars.contains(currentChar)) result.append(currentChar);
            else result.append("\\_ ");
        }
        return result.toString();
    }

    @Override
    public String commandName() {
        return "hangman";
    }

    @Override
    public String commandUsage() {
        return """
        .hangman start
        .hangman guess [litera|haslo]""";
    }

    @Override
    public String helpMessage() {
        return "pozwala rozpoczac i prowadzic gre w wisielca z botem.";
    }
}
