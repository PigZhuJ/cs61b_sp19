package bearmaps.proj2ab;

import java.util.List;

public class KDTree implements PointSet {

    private TreeNode root;

    public KDTree(List<Point> points) {
        root = null;
        for (Point point : points) {
            insert(point);
        }
    }

    // Insert points into the KDTree.
    private void insert(Point point) {
        root = insert(point, root, "x");
    }

    private TreeNode insert(Point point, TreeNode node, String splitDim) {
        if (node == null) {
            return new TreeNode(point, splitDim);
        }

        double nodePos;
        double pointPos;

        if (node.getSplitDim().equals("x")) {
            nodePos = node.getPoint().getX();
            pointPos = point.getX();

            if (pointPos < nodePos) {
                node.left = insert(point, node.getLeft(), "y");
            } else {
                node.right = insert(point, node.getRight(), "y");
            }

        } else {
            nodePos = node.getPoint().getY();
            pointPos = point.getY();

            if (pointPos < nodePos) {
                node.left = insert(point, node.getLeft(), "x");
            } else {
                node.right = insert(point, node.getRight(), "x");
            }
        }

        return node;

    }

    @Override
    public Point nearest(double x, double y) {
        Point target = new Point(x, y);
        return nearest(target, root, root.getPoint(), "x");
    }

    private Point nearest(Point target, TreeNode node, Point best, String splitDim) {
        if (node == null) {
            return best;
        }

        double nodePos;
        double targetPos;

        if (node.getSplitDim().equals("x")) {
            nodePos = node.getPoint().getX();
            targetPos = target.getX();
        } else {
            nodePos = node.getPoint().getY();
            targetPos = target.getY();
        }

        TreeNode goodSideNode;
        TreeNode badSideNode;

        if (targetPos < nodePos) {
            goodSideNode = node.getLeft();
            badSideNode = node.getRight();
        } else {
            goodSideNode = node.getRight();
            badSideNode = node.getLeft();
        }

        // Search the possible nearest point in the region where target locates.
        double currDist = Point.distance(target, best);

        if (goodSideNode != null) {
            double goodDist = Point.distance(target, goodSideNode.getPoint());

            if (goodDist < currDist) {
                best = goodSideNode.getPoint();
            }

            if (node.getSplitDim().equals("x")){
                best = nearest(target, goodSideNode, best, "y");
            } else {
                best = nearest(target, goodSideNode, best, "x");
            }
        }

        // Search the nearest point in the another half region if possible.
        if (badSideNode != null) {

            double badX;
            double badY;

            if (node.getSplitDim().equals("x")) {
                badX = node.getPoint().getX();
                badY = target.getY();
            } else {
                badX = target.getX();
                badY = node.getPoint().getY();
            }

            Point badPoint = new Point(badX, badY);
            double badDist = Point.distance(target, badPoint);
            double bestDist = Point.distance(target, best);

            if (badDist < bestDist) {

                if (node.getSplitDim().equals("x")) {
                    best = nearest(target, badSideNode, best, "y");
                } else {
                    best = nearest(target, badSideNode, best, "x");
                }
            }
        }

        return best;
    }

    private class TreeNode {

        private Point point;
        private TreeNode left;
        private TreeNode right;
        private String splitDim;

        private TreeNode(Point point, String dim) {
            this.point = point;
            left = null;
            right = null;
            splitDim = dim;
        }

        private Point getPoint() {
            return point;
        }

        private TreeNode getLeft() {
            return left;
        }

        private TreeNode getRight() {
            return right;
        }

        private String getSplitDim() {
            return splitDim;
        }
    }

}
