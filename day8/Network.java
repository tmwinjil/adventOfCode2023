package day8;

import Utils.AdventUtils;

import java.util.*;

public class Network {

    private static final String TEST_INPUT1 = "day8/testInput1.txt";

    private static final String TEST_INPUT2 = "day8/testInput2.txt";
    private static final String INPUT = "day8/input.txt";

    private String path;

    private final Map<String, NodeLeaf> nodeMap = new HashMap<>();

    Network(String filename) {
        AdventUtils.parseFileInput(filename, this::parseFileInput);
    }

    public void parseFileInput(String line) {
        if (line.isBlank()) {
            return;
        }

        if (!line.contains("=")) {
            path = line.trim();
        } else {
            String[] tokens = line.split("=");
            nodeMap.put(tokens[0].trim(), new NodeLeaf(tokens[1].trim()));
        }
    }

    public int calculateStepsFromAAAtoZZZ() {
        String node = "AAA";
        int i = 0;
        while (!node.equals("ZZZ")) {
            NodeLeaf leaf = nodeMap.get(node);
            node = (path.charAt(i % path.length()) == 'L') ? leaf.getLeft() : leaf.getRight();
            i++;
        }
        return i;
    }

    /**
     * Calculates the number of steps needed to get from a step where all inputs that end with A to a step where all
     * inputs end with Z. Takes advantage of looping nature of day 8 inputs to shorten runtime
     * @return number of steps needed
     */
    public long calculateGhostStepsFromXXAtoXZ() {
        List<String> nodes = nodeMap.keySet().stream().filter(s -> s.endsWith("A")).toList();
        Map<String, List<Long>> loopMap = new HashMap<>();
        boolean loopPatternFound = false;
        System.out.println(nodes);
        long i = 0;
        while (!nodes.stream().allMatch(s -> s.endsWith("Z"))) {
            List<NodeLeaf> leaves = nodes.stream().map(nodeMap::get).filter(Objects::nonNull).toList();
            boolean useLeft = path.charAt((int)(i % path.length())) == 'L';
            nodes = leaves.stream().map(l -> (useLeft) ? l.getLeft() : l.getRight()).toList();
            System.out.println(nodes);

            List<String> zItems = nodes.stream().filter(s -> s.endsWith("Z")).toList();
            if (!zItems.isEmpty()) {
                for (String item : zItems) {
                    if (!loopMap.containsKey(item)) {
                        loopMap.put(item, new ArrayList<>());
                    }
                    loopMap.get(item).add(i);
                }
            }
            if (loopMap.size() == 6 && loopMap.values().stream().allMatch(l -> l.size() >= 5)) {
                loopPatternFound = true;
                break;
            }
            i++;
        }
        if (loopPatternFound) {
            List<Long> loopRanges = loopMap.values().stream().mapToLong(list -> list.get(1) - list.get(0)).boxed().toList();
            return lowestCommonMultiple(loopRanges);
        }
        return i;
    }

    static long lowestCommonMultiple(List<Long> numbers)
    {
        return numbers.stream().reduce(
                1L, (x, y) -> (x * y) / greatestCommonDenominator(x, y));
    }

    static long greatestCommonDenominator(long a, long b)
    {
        if (b == 0)
            return a;
        return greatestCommonDenominator(b, a % b);
    }

    public static class NodeLeaf {
        private final String left;
        private final String right;
        public NodeLeaf(String nodeString) {
            String[] tokens = nodeString
                    .replace("(", "")
                    .replace(")","")
                    .trim().split(",");
            left = tokens[0].trim();
            right = tokens[1].trim();
        }

        public String getLeft() {
            return left;
        }

        public String getRight() {
            return right;
        }
    }

    public static void main(String[] args) {
        for (String filename : Arrays.asList(TEST_INPUT1, TEST_INPUT2, INPUT)) {
            Network network = new Network(filename);
            System.out.println(filename);
            System.out.println("Number of steps from AAA to ZZZ: " + network.calculateStepsFromAAAtoZZZ());
            System.out.println("Number of ghost steps from XXA to XXZ: " + network.calculateGhostStepsFromXXAtoXZ());
        }
    }
}
