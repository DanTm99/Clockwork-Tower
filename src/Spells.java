/**
 * This class holds all of the spells in the game
 * and has accessor methods for them.
 */
public class Spells {
    private static final Spell[] SPELLS = {
            new Spell("illuminate", false), new Spell("identify", false), new Spell("eliminate", true), new Spell("rejuvenate", true), new Spell("mesmerize", true), new Spell("descend", true)
    };

    /**
     * Get an array containing the names of all of the spells in the game
     *
     * @return An array containing the names of all of the spells in the game
     */
    public static String[] getSpellNames() {
        int spellsLength = SPELLS.length;
        String[] spellNames = new String[spellsLength];

        for (int i = 0; i < spellsLength; i++) {
            spellNames[i] = SPELLS[i].getName();
        }

        return spellNames;
    }

    /**
     * Get a spell with a given name. If the spell doesn't exist return null.
     * @param spellName The name of the spell being searched for.
     * @return The spell being searched for
     */
    public static Spell getSpell(String spellName) {
        for (Spell spell : SPELLS) {
            if (spell.getName().equals(spellName)) return spell;
        }
        return null;
    }

}
