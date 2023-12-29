package day11;

import Utils.AdventUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Universe {
    private static final String TEST_INPUT = "day11/testInput.txt";
    private static final String INPUT = "day11/input.txt";

    private final List<List<Character>> universeGrid = new ArrayList<>();

    private final List<Integer> emptyRows = new ArrayList<>();

    private final List<Integer> emptyColumns = new ArrayList<>();
    private final List<Point> galaxyList = new ArrayList<>();
    Universe(String filename) {
        AdventUtils.parseFileInput(filename, this::loadUniverse);
        expandUniverse();
        findGalaxies();
    }

    public void loadUniverse(String line) {
        universeGrid.add(new ArrayList<>(line.chars().mapToObj(c -> (char)c).toList()));
    }

    public void expandUniverse() {
        for (int y = 0; y < universeGrid.size(); y++) {
            // expand rows
            if (universeGrid.get(y).stream().allMatch(c -> c == '.')) {
                emptyRows.add(y);
            }
        }

        //expand columns
        for (int x = 0; x < universeGrid.getFirst().size(); x++) {
            int xIndex = x;
            if (universeGrid.stream().map(list -> list.get(xIndex)).allMatch(c -> c.equals('.'))) {
                emptyColumns.add(x);
            }
        }
    }

    private void findGalaxies() {
        for (int y = 0; y < universeGrid.size(); y++) {
            for (int x = 0; x < universeGrid.get(y).size(); x++) {
                if (universeGrid.get(y).get(x) == '#') {
                    galaxyList.add(new Point(x, y));
                }
            }
        }
    }

    public long calculateSumOfL1DistanceBetweenGalaxies(long emptyGalaxyMultiplier) {
        int counter = 0;
        long total = 0;
        for (int i = 0; i < galaxyList.size() - 1; i++) {
            for (int j = i + 1; j < galaxyList.size(); j++) {
                int maxX = Math.max(galaxyList.get(i).x, galaxyList.get(j).x);
                int minX = Math.min(galaxyList.get(i).x, galaxyList.get(j).x);
                int maxY = Math.max(galaxyList.get(i).y, galaxyList.get(j).y);
                int minY = Math.min(galaxyList.get(i).y, galaxyList.get(j).y);

                long numEmptyGalaxiesCrossed = 0;
                numEmptyGalaxiesCrossed += emptyRows.stream().filter(value -> value < maxY && value > minY).count();
                numEmptyGalaxiesCrossed += emptyColumns.stream().filter(value -> value < maxX && value > minX).count();
                total += Math.abs(galaxyList.get(i).x  - galaxyList.get(j).x) + Math.abs(galaxyList.get(i).y  - galaxyList.get(j).y);
                total -= numEmptyGalaxiesCrossed;
                total += numEmptyGalaxiesCrossed * emptyGalaxyMultiplier;
                counter++;
            }
        }
        System.out.println("Galaxy pairs: " + counter);
        return total;
    }

    public static void main(String[] args) {
        for (String filename: Arrays.asList(TEST_INPUT, INPUT)) {
            System.out.println(filename);
            Universe universe = new Universe(filename);
            System.out.println("Sum of L1 distances between galaxy pairs (multiplier = 2): " + universe.calculateSumOfL1DistanceBetweenGalaxies(2));
            System.out.println("Sum of L1 distances between galaxy pairs (multiplier = 1 million): " + universe.calculateSumOfL1DistanceBetweenGalaxies(1000000));
        }
    }
}
