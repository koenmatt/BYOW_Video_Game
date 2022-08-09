package byow.Core;


import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

public class World {

    int innerLength1;
    int innerLength2;
    int innerLength3;
    int innerLength4;

    Player p;

    Random r;
    TETile[][] world = new TETile[Engine.WIDTH][Engine.HEIGHT];
    TETile[][] state1 = new TETile[Engine.WIDTH][Engine.HEIGHT];
    TETile[][] state2 = new TETile[Engine.WIDTH][Engine.HEIGHT];

    public World(Random r) {
        this.r = r;

    }

    public TETile[][] getWorld() {
        return world;
    }

    /** This method initializes the world as all water
     */
    public void initializeWorld() {
        for (int x = 0; x < Engine.WIDTH; x++) {
            for (int y = 0; y < Engine.HEIGHT; y++) {
                world[x][y] = Tileset.CALMWATER1;
            }
        }
    }

    public void spawnPlayer() {
        int startX = r.nextInt(3, 80);
        int startY = r.nextInt(3, 40);
        while (world[startX][startY] != Tileset.CALMWATER1) {
            startX = r.nextInt(3, 80);
            startY = r.nextInt(3, 40);
        }
        p = new Player(startX, startY);
        world[startX][startY] = Tileset.AVATAR1;
    }

    public Engine.State move(Engine.Dir d) {

        if (d == Engine.Dir.DOWN) {

            if (outOfBounds(p.getX(), p.getY() - 1)) {
                return Engine.State.NEWWORLD;
            }
            moveDown(p);
            return Engine.State.NEWWORLD;
        }
        else if (d == Engine.Dir.UP) {

            if (outOfBounds(p.getX(), p.getY() + 1)) {
                return Engine.State.NEWWORLD;
            }

            moveUp(p);
            return Engine.State.NEWWORLD;
        }
        else if (d == Engine.Dir.LEFT) {

            if (outOfBounds(p.getX() - 1, p.getY())) {
                return Engine.State.NEWWORLD;
            }

            moveLeft(p);
            return Engine.State.NEWWORLD;
        }

        else if (d == Engine.Dir.RIGHT) {

            if (outOfBounds(p.getX() + 1, p.getY())){
                return Engine.State.NEWWORLD;
            }
            moveRight(p);
            return Engine.State.NEWWORLD;
        } else if (d == Engine.Dir.HOME) {

            if (outOfBounds(p.getX() + 1, p.getY())) {
                return Engine.State.NEWWORLD;
            }
            moveRight(p);
            return Engine.State.NEWWORLD;
        }

        return Engine.State.PLAY;
    }



    public void moveDown(Player p) {
        world[p.getX()][p.getY() - 1] = Tileset.AVATAR1;

        world[p.getX()][p.getY()] = Tileset.CALMWATER1;
        p.updatePosition(p.getX(), p.getY() - 1);
    }

    public void moveUp(Player p) {
        world[p.getX()][p.getY() + 1] = Tileset.AVATAR1;

        world[p.getX()][p.getY()] = Tileset.CALMWATER1;
        p.updatePosition(p.getX(), p.getY() + 1);

    }

    public void moveLeft(Player p) {
        world[p.getX() - 1][p.getY()] = Tileset.AVATAR1;

        world[p.getX()][p.getY()] = Tileset.CALMWATER1;
        p.updatePosition(p.getX() - 1, p.getY());

    }

    public void moveRight(Player p) {
        world[p.getX() + 1][p.getY()] = Tileset.AVATAR1;

        world[p.getX()][p.getY()] = Tileset.CALMWATER1;
        p.updatePosition(p.getX() + 1, p.getY());

    }

    public boolean outOfBounds(int x, int y) {
        if ((x >= 90) || (x <= 0)) {
            return true;
        } else if ((y >= 45) || (y <= 0)) {
            return true;
        }

        if (world[x][y] == Tileset.LAND1) {
            return true;
        }

        if (world[x][y] == Tileset.OCEANLAND1) {
            return true;
        }

        return false;
    }











    public void updateGraphics() {
        for (int x = 0; x < Engine.WIDTH; x++) {
            for (int y = 1; y < Engine.HEIGHT - 1; y++) {
                if ((world[x][y] == Tileset.LAND1)
                        && (world[x][y - 1] == Tileset.CALMWATER1)
                        && (world[x][y + 1] == Tileset.LAND1)) {
                    world[x][y] = Tileset.OCEANLAND1;
                }
            }
        }

    }


    public void createIslands() {

        int randomNumIslands = r.nextInt(8, 50);


        while (randomNumIslands > 0) {
            int randomSize = r.nextInt(40, 100);
            int randomX = r.nextInt(85);
            int randomY = r.nextInt(40);

            drunkardsWalk(randomX, randomY, randomSize);

            randomNumIslands -= 1;

        }


    }


    /** a poorly coded drunkards walk aglorithm
     */
    public void drunkardsWalk(int startX, int startY, int size) {
        //up = 0, down = 1, left = 2, right = 3

        int lastMove = -1;

        while (size > 0) {

            //initial start case
            if (lastMove == -1) {
                int num = r.nextInt(4);
                if (num == 0) {

                    if ((startY + 1) >= 45) {
                        break;
                    }

                    world[startX][startY + 1] = Tileset.LAND1;
                    lastMove = 0;
                    startY += 1;
                    size -= 1;
                } else if (num == 1) {

                    if ((startY - 1) <= 0) {
                        break;
                    }

                    world[startX][startY - 1] = Tileset.LAND1;
                    lastMove = 1;
                    startY -= 1;
                    size -= 1;
                } else if (num == 2) {

                    if ((startX - 1) <= 0) {
                        break;
                    }
                    world[startX - 1][startY] = Tileset.LAND1;
                    lastMove = 2;
                    startX -= 1;
                    size -= 1;
                } else {
                    if ((startX + 1) >= 90) {
                        break;
                    }
                    world[startX + 1][startY] = Tileset.LAND1;
                    lastMove = 3;
                    startX += 1;
                    size -= 1;
                }
            }

            else if ((lastMove == 0) || (lastMove == 1)) {
                int num = r.nextInt(8);
                if (num == 0) {
                    if ((startY + 1) >= 45) {
                        break;
                    }
                    world[startX][startY + 1] = Tileset.LAND1;
                    lastMove = 0;
                    startY += 1;
                    size -= 1;
                } else if (num == 1) {
                    if ((startY - 1) <= 0) {
                        break;
                    }
                    world[startX][startY - 1] = Tileset.LAND1;
                    lastMove = 1;
                    startY -= 1;
                    size -= 1;
                } else if ((num == 2) || (num == 3) || (num == 4))  {
                    if ((startX - 1) <= 0) {
                        break;
                    }
                    world[startX - 1][startY] = Tileset.LAND1;
                    lastMove = 2;
                    startX -= 1;
                    size -= 1;
                } else {

                    if ((startX + 1) >= 90) {
                        break;
                    }
                    world[startX + 1][startY] = Tileset.LAND1;
                    lastMove = 3;
                    startX += 1;
                    size -= 1;
                }
            } else {

                int num = r.nextInt(8);

                if ((num == 0) || (num == 1) || (num == 2)) {
                    if ((startY + 1) >= 45) {
                        break;
                    }
                    world[startX][startY + 1] = Tileset.LAND1;
                    lastMove = 0;
                    startY += 1;
                    size -= 1;
                } else if ((num == 3) || (num == 4) || (num == 5))  {
                    if ((startY - 1) <= 0) {
                        break;
                    }
                    world[startX][startY - 1] = Tileset.LAND1;
                    lastMove = 1;
                    startY -= 1;
                    size -= 1;
                } else if (num == 6) {
                    if ((startX - 1) <= 0) {
                        break;
                    }
                    world[startX - 1][startY] = Tileset.LAND1;
                    lastMove = 2;
                    startX -= 1;
                    size -= 1;
                } else {
                    if ((startX + 1) >= 90) {
                        break;
                    }
                    world[startX + 1][startY] = Tileset.LAND1;
                    lastMove = 3;
                    startX += 1;
                    size -= 1;
                }
            }

        }

    }




}




