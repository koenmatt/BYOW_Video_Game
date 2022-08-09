package byow.Core;


import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

/** Simply a method to store details about a given room.
 *
 *
 * The startX and startY represent the bottom left corner of the room */


public class Room {

    int width;
    int height;

    int startX;
    int startY;

    int doorX;
    int doorY;

    int side; //0 means left, 1 means right, 2 means up, 3 means down

    Random r;

    public Room(int width, int height, int startX, int startY, Random r, int side) {
        this.width = width;
        this.height = height;
        this.startX = startX;
        this.startY = startY;
        this.r = r;
        this.side = side;

    }

    public void addDoor(int side) {

        if ((height == 0) || (width == 0)) {
            return;
        }

        if (side == 0) { //left or right
            doorX = startX + width - 1;
            doorY = startY + r.nextInt(1, height - 1);

        }
        else if (side == 1){ //left side
            doorX = startX - 1;
            doorY = startY + r.nextInt(1, height - 1);
        }
        else if (side == 2) {
             //up side
            doorY = startY + height - 1;
            doorX = startX + r.nextInt(1, width - 1);
        } else { //down side
            doorY = startY - 1;
            doorX = startX + r.nextInt(1, width - 1);
        }
    }


    public TETile[][] render(TETile[][] world, TETile t) {


        for (int x = startX; x < startX + width - 1; x++) {
            for (int y = startY; y < startY + height - 1; y++) {
                if (!((x == doorX) && (y == doorY))) {
                    if (r.nextInt(0, 40) == 0) {
                        world[x][y] = Tileset.BADAPPLE;
                    } else {
                        world[x][y] = t;
                    }
                }
            }
        }
        return world;
    }

    public TETile[][] renderWall(TETile[][] world, TETile t) {

        if (startX - 1 < 0) {
            return world;
        }

        for (int x = startX - 1; x < startX + width; x++) {
            for (int y = startY - 1; y < startY + height; y++) {
                world[x][y] = t;
            }
        }
        return world;
    }

}
