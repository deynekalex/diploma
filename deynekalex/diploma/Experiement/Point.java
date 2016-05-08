package deynekalex.diploma.Experiement;

import java.util.ArrayList;
import java.util.Objects;

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
            }
            return false;
        }
    }
}
