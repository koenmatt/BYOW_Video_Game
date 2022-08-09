package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;




/** Roadmap:
 * 1. Generate a randomly sized quadrilateral
 * 2. Check if the room hits another room + [x + 2][y+2] on either side (so they don't connect)
 * 3. render that room
 *
 *
 */


public class House {

    TETile[][] house = new TETile[Engine.WIDTH][Engine.HEIGHT];
    Random r;
    int currentRoomWidth;
    int currentRoomHeight;

    public House(Random r) {
        this.r = r;

        for (int x = 0; x < Engine.WIDTH; x++) {
            for (int y = 0; y < Engine.HEIGHT; y++) {
                house[x][y] = Tileset.CALMWATER3;
            }
        }
    }


    /** Generates a house with randomly sized nonintersecting rooms into the house object*/
    public void generateHouse(int size) {

        int tries = 0;
        int count = 0;

        while (size > 0) {

            boolean test = false;
            generateRoom();
            int x1 = r.nextInt(2, 88);
            int y1 = r.nextInt(2, 40);
            while (!placeable(x1, y1)) {
                if (tries > 5) {
                    test = true;
                    count += 1;
                    break;
                }
                x1 = r.nextInt(2, 85);
                y1 = r.nextInt(2, 40);
                tries += 1;

            }

            if (count > 300) {
                return;
            }
            if (test) {
                continue;
            }
            addRoom(x1, y1);
            size -= 1;
        }
    }

    public TETile[][] getHouse() {
        return house;
    }


    /** Generates a randomly sized quadrilateral to represent a room */
    public void generateRoom() {
        int type = r.nextInt(0, 2);
        if (type == 0) {
            currentRoomWidth = r.nextInt(10, 15);
            currentRoomHeight = r.nextInt(4, 10);
        } else {
            currentRoomWidth = r.nextInt(4, 10);
            currentRoomHeight = r.nextInt(10, 15);
        }

    }

    /** Returns true if it can be placed, false if not.
     * (x1, y1) are the coordinates of the top right corner of the room
     *
     * */
    public boolean placeable(int x1, int y1) {

        //out of bounds check

        //check if location is valid

        int widthCheck = x1 + currentRoomWidth + 3;
        if (((x1 - 3) < 1) || (widthCheck > 80)) {
            return false;
        }

        int heightCheck = y1 + currentRoomHeight + 3;
        if (((y1 - 3) < 1) || (heightCheck > 44)) {
            return false;
        }

        //intersection check

        for (int x = x1 - 2; x <= x1 + currentRoomWidth + 2; x++) {
            for (int y = y1 - 2; y < y1 + currentRoomHeight + 2; y++) {
                if (house[x][y] == Tileset.FLOOR) {
                    return false;
                }
            }
        }
        return true;
    }


    /** Adds the room to the global house variable */
    public void addRoom(int x1, int y1) {
        for (int x = x1; x < x1 + currentRoomWidth; x++) {
            for (int y = y1; y < y1 + currentRoomHeight; y++) {
                house[x][y] = Tileset.FLOOR;
            }
        }

    }


    /** IDEA: Generate a few random 1x1 rooms to represent an elbow in the path
     *
     * Store each 'room' in an array, which represents a tree like structure.
     * Start with a random point on a random room. Pick another random point on another random room.
     *
     *Use A* to find the shortest path. Then start at the connected room, and pick another random room (exclude the previous one)
     *
     * Then go from that room to another random room etc. until there are no rooms to be found
     *
     * */
    public void addCorridors() {





    }


    //TODO: Add walls around rooms


}
