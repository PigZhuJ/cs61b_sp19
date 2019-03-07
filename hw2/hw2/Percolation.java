package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private boolean[][] grid;
    private int size;
    private int top;
    private int bottom;
    private WeightedQuickUnionUF uf;
    private WeightedQuickUnionUF ufExcludeBottom; // to avoid backwash
    private int numOpenSites = 0;
    private int[][] surroundings = new int[][]{{0, 1}, {0, -1}, {1, 0}, {-1, 0}};

    // create N-by-N grid, with all sites initially blocked
    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException();
        }
        grid = new boolean[N][N];
        size = N;
        top = 0; // virtual top node
        bottom = N * N + 1; // virtual bottom node
        uf = new WeightedQuickUnionUF(N * N + 2);
        ufExcludeBottom = new WeightedQuickUnionUF(N * N + 1);
    }

    // transform (row, col) to 1D coordinate
    private int xyTo1D(int row, int col) {
        return row * size + col + 1;
    }

    // validate the validity of (row, col)
    private void validate(int row, int col) {
        if (row < 0 || col < 0 || row >= size || col >= size) {
            throw new IndexOutOfBoundsException();
        }
    }

    // open the site (row, col) if it is not open already
    public void open(int row, int col) {
        validate(row, col);
        if (!isOpen(row, col)) {
            grid[row][col] = true;
            numOpenSites += 1;
        }
        if (row == 0) {
            uf.union(xyTo1D(row, col), top);
            ufExcludeBottom.union(xyTo1D(row, col), top);
        }
        if (row == size - 1) {
            uf.union(xyTo1D(row, col), bottom);
        }
        for (int[] surrounding : surroundings) {
            int adjacentRow = row + surrounding[0];
            int adjacentCol = col + surrounding[1];
            if (adjacentRow >= 0 && adjacentRow < size) {
                if (adjacentCol >= 0 && adjacentCol < size) {
                    if (isOpen(adjacentRow, adjacentCol)) {
                        uf.union(xyTo1D(row, col), xyTo1D(adjacentRow, adjacentCol));
                        ufExcludeBottom.union(xyTo1D(row, col), xyTo1D(adjacentRow, adjacentCol));
                    }
                }
            }
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        validate(row, col);
        return grid[row][col];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        validate(row, col);
        return ufExcludeBottom.connected(xyTo1D(row, col), top);
    }

    // number of open sites
    public int numberOfOpenSites() {
        return numOpenSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return uf.connected(top, bottom);
    }

    // use for unit testing (not required, but keep this here for the autograder)
    public static void main(String[] args) {

    }

}
