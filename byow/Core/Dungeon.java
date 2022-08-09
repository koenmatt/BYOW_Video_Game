package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Dungeon {

    Random r;
    int numRooms;
    BSPTree t;
    ArrayList<Room> corridors;
    TETile[][] dungeon;
    TETile[][] lineOfSight;
    TETile[][] dungeonNoAvatar;
    int rooms = 0;
    int tries = 0;
    Player p;
    int currentAvatarType;
    int chestX;
    int chestY;
    World islands;
    TETile[][] home;
    boolean hasShovel = false;
    TETile[] rightAvatars = new TETile[3];
    TETile[] leftAvatars = new TETile[3];
    TETile[] upAvatars = new TETile[3];
    TETile[] downAvatars = new TETile[3];

    public Dungeon(Random r, int numRooms) {
        this.r = r;
        this.numRooms = numRooms;
        this.t = new BSPTree(r);
        this.dungeon = initialize();
        this.corridors = new ArrayList<>();

    }

    public TETile[][] getLineOfSight() {
        return lineOfSight;
    }
    public Player getPlayer() {
        return p;
    }

    public Player spawnPlayer() {

        int startX = r.nextInt(3, 80);
        int startY = r.nextInt(5, 40);
        while (dungeon[startX][startY] != Tileset.DUNGEONFLOOR) {
            startX = r.nextInt(3, 80);
            startY = r.nextInt(5, 40);
        }
        p = new Player(startX, startY);
        dungeonNoAvatar = dungeon.clone();
        dungeon[p.getX()][p.getY()] = Tileset.LAVATAR2;
        rightAvatars[0] = Tileset.AVATAR1;
        rightAvatars[1] = Tileset.AVATAR2;
        rightAvatars[2] = Tileset.AVATAR3;

        leftAvatars[0] = Tileset.LAVATAR1;
        leftAvatars[1] = Tileset.LAVATAR2;
        leftAvatars[2] = Tileset.LAVATAR3;

        upAvatars[0] = Tileset.UAVATAR1;
        upAvatars[1] = Tileset.UAVATAR2;
        upAvatars[2] = Tileset.UAVATAR3;

        downAvatars[0] = Tileset.DAVATAR1;
        downAvatars[1] = Tileset.DAVATAR2;
        downAvatars[2] = Tileset.DAVATAR3;


        return p;
    }

    public void spawnChests() {
        int startX = r.nextInt(3, 80);
        int startY = r.nextInt(5, 40);
        while (dungeon[startX][startY] != Tileset.DUNGEONFLOOR) {
            startX = r.nextInt(3, 80);
            startY = r.nextInt(5, 40);
        }
        dungeon[startX][startY] = Tileset.CHEST;
        chestX = startX;
        chestY = startY;
        int startX2 = r.nextInt(3, 80);
        int startY2 = r.nextInt(5, 40);
        while (dungeon[startX2][startY2] != Tileset.DUNGEONFLOOR) {
            startX2 = r.nextInt(3, 80);
            startY2 = r.nextInt(5, 40);
        }
    }

    public Engine.State move(Engine.Dir d) {

        if (d == Engine.Dir.OPEN) {
            lineOfSight = dungeon;
            return Engine.State.PLAY;
        }

        if (d == Engine.Dir.CHEST) {
            if (!noChest()) {
                lineOfSight = getChestContents();
            }
            dungeon[chestX][chestY] = Tileset.OPENCHEST;
            hasShovel = true;
            return Engine.State.PLAY;
        }
        else if (d == Engine.Dir.DOWN) {
            if (hasShovel) {
                lineOfSight = winGame(p.getX(), p.getY());
            }

            if (outOfBounds(p.getX(), p.getY() - 1, hasShovel)) {
                return Engine.State.PLAY;
            }

            moveDown(p);
            return Engine.State.PLAY;
        }
        else if (d == Engine.Dir.UP) {
            if (hasShovel) {
                lineOfSight = winGame(p.getX(), p.getY());
            }
            if (outOfBounds(p.getX(), p.getY() + 1, hasShovel)) {
                return Engine.State.PLAY;
            }

            moveUp(p);
            return Engine.State.PLAY;
        }
        else if (d == Engine.Dir.LEFT) {
            if (hasShovel) {
                lineOfSight = winGame(p.getX(), p.getY());
            }

            if (outOfBounds(p.getX() - 1, p.getY(), hasShovel)) {
                return Engine.State.PLAY;
            }

            moveLeft(p);
            return Engine.State.PLAY;
        }
        else if (d == Engine.Dir.RIGHT) {
            if (hasShovel) {
                lineOfSight = winGame(p.getX(), p.getY());
            }
            if (outOfBounds(p.getX() + 1, p.getY(), hasShovel)) {
                return Engine.State.PLAY;
            }
            moveRight(p);
            return Engine.State.PLAY;
        } else if (d == Engine.Dir.HOME) {
            if ((hasShovel) && (finalOutOfBounds(p.getX(), p.getY()))) {
                dungeon = home;
                return Engine.State.NEWWORLD;
            }
            if (outOfBounds(p.getX() + 1, p.getY(), hasShovel)) {
                return Engine.State.PLAY;
            }
            moveRight(p);
            return Engine.State.PLAY;
        }

        return Engine.State.PLAY;
    }

    public boolean finalOutOfBounds(int x, int y) {
        if ((x >= 88) || (x <= 2)) {
            return true;
        } else if ((y >= 43) || (y <= 2)) {

            return true;
        }
        return false;
    }
    public TETile[][] getChestContents() {
        TETile[][] openChest = new TETile[Engine.WIDTH][Engine.HEIGHT];
        for (int x = 0; x < Engine.WIDTH; x++) {
            for (int y = 0; y < Engine.HEIGHT; y++) {
                openChest[x][y] = Tileset.NOTHING;
            }
        }
        for (int x = 20; x < Engine.WIDTH - 20; x++) {
            for (int y = 10; y < Engine.HEIGHT - 10; y++) {
                openChest[x][y] = Tileset.DARKWALL;
            }
        }

        for (int x = 30; x < 31; x++) {
            for (int y = 15; y < Engine.HEIGHT - 20; y++) {
                openChest[x][y] = Tileset.SHOVELHANDLE;
            }
        }

        for (int x = 27; x < 33; x++) {
            for (int y = Engine.HEIGHT - 20; y < Engine.HEIGHT - 14; y++) {
                openChest[x][y] = Tileset.SHOVELHEAD;
            }
        }
        return openChest;

    }

    public boolean noChest() {
        if (dungeon[p.getX() + 1][p.getY()] == Tileset.CHEST) {
            return false;
        }
        if (dungeon[p.getX() - 1][p.getY()] == Tileset.CHEST) {
            return false;
        }
        if (dungeon[p.getX()][p.getY() + 1] == Tileset.CHEST) {
            return false;
        }
        if (dungeon[p.getX()][p.getY() - 1] == Tileset.CHEST) {
            return false;
        }
        return true;
    }

    public void moveDown(Player p) {
        dungeon[p.getX()][p.getY() - 1] = downAvatars[currentAvatarType + 1];
        currentAvatarType += 1;
        if (currentAvatarType + 1 == 3) {
            currentAvatarType = 0;
        }
        dungeon[p.getX()][p.getY()] = Tileset.DUNGEONFLOOR;
        p.updatePosition(p.getX(), p.getY() - 1);
        updateLineOfSight();
    }

    public void moveUp(Player p) {
        dungeon[p.getX()][p.getY() + 1] = upAvatars[currentAvatarType + 1];
        currentAvatarType += 1;
        if (currentAvatarType + 1 == 3) {
            currentAvatarType = 0;
        }
        dungeon[p.getX()][p.getY()] = Tileset.DUNGEONFLOOR;
        p.updatePosition(p.getX(), p.getY() + 1);
        updateLineOfSight();
    }

    public void moveLeft(Player p) {
        dungeon[p.getX() - 1][p.getY()] = leftAvatars[currentAvatarType + 1];
        currentAvatarType += 1;
        if (currentAvatarType + 1 == 3) {
            currentAvatarType = 0;
        }
        dungeon[p.getX()][p.getY()] = Tileset.DUNGEONFLOOR;
        p.updatePosition(p.getX() - 1, p.getY());
        updateLineOfSight();
    }

    public void moveRight(Player p) {
        dungeon[p.getX() + 1][p.getY()] = rightAvatars[currentAvatarType + 1];

        currentAvatarType += 1;
        if (currentAvatarType + 1 == 3) {
            currentAvatarType = 0;
        }
        dungeon[p.getX()][p.getY()] = Tileset.DUNGEONFLOOR;
        p.updatePosition(p.getX() + 1, p.getY());
        updateLineOfSight();
    }

    public void updateLineOfSight() {
        TETile[][] l = new TETile[Engine.WIDTH][Engine.HEIGHT];

        int leftXAmount;
        int rightXAmount;

        int leftYAmount;
        int rightYAmount;

        if (p.getY() - 4 <= 0) {
            leftYAmount = 0;
        } else {
            leftYAmount = 4;
        }

        if (p.getY() + 4 >= 45) {
            rightYAmount = 0;
        } else {
            rightYAmount = 4;
        }

        if (p.getX() - 4 <= 0) {
            leftXAmount = 0;
        } else {
            leftXAmount = 4;
        }

        if (p.getX() + 4 >= 90) {
            rightXAmount = 0;
        } else {
            rightXAmount = 4;
        }


        for (int x = 0; x < Engine.WIDTH; x++) {
            for (int y = 0; y < Engine.HEIGHT; y++) {
                l[x][y] = Tileset.NOTHING;
            }
        }

        for (int x = p.getX() - leftXAmount; x < p.getX() + rightXAmount; x++) {
            for (int y = p.getY() - leftYAmount; y < p.getY() + rightYAmount; y++) {
                if (dungeon[x][y] == Tileset.MYWALL){
                    l[x][y] = Tileset.DARKWALL;
                } else {
                    l[x][y] = dungeon[x][y];
                }
            }
        }



        lineOfSight = l;
    }

    public TETile[][] winGame(int x, int y) {
        World w = new World(r);
        w.initializeWorld();
        w.createIslands();
        w.updateGraphics();

        if ((x >= 88) || (x <= 2)) {
            TETile[][] store = w.getWorld();
            home = store;
            islands = w;
            return store;
        } else if ((y >= 43) || (y <= 2)) {
            TETile[][] store = w.getWorld();
            home = store;
            islands = w;
            return store;
        }
        return dungeon;
    }

    public void displayPick() {
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("Monaco", Font.BOLD, 30);
        Font fontBigger = new Font("Monaco", Font.BOLD, 20);
        StdDraw.setFont(fontBig);
        StdDraw.text(10, Engine.HEIGHT - 2, "BALLS");

        StdDraw.show();
    }


    public boolean outOfBounds(int x, int y, boolean shovel) {
        if ((x >= 90) || (x <= 0)) {
            return true;
        } else if ((y >= 45) || (y <= 0)) {
            return true;
        }

        if (!shovel) {
            if (dungeon[x][y] == Tileset.MYWALL) {
                return true;
            } else if (dungeon[x][y] == Tileset.CHEST) {
                return true;
            } else if (dungeon[x][y] == Tileset.BCHEST) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }


    public int getRooms() {
        return rooms;
    }

    public void renderDungeon() {
        renderDungeon(t.root);

        while ((tries < 5) && (rooms < 5)) {
            rooms = 0;
            tries += 1;
            t = new BSPTree(new Random(r.nextInt(2, 500)));
            this.dungeon = initialize();
            renderDungeon(t.root);
        }
    }

    public int getTries() {
        return tries;
    }
    public void renderDungeon(BSPTree.Node leaf) {
        if ((leaf.left == null) || (leaf.right == null)) {
            leaf.nestedRoom.addDoor(leaf.nestedRoom.side);
            leaf.nestedRoom.renderWall(dungeon, Tileset.MYWALL);
            leaf.nestedRoom.render(dungeon, Tileset.DUNGEONFLOOR);
            if ((leaf.nestedRoom.width != 0) && (leaf.nestedRoom.height != 0)) {
                rooms += 1;
                leaf.nestedRoom.addDoor(leaf.nestedRoom.side);
            }
            return;
        }

        renderDungeon(leaf.left);
        renderDungeon(leaf.right);
    }

    public TETile[][] getDungeon() {
        return dungeon;
    }


    public TETile[][] initialize() {
        TETile[][] d = new TETile[Engine.WIDTH][Engine.HEIGHT];
        for (int x = 0; x < Engine.WIDTH; x++) {
            for (int y = 0; y < Engine.HEIGHT; y++) {
                d[x][y] = Tileset.NOTHING;
            }
        }
        t.buildTree(numRooms);

        return d;
    }

    public void createCorridors() {
        createCorridors(t.root);
    }

    public void createCorridors(BSPTree.Node leaf) {
        if ((leaf.left == null) || (leaf.right == null)) {

            if ((leaf.nestedRoom.width != 0) && (leaf.nestedRoom.height != 0)) {
                int startX = leaf.parent.left.nestedRoom.doorX - 1;
                int startY = leaf.parent.left.nestedRoom.doorY - 1;
                int targetX = leaf.parent.right.nestedRoom.doorX + 1;
                int targetY = leaf.parent.right.nestedRoom.doorY + 1;
                PathFinder e = new PathFinder(startX, startY, targetX, targetY, dungeon, r);
                e.execute();


                int startX2 = leaf.parent.parent.left.left.nestedRoom.doorX;
                int startY2 = leaf.parent.parent.left.left.nestedRoom.doorY;
                int targetX2 = leaf.parent.parent.right.right.nestedRoom.doorX;
                int targetY2 = leaf.parent.parent.right.right.nestedRoom.doorY;
                PathFinder b = new PathFinder(startX2, startY2, targetX2, targetY2, dungeon, r);
                b.execute();

                if (leaf.parent.parent.parent == null) {
                    return;
                } else {
                    int startX3 = leaf.parent.parent.parent.left.left.left.nestedRoom.doorX;
                    int startY3 = leaf.parent.parent.parent.left.left.left.nestedRoom.doorY;
                    int targetX3 = leaf.parent.parent.parent.right.right.right.nestedRoom.doorX;
                    int targetY3 = leaf.parent.parent.parent.right.right.right.nestedRoom.doorY;
                    PathFinder d = new PathFinder(startX3, startY3, targetX3, targetY3, dungeon, r);
                    d.execute();
                }

                if (leaf.parent.parent.parent.parent == null) {
                    return;
                } else {
                    int startX4 = leaf.parent.parent.parent.parent.left.left.left.left.nestedRoom.doorX;
                    int startY4 = leaf.parent.parent.parent.parent.left.left.left.left.nestedRoom.doorY;

                    int targetX4 = leaf.parent.parent.parent.parent.right.right.right.right.nestedRoom.doorX;
                    int targetY4 = leaf.parent.parent.parent.parent.right.right.right.right.nestedRoom.doorY;
                    PathFinder c = new PathFinder(startX4, startY4, targetX4, targetY4, dungeon, r);
                    c.execute();
                }


                return;
            }

        }

        createCorridors(leaf.left);
        createCorridors(leaf.right);
    }

    /**
     * Makes the corridors look far better
     */
    public void visualize() {
        for (int x = 2; x < Engine.WIDTH - 2; x++) {
            for (int y = 2; y < Engine.HEIGHT - 2; y++) {
                corridorAdjacent(x, y);
                doubleWallFix(x, y);
                addCorridorWalls(x, y);
                hallWayMerge(x, y);
                deleteBlockedWalls(x, y);
                fixHallwayWalls(x, y);
            }
        }
    }

    public void fixHallwayWalls(int x, int y) {
        if (dungeon[x][y] != Tileset.DUNGEONFLOOR) {
            return;
        }
        if (dungeon[x][y + 1] == Tileset.NOTHING) {
            dungeon[x][y + 1] = Tileset.MYWALL;
        }
        if (dungeon[x][y - 1] == Tileset.NOTHING) {
            dungeon[x][y - 1] = Tileset.MYWALL;
        }

        if (dungeon[x + 1][y] == Tileset.NOTHING) {
            dungeon[x + 1][y] = Tileset.MYWALL;
        }
        if (dungeon[x - 1][y] == Tileset.NOTHING) {
            dungeon[x - 1][y] = Tileset.MYWALL;
        }

    }
    public void doubleWallFix(int x, int y) {
        if (dungeon[x][y] != Tileset.MYWALL) {
            return;
        }

        if ((dungeon[x - 1][y] == Tileset.MYWALL) && (dungeon[x][y + 1] == Tileset.MYWALL)
                && (dungeon[x][y - 1] == Tileset.MYWALL)) {
            dungeon[x][y] = Tileset.DUNGEONFLOOR;
        }

        if ((dungeon[x + 1][y] == Tileset.MYWALL) && (dungeon[x][y + 1] == Tileset.MYWALL)
                && (dungeon[x][y - 1] == Tileset.MYWALL)) {
            dungeon[x][y] = Tileset.DUNGEONFLOOR;
            dungeon[x][y + 1] = Tileset.DUNGEONFLOOR;
            dungeon[x][y - 1] = Tileset.DUNGEONFLOOR;
            dungeon[x + 1][y - 1] = Tileset.DUNGEONFLOOR;
            dungeon[x + 1][y] = Tileset.DUNGEONFLOOR;
            dungeon[x + 1][y + 1] = Tileset.DUNGEONFLOOR;
        }


    }

    public void hallWayMerge(int x, int y){
        if (dungeon[x][y] == Tileset.CALMWATER1){
            dungeon[x][y] = Tileset.DUNGEONFLOOR;
        }
    }

    public void deleteBlockedWalls(int x, int y) {
        if (dungeon[x][y] != Tileset.MYWALL) {
            return;
        }
        if ((dungeon[x + 1][y] == Tileset.DUNGEONFLOOR) && (dungeon[x-1][y] == Tileset.DUNGEONFLOOR)) {
            dungeon[x][y] = Tileset.DUNGEONFLOOR;
        }

        if ((dungeon[x][y + 1] == Tileset.DUNGEONFLOOR) && (dungeon[x][y - 1] == Tileset.DUNGEONFLOOR)) {
            dungeon[x][y] = Tileset.DUNGEONFLOOR;
        }
    }

    public void addCorridorWalls(int x, int y) {
        if (dungeon[x][y] != Tileset.NOTHING) {
            return;
        }
        if (dungeon[x + 1][y] == Tileset.CALMWATER1) {
            dungeon[x][y] = Tileset.MYWALL;
        }
        if (dungeon[x - 1][y] == Tileset.CALMWATER1) {
            dungeon[x][y] = Tileset.MYWALL;
        }
        if (dungeon[x][y + 1] == Tileset.CALMWATER1) {
            dungeon[x][y] = Tileset.MYWALL;
        }
        if (dungeon[x][y - 1] == Tileset.CALMWATER1) {
            dungeon[x][y] = Tileset.MYWALL;
        }

    }


    public void corridorAdjacent(int x, int y){
        if (dungeon[x][y] != Tileset.MYWALL) {
            return;
        }

        if (dungeon[x][y-1] == Tileset.CALMWATER1) {
            dungeon[x][y] = Tileset.DUNGEONFLOOR;
        } else if (dungeon[x][y+1] == Tileset.CALMWATER1) {
            dungeon[x][y] = Tileset.DUNGEONFLOOR;
        } else if (dungeon[x + 1][y] == Tileset.CALMWATER1) {
            dungeon[x][y] = Tileset.DUNGEONFLOOR;
        } else if (dungeon[x - 1][y] == Tileset.CALMWATER1) {
            dungeon[x][y] = Tileset.DUNGEONFLOOR;
        }

    }





}
