import java.util.Set;
import java.util.HashMap;

/**
 * Class Room - a room in an adventure game.
 * <p>
 * This class is part of the "World of Zuul" application.
 * "World of Zuul" is a very simple, text based adventure game.
 * <p>
 * A "Room" represents one location in the scenery of the game.  It is
 * connected to other rooms via exits.  For each existing exit, the room
 * stores a reference to the neighboring room.
 *
 * @author Michael Kölling and David J. Barnes
 * @version 2016.02.29
 */

public class Room {
    private final String roomName;
    private final String description;
    private final HashMap<String, Room> exits;        // stores exits of this room.
    private final HashMap<String, Item> items;        // stores items in each room
    private boolean dark;
    private boolean entered;

    /**
     * Create a room described "description". Initially, it has
     * no exits. "description" is something like "a kitchen" or
     * "an open court yard". Set whether it is dark or not and
     * initially it contains no items. And it's name.
     *
     * @param roomName The name of the room.
     * @param description The room's description.
     * @param dark        Whether the room is dark or not.
     */
    public Room(String roomName, String description, boolean dark) {
        this.roomName = roomName;
        this.description = description;
        this.dark = dark;
        entered = false;
        exits = new HashMap<>();
        items = new HashMap<>();
    }

    /**
     * @return The name of the room.
     */
    public String getName() {
        return roomName;
    }

    /**
     * @return Whether or not the room have been entered.
     */
    public boolean getEntered() {
        return entered;
    }

    /**
     * Set entered to true, so it's known that the room
     * has been entered by the player.
     */
    public void enter() {
        entered = true;
    }

    /**
     * Define an exit from this room.
     *
     * @param direction The direction of the exit.
     * @param neighbor  The room to which the exit leads.
     */
    public void setExit(String direction, Room neighbor) {
        exits.put(direction, neighbor);
    }

    /**
     *
     */
    public Set<String> getPossibleExits() {
        return exits.keySet();
    }

    /**
     * Remove an exit from this room.
     * @param direction The direction the room other room is
     */
    public void removeExit(String direction) {
        exits.remove(direction);
    }

    /**
     * Return a description of the room. If it's
     * not dark also, return the exits on the next line
     *
     * @return A String describing the room
     */
    public String getDescription() {
        return "You are " + (dark ? "in a dark room" : this.description + ".\n" + getExitString());
    }

    /**
     * Return a string describing the room's exits, for example
     * "Exits: north west".
     *
     * @return Details of the room's exits.
     */
    private String getExitString() {
        StringBuilder returnString = new StringBuilder("Exits:");
        Set<String> keys = exits.keySet();
        for (String exit : keys) {
            returnString.append(" ").append(exit);
        }
        return returnString.toString();
    }

    /**
     * Return the room that is reached if we go from this room in direction
     * "direction". If there is no room in that direction, return null.
     *
     * @param direction The exit's direction.
     * @return The room in the given direction.
     */
    public Room getExit(String direction) {
        return exits.get(direction);
    }

    /**
     * return if the room is dark or not.
     *
     * @return Whether the room is dark or not.
     */
    public boolean getDark() {
        return dark;
    }

    /**
     * Make the room bright (so not dark).
     */
    public void makeBright() {
        dark = false;
    }

    /**
     * Add an item to the room.
     *
     * @param item The item to be added.
     */
    public void addItem(Item item) {
        items.put(item.getName(), item);
    }

    /**
     * Remove an item from the room.
     *
     * @param item The item to be removed.
     */
    public void removeItem(Item item) {
        items.remove(item.getName());
    }

    /**
     * Print the names of the items in the room that can
     * be picked up.
     */
    public void showItems() {
        Boolean noItems = true;
        StringBuilder printString = new StringBuilder("Items:");

        for (Item item : items.values()) {
            if (item.getObtainable()) {
                printString.append("\n").append(item.getName());
            }
            noItems = false;
        }

        if (!noItems) {
            System.out.println(printString);
        }
    }

    /**
     * Print the names of the items in the room that can't
     * be picked up.
     */
    public void showObjects() {
        Boolean noObjects = true;
        StringBuilder printString = new StringBuilder("Objects:");

        for (Item item : items.values()) {
            if (!item.getObtainable()) {
                printString.append("\n").append(item.getName());
            }
            noObjects = false;
        }

        if (!noObjects) {
            System.out.println(printString);
        }
    }

    /**
     * Return true if the room contains a given item.
     *
     * @param itemName The name of the item you're checking for.
     * @return Whether or not the room contains the item.
     */
    public boolean containsItem(String itemName) {
        return items.containsKey(itemName);
    }

    /**
     * Returns the item specified. If it isn't in the
     * room, return null.
     *
     * @param itemName The name of the item.
     * @return The item being searched for.
     */
    public Item getItem(String itemName) {
        return items.get(itemName);
    }

    /**
     * Whether or not the room has no items.
     * @return A boolean that's true if the room has no items.
     */
    public boolean isEmpty() {
        return items.isEmpty();
    }
}

