import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Stack;

import java.awt.Color;
import java.util.TreeSet;

public class PointSET {
    private final TreeSet<Point2D> points;

    public PointSET() {
        // construct an empty set of points
        points = new TreeSet<>();
    }

    public static void main(String[] args) {
        // unit testing of the methods (optional)

        PointSET brute = new PointSET();
        String filename = "kdtree/Input10.txt";
        In in = new In(filename);

        System.out.println("Is empty?\t" + brute.isEmpty());

        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D point = new Point2D(x, y);
            brute.insert(point);
        }

        System.out.println("Is empty?\t" + brute.isEmpty());
        System.out.println("Size:\t\t" + brute.size());
        System.out.println("Contains:\t" + brute.contains(new Point2D(0, 0.2)));

        StdDraw.clear();
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        brute.draw();

        // Test of range(Rect) function;
        RectHV rect = new RectHV(0, 0, 0.5, 0.5);
        long startRange = System.nanoTime();
        Iterable<Point2D> selectedPoints = brute.range(rect);
        long stopRange = System.nanoTime();
        System.out.println("range() time: " + (stopRange - startRange) + "ns");
        // range() time: 2967333ns, 2885917ns (for input10K.txt)
        // range() time: 1257417ns, 1288500ns (for input10K.txt)

        System.out.println("Selected points:");
        for (Point2D point : selectedPoints) {
            System.out.println("\t\t\t\t" + point);
            StdDraw.setPenRadius(0.002);
            StdDraw.setPenColor(Color.RED);
            StdDraw.circle(point.x(), point.y(), 0.01);
        }
        // Test of nearest() function;
        Point2D point = new Point2D(0.2, 0.25);

        long startNearest = System.nanoTime();
        Point2D nearestPoint = brute.nearest(point);
        long stopNearest = System.nanoTime();
        System.out.println("nearest() time: " + (stopNearest - startNearest) + "ns");
        // nearest() time: 1185709ns, 1512541ns (for input10K.txt)


        System.out.println("\nNearest point:\t" + nearestPoint);
        StdDraw.setPenRadius(0.01);
        StdDraw.setPenColor(Color.BLUE);
        StdDraw.point(point.x(), point.y());
        StdDraw.setPenRadius(0.002);
        StdDraw.line(point.x(), point.y(), nearestPoint.x(), nearestPoint.y());


        StdDraw.show();
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public int size() {
        // number of points in the set
        return points.size();
    }

    public void insert(Point2D p) {
        // add the point to the set (if it is not already in the set)
        if (p == null) throw new IllegalArgumentException("Null argument");
        if (points.contains(p)) return;
        points.add(p);

    }

    public boolean contains(Point2D p) {
        // does the set contain point p?
        if (p == null) throw new IllegalArgumentException("Null argument");
        return points.contains(p);
    }

    public void draw() {
        // draw all points to standard draw
        StdDraw.setPenRadius(0.01);
        StdDraw.setPenColor(Color.BLACK);
        for (Point2D point : points) {
            StdDraw.point(point.x(), point.y());
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        // all points that are inside the rectangle (or on the boundary)
        if (rect == null) throw new IllegalArgumentException("Null argument");
        Stack<Point2D> stack = new Stack<>();
        for (Point2D point : points) {
            if (rect.contains(point)) stack.push(point);
        }
        return stack;
    }

    public Point2D nearest(Point2D p) {
        // a nearest neighbor in the set to point p; null if the set is empty
        if (p == null) throw new IllegalArgumentException("Null argument");
        double bestDist = Double.POSITIVE_INFINITY;
        double distance;
        Point2D nearest = null;
        for (Point2D point : points) {
            distance = p.distanceSquaredTo(point);
            if (distance < bestDist) {
                bestDist = distance;
                nearest = point;
            }
        }
        return nearest;
    }

}
