package bearmaps;

import java.util.ArrayList;
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
        root = insert(point, root, false, 0.0, 0.0, 1.0, 1.0);
    }

    private Node insert(Point point, Node node, boolean isVertical,
                        double minX, double minY, double maxX, double maxY) {
        if (node == null) {
            return new Node(point, new Region(minX, minY, maxX, maxY));
        }
        isVertical = !isVertical;
        double pointPos = isVertical ? point.getX() : point.getY();
        double nodePos = isVertical ? node.getPoint().getX() : node.getPoint().getY();
        if (pointPos < nodePos) {
            node.left = insert(point, node.getLeft(), isVertical, minX, minY,
                    isVertical ? node.getPoint().getX() : maxX, isVertical ? maxY : node.getPoint().getY());
        } else {
            node.right = insert(point, node.getRight(), isVertical,
                    isVertical ? node.getPoint().getX() : minX, isVertical ? minY : node.getPoint().getY(), maxX, maxY);
        }
        return node;
    }

    @Override
    public Point nearest(double x, double y) {
        Point target = new Point(x, y);
        return nearest(target, root, null, false);
    }

    private Point nearest(Point target, Node node, Point currNearest, boolean isVertical) {
        if (node == null) {
            return currNearest;
        }
        isVertical = !isVertical;
        double targetPos = isVertical ? target.getX() : target.getY();
        double nodePos = isVertical ? node.getPoint().getX() : node.getPoint().getY();

        // Search the nearest point in the region where target locates.
        Node nextNode = targetPos < nodePos ? node.getLeft() : node.getRight();
        Node otherNode = targetPos < nodePos ? node.getRight() : node.getLeft();
        Point nextNearest = nearest(target, nextNode, node.getPoint(), isVertical);
        double currDist = 0;
        double nextDist = Point.distance(target, nextNearest);
        if (currNearest == null) {
            currNearest = nextNearest;
            currDist = nextDist;
        } else {
            currDist = Point.distance(target, currNearest);
            if (nextDist < currDist) {
                currNearest = nextNearest;
                currDist = nextDist;
            }
        }
        // Search the nearest point in the another half region.
        if ((otherNode != null) && (otherNode.getRegion().distanceToPoint(target) < currDist)) {
            currNearest = nearest(target, otherNode, currNearest, isVertical);
        }
        return currNearest;
    }

    private class Node {

        private Point point;
        private Region region;
        private Node left;
        private Node right;

        public Node(Point point, Region region) {
            this.point = point;
            this.region = region;
            left = null;
            right = null;
        }

        public Point getPoint() {
            return point;
        }

        public Region getRegion() {
            return region;
        }

        public Node getLeft() {
            return left;
        }

        public Node getRight() {
            return right;
        }
    }

    private class Region {

        private double minX;
        private double minY;
        private double maxX;
        private double maxY;

        public Region(double minX, double minY, double maxX, double maxY) {
            this.minX = minX;
            this.minY = minY;
            this.maxX = maxX;
            this.maxY = maxY;
        }

        public boolean contains(Point point) {
            return (point.getX() >= minX) && (point.getX() <= maxX)
                && (point.getY() >= minY) && (point.getY() <= maxY);
        }

        public double distanceToPoint(Point point) {
            double dx = 0.0;
            double dy = 0.0;
            if (point.getX() < minX) {
                dx = minX - point.getX();
            } else if (point.getX() > maxX) {
                dx = point.getX() - maxX;
            }
            if (point.getY() < minY) {
                dy = minY - point.getY();
            } else if (point.getY() > maxY) {
                dy = point.getY() - maxY;
            }
            return Math.pow(dx, 2) + Math.pow(dy, 2);
        }
    }

    /*
    public static void main(String[] args) {
        Point p1 = new Point(1.1, 2.2); // constructs a Point with x = 1.1, y = 2.2
        Point p2 = new Point(3.3, 4.4);
        Point p3 = new Point(-2.9, 4.2);
        KDTree kdTree = new KDTree(List.of(p1, p2, p3));

        Point nearestPoint = kdTree.nearest(3.0, 4.0); // returns p2
        System.out.println(nearestPoint.getX() + " and " + nearestPoint.getY()); // evaluates to 3.3 and 4.4;
    }
    */
}
