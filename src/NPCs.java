import java.util.HashSet;

/**
 * This class stores the NPCs in the game,
 * and allows you to perform commands on them.
 */
public class NPCs {
    private final HashSet<NPC> npcs;

    /**
     * Create an instance of NPCs
     * @param npcsSet a currently empty set of all
     *                NPCs in the game
     */
    public NPCs(HashSet<NPC> npcsSet) {
        npcs = npcsSet;
    }


    /**
     * Get an NPC from it's name. If there is no NPC with the
     * specified name, return null.
     * @param npcName The name of the NPC you're searching for.
     * @return The NPC being searched for.
     */
    public NPC getNpc(String npcName) {
        for (NPC npc : npcs) {
            if (npc.getName().equals(npcName)) {
                return npc;
            }
        }
        return null;
    }

    /**
     * Return the NPC in a given room. If there are
     * multiple, return a random one.
     * @param room The room to search for and NPC in.
     * @return The NPC in the given room.
     */
    public NPC npcInRoom(Room room) {
        for (NPC npc : npcs) {
            if (npc.getCurrentRoom() == room) {
                return npc;
            }
        }
        return null;
    }

    /**
     * Remove an NPC from the game.
     * @param npc The NPC to remove.
     */
    public void removeNpc(NPC npc) {
        npc.removeFromMap();
        npcs.remove(npc);
    }

    /**
     * Check if there is an NPC in a given room.
     * @param room The room to search for NPCs in.
     * @return Whether or not the room contains an NPC.
     */
    public boolean isNpcInRoom(Room room) {
        for (NPC npc : npcs) {
            if (npc.getCurrentRoom() == room) {
                return true;
            }
        }
        return false;
    }

    /**
     * Move all of the NPCs to the next room in their route.
     */
    public void moveNpcs() {
        for (NPC npc : npcs) {
            npc.moveAlongRoute();
        }
    }
}
