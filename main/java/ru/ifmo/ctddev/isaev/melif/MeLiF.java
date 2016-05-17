package ru.ifmo.ctddev.isaev.melif;

import ru.ifmo.ctddev.isaev.result.Point;
import ru.ifmo.ctddev.isaev.result.RunStats;

import java.io.PrintWriter;


/**
 * @author iisaev
 */
public interface MeLiF {
    RunStats run(Point[] points, PrintWriter out);

    double getSinglePointValue(Point point);
}
