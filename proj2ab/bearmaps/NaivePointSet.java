package bearmaps;

import java.util.List;

public class NaivePointSet implements PointSet {

    private List<Point> points;

    public NaivePointSet(List<Point> points) {
        this.points = points;
    }

    @Override
    public Point nearest(double x, double y) {
        Point targetPoint = new Point(x, y);
        Point nearestPoint = points.get(0);
        double shortestDist = Point.distance(targetPoint, nearestPoint);
        for (int i = 1; i < points.size(); i += 1) {
            double currDist = Point.distance(targetPoint, points.get(i));
            if (currDist < shortestDist) {
                shortestDist = currDist;
                nearestPoint = points.get(i);
            }
        }
        return nearestPoint;
    }

    public static void main(String[] args) {
        Point p1 = new Point(1.1, 2.2); // constructs a Point with x = 1.1, y = 2.2
        Point p2 = new Point(3.3, 4.4);
        Point p3 = new Point(-2.9, 4.2);

        NaivePointSet nn = new NaivePointSet(List.of(p1, p2, p3));
        Point ret = nn.nearest(3.0, 4.0); // returns p2
        System.out.println(ret.getX() + " and " + ret.getY()); // evaluates to 3.3 and 4.4;
    }
}
