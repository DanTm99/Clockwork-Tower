import java.util.Scanner;

/**
 * This class is part of the "World of Zuul" application.
 * "World of Zuul" is a very simple, text based adventure game.
 * <p>
 * This parser reads user input and tries to interpret it as an "Adventure"
 * command. Every time it is called it reads a line from the terminal and
 * tries to interpret the line as a two word command. It returns the command
 * as an object of class Command.
 * <p>
 * The parser has a set of known command words. It checks user input against
 * the known commands, and if the input is not one of the known commands, it
 * returns a command object that is marked as an unknown command.
 *
 * @author Michael Kölling and David J. Barnes
 * @version 2016.02.29
 */
public class Parser {
    private final Scanner reader;         // source of command input

    /**
     *
     */
    public Parser() {
        reader = new Scanner(System.in);
    }

    /**
     * Converts user input into a command, ignoring any words after the third
     * @return The Command constructed from the user input
     */
    public Command getCommand() {
        String inputLine;   // will hold the full input line
        String word1 = null;
        String word2 = null;
        String word3 = null;

        System.out.print("> ");     // print prompt

        inputLine = reader.nextLine();

        // Find up to two words on the line.
        Scanner tokenizer = new Scanner(inputLine);
        if (tokenizer.hasNext()) {
            word1 = tokenizer.next().toLowerCase();      // get first word
            if (tokenizer.hasNext()) {
                word2 = tokenizer.next().toLowerCase();      // get second word
                if (tokenizer.hasNext()) {
                    word3 = tokenizer.next().toLowerCase();      // get third word
                    // note: we just ignore the rest of the input line.
                }
            }
        }

        // Now check whether this word is known. If so, create a command
        // with it. If not, create a "null" command (for unknown command).
        if (CommandWords.isCommand(word1)) {
            return new Command(word1, word2, word3);
        } else {
            return new Command(null, word2, word3);
        }
    }

    /**
     * Print all of the command words.
     */
    public void showCommands() {
        CommandWords.showAll();
    }
}
