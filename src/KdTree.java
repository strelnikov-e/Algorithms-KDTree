import edu.princeton.cs.algs4.*;
import org.w3c.dom.Node;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

public class KdTree {
    private Node root;
    private int size;
    private ArrayList<Node> visitedNodes = new ArrayList<>();

    public KdTree() {
        // construct an empty set of points
    }

    public static void main(String[] args) {
        // unit testing of the methods (optional)

        KdTree kdtree = new KdTree();

        String filename = "kdtree/Input10.txt";
        In in = new In(filename);
        // range() time:    4105000ns
        // nearest() time:  279583ns

        System.out.println("Is empty?\t\t" + kdtree.isEmpty());

        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D point = new Point2D(x, y);
            kdtree.insert(point);
        }

        kdtree.draw();

        Point2D point2D = new Point2D(0.5, 0);

        System.out.println("Contains point:\t" + kdtree.contains(point2D));
        System.out.println("Is empty?\t\t" + kdtree.isEmpty());
        System.out.println("Size:\t\t\t" + kdtree.size());

        // Test of range(Rect) function;
        RectHV rect = new RectHV(0, 0, 0.3, 0.5);
        long startRange = System.nanoTime();
        kdtree.range(rect);
        long stopRange = System.nanoTime();
        System.out.println("range() time:\t" + (stopRange - startRange) + "ns");
        // range() time: 1257417ns, 1288500ns (for input10K.txt)

        // Test of nearest() function;
        Point2D point = new Point2D(0.7, 0.6);
        long startNearest = System.nanoTime();
        Point2D nearestPoint = kdtree.nearest(point);
        long stopNearest = System.nanoTime();
        System.out.println("nearest() time:\t" + (stopNearest - startNearest) + "ns");
        // nearest() time: 51708ns, 48250ns (for input10K.txt)
        StdDraw.line(point.x(),point.y(),nearestPoint.x(),nearestPoint.y());

        System.out.println("\nNearest point:\t" + nearestPoint);


        int i = 0;
        int j = 0;
        int k = 0;
        StdDraw.setPenRadius(0.01);
        for (Node node: kdtree.getVisitedNodes()) {
            try {
                Thread.sleep(1000);
            }catch (InterruptedException e){

            }
            double x = (node.rect.xmax() - node.rect.xmin())/2;
            double y = (node.rect.ymax() - node.rect.ymin())/2;
            System.out.println(x + " " + y + " " + node.rect.width()/2 + " " + node.rect.height()/2);
            System.out.println(node.rect + " Point: " + node.point);
            System.out.println();




            StdDraw.setPenColor(i,j,k);
//            StdDraw.filledRectangle(x,y,node.rect.width()/2,node.rect.height()/2);
//            kdtree.draw();
            StdDraw.setPenColor(Color.RED);
            node.rect.draw();
            i= i + 5;
            j = j + 20;
            k = k + 10;

        }
//
    }


    public ArrayList<Node> getVisitedNodes() {
        return visitedNodes;
    }

    public boolean isEmpty() {
        return this.root == null;
    }

    public int size() {
        // number of points in the set
        return this.size;
    }

    public void insert(Point2D p) {
        // add the point to the set (if it is not already in the set)
        if (p == null) throw new IllegalArgumentException("Null argument");
        root = insert(root, p, 0.0, 0.0, 1.0, 1.0, true);
    }

    private Node insert(Node node, Point2D p, double x0, double y0, double x1, double y1, boolean isVertical) {

        if (node == null) {
            this.size++;
            return new Node(p, new RectHV(x0, y0, x1, y1), null, null, isVertical);
        }
        if (p.equals(node.point)) return node;

        if (node.isVertical) {
            // rectangle should divide space by x-axis.
            double cmp = p.x() - node.point.x();

            if (cmp < 0) {
                node.leftBottom = insert(node.leftBottom, p, x0, y0, node.point.x(), y1, false);
            } else {
                node.rightTop = insert(node.rightTop, p, node.point.x(), y0, x1, y1, false);
            }
        } else {
            double cmp = p.y() - node.point.y();

            if (cmp < 0) {
                node.leftBottom = insert(node.leftBottom, p, x0, y0, x1, node.point.y(), true);
            } else {
                node.rightTop = insert(node.rightTop, p, x0, node.point.y(), x1, y1, true);
            }
        }
        return node;
    }

    public boolean contains(Point2D p) {
        // does the set contain point p?
        if (p == null) throw new IllegalArgumentException("Null argument");
        return contains(this.root, p);
    }

    private boolean contains(Node node, Point2D p) {
        if (node == null) return false;
        if (node.point.compareTo(p) == 0) return true;
        double cmp;
        if (node.isVertical) {
            cmp = p.x() - node.point.x();
        } else {
            cmp = p.y() - node.point.y();
        }
        if (cmp < 0) return contains(node.leftBottom, p);
        else return contains(node.rightTop, p);
    }

    public void draw() {
        // draw all points to standard draw
       draw(root);
    }

    private void draw(Node node) {
        if (node == null) return;
        StdDraw.setPenRadius(0.01);
        StdDraw.setPenColor(Color.BLACK);
        StdDraw.point(node.point.x(), node.point.y());

        StdDraw.setPenRadius();
        if (node.isVertical) {
            StdDraw.setPenColor(Color.RED);
            StdDraw.line(node.point.x(), node.rect.ymin(), node.point.x(), node.rect.ymax());
        } else {
            StdDraw.setPenColor(Color.BLUE);
            StdDraw.line(node.rect.xmin(), node.point.y(), node.rect.xmax(), node.point.y());
        }
        draw(node.leftBottom);
        draw(node.rightTop);
    }

    public Iterable<Point2D> range(RectHV rect) {
        // all points that are inside the rectangle (or on the boundary)
        if (rect == null) throw new IllegalArgumentException("Null argument");
        Queue<Point2D> queue = new Queue<>();
        range(rect, queue, root);
        return queue;
    }

    private void range(RectHV rect, Queue<Point2D> points, Node node) {
        if (node == null) return;
        if (rect.contains(node.point)) points.enqueue(node.point);
        if (rect.intersects(node.rect)) {
            range(rect, points, node.leftBottom);
            range(rect, points, node.rightTop);
        }
    }

    public Point2D nearest(Point2D p) {
        // a nearest neighbor in the set to point p; null if the set is empty
        if (p == null) throw new IllegalArgumentException("Null argument");
        if (root == null) return null;
        return nearest(p, root.point, root);
    }

    private Point2D nearest(Point2D p, Point2D c, Node node) {
        Point2D nearest = c;
        if (node == null) return nearest;
        visitedNodes.add(node);
        if (node.point.distanceSquaredTo(p) < nearest.distanceSquaredTo(p)) nearest = node.point;

        if (node.rect.distanceSquaredTo(p) < nearest.distanceSquaredTo(p)) {
            if ((node.isVertical && node.point.x() > p.x()) || (!node.isVertical && node.point.y() > p.y())) {
                nearest = nearest(p, nearest, node.leftBottom);
            }
            else {
                nearest = nearest(p, nearest, node.rightTop);
            }
        }
        return nearest;
    }

    private static class Node {
        private final Point2D point;
        private final RectHV rect;
        private final boolean isVertical;
        private Node leftBottom;
        private Node rightTop;

        public Node(Point2D point, RectHV rect, Node leftBottom, Node rightTop, boolean isVertical) {
            this.point = point;
            this.rect = rect;
            this.leftBottom = leftBottom;
            this.rightTop = rightTop;
            this.isVertical = isVertical;
        }
    }
}


