package org.neo4j.spatial.algo.wsg84;

import org.neo4j.spatial.core.LineSegment;
import org.neo4j.spatial.core.Point;
import org.neo4j.spatial.core.Polygon;
import org.neo4j.spatial.core.Vector;

public interface Distance {
    /**
     * @param a
     * @param b
     * @return The minimum distance between two polygons. Returns 0 if one polygon is (partially) contained by the other
     */
    public static double distance(Polygon a, Polygon b) {
        //TODO implement this method
        return 0;
    }

    /**
     * @param polygon
     * @param lineSegment
     * @return The minimum distance between a polygon and line segment. Returns 0 if the line segment is (partially) contained by the polygon
     */
    public static double distance(Polygon polygon, LineSegment lineSegment) {
        LineSegment[] lineSegments = polygon.toLineSegments();

        double minDistance = Double.MAX_VALUE;

        for (LineSegment currentLineSegment : lineSegments) {
            double current = distance(currentLineSegment, lineSegment);

            if (current < minDistance) {
                minDistance = current;
            }
        }

        return minDistance;
    }

    /**
     * @param polygon
     * @param point
     * @return The minimum distance between a polygon and point. Returns 0 if point is within the polygon
     */
    public static double distance(Polygon polygon, Point point) {
        //TODO if point is inside the polygon, the distance is 0

        LineSegment[] lineSegments = polygon.toLineSegments();

        double minDistance = Double.MAX_VALUE;

        for (LineSegment lineSegment : lineSegments) {
            double current = distance(lineSegment, point);
            if (current < minDistance) {
                minDistance = current;
            }
        }

        return minDistance;
    }

    /**
     * @param lineSegment
     * @param point
     * @return The minimum distance between a line segment and a point
     */
    public static double distance(LineSegment lineSegment, Point point) {
        Vector u1 = new Vector(lineSegment.getPoints()[0]);
        Vector u2 = new Vector(lineSegment.getPoints()[1]);
        Vector v = new Vector(point);

        //Check whether the point is within the extent of the line segment
        Vector u1v = v.subtract(u1);
        Vector u2v = v.subtract(u2);
        Vector u1u2 = u2.subtract(u1);
        Vector u2u1 = u1.subtract(u2);

        //These dot products tell us whether the point is on the same side as a point of the line segment compared to the remaining point of the line segment
        double extent1 = u1v.dot(u1u2);
        double extent2 = u2v.dot(u2u1);

        boolean isSameHemisphere = v.dot(u1) >= 0 && v.dot(u2) >= 0;

        boolean withinExtend = extent1 >= 0 && extent2 >= 0 && isSameHemisphere;

        if (withinExtend && !u1.equals(u2)) {
            Vector c1 = u1.cross(u2); // n1×n2 = vector representing great circle through the line segments
            Vector c2 = v.cross(c1);  // n0×c1 = vector representing great circle through the point normal to c1
            Vector n = c1.cross(c2);  // c2×c1 = nearest point on c1 to n0

            return distance(v, n);
        } else {
            double d1 = distance(v, u1);
            double d2 = distance(v, u2);

            return d1 < d2 ? d1 : d2;
        }
    }

    /**
     * @param a
     * @param b
     * @return The minimum distance between two line segments
     */
    public static double distance(LineSegment a, LineSegment b) {
        Point intersect = Intersect.intersect(a, b);
        if (intersect != null) {
            return 0;
        }

        double minDistance = Double.MAX_VALUE;
        for (int i = 0; i < a.getPoints().length; i++) {
            double distance = distance(b, a.getPoints()[i]);
            if (distance < minDistance) {
                minDistance = distance;
            }
        }
        for (int i = 0; i < b.getPoints().length; i++) {
            double distance = distance(a, b.getPoints()[i]);
            if (distance < minDistance) {
                minDistance = distance;
            }
        }

        return minDistance;
    }

    /**
     * @param p1
     * @param p2
     * @return The minimum distance between two points
     */
    public static double distance(Point p1, Point p2) {
        double radius = 6371e3;

        Vector u = new Vector(p1);
        Vector v = new Vector(p2);

        //Distance (in meters)
        return radius * Math.atan2(u.cross(v).magnitude(), u.dot(v));
    }

    /**
     * @param u
     * @param v
     * @return The minimum distance between two vectors representing points
     */
    public static double distance(Vector u, Vector v) {
        double radius = 6371e3;

        //Distance (in meters)
        return radius * Math.atan2(u.cross(v).magnitude(), u.dot(v));
    }
}