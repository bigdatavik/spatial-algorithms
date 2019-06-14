package org.neo4j.spatial.algo;

import org.neo4j.spatial.core.LineSegment;
import org.neo4j.spatial.core.Point;
import org.neo4j.spatial.core.Polygon;
import org.neo4j.spatial.core.Polyline;

public abstract class LinearReference {
    /**
     * Finds the point on the polygon which is distance d from the start point of the polygon.
     *
     * @param polygon
     * @param d
     * @return The new point, and null if the distance is negative
     */
    public Point reference(Polygon.SimplePolygon polygon, Point start, Point direction, double d) {
        if (d < 0) {
            return null;
        }

        polygon.startTraversal(start, direction);
        Point previous = polygon.getNextPoint();
        Point point = null;
        while (d >= 0) {
            Point current = polygon.getNextPoint();
            double length = DistanceCalculator.distance(previous, current);

            if (length < d) {
                d -= length;
            } else {
                point = reference(previous, current, d);
                break;
            }
            previous = current;
        }

        return point;
    }

    /**
     * Finds the point on the polyline which is distance d from the start point of the polyline.
     *
     * @param polyline
     * @param d
     * @return The new point, and null if the distance is negative or is greater than the length of the polyline
     */
    public Point reference(Polyline polyline, Point start, Point direction, double d) {
        if (d < 0) {
            return null;
        }

        polyline.startTraversal(start, direction);
        Point previous = polyline.getNextPoint();
        Point point = null;
        while (!polyline.fullyTraversed()) {
            Point current = polyline.getNextPoint();
            double length = DistanceCalculator.distance(previous, current);

            if (length < d) {
                d -= length;
            } else {
                point = reference(previous, current, d);
                break;
            }
            previous = current;
        }

        return point;
    }

    /**
     * Finds the point on the line segment which is distance d from the start point of the line segment.
     *
     * @param lineSegment
     * @param d
     * @return The new point, and null if the distance is not in the range of the line segment
     */
    public abstract Point reference(LineSegment lineSegment, double d);

    /**
     * Finds the point on the line segment between the given points which is distance d from a
     *
     * @param a
     * @param b
     * @param d
     * @return The new point, and null if the distance is not in the range of the line segment
     */
    protected abstract Point reference(Point a, Point b, double d);
}
