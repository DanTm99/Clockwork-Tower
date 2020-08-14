import java.util.Stack;

/**
 * This class represents an NPC in the game (Non playable character),
 * storing it's name, the room it is in, the rooms it will
 * move along, and whether or not it is asleep.
 */
public class NPC {
    private final String name;
    private Room currentRoom;
    private final Stack<Room> route;
    private boolean asleep;

    /**
     * Create an instance of NPC, setting it's name and route.
     * Set it's current room to that at the top of the stack,
     * and asleep to false.
     *
     * @param name The name of the NPC.
     * @param route The rooms it will travel along throughout the game.
     */
    public NPC(String name, Stack<Room> route) {
        this.name = name;
        currentRoom = route.pop();
        this.route = route;
        asleep = false;
    }

    /**
     * If the NPC is awake move it to the next room in it's route.
     * and remove that room from the upcoming.
     */
    public void moveAlongRoute() {
        if (!route.empty() && !asleep) {
            currentRoom = route.pop();
        }
    }

    /**
     * Return the room the NPC is currently in.
     * @return The room the NPC is currently in.
     */
    public Room getCurrentRoom() {
        return currentRoom;
    }

    /**
     * Return the name of the NPC.
     * @return the name of the NPC.
     */
    public String getName() {
        return name;
    }

    /**
     * Set the NPCs current room to null, removing
     * it from the game's internal map.
     */
    public void removeFromMap() {
        currentRoom = null;
    }

    /**
     * Wake up the NPC, setting asleep to false.
     */
    public void awaken() {
        asleep = false;
    }

    /**
     * Put the NPC to sleep, setting sleep to true.
     */
    public void sleep() {
        asleep = true;
    }

    /**
     * Return whether or not the NPC is asleep.
     * @return whether or not the NPC is asleep.
     */
    public boolean getAsleep() {
        return asleep;
    }
}
