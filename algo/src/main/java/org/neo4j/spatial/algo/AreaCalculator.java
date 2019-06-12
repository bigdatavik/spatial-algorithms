package org.neo4j.spatial.algo;

import org.neo4j.spatial.core.CRS;
import org.neo4j.spatial.core.MultiPolygon;
import org.neo4j.spatial.core.Polygon;

public class AreaCalculator {
    private static org.neo4j.spatial.algo.cartesian.Area cartesian;
    private static org.neo4j.spatial.algo.wgs84.Area wgs84;

    private static Area getCartesian() {
        if (cartesian == null) {
            cartesian = new org.neo4j.spatial.algo.cartesian.Area();
        }
        return cartesian;
    }

    private static Area getWGS84() {
        if (wgs84 == null) {
            wgs84 = new org.neo4j.spatial.algo.wgs84.Area();
        }
        return wgs84;
    }

    /**
     * @param polygon
     * @return The area of the polygon
     */
    public static double area(MultiPolygon polygon) {
        if (CRSChecker.check(polygon) == CRS.Cartesian) {
            return getCartesian().area(polygon);
        } else {
            return getWGS84().area(polygon);
        }
    }

    /**
     * @param polygon
     * @return The area of the simple polygon
     */
    public static double area(Polygon.SimplePolygon polygon) {
        if (CRSChecker.check(polygon) == CRS.Cartesian) {
            return getCartesian().area(polygon);
        } else {
            return getWGS84().area(polygon);
        }
    }
}