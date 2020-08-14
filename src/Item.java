
/**
 * This class represents an item in the game. Storing it's
 * name, weight, whether it can be picked up or not and
 * whether it can be used or not.
 */
public class Item {
    private final String name;
    private final int weight;
    private final boolean obtainable;
    private boolean usable;

    /**
     * Create an item, setting all of the variables.
     * @param name The name of the item.
     * @param weight The weight of the item.
     * @param obtainable Whether it item can be picked up or not.
     * @param usable Whether the item can be used or not.
     */
    public Item(String name, int weight, boolean obtainable, boolean usable) {
        this.name = name;
        this.weight = weight;
        this.obtainable = obtainable;
        this.usable = usable;
    }

    /**
     * Get the weight of the item.
     * @return The weight of the item.
     */
    public int getWeight() {
        return weight;
    }

    /**
     * Get the name of the item.
     * @return The name of the item.
     */
    public String getName() {
        return name;
    }

    /**
     * Get whether the item is obtainable or not.
     * @return Whether or not the item is obtainable.
     */
    public boolean getObtainable() {
        return obtainable;
    }

    /**
     * Set usable to a given value.
     * @param usable The value to set it to
     */
    public void setUsable(boolean usable) {
        this.usable = usable;
    }

    /**
     * Get whether or not the item is usable.
     * @return Whether or not the item is usable.
     */
    public boolean getUsable() {
        return usable;
    }
}
