package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

public class PathFinder {

    int startX;
    int startY;

    int currentX;
    int currentY;


    int targetX;
    int targetY;
    Random r;

    TETile[][] world;

    /** Uses a drunkards walk to connect rooms */
    public PathFinder(int startX, int startY, int targetX, int targetY, TETile[][] world, Random r) {
        this.startX = startX;
        this.startY = startY;
        this.currentX = startX;
        this.currentY = startY;
        this.targetX = targetX;
        this.targetY = targetY;
        this.world = world;
        this.r = r;
    }


    public void execute() {
        while ((targetX != currentX) || (targetY != currentY)) {
            drunkWalk();
        }
    }
    public void drunkWalk() {
        int[] move = chooseMove();

        if (move == null) {
            updatePath(2, 1);
        } else if (targetX == currentX) {
            if (currentY > targetY) {
                updatePath(2, -1);
            } else {
                updatePath(2, 1);
            }
        } else if (targetY == currentY) {
            if (currentX > targetX) {
                updatePath(0, -1);
            } else {
                updatePath(0, 1);
            }
        } else {
            updatePath(move[1], move[0]);
        }

    }

    public void updatePath(int dir, int val) {
        if ((dir == 2) || (dir == 3)) {
            world[currentX][currentY + val] = Tileset.CALMWATER1;
            world[currentX][currentY + val] = Tileset.CALMWATER1;
            currentY = currentY + val;
        } else {
            world[currentX + val][currentY] = Tileset.CALMWATER1;
            currentX = currentX + val;
        }
    }

    public int[] chooseMove() {
        //by index: 0--> left, 1 ---> right, 2---> up, 3---> down
        int[] choices = new int[4];
        if (currentX > targetX) {
            if (left()) {
                choices[0] = -1;
            }
        } else if (currentX < targetX) {
            if (right()) {
                choices[1] = 1;
            }
        }
        if (currentY > targetY) {
            if (down()) {
                choices[3] = -1;
            }
        } else if (currentY < targetY) {
            if (up()) {
                choices[2] = 1;
            }
        }
        boolean moveExists = false;
        for (int i = 0; i < 4; i++) {
            if (choices[i] != 0) {
                moveExists = true;
                break;
            }
        }
        if (!moveExists) {
            return null;
        }

        int type = r.nextInt(0, 4);
        int test = choices[type];
        while (test == 0) {
            test = choices[r.nextInt(0, 4)];
        }
        return new int[]{choices[type], type};
    }



    public boolean up() {
        return isPossible(currentX, currentY + 1);
    }

    public boolean down() {
        return isPossible(currentX, currentY - 1);
    }

    public boolean left() {
        return isPossible(currentX - 1, currentY);
    }

    public boolean right() {
        return isPossible(currentX + 1, currentY);
    }

    public boolean isPossible(int x, int y) {
        if ((x >= 90) || (x <= 0)) {
            return false;
        } else if ((y >= 45) || (y <= 0)) {
            return false;
        }
//        } else if (world[x][y] == Tileset.MYWALL) {
//            return false;
//        else return world[x][y] != Tileset.MYWALL;
        return true;
    }



}
