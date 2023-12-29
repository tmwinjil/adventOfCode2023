package day10;

import Utils.AdventUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PipeMaze {
    private static final String TEST_INPUT_X = "day10/testInput";
    private static final String INPUT= "day10/input.txt";


    private final List<List<Pipe>> pipeMap = new ArrayList<>();

    private final List<Point> loop = new ArrayList<>();

    List<Point> pointsInPolygon = new ArrayList<>();
    private boolean isMapReduced = false;

    Point startPosition;

    public PipeMaze(String filename) {
        AdventUtils.parseFileInput(filename, this::populatePipeMap);
    }
    public void populatePipeMap(String line) {
        pipeMap.add(line.chars().mapToObj((i) -> Pipe.getPipe((char) i)).collect(Collectors.toList()));
        if (line.contains("S")) {
            int linePos = line.indexOf("S");
            startPosition = new Point(linePos, pipeMap.size()-1);
        }
    }

    public void reduceMap() {
        if (loop.isEmpty() || isMapReduced) {
            return;
        }
        for (int y = 0; y < pipeMap.size(); y++) {
            List<Pipe> line = pipeMap.get(y);
            for (int x = 0; x < line.size(); x++) {
                if (!loop.contains(new Point(x, y))) {
                    pipeMap.get(y).set(x, Pipe.GROUND);
                }
            }
        }

        Point next = loop.get(1);
        Point prev = loop.getLast();
        int nextXdiff = next.x - startPosition.x;
        int nextYdiff = startPosition.y - next.y;
        int prevXdiff = prev.x - startPosition.x;
        //int prevYdiff = startPosition.y - prev.y;

        if (nextXdiff == 0 && nextYdiff > 0) { //north
            if (prevXdiff > 0) {
                pipeMap.get(startPosition.y).set(startPosition.x, Pipe.NORTH_EAST);
            } else if (prevXdiff == 0) {
                pipeMap.get(startPosition.y).set(startPosition.x, Pipe.NORTH_SOUTH);
            } else {
                pipeMap.get(startPosition.y).set(startPosition.x, Pipe.NORTH_WEST);
            }
        } else if (nextXdiff > 0 && nextYdiff == 0){ //East
            if (prevXdiff == 0) {
                pipeMap.get(startPosition.y).set(startPosition.x, Pipe.SOUTH_EAST);
            } else {
                pipeMap.get(startPosition.y).set(startPosition.x, Pipe.EAST_WEST);
            }
        } else {
            pipeMap.get(startPosition.y).set(startPosition.x, Pipe.SOUTH_WEST);
        }

        isMapReduced = true;
    }


    public void printMap() {
        for (int i = 0; i < pipeMap.size(); i++) {
            List<Pipe> line = pipeMap.get(i);
            for (int j = 0; j < line.size(); j++) {
                Point p = new Point(j, i);
                if (p.equals(startPosition)) {
                    System.out.print("\u001B[32m" + pipeMap.get(i).get(j) + "\u001B[0m");
                } else if (pointsInPolygon.contains(p)) {
                    System.out.print("\u001B[31m" + pipeMap.get(i).get(j) + "\u001B[0m");
                } else {
                    System.out.print(pipeMap.get(i).get(j));
                }
                System.out.print(' ');
            }
            System.out.print("\n");
        }
        System.out.println();
    }

    public void traversePipeMap() {
        Point currentPoint;
        Point nextPoint = startPosition;

        while (nextPoint != null) {
            currentPoint = nextPoint;
            loop.add(currentPoint);
            nextPoint = findLocationOfNextPipe(currentPoint);
        }
        reduceMap();
        fillPointsInPolygonUsingRayTest();
    }

    // Ray test to determine the number of points in the polygon: https://en.wikipedia.org/wiki/Point_in_polygon
    // We count the number of edges we cross, i.e |, F--J(under to over) and L--7(over to under) with any number of '-'.
    // Do not count anything else
    public void fillPointsInPolygonUsingRayTest() {
        for (int y = 0; y < pipeMap.size(); y++) {
            boolean insidePipe = false;
            for (int x = 0; x < pipeMap.get(y).size(); x++) {
                Pipe currentPipe = pipeMap.get(y).get(x);
                if (currentPipe == Pipe.NORTH_SOUTH) {
                    insidePipe = !insidePipe;
                } else if (currentPipe == Pipe.SOUTH_EAST) {
                    do {
                        x++;
                    } while (x < pipeMap.get(y).size() && pipeMap.get(y).get(x) == Pipe.EAST_WEST);
                    if (pipeMap.get(y).get(x) == Pipe.NORTH_WEST) {
                        insidePipe = !insidePipe;
                    }
                } else if (currentPipe == Pipe.NORTH_EAST) {
                    do {
                        x++;
                    } while(x < pipeMap.get(y).size() && pipeMap.get(y).get(x) == Pipe.EAST_WEST);

                    if (pipeMap.get(y).get(x) == Pipe.SOUTH_WEST) {
                        insidePipe = !insidePipe;
                    }
                } else if (insidePipe && currentPipe == Pipe.GROUND) {
                    pointsInPolygon.add(new Point(x,y));
                }
            }
        }
    }

    private Point findLocationOfNextPipe(Point currentPoint) {
        Pipe pipe = pipeMap.get(currentPoint.y).get(currentPoint.x);

        //check north
        if (pipe.checkNorth()) {
            Point nextPoint = new Point(currentPoint.x, currentPoint.y - 1);
            if (nextPoint.y >= 0 && !loop.contains(nextPoint) && pipeMap.get(nextPoint.y).get(nextPoint.x).checkSouth()) {
                //System.out.println("NORTH");
                return nextPoint;
            }
        }

        //check east
        if (pipe.checkEast()) {
            Point nextPoint = new Point(currentPoint.x + 1, currentPoint.y);
            if (nextPoint.x < pipeMap.get(0).size() && !loop.contains(nextPoint) && pipeMap.get(nextPoint.y).get(nextPoint.x).checkWest()) {
                //System.out.println("EAST");
                return nextPoint;
            }
        }

        //check south
        if (pipe.checkSouth()) {
            Point nextPoint = new Point(currentPoint.x, currentPoint.y + 1);
            if (nextPoint.y < pipeMap.size() && !loop.contains(nextPoint) && pipeMap.get(nextPoint.y).get(nextPoint.x).checkNorth()) {
                //System.out.println("SOUTH");
                return nextPoint;
            }
        }

        //check west
        if (pipe.checkWest()) {
            Point nextPoint = new Point(currentPoint.x - 1, currentPoint.y);
            if (nextPoint.x >= 0 && !loop.contains(nextPoint) && pipeMap.get(nextPoint.y).get(nextPoint.x).checkEast()) {
                //System.out.println("WEST");
                return nextPoint;
            }
        }

        return null;
    }



    public static void main(String[] args) {
        //Test loop finding

        for (int i = 1; i <= 7; i++) {
            String filename = TEST_INPUT_X + i + ".txt";
            System.out.println(filename);
            PipeMaze maze = new PipeMaze(filename);
            maze.printMap();
            maze.traversePipeMap();
            System.out.println("Steps to furthest point: " + maze.loop.size() / 2);
            System.out.println("Number of points inside loop: " + maze.pointsInPolygon.size());
            maze.printMap();
        }

        System.out.println(INPUT);
        PipeMaze maze = new PipeMaze(INPUT);
        maze.printMap();
        maze.traversePipeMap();
        maze.printMap();
        System.out.println("Steps to furthest point: " + maze.loop.size() / 2);
        System.out.println("Number of points inside loop: " + maze.pointsInPolygon.size());
    }

}
