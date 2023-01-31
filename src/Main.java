import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import org.w3c.dom.Node;

import java.awt.*;

public class Main {
    public static void main(String[] args) {
        KdTree kdtree = new KdTree();

        String filename = "kdtree/Input10.txt";
        In in = new In(filename);

        System.out.println("Is empty?\t" + kdtree.isEmpty());

        StdDraw.setPenRadius(0.02);
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D point = new Point2D(x, y);
            kdtree.insert(point);
        }

        Point2D point2D = new Point2D(0.5, 0);
        System.out.println("Contains point:\t" + kdtree.contains(point2D));

        System.out.println("Is empty?\t" + kdtree.isEmpty());
        System.out.println("Size:\t\t" + kdtree.size());

        StdDraw.clear();
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        kdtree.draw();

        // Test of range(Rect) function;
        RectHV rect = new RectHV(0, 0, 0.5, 0.5);
        long startRange = System.nanoTime();
        Iterable<Point2D> selectedPoints = kdtree.range(rect);
        long stopRange = System.nanoTime();
        System.out.println("range() time: " + (stopRange - startRange) + "ns");
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
        Point2D nearestPoint = kdtree.nearest(point);
        long stopNearest = System.nanoTime();
        System.out.println("nearest() time: " + (stopNearest - startNearest) + "ns");
        // nearest() time: 51708ns, 48250ns (for input10K.txt)

        System.out.println("\nNearest point:\t" + nearestPoint);
        StdDraw.setPenRadius(0.01);
        StdDraw.setPenColor(Color.BLUE);
        StdDraw.point(point.x(), point.y());
        StdDraw.setPenRadius(0.002);
        StdDraw.line(point.x(), point.y(), nearestPoint.x(), nearestPoint.y());


        StdDraw.show();
    }
}