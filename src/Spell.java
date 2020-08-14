
/**
 * This class represents a spell in the game,
 * storing it's name and whether or not it
 * is a single use spell
 */
public class Spell {
    private final String name;
    private final boolean singleUse;

    /**
     * Create a spell, setting all of it's variables.
     * @param name The name of the spell.
     * @param singleUse Whether or not the spell can only be used once.
     */
    public Spell(String name, boolean singleUse) {
        this.name = name;
        this.singleUse = singleUse;
    }

    /**
     * Get the name of the spell.
     * @return The name of the spell.
     */
    public String getName() {
        return name;
    }

    /**
     * Get whether or not the spell is a single use spell.
     * @return whether or not the spell is a single use spell.
     */
    public boolean getSingleUse() {
        return singleUse;
    }

}
