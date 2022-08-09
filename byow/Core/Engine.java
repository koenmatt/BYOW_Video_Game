package byow.Core;

import byow.InputDemo.InputSource;
import byow.InputDemo.KeyboardInputSource;
import byow.InputDemo.StringInputDevice;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import static byow.Core.Utils.readContentsAsString;
import static byow.Core.Utils.join;
import static byow.Core.Utils.writeContents;

public class Engine {
    TERenderer ter = new TERenderer();

    public static final int WIDTH = 90;
    public static final int HEIGHT = 45;
    public StringBuilder inputs = new StringBuilder("");
    public enum State {START, PLAY, SEED, NEWWORLD};
    public enum Dir {UP, DOWN, LEFT, RIGHT, OPEN, CHEST, HOME};
    public StringBuilder seedBuilder = new StringBuilder("");
    private State state;
    public TETile[][] currentWorld;
    public Dungeon dungeon;
    public World w;
    public boolean loaded = false;
    Player p;

    Random r;

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {

        initializeStd();
        state = State.START;
        ter.initialize(WIDTH, HEIGHT);

        while (true) {
            InputSource inputSource = new KeyboardInputSource();
            createMenu();//IDEA: make menu real trippy

            while (state != State.PLAY) {
                handleMenuInputs(inputSource);
            }

            if (!loaded) {
                TETile[][] c = buildDungeon(r);
                dungeon.lineOfSight = c;
                p = dungeon.spawnPlayer();
                dungeon.spawnChests();
                ter.renderFrame(dungeon.getDungeon());
            }
            while (state == State.PLAY) {
                while (!StdDraw.hasNextKeyTyped()) {
                    int x = (int) StdDraw.mouseX();
                    int y = (int) StdDraw.mouseY();
                    displayHUD(getTileType(x, y));
                    ter.renderFrame(dungeon.getLineOfSight());
                }
                handleActivePlayInputs(inputSource);

                ter.renderFrame(dungeon.getLineOfSight());

            }
            dungeon.islands.spawnPlayer();
            displayWin();
            while (state == State.NEWWORLD) {
                handleNewPlayInputs(inputSource);
                ter.renderFrame(dungeon.islands.world);
                displayWin();
            }
        }

    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, both of these calls:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        // Fill out this method so that it run the engine using the input
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.



        InputSource inputSource = new StringInputDevice(input);
        long seed = Long.parseLong(input.replaceAll("[^0-9]", ""));
        state = State.START;
        while (state != State.PLAY) {
            handleMenuInputs(inputSource);
        }
        dungeon = getDungeonForInter(seed);
        while (state == State.PLAY) {
            if (!inputSource.possibleNextInput() && (dungeon.dungeon != null)) {
                return dungeon.dungeon;
            }
            if (!inputSource.possibleNextInput() && (dungeon.dungeon == null)) {
                return null;
            }
            handleActivePlayInputs(inputSource);
        }
        while (state == State.NEWWORLD) {
            if (!inputSource.possibleNextInput() && (dungeon.dungeon != null)) {
                return dungeon.home;
            }
            if (!inputSource.possibleNextInput() && (dungeon.dungeon == null)) {
                return null;
            }
        }

        return null;

    }


    public Dungeon getDungeonForInter(long seed) {
        initializeStd();
        ter.initialize(WIDTH, HEIGHT);
        Random currentR = new Random(seed);
        TETile[][] c = buildDungeon(currentR);
        dungeon.lineOfSight = c;
        p = dungeon.spawnPlayer();
        dungeon.spawnChests();

        return dungeon;

    }
    public void displayHUD(String tileName) {
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("Monaco", Font.BOLD, 10);
        Font fontBigger = new Font("Monaco", Font.BOLD, 20);
        StdDraw.setFont(fontBig);
        StdDraw.text(10, HEIGHT - 2, tileName);
        DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        String time = formatter.format(date);
        StdDraw.setFont(fontBig);
        StdDraw.text(70, HEIGHT - 2, time);
        StdDraw.show();
    }

    public void displayWin() {
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("Monaco", Font.BOLD, 25);
        StdDraw.setFont(fontBig);

        StdDraw.text(WIDTH/2, HEIGHT - 2, "YOU WON!!!! Enjoy your new island home.");
        StdDraw.show();
    }


    public String getTileType(int x, int y) {

        if ((x >= 90) || (x <= 0)) {
            return "";
        }

        if ((y >= 45) || (y <= 0)) {
            return "";
        }

        if (dungeon.dungeon[x][y] == Tileset.MYWALL) {
            return "A mossy wall";
        }
        if (dungeon.dungeon[x][y] == Tileset.DUNGEONFLOOR) {
            return "Dungeon floor";
        }

        if (dungeon.dungeon[x][y] == Tileset.CHEST) {
            return "A chest";
        }

        if (dungeon.dungeon[x][y] == Tileset.OPENCHEST) {
            return "An open chest";
        }

        if ((dungeon.dungeon[x][y] == Tileset.AVATAR1) || (dungeon.dungeon[x][y] == Tileset.AVATAR2)
                || (dungeon.dungeon[x][y] == Tileset.AVATAR3)) {
            return "A little worm or sperm depending on how you look at it";
        }
        if ((dungeon.dungeon[x][y] == Tileset.LAVATAR1) || (dungeon.dungeon[x][y] == Tileset.LAVATAR2)
                || (dungeon.dungeon[x][y] == Tileset.LAVATAR3)) {
            return "A little worm or sperm depending on how you look at it";
        }
        if ((dungeon.dungeon[x][y] == Tileset.DAVATAR1) || (dungeon.dungeon[x][y] == Tileset.DAVATAR2)
                || (dungeon.dungeon[x][y] == Tileset.DAVATAR3)) {
            return "A little worm or sperm depending on how you look at it";
        }
        if ((dungeon.dungeon[x][y] == Tileset.UAVATAR1) || (dungeon.dungeon[x][y] == Tileset.UAVATAR2)
                || (dungeon.dungeon[x][y] == Tileset.UAVATAR3)) {
            return "A little worm or sperm depending on how you look at it";
        } else {
            return "";
        }
    }
    public void handleActivePlayInputs(InputSource inputSource) {

        char ch = Character.toUpperCase(inputSource.getNextKey());
        switch (ch) {
            case 'W':
                state = dungeon.move(Dir.UP);
                inputs.append(ch);
                break;
            case 'S':
                state = dungeon.move(Dir.DOWN);
                inputs.append(ch);
                break;
            case 'A':
                state = dungeon.move(Dir.LEFT);
                inputs.append(ch);
                break;
            case 'D':
                state = dungeon.move(Dir.RIGHT);
                inputs.append(ch);
                break;
            case 'O':
                state = dungeon.move(Dir.OPEN);
                inputs.append(ch);
                break;
            case 'E':
                state = dungeon.move(Dir.CHEST);
                inputs.append(ch);
                break;
            case 'H':
                state = dungeon.move(Dir.HOME);
                inputs.append(ch);
                w = dungeon.islands;
                break;
            case ':':
                inputs.append(ch);
                break;
            case 'Q':
                if (inputs.indexOf(":") == inputs.length() - 1) {
                    save();
                    System.exit(0);
            }

        }

    }

    public void handleNewPlayInputs(InputSource inputSource) {

        char ch = Character.toUpperCase(inputSource.getNextKey());
        switch (ch) {
            case 'W':
                state = dungeon.islands.move(Dir.UP);
                inputs.append(ch);
                break;
            case 'S':
                state = dungeon.islands.move(Dir.DOWN);
                inputs.append(ch);
                break;
            case 'A':
                state = dungeon.islands.move(Dir.LEFT);
                inputs.append(ch);
                break;
            case 'D':
                state = dungeon.islands.move(Dir.RIGHT);
                inputs.append(ch);
                break;
            case ':':
                inputs.append(ch);
                break;
            case 'Q':
                if (inputs.indexOf(":") == inputs.length() - 1) {
                    save();
                    System.exit(0);
                }

        }

    }

    public void handleMenuInputs(InputSource inputSource) {
        char ch = Character.toUpperCase(inputSource.getNextKey());

        if ((state == State.START) && (ch == 'N')) {
            inputs.append(ch);
            drawSeedMenu();
            state = State.SEED;
        }

        else if ((state == State.START) && (ch == 'L')) {
            loaded = true;
            load();
        }

        else if (state == State.SEED && Character.isDigit(ch)) {
            seedBuilder.append(ch);
            inputs.append(ch);
            drawSeedMenu();
        }
        else if (state == State.SEED && ch == 'S') {
            inputs.append(ch);
            long seed = Long.parseLong(seedBuilder.toString().replaceAll("[^0-9]", ""));
            r = new Random(seed);
            state = State.PLAY;
        }
    }

    public void save() {
        File CWD = new File(System.getProperty("user.dir"));
        File SAVED = join(CWD, "lastGame.txt");
        writeContents(SAVED, inputs.toString());
    }
    public void load() {
        File CWD = new File(System.getProperty("user.dir"));
        File lastCommit = join(CWD, "lastGame.txt");
        String head = readContentsAsString(lastCommit);
        ter.renderFrame(interactWithInputString(head));
    }

    public void drawSeedMenu() {

        String input = inputs.toString();
        input = input.substring(input.indexOf('N') + 1);

        StdDraw.clear(Color.PINK);
        StdDraw.setPenColor(Color.white);
        StdDraw.setFont(new Font("Monaco", Font.BOLD, 35));
        StdDraw.text(WIDTH / 2, HEIGHT - 20, "PLEASE ENTER A SEED");
        StdDraw.setFont(new Font("Monaco", Font.BOLD, 25));
        StdDraw.text((WIDTH / 2) - 10, HEIGHT - 22, "SEED:");
        StdDraw.text(WIDTH / 2, HEIGHT - 22, input);
        StdDraw.text(WIDTH / 2, HEIGHT - 30, "Press S to continue");

        StdDraw.setPenColor(Color.yellow);


        StdDraw.show();
    }




    public TETile[][] buildDungeon(Random r) {
        Dungeon d = new Dungeon(r, r.nextInt(2, 4));
        d.renderDungeon();
        d.createCorridors();
        d.renderDungeon();
        d.visualize();
        dungeon = d;
        return d.getDungeon();
    }



    public void createMenu() {
        StdDraw.clear(Color.PINK);
        StdDraw.setPenColor(Color.BLUE);
        StdDraw.setFont(new Font("Monaco", Font.BOLD, 35));
        StdDraw.text(WIDTH / 2, HEIGHT - 15, "NORIEGA");
        StdDraw.setFont(new Font("Monaco", Font.BOLD, 20));
        StdDraw.text(WIDTH / 2, HEIGHT / 2, "NEW GAME (N)");
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 2, "LOAD GAME (L)");
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 4, "QUIT (Q)");
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 6, "CHOOSE CHARACTER (C)");
        StdDraw.show();
    }

    public void initializeStd() {
        StdDraw.setCanvasSize(WIDTH * 16, HEIGHT * 16);
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, HEIGHT);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();
    }


}
