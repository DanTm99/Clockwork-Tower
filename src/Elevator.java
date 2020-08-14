import java.util.ArrayList;

/**
 * This class is a type of room that can 'move', changing
 * it's exit, allowing the traversal between normally
 * unconnected 'chunks' of rooms
 */
public class Elevator extends Room {
    private int currentFloorNumber; //The floor the elevator is currently on
    private final ArrayList<Room> floorExits; //The exits the elevator can have, based on it's floor

    /**
     * Alongside using the constructor of Room, the floor
     * it starts that is also set here. This refers to which
     * of the floorExits is 'active'.
     * @param roomName The name of the room.
     * @param description A description of the room.
     * @param dark Whether or not the room is dark.
     * @param startingFloorNumber The floor at which the elevator starts.
     */
    public Elevator(String roomName, String description, boolean dark, int startingFloorNumber) {
        super(roomName, description, dark);
        this.currentFloorNumber = startingFloorNumber;
        floorExits = new ArrayList<>();
    }

    /**
     * Add an exit to the list of exits the elevator can 'move' between.
     * @param exitRoom The room to add to the list.
     */
    public void addFloorExit(Room exitRoom) {
        floorExits.add(exitRoom);
    }

    /**
     * Return The room corresponding to the given floor number.
     * @param floorNumber The floor number you're searching at.
     * @return The Room at the floor.
     */
    private Room getFloorExit(int floorNumber) {
        return floorExits.get(floorNumber - 1);
    }

    /**
     * Return the room that is currently the Elevator's exit.
     * @return The room that is the exit.
     */
    private Room getCurrentFloorExit() {
        return getFloorExit(currentFloorNumber);
    }

    /**
     * Move the elevator to a given floor.
     * @param floorNumber The number of the floor.
     */
    public boolean move(int floorNumber) {

        if (!(currentFloorNumber == floorNumber)) {
            getCurrentFloorExit().removeExit("south");
            setExit("north", getFloorExit(floorNumber));
            getCurrentFloorExit().setExit("south", this);
            currentFloorNumber = floorNumber;
            return true;
        } else {
            return false;
        }
    }

    /**
     * If the player enters the Elevator from a room that
     * can be one of it's exits. Move the elevator so that
     * that room is now the exit.
     * @param room The room the player is entering from.
     */
    public void enterFromRoom(Room room) {
        if (floorExits.contains(room)) {
            move(floorExits.indexOf(room) + 1);
        }
    }
}
