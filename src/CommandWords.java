/**
 * This class is part of the "World of Zuul" application.
 * "World of Zuul" is a very simple, text based adventure game.
 * <p>
 * This class holds an enumeration of all command words known to the game.
 * It is used to recognise commands as they are typed in.
 *
 * @author Michael Kölling and David J. Barnes
 * @version 2016.02.29
 */

public class CommandWords {
    // a constant array that holds all valid command words
    private static final String[] COMMANDS = {
            "go", "quit", "help", "investigate", "cast", "take", "use", "inventory", "back", "drop"
    };

    /**
     * Check whether a given String is a valid command word.
     *
     * @return true if it is, false if it isn't.
     */
    public static boolean isCommand(String aString) {
        //for(int i = 0; i < COMMANDS.length; i++) {
        for (String command : COMMANDS) {
            if (command.equals(aString))
                return true;
        }
        // if we get here, the string was not found in the commands
        return false;
    }

    /**
     * Print all valid commands to System.out.
     */
    public static void showAll() {
        for (String command : COMMANDS) {
            System.out.print(command + "  ");
        }
        System.out.println();
    }
}
