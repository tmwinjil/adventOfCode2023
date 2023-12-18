package day6;

import Utils.AdventUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import static java.lang.Math.*;

public class BoatRace {

    private static final String TEST_INPUT = "day6/testInput.txt";
    private static final String INPUT = "day6/input.txt";
    private final List<Integer> timeList = new ArrayList<>();
    private final List<Integer> distanceList = new ArrayList<>();
    BoatRace(String filename){
        File testInput = new File(filename);
        try (Scanner in = new Scanner(testInput)) {
            in.next("Time:");
            while (in.hasNextInt()) {
                timeList.add(in.nextInt());
            }
            in.next("Distance:");
            while (in.hasNextInt()) {
                distanceList.add(in.nextInt());
            }

            System.out.println("Time: " + timeList);
            System.out.println("Distance: " + distanceList);

        } catch (FileNotFoundException e) {
            System.out.println(filename + " could not be found. Please ensure this file exists");
        }
    }

    public static double[] findQuadraticSolutions(double a, double b, double c) {
        double x1;
        double x2;

        x1 = (-b - sqrt(pow(b, 2) - (4 * a * c))) / (2 * a);
        x2 = (-b + sqrt(pow(b, 2) - (4 * a * c))) / (2 * a);


        return new double[]{x1, x2};
    }

    private List<Integer> getNumberOfIntegersInRange(double start, double end) {
        assert start < end : "Start MUST be less than End";
        List<Integer> inRangeNumbers = new ArrayList<>();
        for (int i = (int) start - 1; i < (int) end  + 1; i++) {
            if ((double)i > start && (double)i < end) {
                inRangeNumbers.add(i);
            }
        }
        return inRangeNumbers;
    }

    public List<Integer> calculateWinningIntegers() {
        List<Integer> winningNumberOfSolutionsList = new ArrayList<>();
        for (int i = 0; i < timeList.size(); i++) {
            double[] range = findQuadraticSolutions(1,  - timeList.get(i), distanceList.get(i));
            List<Integer> integersInRange = getNumberOfIntegersInRange(range[0], range[1]);
            winningNumberOfSolutionsList.add(integersInRange.size());
            System.out.println("Race " + i + " results: " + integersInRange);
        }
        return winningNumberOfSolutionsList;
    }

    public long calculateWinningNumber() {
        long time = Long.parseLong(timeList.stream().map(Object::toString).collect(Collectors.joining()));
        long distance = Long.parseLong(distanceList.stream().map(Object::toString).collect(Collectors.joining()));

        double[] range = findQuadraticSolutions(1,  - time, distance);

        return (long) (range[1] - range[0]);
    }


    public static void main(String[] args) {
        for (String filename : Arrays.asList(TEST_INPUT, INPUT)) {
            System.out.println(filename);
            BoatRace boatRace = new BoatRace(filename);
            List<Integer> answerList1 = boatRace.calculateWinningIntegers();
            System.out.println("Answer 1 = " + AdventUtils.findProductOfCollection(answerList1));
            System.out.println("Answer 2 = " + boatRace.calculateWinningNumber());
        }
    }
}
