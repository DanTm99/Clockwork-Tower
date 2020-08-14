import java.util.ArrayList;

/**
 * This class stores items with a limited weight capacity.
 * It also includes methods that allow you to access and
 * mutate the inventory in different ways.
 */
public class Inventory {
    private final ArrayList<Item> items;
    private final int maxSize;

    /**
     * Create an instance on Inventory, setting it's maximum size.
     * @param maxSize The highest sum of weights in the inventory.
     */
    public Inventory(int maxSize) {
        items = new ArrayList<>();
        this.maxSize = maxSize;
    }

    /**
     * Add an item to the inventory. If adding it would increase.
     * the weight of the inventory past it's max, don't add it.
     * @param item The item to add.
     * @return Whether or not the item was successfully added.
     */
    public boolean addItem(Item item) {
        if (!item.getObtainable()) {
            return false;
        }

        if (item.getWeight() + getTotalWeight() > maxSize) {
            return false;
        }

        items.add(item);
        return true;
    }

    /**
     * Return the sum of the weights of the items in the inventory.
     * @return The total weight of the inventory.
     */
    private int getTotalWeight() {
        int totalWeight = 0;
        for (Item item : items) {
            totalWeight += item.getWeight();
        }
        return totalWeight;
    }

    /**
     * Return max size.
     * @return The maximum size of the inventory.
     */
    public int getMaxSize() {
        return maxSize;
    }

    /**
     * Print the names of the items in the inventory,
     * each of which is followed by it's weight.
     */
    public void showItemsAndWeights() {
        System.out.println("");
        for (Item item : items) {
            System.out.println(item.getName() + ", weight: " + item.getWeight());
        }
        System.out.println();
    }

    /**
     * Remove the first item in the inventory with a specified name.
     *
     * @param itemName The name of the item to remove.
     */
    public void removeItem(String itemName) {
        items.remove(getItem(itemName));
    }

    /**
     * Return the number of items in the list with a given name.
     *
     * @param itemName The name of the items to search for.
     * @return The number of items with the given name.
     */
    public int numberOfItem(String itemName) {
        int itemCount = 0;
        for (Item item : items) {
            if (item.getName().equals(itemName)) {
                itemCount++;
            }
        }
        return itemCount;
    }

    /**
     * Return the first item in the inventory that has a specified name.
     * If there is no item with that name, return null.
     *
     * @param itemName The name of the item to search for.
     * @return The with the give name.
     */
    public Item getItem(String itemName) {
        for (Item item : items) {
            if (item.getName().equals(itemName)) {
                return item;
            }
        }
        return null;
    }

    /**
     * Check if the inventory contains an item with a given name.
     * @param itemName The name of the item.
     * @return Whether or not the inventory contains the item.
     */
    public boolean hasItem(String itemName) {
        return getItem(itemName) != null;
    }
}
