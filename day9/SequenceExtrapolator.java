package day9;

import Utils.AdventUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class SequenceExtrapolator {
    private static final String TEST_INPUT = "day9/testInput.txt";
    private static final String INPUT = "day9/input.txt";

    private final List<List<Integer>> sequenceList = new ArrayList<>();

    SequenceExtrapolator(String filename) {
        AdventUtils.parseFileInput(filename, this::loadSequence);
    }

    private void loadSequence(String line) {
        assert !line.isBlank();
        sequenceList.add(Arrays.stream(line.split(" ")).map(Integer::parseInt).toList());
    }

    public int findNextNumberInSequence(List<Integer> sequence) {
        List<List<Integer>> sequenceDifferenceList = createSequencePyramid(sequence);

        sequenceDifferenceList.getLast().add(0);
        for (int i = sequenceDifferenceList.size() - 1; i > 0; i--) {
            sequenceDifferenceList.get(i - 1).add(sequenceDifferenceList.get(i).getLast() + sequenceDifferenceList.get(i-1).getLast());
        }
        printPyramid(sequenceDifferenceList);
        return sequenceDifferenceList.getFirst().getLast();
    }

    public int findPreviousNumberInSequence(List<Integer> sequence) {
        List<List<Integer>> sequenceDifferenceList = createSequencePyramid(sequence);
        sequenceDifferenceList.getLast().addFirst(0);
        for (int i = sequenceDifferenceList.size() - 1; i > 0; i--) {
            sequenceDifferenceList.get(i - 1).addFirst(sequenceDifferenceList.get(i-1).getFirst() - sequenceDifferenceList.get(i).getFirst());
        }
        printPyramid(sequenceDifferenceList);
        return sequenceDifferenceList.getFirst().getFirst();
    }

    public List<List<Integer>> createSequencePyramid(List<Integer> sequence) {
        List<List<Integer>> sequenceDifferenceList = new ArrayList<>();
        sequenceDifferenceList.add(new ArrayList<>(sequence));

        while(!sequenceDifferenceList.getLast().stream().allMatch(i -> i == 0)) {
            List<Integer> diffList = sequenceDifferenceList.getLast();
            List<Integer> diffPositions = IntStream.range(0, diffList.size() - 1).boxed().toList();
            sequenceDifferenceList.add(new ArrayList<>(
                    diffPositions.stream()
                            .mapToInt(i -> diffList.get(i + 1) - diffList.get(i))
                            .boxed().toList()));
        }
        return sequenceDifferenceList;
    }

    public void printPyramid(List<List<Integer>> sequencePyramid) {
        int width = 8;
        for (int i = 0; i < sequencePyramid.size(); i++) {
            for (int j = 0; j < i; j++) {
                System.out.printf("%1$" + width/2 + "s", "");
            }
            for (int num : sequencePyramid.get(i)) {
                System.out.printf("%" + width + "d", num);
            }
            System.out.println("\n");
        }
        System.out.println();
        System.out.println();
    }
    public int findSumOfExtrapolatedNumbers(boolean useForwardExtrapolation) {
        List<Integer> extrapolatedNumbers = new ArrayList<>();
        for (List<Integer> sequence : sequenceList) {
            if (useForwardExtrapolation) {
                extrapolatedNumbers.add(findNextNumberInSequence(sequence));
            } else {
                extrapolatedNumbers.add(findPreviousNumberInSequence(sequence));
            }
        }
        return AdventUtils.findSumOfCollection(extrapolatedNumbers);
    }


    public static void main(String[] args) {
        for (String filename: Arrays.asList(TEST_INPUT, INPUT)) {
            System.out.println(filename);
            SequenceExtrapolator extrapolator = new SequenceExtrapolator(filename);
            System.out.println("Sum of forward extrapolated numbers = " + extrapolator.findSumOfExtrapolatedNumbers(true));
            System.out.println("Sum of backward extrapolated numbers = " + extrapolator.findSumOfExtrapolatedNumbers(false));
        }
    }
}
