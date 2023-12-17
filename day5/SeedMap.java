package day5;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class SeedMap {

    private static final String TEST_INPUT_1 = "day5/testInput1.txt";
    private static final String INPUT_1 = "day5/input1.txt";
    private final List<Map.Entry<String, RangeMap>> conversionMapList = new ArrayList<>();
    private List<Long> seedList;
    private SeedMap(String filename) {
        File testInput = new File(filename);
        try (Scanner in = new Scanner(testInput)) {
            String firstLine = in.nextLine();
            assert firstLine.startsWith("seeds:") : "First line has wrong format. Must begin with \"seeds:\"";
            seedList = Arrays.stream(firstLine.substring(firstLine.indexOf("seeds:") + "seeds:".length()).trim()
                            .split(" "))
                    .mapToLong(Long::parseLong)
                    .boxed().toList();
            System.out.println("seed list: " + seedList);
            while (in.hasNextLine()) { //loop through all maps
                String line = in.nextLine();
                if (line.isBlank()) {
                    continue;
                }

                if (line.endsWith(":"))  {
                    String mapName;
                    RangeMap map = new RangeMap();
                    mapName = line.trim();
                    while(in.hasNextLong()) {
                        long sourceStart = in.nextLong();
                        long destStart = in.nextLong();
                        long range = in.nextLong();
                        map.addRange(sourceStart, destStart, range);
                    }
                    //System.out.println("Found " + mapName + " start: " + map.sourceStart +  ", dest" + map.destinationStart + ", range: " + map.range);
                    conversionMapList.add(new AbstractMap.SimpleEntry<>(mapName, map));
                }
            }
        } catch (IOException e) {
            System.out.println("testInput.txt could not be found. Please ensure this file exists");
        }
    }

    public List<Long> calculateLocationListFromSeeds() {
        return seedList.stream().mapToLong(this::processSeed).boxed().toList();
    }

    public long calculateMinValueFromSeedPairs() {
        long minValue = Long.MAX_VALUE;
        for (int i = 0; i < seedList.size(); i++) {
            long start = seedList.get(i);
            long range = seedList.get(++i);
            for (long j = 0; j < range; j++) {
                long value = processSeed(start + j);
                if (value < minValue) {
                    minValue = value;
                }
            }
        }

        return minValue;
    }

    public long processSeed(long seed) {
        long value = seed;
        for (Map.Entry<String, RangeMap> stringRangeMapEntry : conversionMapList)
            value = stringRangeMapEntry.getValue().mapValue(value);
        return value;
    }

    public static void main(String[] args) {
        for (String filename: Arrays.asList(TEST_INPUT_1, INPUT_1)) {
            System.out.println(filename);
            SeedMap seedmap = new SeedMap(filename);
            List<Long> locationList = seedmap.calculateLocationListFromSeeds();
            System.out.println("Mapped locations: " + locationList);
            long answer = Collections.min(locationList);
            System.out.println("Minimum value = " + answer);
            long answer2 = seedmap.calculateMinValueFromSeedPairs();
            System.out.println("Minimum value 2 = " + answer2);
        }
    }

    private static class RangeMap {
        private final List<Long> sourceStart = new ArrayList<>();
        private final List<Long> destinationStart = new ArrayList<>();
        private final List<Long> range = new ArrayList<>();

        public RangeMap() {}

        public void addRange(long destinationStart, long sourceStart, long range) {
            this.destinationStart.add(destinationStart);
            this.sourceStart.add(sourceStart);
            this.range.add(range);
        }

        public long mapValue(long value) {
            for (int i = 0; i < sourceStart.size(); i++) {
                if (value >= sourceStart.get(i) && value < sourceStart.get(i) + range.get(i)) {
                    return destinationStart.get(i) + (value - sourceStart.get(i) );
                }
            }
            return value;
        }
    }


}
