package deynekalex.diploma.MelifResultsPreprocess;

import java.io.*;
import java.util.ArrayList;

import static java.lang.Math.abs;

/**
 * Created by deynekalex on 07.05.16.
 */
public class Preprocessor {
    public static String line = null;
    public static BufferedReader br;
    public static PrintWriter out;
    static ArrayList<PointExecution> allPointExecutions = new ArrayList<>();
    static final double step = 0.25;

    public static void preprocess(String inName, String outName) {
        try {
            readIn(inName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        writeOut(outName);
    }

    public static void bestPointGetter(String inName, String outName){
        try {
            readIn(inName);
            out = new PrintWriter(outName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        double bestValue = 0;
        ArrayList<ArrayList<Double>> bestPoints = new ArrayList<>();
        for (PointExecution curPointExecution : allPointExecutions) {
            if (curPointExecution.result == bestValue){
                bestPoints.addAll(curPointExecution.points);
            }
            if (curPointExecution.result > bestValue) {
                bestPoints.clear();
                bestValue = curPointExecution.result;
                bestPoints.addAll(curPointExecution.points);
            }
        }
        out.println(bestValue);
        for(ArrayList<Double> point : bestPoints){
            for(int i = 0; i < point.size()-1; i++){
                out.print(point.get(i) + ", ");
            }
            out.println(point.get(point.size()-1));
        }
        out.close();
    }

    private static void writeOut(String outName) {
        try {
            out = new PrintWriter(outName);
            for (PointExecution curPointExecution : allPointExecutions) {
                writePointToOut(curPointExecution);
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
        out.println("<---Global best PointExecution--->");
        out.println();
        double bestValue = 0;
        PointExecution bestPointExecution = null;
        for (PointExecution curPointExecution : allPointExecutions) {
            if (curPointExecution.result > bestValue) {
                bestValue = curPointExecution.result;
                bestPointExecution = curPointExecution;
            } else if (curPointExecution.result == bestValue) {
                if (getDistance(curPointExecution.points.get(0), curPointExecution.startPoint)
                        < getDistance(bestPointExecution.points.get(0), bestPointExecution.startPoint)) {
                    bestValue = curPointExecution.result;
                    bestPointExecution = curPointExecution;
                }
            }
        }
        writePointToOut(bestPointExecution);
    }

    private static void writePointToOut(PointExecution curPointExecution) {
        out.print("<Start point>");
        out.print(" [");
        for (int j = 0; j < curPointExecution.startPoint.size() - 1; j++) {
            out.print(curPointExecution.startPoint.get(j) + ", ");
        }
        out.print(curPointExecution.startPoint.get(curPointExecution.startPoint.size() - 1));
        out.println("]");

        out.println("<Best result from point>" + curPointExecution.result);
        out.println("<Distance to point'(s)>");
        int i = 1;
        for (ArrayList<Double> p : curPointExecution.points) {
            out.print("  <" + (i++) + ">");
            out.print(getDistance(p, curPointExecution.startPoint));
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
        while ((line = br.readLine()) != null || (line.contains("<---Global best PointExecution--->"))) {
            PointExecution curPointExecution = new PointExecution();
            readWhile("<Start point>");
            pointSize = line.length() - line.replace(",", "").length() + 1;
            curPointExecution.startPoint = new ArrayList<>();
            line = line.substring(line.indexOf(">") + 2);
            curPointExecution.startPoint = readPoint(pointSize, curPointExecution.startPoint);
            readWhile("<Best result from point>");
            line = line.substring(line.indexOf(">") + 1);
            curPointExecution.result = Double.parseDouble(line);
            ArrayList<Double> t;
            readWhile("<1>");
            do {
                t = new ArrayList<>();
                line = line.substring(line.indexOf("[") + 1);
                t = readPoint(pointSize, t);
                curPointExecution.points.add(t);
            } while (((line = br.readLine()) != null) && !line.equals(""));
            allPointExecutions.add(curPointExecution);
            if (allPointExecutions.size() == pointSize + 1){
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
        allPointExecutions.clear();
        out = null;
        br = null;
    }
}