package byow.lab12;
import org.junit.Test;
import static org.junit.Assert.*;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {

    /**
     * Adds a hexagon of side length s to a given position in the world.
     */
    public static void addHexagon(TETile[][] world, int x, int y, int s, TETile tile) {
        if (s < 2) {
            throw new IllegalArgumentException("Hexagon must be at least size 2.");
        }
        addLowerHalf(world, x, y, s, tile);
        addUpperHalf(world, x, y + 2 * s - 1, s, tile);
    }

    /**
     * Constructs the lower half of a hexagon from bottom to middle.
     */
    private static void addLowerHalf(TETile[][] world, int x, int y, int s, TETile tile) {
        int height = s;

        for (int i = 0; i < height; i += 1) {
            for (int j = 0; j < s + 2 * i; j += 1) {
                // The height of the lower half of a hexagon equals to the side length s.
                // When height increases i, the start position in that level will shift i
                // to the left, i.e. decreases i. Meanwhile, in each level, the number of
                // tiles will increase (2 * current height) amount.
                world[(x - i) + j][y + i] = tile;
            }
        }
    }

    /**
     * Constructs the upper half of a hexagon from up to middle.
     */
    private static void addUpperHalf(TETile[][] world, int x, int y, int s, TETile tile) {
        int depth = s;

        for (int i = 0; i < depth; i += 1) {
            for (int j = 0; j < s + 2 * i; j += 1) {
                // The depth of the upper half of a hexagon equals to the side length s.
                // When depth increases i, the start position in that level will shift i
                // to the left, i.e. decreases i. Meanwhile, in each level, the number of
                // tiles will increase (2 * current depth) amount.
                world[(x - i) + j][y - i] = tile;
            }
        }
    }

    /**
     * Constructs a tesselation of hexagons of same side length s, which has centre column
     * locates at (x, y).
     */
    public static void addTesselationOfHexagons(TETile[][] world, int x, int y, int s) {
        for (int diff = -s + 1; diff < s; diff += 1) {
            for (int i = 0; i < 2 * s - 1; i += 1) {
                addColumnOfHexagons(world, x, y, s, diff);
            }
        }

    }

    /**
     * Constructs one column of consecutive hexagons of same side length s from bottom to up.
     * diff is the num of columns away from centre column, left minus, right plus.
     */
    private static void addColumnOfHexagons(TETile[][] world, int x, int y, int s, int diff) {
        int numOfHex = s + (s - 1) - Math.abs(diff);
        int heightOfEachHex = 2 * s;
        int startX = calcStartX(x, s, diff);
        int startY = calcStartY(y, s, diff);
        TETile tile;

        for (int i = 0; i < numOfHex; i += 1) {
            startY += heightOfEachHex;
            tile = randomTile();
            addHexagon(world, startX, startY, s, tile);
        }
    }

    /**
     * Calculates x start coordinate for column of hexagons.
     */
    private static int calcStartX(int x, int s, int diff) {
        int offset = 2 * Math.abs(diff) * (s - 1) + Math.abs(diff);
        if (diff >= 0) {
            return x + offset;
        }
        return x - offset;
    }

    /**
     * Calculates y start coordinate for column of hexagons.
     */
    private static int calcStartY(int y, int s, int diff) {
        int offset = s * Math.abs(diff);
        if (diff == 0) {
            return y;
        }
        return y + offset;
    }

    /** Picks a RANDOM tile with a 20% change of being
     *  a wall, 20% chance of being a flower, 20%
     *  chance of being a grass, 20% chance of being a
     *  floor, 20% chance of being a tree.
     */
    private static TETile randomTile() {
        Random dice = new Random();
        int tileNum = dice.nextInt(5);
        switch (tileNum) {
            case 0: return Tileset.WALL;
            case 1: return Tileset.FLOWER;
            case 2: return Tileset.GRASS;
            case 3: return Tileset.FLOOR;
            case 4: return Tileset.TREE;
            default: return Tileset.NOTHING;
        }
    }

    public static void main(String[] args) {
        // initialize the tile rendering engine with a window of size WIDTH x HEIGHT
        TERenderer ter = new TERenderer();
        ter.initialize(70, 70);
        TETile[][] world = new TETile[70][70];
        for (int x = 0; x < 70; x += 1) {
            for (int y = 0; y < 70; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
//        HexWorld.addHexagon(world, 29, 0, 3, Tileset.FLOWER);
//        HexWorld.addHexagon(world, 29, 6, 3, Tileset.GRASS);
//        HexWorld.addHexagon(world, 24, 3, 3, Tileset.WALL);
        HexWorld.addTesselationOfHexagons(world, 29, 0, 3);
        ter.renderFrame(world);

//        assertEquals(24, calcStartX(29, 3, -1));
//        assertEquals(29, calcStartX(29, 3, 0));
//        assertEquals(34, calcStartX(29, 3, 1));
//        assertEquals(19, calcStartX(29, 3, -2));
//        assertEquals(39, calcStartX(29, 3, 2));
    }
}
