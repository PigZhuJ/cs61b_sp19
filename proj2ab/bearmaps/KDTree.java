package bearmaps;

import java.util.List;

public class KDTree implements PointSet {

    private Node root;

    public KDTree(List<Point> points) {
        root = null;
        for (Point point : points) {
            insert(point);
        }
    }

    // Insert points into the KDTree.
    private void insert(Point point) {
        root = insert(point, root, true);
    }

    private Node insert(Point point, Node node, boolean isNodeSplitX) {
        if (node == null) {
            return new Node(point);
        }
        double pointPos = isNodeSplitX ? point.getX() : point.getY();
        double nodePos = isNodeSplitX ? node.getPoint().getX() : node.getPoint().getY();
        if (pointPos < nodePos) {
            node.left = insert(point, node.getLeft(), !isNodeSplitX);
        } else {
            node.right = insert(point, node.getRight(), !isNodeSplitX);
        }
        return node;
    }

    @Override
    public Point nearest(double x, double y) {
        Point target = new Point(x, y);
        return nearest(target, root, root.getPoint(), true);
    }

    private Point nearest(Point target, Node node, Point globalNearest, boolean isNodeSplitX) {
        if (node == null) {
            return globalNearest;
        }
        double targetPos = isNodeSplitX ? target.getX() : target.getY();
        double nodePos = isNodeSplitX ? node.getPoint().getX() : node.getPoint().getY();

        // Search the nearest point in the region where target locates.
        Node goodNode = targetPos < nodePos ? node.getLeft() : node.getRight();
        Node badNode = targetPos < nodePos ? node.getRight() : node.getLeft();
        if (goodNode != null) {
            double currDist = Point.distance(target, globalNearest);
            double goodDist = Point.distance(target, goodNode.getPoint());
            if (goodDist < currDist) {
                globalNearest = goodNode.getPoint();
            }
            globalNearest = nearest(target, goodNode, globalNearest, !isNodeSplitX);
        }

        // Search the nearest point in the another half region if possible.
        if (badNode != null) {
            double nearestDist = Point.distance(target, globalNearest);
            double virtualX = isNodeSplitX ? node.getPoint().getX() : target.getX();
            double virtualY = isNodeSplitX ? target.getY() : node.getPoint().getY();
            Point virtualPoint = new Point(virtualX, virtualY);
            double virtualDist = Point.distance(target, virtualPoint);
            if (virtualDist < nearestDist) {
                globalNearest = nearest(target, badNode, globalNearest, !isNodeSplitX);
            }
        }
        return globalNearest;
    }

    private class Node {

        private Point point;
        private Node left;
        private Node right;

        private Node(Point point) {
            this.point = point;
            left = null;
            right = null;
        }

        private Point getPoint() {
            return point;
        }

        private Node getLeft() {
            return left;
        }

        private Node getRight() {
            return right;
        }
    }

    /*
    public static void main(String[] args) {
        Point p1 = new Point(1.1, 2.2); // constructs a Point with x = 1.1, y = 2.2
        Point p2 = new Point(3.3, 4.4);
        Point p3 = new Point(-2.9, 4.2);
        KDTree kdTree = new KDTree(List.of(p1, p2, p3));

        Point nearest = kdTree.nearest(3.0, 4.0); // returns p2
        System.out.println(nearest.getX() + " and " + nearest.getY()); // evaluates to 3.3 and 4.4;
    }
    */

}
