package deynekalex.diploma.MelifResultsPreprocess;

import deynekalex.diploma.Utils;

import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static java.lang.Math.abs;

/**
 * Created by deynekalex on 07.05.16.
 */
public class Preprocessor {
    public static String line = null;
    public static BufferedReader br;
    public static PrintWriter out;
    static ArrayList<Point> allPoints = new ArrayList<>();
    static final double step = 0.25;

    public static void preprocess(String inName, String outName) {
        try {
            readIn(inName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        writeOut(outName);
    }

    private static void writeOut(String outName) {
        try {
            out = new PrintWriter(outName);
            for (Point curPoint : allPoints) {
                writePointToOut(curPoint);
                out.println();
            }
            writeBestPoint();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void writeBestPoint() {
        //TODO: Think about comparing quality of points
        out.println("<---Global best Point--->");
        out.println();
        double bestValue = 0;
        Point bestPoint = null;
        for (Point curPoint : allPoints) {
            if (curPoint.result > bestValue) {
                bestValue = curPoint.result;
                bestPoint = curPoint;
            } else if (curPoint.result == bestValue) {
                if (getDistance(curPoint.points.get(0), curPoint.startPoint)
                        < getDistance(bestPoint.points.get(0), bestPoint.startPoint)) {
                    bestValue = curPoint.result;
                    bestPoint = curPoint;
                }
            }
        }
        writePointToOut(bestPoint);
    }

    private static void writePointToOut(Point curPoint) {
        out.print("<Start point>");
        out.print(" [");
        for (int j = 0; j < curPoint.startPoint.size() - 1; j++) {
            out.print(curPoint.startPoint.get(j) + ", ");
        }
        out.print(curPoint.startPoint.get(curPoint.startPoint.size() - 1));
        out.println("]");

        out.println("<Best result from point>" + curPoint.result);
        out.println("<Distance to point'(s)>");
        int i = 1;
        for (ArrayList<Double> p : curPoint.points) {
            out.print("  <" + (i++) + ">");
            out.print(getDistance(p, curPoint.startPoint));
            out.print(" [");
            for (int j = 0; j < p.size() - 1; j++) {
                out.print(p.get(j) + ", ");
            }
            out.print(p.get(p.size() - 1));
            out.println("]");
        }
    }

    private static int getDistance(ArrayList<Double> p, ArrayList<Double> startPoint) {
        if (p.size() != startPoint.size()) {
            try {
                throw new Exception("Sizes of initial point and point is not equal");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        double sum = 0;
        for (int i = 0; i < startPoint.size(); i++) {
            sum += abs(startPoint.get(i) - p.get(i));
        }
        double distance = sum / step;
        return (int) distance;
    }

    public static void readIn(String inName) throws IOException {
        br = new BufferedReader(new FileReader(inName));
        //reading results from points
        int pointSize;
        while ((line = br.readLine()) != null || (line.contains("<---Global best Point--->"))) {
            Point curPoint = new Point();
            readWhile("<Start point>");
            pointSize = line.length() - line.replace(",", "").length() + 1;
            curPoint.startPoint = new ArrayList<>();
            line = line.substring(line.indexOf(">") + 2);
            curPoint.startPoint = readPoint(pointSize, curPoint.startPoint);
            readWhile("<Best result from point>");
            line = line.substring(line.indexOf(">") + 1);
            curPoint.result = Double.parseDouble(line);
            ArrayList<Double> t;
            readWhile("<1>");
            do {
                t = new ArrayList<>();
                line = line.substring(line.indexOf("[") + 1);
                t = readPoint(pointSize, t);
                curPoint.points.add(t);
            } while (((line = br.readLine()) != null) && !line.equals(""));
            allPoints.add(curPoint);
            if (allPoints.size() == pointSize + 1){
                break;
            }
        }
    }

    private static void readWhile(String part) {
        while (!line.contains(part)) {
            try {
                line = br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static ArrayList<Double> readPoint(int pointCount, ArrayList<Double> point) {
        for (int i = 0; i < pointCount - 1; i++) {
            point.add(Double.parseDouble(line.substring(0, line.indexOf(",") - 1)));
            line = line.substring(line.indexOf(",") + 1);
        }
        point.add(Double.parseDouble(line.substring(0, line.indexOf("]"))));
        return point;
    }

    public void clear() {
        allPoints.clear();
        out = null;
        br = null;
    }
}