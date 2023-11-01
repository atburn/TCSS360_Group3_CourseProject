package com.tcss.dungeonadventure.model;

import com.tcss.dungeonadventure.Helper;
import com.tcss.dungeonadventure.objects.items.PillarOfAbstraction;
import com.tcss.dungeonadventure.objects.items.PillarOfEncapsulation;
import com.tcss.dungeonadventure.objects.items.PillarOfInheritance;
import com.tcss.dungeonadventure.objects.items.PillarOfPolymorphism;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Represents a randomly generated maze of type {@link Room}.
 *
 * @author Aaron, Sunny, Hieu
 * @version TCSS 360, Fall 2023
 */
public class Dungeon {

    private static final Dimension MAZE_SIZE = new Dimension(6, 6);

    /**
     * The 2D representation of the {@link Dungeon}.
     */
    private final Room[][] myMaze;

    /**
     * The entrance of the {@link Dungeon}.
     */
    private final Room myStartingRoom;

    /**
     * The exit of the {@link Dungeon}.
     */
    private final Room myExitRoom;

    /**
     * The room that contains a pillar of Object-Oriented.
     */
    private final Room[] myPillarRooms;

    /**
     * The current player's location.
     */
    private Room myCharacterLocation;

    /**
     * Initializes a 6x6 traversable {@link Dungeon}.
     *
     * @param theStartingRoom the entrance of the {@link Dungeon}
     * @param theExitRoom     the exit of the {@link Dungeon}
     * @param thePillarRooms  the room that contains a pillar of Object-Oriented
     */
    public Dungeon(final Room theStartingRoom,
                   final Room theExitRoom,
                   final Room[] thePillarRooms) {

        myMaze = new Room[(int) MAZE_SIZE.getHeight()][(int) MAZE_SIZE.getWidth()];
        myStartingRoom = theStartingRoom;
        myExitRoom = theExitRoom;
        myPillarRooms = thePillarRooms;
        myCharacterLocation = theStartingRoom;
        generateDungeon();
    }

    public Dungeon() {
        this(
                new Room(true, false, null),
                new Room(false, true, null),
                generatePillarRooms());
    }

    public static Room[] generatePillarRooms() {
        final Room[] pillarRooms = new Room[4];
        final Class<?>[] pillars = new Class[]{
            PillarOfInheritance.class,
            PillarOfAbstraction.class,
            PillarOfPolymorphism.class,
            PillarOfEncapsulation.class};

        for (int i = 0; i < pillars.length; i++) {
            pillarRooms[i] = new Room(false, false, pillars[i]);
        }
        return pillarRooms;
    }


    /**
     * Constructs a random-generated maze of type {@link Room}
     * with a traversable path from the entrance to the exit
     * and 4 pillars of Object-Oriented randomly placed in the maze.
     */
    private void generateDungeon() {
        // Generates an array of essentials rooms in the maze
        final List<Room> essentialRooms = new ArrayList<>();
        essentialRooms.addAll(Arrays.asList(myStartingRoom, myExitRoom));
        essentialRooms.addAll(Arrays.asList(myPillarRooms));

        /*
         A random generator for three uses:
            -shuffling rooms in essentialRooms
            -choosing random index in the maze
            -randomly either choosing an essential room or a dead-end room
         */

        // Shuffles the list of rooms randomly
        Collections.shuffle(essentialRooms, Helper.getRandom());

        /*
        totalSpots is the total # of indices in the maze,
        filledCount is the # of occupied indices
        essentialRoomsIndex is the index of the shuffled essentialRooms
         */
        final int totalSpots = myMaze.length * myMaze[0].length;
        int filledCount = 0;
        int essentialRoomsIndex = 0;

        // Fills the 2D array until no empty spots are left
        while (filledCount < totalSpots && essentialRoomsIndex < essentialRooms.size()) {

            int randomRow;
            int randomCol;

            // prevents rooms from overwriting each other
            do {
                randomRow = Helper.getRandomIntBetween(0, myMaze.length);
                randomCol = Helper.getRandomIntBetween(0, myMaze[0].length);
            } while (myMaze[randomRow][randomCol] != null);

            // A list in which its chosen element
            // can be either an essential room or a dead-end room
            final List<Room> randomRooms = new ArrayList<>();
            randomRooms.add(essentialRooms.get(essentialRoomsIndex));
            randomRooms.add(new Room(false, false, null));

            // 0 is essential, 1 is non essential
            final boolean isEssential = Helper.getRandomDoubleBetween(0, 1) > 0.5;

            // Fills in the unoccupied spot in the maze with a room in randomRooms
            if (myMaze[randomRow][randomCol] == null) {
                if (isEssential) {
                    myMaze[randomRow][randomCol] = randomRooms.get(0);
                    essentialRoomsIndex++;
                } else {
                    myMaze[randomRow][randomCol] = randomRooms.get(1);
                }

                filledCount++;
            }
        }

        //TODO: implement the algorithm to check
        // if the maze is traversable, otherwise regenerate a new maze
    }

    /**
     * Returns the dungeon as a maze of type {@link Room}.
     *
     * @return a maze of type {@link Room}
     */
    public Room[][] getRooms() {
        return myMaze;
    }

    /**
     * Returns the player's current location in the {@link Dungeon}.
     *
     * @return the player's current location in the {@link Dungeon}
     */
    public Room getCharacterLocation() {
        return myCharacterLocation;
    }

    /**
     * Returns the {@link Room} in the maze at the specified coordinates.
     *
     * @param theX the x-coordinate of the {@link Room}
     * @param theY the y-coordinate of the {@link Room}
     * @return the {@link Room} in the maze at the specified coordinates
     */
    public Room getRoomAt(final int theX, final int theY) {
        return myMaze[theX][theY];
    }

    @Override
    public String toString() {
        final StringBuilder stringBuilder = new StringBuilder();

        //TODO: maybe improve this implementation
        for (Room[] rooms : myMaze) {
            for (Room room : rooms) {
                if (room == null) {
                    stringBuilder.append("null"); // IDEALLY nothing should be null.
                } else if (room.isEntranceRoom()) {
                    stringBuilder.append("ENTR");
                } else if (room.isExitRoom()) {
                    stringBuilder.append("EXIT");
                }else if (room.getPillar() != null) {
                    stringBuilder.append(" ").append(room.getPillar().getDisplayChar()).append("  ");
                } else {
                    stringBuilder.append("ROOM");
                }

                stringBuilder.append(" ");

            }

            stringBuilder.append("\n");
        }

        return stringBuilder.toString();
    }
}
