package org.neo4j.spatial.benchmarks.micro;

import org.neo4j.spatial.algo.Distance;
import org.neo4j.spatial.algo.DistanceCalculator;
import org.neo4j.spatial.core.CRS;
import org.neo4j.spatial.core.Point;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.infra.Blackhole;

public class DistanceBenchmarks {

    @Benchmark
    public void testCartesianDistance(Blackhole bh) {
        Distance calculator = DistanceCalculator.getCalculator(CRS.Cartesian);
        for (double x = -1000.0; x < 1000.0; x += 1.0) {
            for (double y = -1000.0; y < 1000.0; y += 1.0) {
                bh.consume(calculator.distance(Point.point(CRS.Cartesian, x, y), Point.point(CRS.Cartesian, -x, -y)));
            }
        }
    }

    @Benchmark
    public void testGeographicDistance(Blackhole bh) {
        Distance calculator = DistanceCalculator.getCalculator(CRS.WGS84);
        for (double x = -10.0; x < 10.0; x += 0.01) {
            for (double y = -10.0; y < 10.0; y += 0.01) {
                bh.consume(calculator.distance(Point.point(CRS.WGS84, x, y), Point.point(CRS.WGS84, -x, -y)));
            }
        }
    }
}
