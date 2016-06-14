package meLiF.melif;

import meLiF.result.RunStats;
import meLiF.result.Point;

import java.io.PrintWriter;


/**
 * @author iisaev
 */
public interface MeLiF {
    RunStats run(Point[] points, PrintWriter out);

    double getSinglePointValue(Point point);
}
