package day2;

import java.io.File;
import java.io.IOException;
import java.util.*;

import Utils.AdventUtils;

public class CubeConundrum {
    private static final String TEST_INPUT = "day2/testInput.txt";
    private static final String INPUT = "day2/input.txt";
    private final Map<Integer, Map<String, Integer>> gamesMap = new HashMap<>();

    public CubeConundrum(String filename) {
        File testInput = new File(filename);
        try (Scanner in = new Scanner(testInput)) {
            while (in.hasNextLine()) {
                parseLineAndPopulateGamesMap(in.nextLine());
            }
        } catch (IOException e) {
            System.out.println("testInput.txt could not be found. Please ensure this file exists");
        }
    }

    private void parseLineAndPopulateGamesMap(String line) {
        String[] tokens = line.split(":");
        String gameTag = tokens[0];
        String gameInfo = tokens[1];
        String[] gameId = gameTag.split(" ");
        assert gameId[0].equals("Game");
        gamesMap.put(Integer.parseInt(gameId[1]), createGameMap(gameInfo));
    }

    private Map<String, Integer> createGameMap(String gameLine) {
        Map<String, Integer> gameMap = new HashMap<>();
        String[] rounds = gameLine.split(";");
        for (String round: rounds) {
            String[] groupsOfCubes = round.split(",");
            for (String group : groupsOfCubes) {
                group = group.trim();
                String[] colorValuePair = group.split(" ");
                Integer value = Integer.parseInt(colorValuePair[0]);
                String color = colorValuePair[1];
                if (!gameMap.containsKey(color) || gameMap.get(color) < value) {
                    gameMap.put(color, value);
                }
            }
        }
        return gameMap;
    }

    public Collection<Integer> getApplicableGamesForRGBCombo(int red, int green, int blue) {
        Collection<Integer> applicableGames = gamesMap.entrySet().stream()
                .filter(
                        entry -> entry.getValue().get("red") <= red
                                && entry.getValue().get("green") <= green
                                && entry.getValue().get("blue") <= blue)
                .map(Map.Entry::getKey)
                .toList();
        System.out.println("Applicable games: " + applicableGames);
        return applicableGames;
    }

    public Collection<Integer> findPowersOfMapIndices() {
        assert !gamesMap.isEmpty();
        Collection<Integer> productList = gamesMap.values().stream()
                .mapToInt(game -> AdventUtils.findProductOfCollection(game.values())).boxed().toList();
        assert productList.size() == gamesMap.keySet().size();
        System.out.println("Found products: " + productList);
        return productList;
    }

    public static void main(String[] args) {
        CubeConundrum cubeConundrumTest = new CubeConundrum(TEST_INPUT);
        System.out.println(TEST_INPUT + ":");
        Collection<Integer> answerTest1 = cubeConundrumTest.getApplicableGamesForRGBCombo(12,13,14);
        System.out.println("Sum of indices for test input = " + AdventUtils.findSumOfCollection(answerTest1));
        Collection<Integer> answerTest2 = cubeConundrumTest.findPowersOfMapIndices();
        System.out.println("Sum of powers for test input= " + AdventUtils.findSumOfCollection(answerTest2));

        System.out.println(INPUT + ":");
        CubeConundrum cubeConundrum = new CubeConundrum(INPUT);
        Collection<Integer> answer1 = cubeConundrum.getApplicableGamesForRGBCombo(12,13,14);
        System.out.println("Sum of answer1 = " + AdventUtils.findSumOfCollection(answer1));
        Collection<Integer> answer2 = cubeConundrum.findPowersOfMapIndices();
        System.out.println("Sum of powers = " + AdventUtils.findSumOfCollection(answer2));
    }
}
