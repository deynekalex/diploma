package deynekalex.diploma.Experiement;

import java.util.ArrayList;

import static java.lang.Math.abs;

/**
 * Created by deynekalex on 08.05.16.
 */
public class Point {
    ArrayList<Double> coordinates = new ArrayList<>();

    @Override
    public String toString() {
        if (coordinates.size() == 0)
            return "[]";
        else {
            String res = "[";
            for(int i = 0; i < coordinates.size()-1; i++){
                res += coordinates.get(i) + " ,";
            }
            res += coordinates.get(coordinates.size() - 1);
            res += "]";
            return res;
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof Point)) {
            return false;
        }else{
            if (this.coordinates.equals(((Point) other).coordinates)){
                return true;
            }else{
                if (linearDependent((Point)other)){
                    return true;
                }
            }
            return false;
        }
    }

    boolean bothAreZeroOrNotZero(Double d1, Double d2){
        if (d1 == 0.0 && d2 == 0.0){
            return true;
        }
        if (d1 != 0.0 && d2 != 0.0){
            return true;
        }
        return false;
    }

    boolean bothArePoitiveOrNegative(Double d1, Double d2){
        if (d1 > 0 && d2 > 0){
            return true;
        }
        if (d1 < 0 && d2 < 0){
            return true;
        }
        return false;
    }

    private boolean linearDependent(Point other) {
        ArrayList<Integer> zeroCoordinates = new ArrayList<>();
        ArrayList<Integer> nonZeroCoordinates = new ArrayList<>();
        for (int i = 0; i < other.coordinates.size(); i++) {
            if (bothAreZeroOrNotZero(this.coordinates.get(i), other.coordinates.get(i))) {
                if (this.coordinates.get(i) == 0.0) {
                    zeroCoordinates.add(i);
                } else {
                    nonZeroCoordinates.add(i);
                }
            } else {
                return false;
            }
        }
        //
        if (nonZeroCoordinates.size() == 1) {
            if (bothArePoitiveOrNegative(other.coordinates.get(nonZeroCoordinates.get(0)),
                    this.coordinates.get(nonZeroCoordinates.get(0))))
                return true;
            else
                return false;
        }
        if (nonZeroCoordinates.size() == 2) {
            int d0 = nonZeroCoordinates.get(0);
            int d1 = nonZeroCoordinates.get(1);
            if (abs(this.coordinates.get(d0) / this.coordinates.get(d1) -
                    other.coordinates.get(d0) / other.coordinates.get(d1)) < 0.000001) {
                return true;
            }
        }
        if (nonZeroCoordinates.size() == 3) {
            int d0 = nonZeroCoordinates.get(0);
            int d1 = nonZeroCoordinates.get(1);
            int d2 = nonZeroCoordinates.get(2);
            if (abs(this.coordinates.get(d0) / this.coordinates.get(d1) -
                    other.coordinates.get(d0) / other.coordinates.get(d1)) < 0.000001) {
                if (abs(this.coordinates.get(d1) / this.coordinates.get(d2) -
                        other.coordinates.get(d1) / other.coordinates.get(d2)) < 0.000001) {
                    return true;
                }
                return false;
            }
        }

        if (nonZeroCoordinates.size() == 4) {
            int d0 = nonZeroCoordinates.get(0);
            int d1 = nonZeroCoordinates.get(1);
            int d2 = nonZeroCoordinates.get(2);
            int d3 = nonZeroCoordinates.get(3);
            double res1 = this.coordinates.get(d0) / this.coordinates.get(d1);
            double res2 = other.coordinates.get(d0) / this.coordinates.get(d1);
            if (abs(this.coordinates.get(d0) / this.coordinates.get(d1) -
                    other.coordinates.get(d0) / other.coordinates.get(d1)) < 0.000001) {
                if (abs(this.coordinates.get(d1) / this.coordinates.get(d2) -
                        other.coordinates.get(d1) / other.coordinates.get(d2)) < 0.000001) {
                    if (abs(this.coordinates.get(d2) / this.coordinates.get(d3) -
                            other.coordinates.get(d2) / other.coordinates.get(d3)) < 0.000001) {
                        return true;
                    }
                    return false;
                }
                return false;
            }
        }
        return false;
    }
}
