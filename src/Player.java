import java.util.HashSet;
import java.util.Stack;
import java.util.Arrays;

/**
 * This class represents the player of the game,
 * storing their name, the room they are currently in,
 * their inventory, usable spells, the rooms the player
 * has been in, and how many coins the player has collected.
 */
public class Player {
    private Room currentRoom;           // The room the player is currently in
    private final Inventory inventory;        // The player's inventory
    private final HashSet<String> spellbook;  // The names of the spells the player can use
    private final Stack<Room> previousRooms;  // The previous rooms the player has been in
    private String name;          // The name of the player
    private int balance;                // The coin balance of the player

    /**
     * Create a new instance of the player, their name
     * initially not set.
     * @param inventorySize The size of the player's inventory
     * @param spellNames An array contain the names of the spells the player can use
     */
    Player(int inventorySize, String[] spellNames) {
        inventory = new Inventory(inventorySize);
        spellbook = new HashSet<>(Arrays.asList(spellNames));
        previousRooms = new Stack<>();
        name = "";
        balance = 0;
        setupInventory();
    }

    /**
     * Initialize the player's inventory, creating items they begin with
     */
    private void setupInventory() {
        inventory.addItem(new Item("compass", 0, true, false));
        inventory.addItem(new Item("spellbook", 1, true, true));
        inventory.addItem(new Item("coin_pouch", 1, true, false));
    }

    /**
     * Add an item to the player's inventory.
     * @param item The item to add.
     * @return Whether or not this was successful.
     */
    public boolean addItem(Item item) {
        return inventory.addItem(item);
    }

    /**
     * Set the name of the player to a given String.
     * @param name The name of the player.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the name of the player.
     * @return The name of the player.
     */
    public String getName() {
        return name;
    }

    /**
     * Get the room the player is currently in.
     * @return The room the player is currently in.
     */
    public Room getCurrentRoom() {
        return currentRoom;
    }

    /**
     * Check if the player's inventory has an item with a given name.
     * @param itemName The name of the item.
     * @return Whether or not the player's inventory contains it.
     */
    public boolean inventoryHas(String itemName) {
        return inventory.hasItem(itemName);
    }

    /**
     * Get the room the player was in before this one.
     * @return The room the player was in.
     */
    public Room getPreviousRoom() {
        return previousRooms.peek();
    }

    /**
     * Remove a spell from the player's spellbook.
     * @param spellName The name of the spell to remove.
     */
    public void removeSpell(String spellName) {
        spellbook.remove(spellName);
    }

    /**
     * Print all the spells the player can currently use.
     */
    public void printAvailableSpells() {
        for (String spellName : spellbook) {
            System.out.println(spellName);
        }
    }

    /**
     * Check if the player's spellbook has a spell with a given name.
     * @param spellName The name of the spell.
     * @return Whether or not the player's spellbook contains it.
     */
    public boolean hasSpell(String spellName) {
        return spellbook.contains(spellName);
    }

    /**
     * Get the current size of the player's inventory.
     * @return The current size of the player's inventory.
     */
    public int getInventorySize() {
        return inventory.getMaxSize();
    }

    /**
     * Return the coin balance of the player.
     * @return The coin balance of the player.
     */
    public int getBalance() {
        return balance;
    }

    /**
     * Print the contents of the player's inventory along with
     * the wights of each item.
     */
    public void showInventory() {
        inventory.showItemsAndWeights();
    }

    /**
     * Set the current room of the player after clearing
     * the previous rooms the player has been in.
     * @param room The room the player moves to.
     */
    public void goToRoom(Room room) {
        previousRooms.clear();
        currentRoom = room;
    }

    /**
     * Set the current room of the player after adding
     * the room the player was just in to previousRooms.
     * @param room The room the player moves to.
     */
    public void walkToRoom(Room room) {
        previousRooms.push(currentRoom);
        currentRoom = room;
    }

    /**
     * Clear the previous rooms the player has been in.
     */
    public void clearPreviousRooms() {
        previousRooms.clear();
    }

    /**
     * Return the number of items with a given name
     * are in the player's inventory.
     * @param itemName The name of the item being searched for.
     * @return The number of items in the player's inventory with that name.
     */
    public int itemCount(String itemName) {
        return inventory.numberOfItem(itemName);
    }

    /**
     * Remove an item with a given name from the player's inventory
     * @param itemName The name of the item to remove
     */
    public void removeItem(String itemName) {
        inventory.removeItem(itemName);
    }

    /**
     * Return an item with a give name in the inventory.
     * @param itemName The name of the item to return.
     * @return The item being searched for.
     */
    public Item getItem(String itemName) {
        return inventory.getItem(itemName);
    }

    /**
     * Return whether or not the player's inventory
     * contains and item with a given name.
     * @param itemName The name of the item being searched for.
     * @return Whether or not the player's inventory contains the item.
     */
    public boolean hasItem(String itemName) {
        return inventory.hasItem(itemName);
    }

    /**
     * Go to the room you were in before this one.
     * If used again, travel backwards along the path
     * taken throughout this game. If previousRooms is
     * empty, return false. Otherwise return true.
     *
     * @return Whether or not the player successfully moved to the previous room
     */
    public boolean goBack() {
        if (previousRooms.empty()) {
            return false;
        } else {
            currentRoom = previousRooms.pop();
            return true;
        }
    }

    /**
     * Increase the player's coin balance by 1.
     */
    public void incrementBalance() {
        balance++;
    }

}
