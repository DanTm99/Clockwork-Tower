import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Stack;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.io.BufferedReader;

/**
 * This class is based on the "World of Zuul" application.
 * It is the main class of "The Clockwork Tower"; a text bases adventure
 * game where users can walk from room to room and have to complete
 * certain objectives to escape the tower and win the game
 * <p>
 * To play this game, create an instance of this class and call the "play"
 * method.
 *
 * @author Michael Kölling and David J. Barnes
 * @version 2016.02.29
 */

public class Game {
    private final Parser parser;              // An instance of the parser class
    private final ArrayList<Room> rooms;      // A list of rooms in the game
    private boolean bossIdentified;     // Whether the boss of the game has been identified or not
    private NPCs npcs;                  // An instance of the NPCs class
    private final Scanner scanner;
    private final Player player;

    /**
     * Create the game and initialise its internal map,
     * the player, NPCs, and items.
     */
    public Game() {
        player = new Player(10, Spells.getSpellNames());
        rooms = new ArrayList<>();
        parser = new Parser();
        scanner = new Scanner(System.in);
        setupRooms();
        setupItems();
        setupNpcs();
        bossIdentified = false;
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void setupRooms() {
        Room stoneRoom, chestRoom, shop, workshop, library, armory,
                soldiersQuarters, lab, dungeon, trollsRoom, theatre, bossRoom, teleporterRoom;

        Elevator elevator;
        // create the rooms

        elevator = new Elevator("elevator", "in an elevator", false, 1);

        stoneRoom = new Room("stoneRoom", "in an empty room with stone walls", true);
        chestRoom = new Room("chestRoom", "in a room containing many old, opened chests", false);
        shop = new Room("shop", "in a small shop", false);
        workshop = new Room("workshop", "in a metal workshop containing a high tech forge", true);

        library = new Room("library", "in a library", false);
        armory = new Room("armory", "in an armory", false);
        soldiersQuarters = new Room("soldiersQuarters", "in the soldiers' quarters", false);
        lab = new Room("lab", "in a science lab", true);
        dungeon = new Room("dungeon", "in a dungeon", true);

        trollsRoom = new Room("trollsRoom", "in a narrow hallway", false);
        theatre = new Room("theatre", "in a theatre. You can feel an ominous presence nearby", true);
        bossRoom = new Room("bossRoom", "in a large lavish room", false);

        teleporterRoom = new Room("teleporterRoom", "in a room filled with a bright light", false);

        // setup rooms HashMap
        rooms.add(elevator);

        rooms.add(stoneRoom);
        rooms.add(chestRoom);
        rooms.add(shop);
        rooms.add(workshop);

        rooms.add(library);
        rooms.add(armory);
        rooms.add(soldiersQuarters);
        rooms.add(lab);
        rooms.add(dungeon);

        rooms.add(trollsRoom);
        rooms.add(theatre);
        rooms.add(bossRoom);

        rooms.add(teleporterRoom);

        // initialise room exits
        elevator.setExit("north", workshop);

        // floor 1
        stoneRoom.setExit("east", chestRoom);

        chestRoom.setExit("west", stoneRoom);
        chestRoom.setExit("north", shop);
        chestRoom.setExit("east", workshop);

        shop.setExit("south", chestRoom);

        workshop.setExit("west", chestRoom);
        workshop.setExit("south", elevator);
        workshop.setExit("east", teleporterRoom);

        teleporterRoom.setExit("west", workshop);

        // floor 2
        library.setExit("north", armory);
        library.setExit("south", elevator);

        armory.setExit("north", lab);
        armory.setExit("east", dungeon);
        armory.setExit("west", soldiersQuarters);
        armory.setExit("south", library);

        lab.setExit("south", armory);

        soldiersQuarters.setExit("east", armory);

        dungeon.setExit("west", armory);

        // floor 3
        trollsRoom.setExit("south", elevator);
        trollsRoom.setExit("north", theatre);

        theatre.setExit("east", bossRoom);
        theatre.setExit("south", trollsRoom);

        bossRoom.setExit("west", theatre);

        // set elevator exits
        elevator.addFloorExit(workshop);
        elevator.addFloorExit(library);
        elevator.addFloorExit(trollsRoom);

        player.goToRoom(stoneRoom);  // start game in the stone room
    }

    /**
     * Create and add the items to the map.
     */
    private void setupItems() {
        rooms.get(6).addItem(new Item("iron_sword", 3, true, false));
        rooms.get(8).addItem(new Item("oil_can", 2, true, true));
        rooms.get(1).addItem(new Item("gold_coin", 0, true, false));
        rooms.get(2).addItem(new Item("gold_coin", 0, true, false));
        rooms.get(0).addItem(new Item("gold_coin", 0, true, false));
        rooms.get(5).addItem(new Item("gold_coin", 0, true, false));
        rooms.get(9).addItem(new Item("gold_coin", 0, true, false));
        rooms.get(10).addItem(new Item("gold_coin", 0, true, false));
        rooms.get(9).addItem(new Item("platinum_piece", 2, true, false));
        rooms.get(4).addItem(new Item("platinum_piece", 2, true, false));
        rooms.get(7).addItem(new Item("platinum_piece", 2, true, false));

        rooms.get(0).addItem(new Item("lever_1", 0, false, true));
        rooms.get(0).addItem(new Item("lever_2", 0, false, true));
        rooms.get(0).addItem(new Item("lever_3", 0, false, false));
        rooms.get(4).addItem(new Item("forge", 0, false, true));
        rooms.get(5).addItem(new Item("magic_book", 0, false, true));
        rooms.get(5).addItem(new Item("riddle_book", 0, false, true));
        rooms.get(13).addItem(new Item("glowing_pillar", 0, false, true));
    }

    /**
     * Main play routine.  Loops until end of play.
     */
    public void play() {
        boolean finished;
        playStart();
        wait(2000);
        finished = initialCutscene();
        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.

        while (!finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
            npcs.moveNpcs();
        }
        System.out.println("Thank you for playing.  Good bye.");
        wait(10000);
    }

    /**
     * Take the user's name before showing the startup graphic.
     */
    private void playStart() {
        System.out.println("");
        System.out.println("What is your name?");
        System.out.print("> ");
        player.setName(scanner.nextLine().trim());
        System.out.println("Hello " + player.getName() + ", welcome to...");
        wait(2000);
        printFile("logo.txt");
    }

    /**
     * Print the contents of a text file.
     * @param filename The name of the text file.
     */
    private void printFile(String filename) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Game.class.getResourceAsStream(filename), "UTF-8"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (NullPointerException ignored) {
            System.err.println(filename + " not found");
        } catch (java.io.IOException ignored) {
        }
    }

    /**
     * Preform the necessary actions when entering a room,
     * depending on the name of the room.
     * @return Whether or not this sequence causes the game to end.
     */
    private boolean enterCurrentRoomSequence() {
        boolean wantToQuit = false;
        Room currentRoom = player.getCurrentRoom();
        boolean entered = currentRoom.getEntered();

        switch (currentRoom.getName()) {
            case "shop":
                System.out.println(currentRoom.getDescription());
                if (!entered) {
                    System.out.println("You see a dazed looking large man with a smile on his face");
                    System.out.println("\"Hello there! Welcome to my shop, see anything you want?\"");
                    System.out.println("You notice a broken jar in front of the man, a blue liquid surrounding it, it's fumes surrounding him");
                } else {
                    System.out.println("\"Welcome back! What can I get for you?\" He says, the dazed expression still on his face");
                }
                break;
            case "trollsRoom":
                NPC troll = npcs.npcInRoom(currentRoom);
                if (troll.getAsleep()) {
                    System.out.println("\nYou hear snoring from a dark corner of the hallway");
                    System.out.println(currentRoom.getDescription());
                } else {
                    System.out.println("A troll covered in armor stands in front of you, not letting you pass");
                    if (!entered) {
                        System.out.println("\"Welcome " + player.getName() + ". I'm sure you've met my boss\"");
                        System.out.println("\"He's given me the job of keeping you out. But let's make this a game\"");
                        System.out.println("\"I'll give you a riddle. Answer it correctly and you can pass\"");
                        System.out.println("\"But if you get it wrong, back to the elevator with you!\"");
                    } else {
                        System.out.println("\"You think you've figured it out? Here's the riddle in case you forget it\"");
                    }
                    wait(5000);
                    System.out.println("\"Throw me out of a the window and you see a grieving wife.\nBring me back, but through the door and you see someone giving life.\nWhat am I?\"");
                    String response = scanner.nextLine().trim().toLowerCase();
                    if (response.equals("n")) {
                        System.out.println("Wow, how did you figure that out? I guess a deal's a deal\nI'm going to sleep, don't tell the boss");
                        npcs.npcInRoom(currentRoom).sleep();
                        System.out.println(currentRoom.getDescription());
                    } else {

                        if (response.equals("rumpelstiltskin")) {
                            System.out.println("Nice try, but wrong story");
                        }
                        System.out.println("Unfortunately you got it wrong. You know what that means");
                        System.out.println("With a surprisingly powerful punch, he knocks you out of the room");
                        player.goToRoom(rooms.get(0));
                        enterCurrentRoomSequence();
                    }
                }
                break;
            case "bossRoom":
                if (!entered) {
                    System.out.println("\"So you finally made it, I was beginning to get worried\"");
                    if (bossIdentified) {
                        System.out.println("You found out my name too. You should've killed me when you had the chance!");
                    }
                } else {
                    System.out.println("\"When will you learn that resistance is futile?\"");
                }
                System.out.println("I won't let you escape!");
                wait(2000);
                if (!player.inventoryHas("platinum_sword")) {
                    System.out.println("He casts a ball of energy at you, knocking you to your feet");
                    System.out.println("\"I have to keep you alive\"");
                    System.out.println("He raises his hand as a glow purple surround both you and it. You float in the air, unable to get down");
                    System.out.println("With a flick of his wrist, you are flung out of the room");
                    wait(5000);
                    player.goToRoom(rooms.get(11));
                } else {
                    System.out.println("He casts a ball of energy at you, it disperses around you");
                    System.out.println("\"Impossible!\" He shouts as he raises his hand as it flickers a purple glow around itself");
                    wait(2000);
                    System.out.println("You approach him, wielding the platinum sword. \"No! Platinum!?\" He shouts as you strike him with the sword");
                    System.out.println("He erupts into a cloud of smoke, his face glowing in it as it fills the room");
                    System.out.println("\"This is not the end\" He laughs as the door swings open and the smoke flies out");
                    wait(2000);
                    System.out.println("You leave through the door and watch as the cloud of smoke drifts up into the blue sky");
                    System.out.println("You walk away from the Clockwork Tower after your successful escape");
                    wait(5000);
                    printFile("tower.txt");
                    System.out.println("You won the game!");
                    System.out.println("Now try to find the secret ending...");
                    npcs.removeNpc(npcs.getNpc("glinlok"));
                    wantToQuit = true;
                }
                break;
            case "elevator":
                try {
                    ((Elevator) rooms.get(0)).enterFromRoom(player.getPreviousRoom());
                } catch (java.util.EmptyStackException ignored) {
                }
                System.out.println(currentRoom.getDescription());
                break;
            case "theatre":
                if (npcs.npcInRoom(currentRoom) == null) {
                    System.out.println(currentRoom.getDescription());
                } else {
                    System.out.println("As you enter the room zombie lunges at you!");
                    wait(1000);
                    if (!(player.inventoryHas("platinum_sword") || player.inventoryHas("iron_sword"))) {
                        System.out.println("With no weapon, you decide it's best to escape");
                        player.goToRoom(rooms.get(10));
                    } else {
                        System.out.println("You attack it with your sword, causing it to collapse");
                        npcs.removeNpc(npcs.npcInRoom(currentRoom));
                        System.out.println(currentRoom.getDescription());
                        break;
                    }
                }
            case "soldiersQuarters":
                if (!entered) {
                    System.out.println("You see a soldier sitting on his bed in a neutral position, with a blank expression on his face");
                    System.out.println("\"Help me\" He says in a monotone voice, staring into the distance");
                    System.out.println("\"He cast a spell on me, he took over the tower. Kill him to free me\"");
                    System.out.println("\"He has a single weakness, you must exploit it. You must forge the platinum sword\"");
                } else {
                    System.out.println("\"Forge the platinum sword. Exploit his only weakness\" The soldier says in a monotone voice");
                }
                System.out.println(currentRoom.getDescription());
                break;
            default:
                System.out.println(currentRoom.getDescription());
                break;
        }
        if (!wantToQuit) currentRoom.enter();
        return wantToQuit;
    }


    /**
     * Play the opening cutscene for the player. Play the secret (early) ending
     * if the player use a certain combination of commands to trigger it
     *
     * @return Whether or not the game ends after this.
     */
    private boolean initialCutscene() {
        boolean wantToQuit = false;
        System.out.println("");
        System.out.println("You wake up on the floor of a dark room. Lying at the feet of a shadowy figure.");
        System.out.println("\"You're finally awake\" He says.");

        Command command = parser.getCommand();
        if (!command.isUnknown()) {
            if (command.getCommandWord().equals("cast") && command.hasSecondWord()) {
                cast(command);
            }
        }

        if (bossIdentified) {
            System.out.println("\"What are you doing? Stop that!\" Glinlok says");

            command = parser.getCommand();
            if (!command.isUnknown()) {
                if (command.getCommandWord().equals("cast") && command.hasSecondWord()) {
                    wantToQuit = cast(command);
                }
            }
        }

        if (!wantToQuit) {
            System.out.println("\"I'm sorry, is my monologue not worthy of your attention?\"");
            System.out.println("\"I'll get to the point. As you can see I've captured you.\"");
            System.out.println("\"Meet me at the bottom floor if you want to escape.\"");
            System.out.println("He holds out his hand and your spellbook flies into it");
            System.out.println("\"Lets get rid of some of the more powerful spells. Can't have you cheating.\" He flicks through your spellbook");
            wait(10000);
            removePowerfulSpells();
            System.out.println("Your spellbook drops to the floor as he disappears, filling the room with darkness");
            npcs.moveNpcs();
            wait(5000);
            System.out.println("");
            System.out.println("---THE CLOCKWORK TOWER---");
            System.out.println("");
            System.out.println("Hint: Use investigate in every room to make sure you don't miss anything out\nAnd use the illuminate spell to light up a dark room");
            System.out.println("Type 'help' if you need help");
            System.out.println(player.getCurrentRoom().getDescription());
        }

        return wantToQuit;
    }

    /**
     * Removes the spells the boss deems are powerful at the
     * start of the game from the spellbook and print remaining spells
     */
    private void removePowerfulSpells() {
        System.out.println("");
        player.removeSpell("eliminate");
        player.removeSpell("rejuvenate");
        player.removeSpell("mesmerize");
        player.removeSpell("descend");
        System.out.println("eliminate, rejuvenate, mesmerize, and descend have been removed from your spellbook");
        System.out.println("Remaining spells:");
        player.printAvailableSpells();
        System.out.println("");
    }

    /**
     * Create the NPCs, and the paths they will
     * traverse throughout the game
     */
    private void setupNpcs() {
        HashSet<NPC> npcsSet = new HashSet<>();
        NPC glinlok, bongo, rumpelstiltskin, zombie;

        Stack<Room> glinlokRoute = new Stack<>();
        Stack<Room> bongoRoute = new Stack<>();
        Stack<Room> rumpelstiltskinRoute = new Stack<>();
        Stack<Room> zombieRoute = new Stack<>();

        glinlokRoute.push(rooms.get(12));
        glinlokRoute.push(rooms.get(11));
        glinlokRoute.push(rooms.get(10));
        glinlokRoute.push(rooms.get(0));
        glinlokRoute.push(rooms.get(4));
        glinlokRoute.push(rooms.get(2));
        glinlokRoute.push(rooms.get(1));
        bongoRoute.push(rooms.get(3));
        rumpelstiltskinRoute.push(rooms.get(10));
        zombieRoute.push(rooms.get(11));

        glinlok = new NPC("glinlok", glinlokRoute);
        bongo = new NPC("bongo", bongoRoute);
        rumpelstiltskin = new NPC("rumpelstiltskin", rumpelstiltskinRoute);
        zombie = new NPC("dave", zombieRoute);

        npcsSet.add(glinlok);
        npcsSet.add(bongo);
        npcsSet.add(rumpelstiltskin);
        npcsSet.add(zombie);

        npcs = new NPCs(npcsSet);
    }

    /**
     * Given a command, process (that is: execute) the command.
     *
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) {
        boolean wantToQuit = false;

        if (command.isUnknown()) {
            System.out.println("I don't know what you mean...");
            return false;
        }

        String commandWord = command.getCommandWord();
        switch (commandWord) {

            case "help":
                help(command);
                break;
            case "go":
                wantToQuit = goRoom(command);
                break;
            case "quit":
                wantToQuit = quit(command);
                break;
            case "cast":
                wantToQuit = cast(command);
                break;
            case "investigate":
                investigate(command);
                break;
            case "take":
                take(command);
                break;
            case "use":
                use(command);
                break;
            case "inventory":
                if (!command.hasThirdWord()) {
                    System.out.println("Total inventory capacity: " + player.getInventorySize());
                    System.out.println("Coin balance:" + player.getBalance());
                    player.showInventory();
                } else {
                    System.out.println("inventory what?");
                }
                break;
            case "back":
                goBack(command);
                break;
            case "drop":
                drop(command);
        }

        return wantToQuit;
    }

    /**
     * Teleport the player to a random room (excluding the teleporter room).
     */
    private void randomTeleport() {
        Random rand = new Random();
        player.goToRoom(rooms.get(rand.nextInt(rooms.size() - 1)));
        enterCurrentRoomSequence();
    }

    /**
     * Move the elevator and print the appropriate lines depending on if
     * the elevator stayed still or moved.
     * @param leverNumber The number of the lever that was used.
     */
    private void useLever(int leverNumber) {
        if(((Elevator) rooms.get(0)).move(leverNumber)) {
            System.out.println("You hear the sound of clockwork around you as the ground shakes");
            System.out.println("The lever snaps back up as the sound stops");
            player.clearPreviousRooms();
        } else {
            System.out.println("The lever snaps back up");
        }
    }
    // implementations of user commands:

    /**
     * Remove a specified item from the player's inventory
     * and place it in the current room. If a required command
     * word is missing, tell the user and do not continue.
     * @param command A command containing the name of the item
     *                the user wants to drop.
     */
    private void drop(Command command) {
        Room currentRoom = player.getCurrentRoom();

        if (!command.hasSecondWord()) {
            System.out.println("Drop what?");
        } else if (currentRoom.getDark()) {
            System.out.println("The room is too dark, you might lose it");
        } else {
            String secondWord = command.getSecondWord();
            Item item = player.getItem(secondWord);
            String itemName = item.getName();
            if (player.hasItem(secondWord) && !(itemName.equals("compass") || itemName.equals("spellbook"))) {
                currentRoom.addItem(item);
                player.removeItem(secondWord);
                System.out.println("You dropped " + secondWord);
            } else {
                System.out.println("Your inventory does not contain " + secondWord);
            }
        }
    }

    /**
     * Go to the room the player was in before the current one.
     * If used again, travel backwards along the path
     * taken throughout this game.
     */
    private void goBack(Command command) {
        if(command.hasSecondWord()) {
            System.out.println("Back what?");
            return;
        }

        if (player.goBack()) {
            enterCurrentRoomSequence();
        } else {
            System.out.println("You cannot go back");
        }
    }

    /**
     * Cast the spell the player has specified and on the target
     * specified (if relevant). If a required command word is
     * missing, tell the user and do not execute the spell.
     *
     * @param command A command containing the name of the spell the
     *                player wants to cast, and the target (if applicable).
     * @return Whether or not casting the spell causes the game to end.
     */
    private boolean cast(Command command) {
        Room currentRoom = player.getCurrentRoom();

        if (!command.hasSecondWord()) {
            // if there is no second word, we don't know what to cast...
            System.out.println("Cast what?");
            System.out.println("Available spells:");
            player.printAvailableSpells();
            return false;
        }

        String spellName = command.getSecondWord();

        if (!player.hasSpell(spellName)) {
            System.out.println("You don't have that spell!");
            return false;
        }

        if (Spells.getSpell(spellName).getSingleUse()) {
            player.removeSpell(spellName);
        }

        switch (spellName) {
            case "illuminate":
                if (currentRoom.getDark()) {
                    currentRoom.makeBright();
                    System.out.println("The room fills with light");
                    System.out.println(currentRoom.getDescription());
                } else {
                    System.out.println("The room is not dark");
                }
                break;
            case "identify":
                if (npcs.isNpcInRoom(currentRoom)) {
                    String npcName = npcs.npcInRoom(currentRoom).getName();
                    System.out.println("A name comes into your mind: " + npcName);
                    if (npcName.equals("glinlok")) {
                        bossIdentified = true;
                    }
                } else {
                    System.out.println("There is nobody else in the room...");
                }
                break;
            case "eliminate":
                if (command.hasThirdWord()) {
                    String targetName = command.getThirdWord();
                    NPC npc = npcs.npcInRoom(currentRoom);
                    String npcName = npc.getName();

                    if (npcName.equals(targetName) && npcName.equals("glinlok") && bossIdentified) {
                        System.out.println("\"No! I cannot be defeated!\" Glinlok says as he casts a ball of energy, narrowly missing you and destroying the wall behind you");
                        System.out.println("With the power of your spell Glinlok erupts in a powerful ball of light");
                        npcs.removeNpc(npc);
                        System.out.println("You jump out of the hole in the wall and cast descend, teleporting you to the ground");
                        System.out.println("You walk away from the Clockwork Tower after your successful escape");
                        printFile("tower.txt");
                        System.out.println("You won the game! Great job on finding this secret ending!");
                        return true;
                    }

                    if (npcName.equals(targetName) && !npcName.equals("glinlok")) {
                        System.out.println("Before they could react " + npcName + " erupts in a powerful ball of light");
                        npcs.removeNpc(npc);
                    }

                } else {
                    System.out.println("Eliminate who?");
                }
                break;
            case "rejuvenate":
                if (!command.hasThirdWord()) {
                    System.out.println("Rejuvenate who?");
                } else { //This spell is not being used in the game, so this is just a general structure
                    System.out.println("It didn't work. Your magic is being suppressed...");
                }
                break;
            case "mesmerize":
                if (!command.hasThirdWord()) {
                    System.out.println("Mesmerize who?");
                } else { //This spell is not being used in the game, so this is just a general structure
                    System.out.println("It didn't work. Your magic is being suppressed...");
                }
                break;
            case "descend":
                System.out.println("It didn't work. Your magic is being suppressed...");
                break;
        }

        return false;
    }

    /**
     * If the player has enough inventory space, and the room isn't dark
     * remove the item from the room and add it to the inventory
     * @param command A command containing the item the player
     *                wants to take
     */
    private void take(Command command) {
        Room currentRoom = player.getCurrentRoom();

        if (!command.hasSecondWord()) {
            System.out.println("Take what?");
        } else if (currentRoom.getDark()) {
            System.out.println("The room is too dark");
        } else {
            String secondWord = command.getSecondWord();

            if (currentRoom.containsItem(secondWord)) {
                Item item = currentRoom.getItem(secondWord);

                if (item.getName().equals("gold_coin")) {
                    player.incrementBalance();
                    currentRoom.removeItem(item);
                    System.out.println("You put the gold coin in your coin pouch");
                    return;
                }

                boolean successful = player.addItem(item);
                if (successful) {
                    currentRoom.removeItem(item);
                    System.out.println("You place " + secondWord + " into your inventory");
                } else {
                    System.out.println("You couldn't pick it up\n(hint: check the item weights in your inventory)");
                }

            } else {
                System.out.println("This room doesn't contain an item called " + secondWord);
            }

        }

    }

    /**
     * Use the specified item. If an item is not specified,
     * or isn't available tell the user and do not continue.
     *
     * @param command A command containing the item the player
     *                wants to use, and the target (if applicable).
     */
    private void use(Command command) {
        Room currentRoom = player.getCurrentRoom();
        if (!command.hasSecondWord()) {
            System.out.println("Use what?");
        } else if (currentRoom.getDark()) {
            System.out.println("The room is too dark");
        } else {
            String secondWord = command.getSecondWord();
            if (currentRoom.containsItem(secondWord)) {

                switch (secondWord) {
                    case "lever_1":
                        useLever(1);
                        break;
                    case "lever_2":
                        useLever(2);
                        break;
                    case "lever_3":
                        if (currentRoom.getItem("lever_3").getUsable()) {
                            useLever(3);
                        } else {
                            System.out.println("You try to pull the lever down, but it's too rusty. Maybe some oil could help");
                        }
                        break;
                    case "forge":
                        System.out.println("A laser mounted on the ceiling analyses you before turning off");
                        if (player.itemCount("platinum_piece") == 3 && player.inventoryHas("iron_sword")) {
                            player.removeItem("iron_sword");
                            for (int i = 0; i < 3; i++) {
                                player.removeItem("platinum_piece");
                            }
                            System.out.println("The robotic arms of the forge spring to life, grabbing 3 platinum pieces and the iron sword from your inventory");
                            System.out.println("You watch as the arms manipulate the metal with the heat of the forge and various tools");
                            System.out.println("Finally, the arms present the platinum sword for you to take");
                            wait(2000);
                            rooms.get(4).addItem(new Item("platinum_sword", 6, true, true));
                        } else {
                            System.out.println("\"NOT ENOUGH MATERIAL\" You hear a robotic voice saying, the sound coming from the forge");
                        }
                        break;
                    case "magic_book":
                        System.out.println("You open the book, most of the pages too faded to read. You manage to decipher some key information from it");
                        System.out.println("\"And these are the spells the great wizard " + player.getName() + "often carries\"");
                        System.out.println("\"illuminate: create a ball of light, relinquishing nearby darkness\"");
                        System.out.println("\"identify: reveal the name of the closest person to you\"");
                        System.out.println("\"eliminate: kill a person of your choice\"");
                        System.out.println("\"rejuvenate: heal a person of your choice (works on the undead!)\"");
                        System.out.println("\"mesmerize: put a person of your choice in a trance like state\"");
                        System.out.println("\"descend: teleport to the ground below you (perfect for great heights!)\"");
                        System.out.println("");
                        wait(5000);
                        System.out.println("\"Be warned! Weapons made of platinum create a null field around them. Absorbing all magic targeted near them\"");
                        System.out.println("");
                        System.out.println("\"Spells can also come in single use scrolls (see one attached at the back of the book)\"");
                        wait(2000);
                        System.out.println("On the final page of the book you find a scroll for the spell rejuvenate");
                        System.out.println("One glance at it, and you can tell it's a fake");
                        System.out.println("");
                        break;
                    case "riddle_book":
                        System.out.println("You open the book, most of the pages too faded to read. You find a single legible riddle ");
                        System.out.println("Riddle:\nThrow me out of a the window and you see a grieving wife.\nBring me back, but through the door and you see someone giving life.\nWhat am I?");
                        System.out.println("Answer: n");
                        System.out.println("Explanation:\nIf you remove the letter n from the word \"window\" you get the word \"widow\": a grieving wife");
                        System.out.println("If you add the letter n to the middle of the word \"door\" you get the word \"donor\": someone giving life");
                        System.out.println("");
                        break;
                    case "glowing_pillar":
                        System.out.println("As soon as you touch the pillar the door behind you and the pillar disappear, leaving nothing but white");
                        System.out.println("The room collapses as you are teleported out out it");
                        rooms.get(13).removeExit("West");
                        rooms.get(4).removeExit("East");
                        rooms.remove(12);
                        wait(3000);
                        randomTeleport();
                        break;
                    default:
                        break;
                }

            } else if (player.hasItem(secondWord)) {
                if (player.getItem(secondWord).getUsable()) {
                    if (!command.hasThirdWord()) {
                        System.out.println("Use " + secondWord + " on what?");
                    } else {
                        switch (secondWord) {
                            case "oil_can":
                                if (command.getThirdWord().equals("lever_3") && currentRoom.getName().equals("elevator")) {
                                    System.out.println("You oil lever_3, allowing to to move freely");
                                    currentRoom.getItem("lever_3").setUsable(true);
                                } else {
                                    System.out.println("You can't do that!");
                                }
                                break;
                            default:
                                break;
                        }
                    }
                }
            } else {
                System.out.println("There's no item called " + secondWord);
            }

        }
    }

    /**
     * If the room isn't dark, list the items in the room.
     */
    private void investigate(Command command) {
        Room currentRoom = player.getCurrentRoom();
        if (command.hasSecondWord()) {
            System.out.println("Investigate what?");
        return;
        }

        if (currentRoom.getDark()) {
            System.out.println("The room is too dark");
        } else if (currentRoom.isEmpty()) {
            System.out.println("The room is empty");
        } else {
            System.out.println("The room contains");
            currentRoom.showItems();
            System.out.println("");
            currentRoom.showObjects();
        }

    }

    /**
     * If there is no second command word, tell the user the available command words.
     * If there is, tell the user what the command word does.
     *
     * @param command A command containing the command word the user needs
     *                more information about (iff applicable).
     */
    private void help(Command command) {
        System.out.println("You are clueless. You are lost. You think...");
        System.out.println();
        if (!command.hasSecondWord()) {
            System.out.println(player.getCurrentRoom().getDescription());
            System.out.println("");
            System.out.println("Your command words are:");
            parser.showCommands();
            System.out.println("For more information about a command word type: help [command word]");
        } else if (CommandWords.isCommand(command.getSecondWord())) {
            String secondWord = command.getSecondWord();
            switch (secondWord) {
                case "go":
                    System.out.println("Go to another room \nformat: go <north/south/east/west>");
                    break;
                case "quit":
                    System.out.println("Quit the game\nformat: quit");
                    break;
                case "help":
                    System.out.println("List available commands\nformat: help");
                    System.out.println("Or give information about a command\nformat: help [command word]");
                    break;
                case "investigate":
                    System.out.println("List the items and objects in the current room\nformat: investigate");
                    break;
                case "cast":
                    System.out.println("Available spells:");
                    player.printAvailableSpells();
                    System.out.println("Cast a spell in your spellbook\nformat: cast [spell name]");
                    System.out.println("Cast a spell in your spellbook on a target (if applicable)\nformat: cast [spell name] [target name]");
                    break;
                case "take":
                    System.out.println("Take an item from the current room and put it in your inventory\nformat: take [item name]");
                    break;
                case "use":
                    System.out.println("Use an item in your inventory or in the current room\nformat: use [item name]");
                    System.out.println("Use an item on an object in the current room\nformat: use [item name] [object name]");
                    break;
                case "inventory":
                    System.out.println("List your coin balance, the size of your inventory, the items in it and their weights\nformat: inventory");
                    break;
                case "back":
                    System.out.println("Go to the previous room you were in. If used again, go back further\nformat: back");
                    break;
                case "drop":
                    System.out.println("Remove an item from your inventory and place it in the room\nformat: drop [item name]");
                    break;
            }

        } else {
            System.out.println("Format not recognized. Type \"help\" or \"help [command word]\"");
        }

    }

    /**
     * Try to go in to one direction. If there is an exit, enter the
     * room, otherwise tell the user they cannot.
     *
     * @param command A command containing the direction the
     *                user wants to travel in.
     * @return Whether or not going into the room causes the game to end.
     */
    private boolean goRoom(Command command) {

        if (!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return false;
        }

        Room currentRoom = player.getCurrentRoom();

        if (currentRoom.getDark()) {
            System.out.println("The room is too dark. You can't see the exits");
            return false;
        }

        // Try to leave current room.
        String direction = command.getSecondWord();
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
            System.out.println("There is no exit at " + direction);
        } else {
            player.walkToRoom(nextRoom);
            return enterCurrentRoomSequence();
        }

        return false;
    }

    /**
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     *
     * @param command A command based on the user input.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) {
        if (command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        } else {
            return true;  // signal that we want to quit
        }
    }

    /**
     * Wait for a given amount of time
     *
     * @param time the time you want to wait in milliseconds
     */
    private void wait(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException ignored) {
        }
    }

}
