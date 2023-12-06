package day3;

import java.awt.Point;
import java.util.*;
import java.util.List;

import Utils.AdventUtils;
public class EngineSchematic {

    private static final String TEST_INPUT_1 = "day3/testInput1.txt";
    private static final String INPUT_1 = "day3/input1.txt";

    private int maxLength = -1;

    private final List<List<Character>> engineSchematicArray = new ArrayList<>();
    private final Map<Point, EngineItem> itemMap = new HashMap<>();
    private final Map<Point, Character> symbolPositionMap = new HashMap<>();

    public EngineSchematic(String filename) {
        AdventUtils.parseFileInput(filename, this::populateSchematic);
    }

    public void populateSchematic(String line) {
        if (maxLength == -1) {
            maxLength = line.length();
        }
        assert line.length() == maxLength;

        engineSchematicArray.add(line.chars().mapToObj(c -> (char)c).toList());
        List<Character> row = engineSchematicArray.getLast();
        int y = engineSchematicArray.size() -1;

        for (int i = 0; i < row.size(); i++) {
            char c = row.get(i);
            if (c == '.') {
                continue;
            }
            Point position = new Point(i, y);
            if (!isDigit(c)) {
                symbolPositionMap.put(position, c);
            } else {
                int j = i;
                while ( j < row.size() && isDigit(row.get(j))) {
                    j++;
                }
                EngineItem item = new EngineItem(position, Integer.parseInt(line.substring(i, j)));
                for (int it = i; it < j; it++) {
                    itemMap.put(new Point(it, y), item);
                }
                i = j-1;// set to last integer added
            }
        }
    }


    public List<Integer> findPartNumbers() {
        List<EngineItem> partNumbers = new ArrayList<>();
        int maxX = engineSchematicArray.getFirst().size();
        int maxY = engineSchematicArray.size();

        for (Point p : symbolPositionMap.keySet()) {
            int y = (int) p.getY();
            int x = (int) p.getX();
            // for each row deviation
            for (int dy = (y > 0 ? -1 : 0); dy <= (y < maxY ? 1 : 0); dy++) {

                // for each column deviation
                for (int dx = (x > 0 ? -1 : 0); dx <= (x < maxX ? 1 : 0); dx++) {
                    Point checkPoint = new Point(x + dx, y + dy);
                    if (itemMap.containsKey(checkPoint) && !partNumbers.contains(itemMap.get(checkPoint))) {
                        EngineItem foundItem = itemMap.get(checkPoint);
                        partNumbers.add(foundItem);
                    }
                }
            }
        }
        return partNumbers.stream().mapToInt(EngineItem::getValue).boxed().toList();
    }
    public List<Integer> findGearRatios() {
        List<Integer> gearRatios = new ArrayList<>();
        int maxX = engineSchematicArray.getFirst().size();
        int maxY = engineSchematicArray.size();
        List<Point> listOfStarSymbols = symbolPositionMap.entrySet().stream().filter(entry -> entry.getValue().equals('*')).map(Map.Entry::getKey).toList();
        for (Point p : listOfStarSymbols) {
            List<EngineItem> gearItems = new ArrayList<>();
            int y = (int) p.getY();
            int x = (int) p.getX();
            // for each row deviation
            for (int dy = (y > 0 ? -1 : 0); dy <= (y < maxY ? 1 : 0); dy++) {

                // for each column deviation
                for (int dx = (x > 0 ? -1 : 0); dx <= (x < maxX ? 1 : 0); dx++) {
                    Point checkPoint = new Point(x + dx, y + dy);
                    if (itemMap.containsKey(checkPoint) && !gearItems.contains(itemMap.get(checkPoint))) {
                        EngineItem foundItem = itemMap.get(checkPoint);
                        gearItems.add(foundItem);
                    }
                }
            }
            if (gearItems.size() == 2) {
                gearRatios.add(AdventUtils.findProductOfCollection(Arrays.asList(gearItems.getFirst().getValue(), gearItems.getLast().getValue())));
            }
        }
        return gearRatios;
    }

    public static boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    public static void main(String[] args) {
        for (String test: Arrays.asList(TEST_INPUT_1, INPUT_1)) {
            System.out.println("\n\n" + test);
            EngineSchematic engineSchematic = new EngineSchematic(test);
            List<Integer> answer1 = engineSchematic.findPartNumbers();
            System.out.println("Part numbers:" + answer1);
            System.out.println("Sum = " + AdventUtils.findSumOfCollection(answer1));
            List<Integer> answer2 = engineSchematic.findGearRatios();
            System.out.println("Gear ratios:" + answer2);
            System.out.println("Sum = " + AdventUtils.findSumOfCollection(answer2));
        }
    }
}
