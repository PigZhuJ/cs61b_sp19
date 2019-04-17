package bearmaps.proj2ab;

import java.util.Collections;
import java.util.List;

public class KDTree implements PointSet {
    private static final boolean HORIZONTAL = false;
    private Node root;

    public KDTree(List<Point> points) {
        Collections.shuffle(points);
        for (Point point : points) {
            root = insert(point, root, HORIZONTAL);
        }
    }

    // Insert points into the KDTree.
    private Node insert(Point point, Node node, boolean splitDim) {
        if (node == null) {
            return new Node(point, splitDim);
        }

        // If point has the same coordinate as the node point,
        // just return the node;
        if (point.equals(node.getPoint())) {
            return node;
        }

        int cmp = comparePoints(point, node.getPoint(), splitDim);
        if (cmp < 0) {
            node.left = insert(point, node.getLeft(), !splitDim);
        } else {
            node.right = insert(point, node.getRight(), !splitDim);
        }

        return node;
    }

    @Override
    // Find the nearest point to the target point.
    public Point nearest(double x, double y) {
        Point target = new Point(x, y);
        return nearest(root, target, root.getPoint());
    }

    private Point nearest(Node node, Point target, Point best) {
        if (node == null) {
            return best;
        }

        // Compare the current best point with the current node's point.
        double bestDist = Point.distance(best, target);
        double currDist = Point.distance(node.getPoint(), target);
        if (Double.compare(currDist, bestDist) < 0) {
            best = node.getPoint();
        }

        Node goodSideNode;
        Node badSideNode;
        int cmp = comparePoints(target, node.getPoint(), node.getSplitDim());
        if (cmp < 0) {
            goodSideNode = node.getLeft();
            badSideNode = node.getRight();
        } else {
            goodSideNode = node.getRight();
            badSideNode = node.getLeft();
        }

        // DFS in goodSide first, then check the badSide, done recursively.
        best = nearest(goodSideNode, target, best);
        if (isWorthLooking(node, target, best)) {
            best = nearest(badSideNode, target, best);
        }

        return best;
    }

    // Check whether the badSide intersects with the circle that,
    // centred at target point with radius of square distance between
    // target point and best point. If intersects, then the badSide is
    // worth looking.
    private boolean isWorthLooking(Node node, Point target, Point best) {
        double distToBest = Point.distance(best, target);
        double distToBad;
        if (node.splitDim == HORIZONTAL) {
            distToBad = Point.distance(new Point(node.point.getX(), target.getY()), target);
        } else {
            distToBad = Point.distance(new Point(target.getX(), node.point.getY()), target);
        }
        return Double.compare(distToBad, distToBest) < 0;
    }

    // Compare two points based on the split dimension of the current node.
    private int comparePoints(Point a, Point b, boolean splitDim) {
        if (splitDim == HORIZONTAL) {
            return Double.compare(a.getX(), b.getX());
        } else {
            return Double.compare(a.getY(), b.getY());
        }
    }

    private class Node {
        private Point point;
        private boolean splitDim;
        private Node left;
        private Node right;

        Node(Point point, boolean splitDim) {
            this.point = point;
            this.splitDim = splitDim;
            left = null;
            right = null;
        }

        public Point getPoint() {
            return point;
        }

        public Node getLeft() {
            return left;
        }

        public Node getRight() {
            return right;
        }

        public boolean getSplitDim() {
            return splitDim;
        }
    }

//    public static void main(String[] args) {
//
//    }
}
