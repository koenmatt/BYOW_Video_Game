package byow.TileEngine;

import java.awt.Color;

/**
 * Contains constant tile objects, to avoid having to remake the same tiles in different parts of
 * the code.
 *
 * You are free to (and encouraged to) create and add your own tiles to this file. This file will
 * be turned in with the rest of your code.
 *
 * Ex:
 *      world[x][y] = Tileset.FLOOR;
 *
 * The style checker may crash when you try to style check this file due to use of unicode
 * characters. This is OK.
 */

public class Tileset {
    public static final TETile AVATAR = new TETile('@', Color.white, Color.black, "you");
    public static final TETile WALL = new TETile('#', new Color(216, 128, 128), Color.darkGray,
            "wall");
    public static final TETile FLOOR = new TETile('·', new Color(128, 192, 128), Color.black,
            "floor");
    public static final TETile NOTHING = new TETile(' ', Color.black, Color.black, "nothing");
    public static final TETile GRASS = new TETile('"', Color.green, Color.black, "grass");
    public static final TETile WATER = new TETile('≈', Color.blue, Color.black, "water");
    public static final TETile FLOWER = new TETile('❀', Color.magenta, Color.pink, "flower");
    public static final TETile LOCKED_DOOR = new TETile('█', Color.orange, Color.black,
            "locked door");
    public static final TETile UNLOCKED_DOOR = new TETile('▢', Color.orange, Color.black,
            "unlocked door");
    public static final TETile SAND = new TETile('▒', Color.yellow, Color.black, "sand");
    public static final TETile MOUNTAIN = new TETile('▲', Color.gray, Color.black, "mountain");
    public static final TETile TREE = new TETile('♠', Color.green, Color.black, "tree");
    public static final TETile CALMWATER1 = new TETile('A', Color.green, Color.black, "calmwater1", "/Users/matthewkoen/cs61bl/su22-s192/proj3/byow/pics/water/calm1.png");
    public static final TETile CALMWATER2 = new TETile('A', Color.green, Color.black, "calmwater1", "/Users/matthewkoen/cs61bl/su22-s192/proj3/byow/pics/water/calm2.png");
    public static final TETile CALMWATER3 = new TETile('A', Color.green, Color.black, "calmwater1", "/Users/matthewkoen/cs61bl/su22-s192/proj3/byow/pics/water/calm3.png");
    public static final TETile CALMWATER4 = new TETile('A', Color.green, Color.black, "calmwater1", "/Users/matthewkoen/cs61bl/su22-s192/proj3/byow/pics/water/calm4.png");

    public static final TETile LAND1 = new TETile('A', Color.green, Color.black, "land", "/Users/matthewkoen/cs61bl/su22-s192/proj3/byow/pics/land/emptyLand.png");
    public static final TETile OCEANLAND1 = new TETile('A', Color.green, Color.black, "land", "/Users/matthewkoen/cs61bl/su22-s192/proj3/byow/pics/land/oceanLand.png");
    public static final TETile OCEANLAND2 = new TETile('A', Color.green, Color.black, "land", "/Users/matthewkoen/cs61bl/su22-s192/proj3/byow/pics/land/oceanLand1.png");

    public static final TETile BUSH = new TETile('A', Color.green, Color.black, "land", "/Users/matthewkoen/cs61bl/su22-s192/proj3/byow/pics/land/bush.png");
    public static final TETile ROCK = new TETile('A', Color.green, Color.black, "land", "/Users/matthewkoen/cs61bl/su22-s192/proj3/byow/pics/land/rock.png");

    public static final TETile MYWALL = new TETile('A', Color.green, Color.black, "land", "/Users/matthewkoen/cs61bl/su22-s192/proj3/byow/pics/land/wall.png");
    public static final TETile DUNGEONFLOOR = new TETile('A', Color.green, Color.black, "land", "/Users/matthewkoen/cs61bl/su22-s192/proj3/byow/pics/land/dungeonFloor.png");

    public static final TETile AVATAR1 = new TETile('A', Color.green, Color.black, "land", "/Users/matthewkoen/cs61bl/su22-s192/proj3/byow/pics/avatars/worm1.png");
    public static final TETile AVATAR2 = new TETile('A', Color.green, Color.black, "land", "/Users/matthewkoen/cs61bl/su22-s192/proj3/byow/pics/avatars/worm2.png");
    public static final TETile AVATAR3 = new TETile('A', Color.green, Color.black, "land", "/Users/matthewkoen/cs61bl/su22-s192/proj3/byow/pics/avatars/worm3.png");

    public static final TETile LAVATAR1 = new TETile('A', Color.green, Color.black, "land", "/Users/matthewkoen/cs61bl/su22-s192/proj3/byow/pics/avatars/leftAvatar1.png");
    public static final TETile LAVATAR2 = new TETile('A', Color.green, Color.black, "land", "/Users/matthewkoen/cs61bl/su22-s192/proj3/byow/pics/avatars/leftAvatar2.png");
    public static final TETile LAVATAR3 = new TETile('A', Color.green, Color.black, "land", "/Users/matthewkoen/cs61bl/su22-s192/proj3/byow/pics/avatars/leftAvatar3.png");

    public static final TETile DAVATAR1 = new TETile('A', Color.green, Color.black, "land", "/Users/matthewkoen/cs61bl/su22-s192/proj3/byow/pics/avatars/downAvatar1.png");
    public static final TETile DAVATAR2 = new TETile('A', Color.green, Color.black, "land", "/Users/matthewkoen/cs61bl/su22-s192/proj3/byow/pics/avatars/downAvatar2.png");
    public static final TETile DAVATAR3 = new TETile('A', Color.green, Color.black, "land", "/Users/matthewkoen/cs61bl/su22-s192/proj3/byow/pics/avatars/downAvatar1.png");

    public static final TETile UAVATAR1 = new TETile('A', Color.green, Color.black, "land", "/Users/matthewkoen/cs61bl/su22-s192/proj3/byow/pics/avatars/upAvatar1.png");
    public static final TETile UAVATAR2 = new TETile('A', Color.green, Color.black, "land", "/Users/matthewkoen/cs61bl/su22-s192/proj3/byow/pics/avatars/upAvatar2.png");
    public static final TETile UAVATAR3 = new TETile('A', Color.green, Color.black, "land", "/Users/matthewkoen/cs61bl/su22-s192/proj3/byow/pics/avatars/upAvatar1.png");

    public static final TETile BADAPPLE = new TETile('A', Color.green, Color.black, "land", "/Users/matthewkoen/cs61bl/su22-s192/proj3/byow/pics/food/badApple.png");
    public static final TETile DARKWALL = new TETile('A', Color.green, Color.black, "land", "/Users/matthewkoen/cs61bl/su22-s192/proj3/byow/pics/land/darkWall.png");

    public static final TETile CHEST = new TETile('A', Color.green, Color.black, "land", "/Users/matthewkoen/cs61bl/su22-s192/proj3/byow/pics/land/chest.png");
    public static final TETile BCHEST = new TETile('A', Color.green, Color.black, "land", "/Users/matthewkoen/cs61bl/su22-s192/proj3/byow/pics/land/badChest.png");
    public static final TETile OPENCHEST = new TETile('A', Color.green, Color.black, "land", "/Users/matthewkoen/cs61bl/su22-s192/proj3/byow/pics/land/open_chest.png");

    public static final TETile SHOVELHEAD = new TETile('A', Color.green, Color.black, "land", "/Users/matthewkoen/cs61bl/su22-s192/proj3/byow/pics/land/shovelHead.png");
    public static final TETile SHOVELHANDLE = new TETile('A', Color.green, Color.black, "land", "/Users/matthewkoen/cs61bl/su22-s192/proj3/byow/pics/land/shovel.png");

}


