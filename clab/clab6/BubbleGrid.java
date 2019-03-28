/**
 * 1. The hit one is 0, it can connect nothing to the ceiling.
 * 2. The hit one is 1, but it cannot connect to top, then no
 * one can connect to top by it.
 * 3. The hit one is 1, it can connect to the ceiling, but the near
 * ones can connect to the ceiling by other bubbles too; for this case
 * when remove the hit one, ones that connected to it will not drop.
 * 4. The hit one is 1, it can connect to the ceiling, and the near
 * ones can connect to the ceiling by it and there are no other ways
 * for them to connect to the ceiling; for this case, there will be
 * some bubbles drop after the hit one been removed.
 */
public class BubbleGrid {
    private int[][] grid;
    private int rowNum;
    private int colNum;
    private int ceiling = 0;
    private int[][] dirs = new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
    /* Create new BubbleGrid with bubble/space locations specified by grid.
     * Grid is composed of only 1's and 0's, where 1's denote a bubble, and
     * 0's denote a space. */
    public BubbleGrid(int[][] grid) {
        this.grid = grid;
        rowNum = grid.length;
        colNum = grid[0].length;
    }

    /* Returns an array whose i-th element is the number of bubbles that
     * fall after the i-th dart is thrown. Assume all elements of darts
     * are unique, valid locations in the grid. Must be non-destructive
     * and have no side-effects to grid. */
    public int[] popBubbles(int[][] darts) {
        // DONE
        /* 0th element of uf will be the ceiling. */
        UnionFind uf = new UnionFind(rowNum * colNum + 1);
        // If there is a bubble at where dart will hit, set that grid position to 2.
        for (int[] dart : darts) {
            if (grid[dart[0]][dart[1]] == 1) {
                grid[dart[0]][dart[1]] = 2;
            }
        }
        // Union the rest bubbles which will not be hit.
        // Because those bubbles that will be hit by dart have
        // been set to 2, can be sure that if the rest bubbles
        // can still be connected to the ceiling, they will not
        // fall be sure.
        for (int row = 0; row < rowNum; row += 1) {
            for (int col = 0; col < colNum; col += 1) {
                if (grid[row][col] == 1) {
                    unionNeighbors(row, col, grid, uf);
                }
            }
        }

        int[] res = new int[darts.length];
        // Count how many bubbles are connected to the ceiling.
        int count = uf.sizeOf(ceiling);
        // Reversely put 1 back to the position that will be hit.
        for (int i = darts.length - 1; i >= 0; i -= 1) {
            int row = darts[i][0];
            int col = darts[i][1];
            if (grid[row][col] == 2) {
                unionNeighbors(row, col, grid, uf);
                grid[row][col] = 1;
            }
            int newCount = uf.sizeOf(ceiling);
            // If newCount > count, it means that there are some bubbles connecting
            // to the ceiling through the bubble that will hit by dart. So when that
            // bubble be hit, those "newly" connected bubbles will fall.
            // newCount - count - 1 because the bubble be hit does not count.
            res[i] = newCount > count ? newCount - count - 1 : 0;
            // Update the count, because the put-back bubble might
            // connects some bubbles to the ceiling.
            count = newCount;
        }
        return res;
    }

    private void unionNeighbors(int row, int col, int[][] grid, UnionFind uf) {
        if (row == 0) {
            uf.union(xyTo1D(row, col), ceiling);
        }
        for (int[] dir : dirs) {
            int adjRow = row + dir[0];
            int adjCol = col + dir[1];
            if (adjRow < 0 || adjRow == rowNum || adjCol < 0 || adjCol == colNum || grid[adjRow][adjCol] != 1) {
                continue;
            }
            uf.union(xyTo1D(row, col), xyTo1D(adjRow, adjCol));
        }
    }

    // Turn (row, col) to uf index.
    private int xyTo1D(int row, int col) {
        return row * rowNum + col + 1;
    }


}
