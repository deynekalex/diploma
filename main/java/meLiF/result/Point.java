package meLiF.result;

import java.util.ArrayList;

/**
 * @author iisaev
 */
public class Point implements Comparable<Point> {
    private final double[] coordinates;

    public Point(double... coordinates) {
        this.coordinates = coordinates.clone();
    }

    public Point(Point point) {
        this(point.getCoordinates().clone());
    }

    public Point(ArrayList<Double> list) {
        coordinates = new double[list.size()];
        for (int i = 0; i < coordinates.length; i++) {
            coordinates[i] = list.get(i);
        }
    }

    public double[] getCoordinates() {
        return coordinates;
    }

    @Override
    public int compareTo(Point other) {
        for (int i = 0; i < coordinates.length; i++) {
            if (coordinates[i] - other.coordinates[i] > 0.001) {
                return 1;
            }
            if (coordinates[i] - other.coordinates[i] < -0.001) {
                return -1;
            }
        }
        return 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for(int i = 0; i < coordinates.length - 1; i++){
            sb.append(coordinates[i] + " ,");
        }
        sb.append(coordinates[coordinates.length - 1]);
        sb.append("]");
        return sb.toString();
    }
}
