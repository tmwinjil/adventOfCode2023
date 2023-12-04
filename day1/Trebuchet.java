package day1;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Trebuchet {
    private static final String TEST_INPUT = "day1/testInput.txt";

    private static final String TEST_INPUT2 = "day1/testInput2.txt";
    private static final String INPUT = "day1/input.txt";
    private static final String INPUT2 = "day1/input2.txt";

    public Trebuchet() {
    }

    public void processInputFile(String filename) {
        File testInput = new File(filename);
        int total = 0;
        try (Scanner in = new Scanner(testInput)) {
            int lineNum = 1;
            while (in.hasNextLine()) {
                int value = parseLineForDigits(in.nextLine());
                System.out.println("Line " + (lineNum++) + " found number " + value);
                total += value;
            }
        } catch (IOException e) {
            System.out.println("testInput.txt could not be found. Please ensure this file exists");
            return;
        }

        System.out.println("Total for " + filename + ": " + total);
    }

    private static final Map<String, Integer> digitStringMap = Map.ofEntries(
            Map.entry("zero", 0),
            Map.entry("0", 0),
            Map.entry("one", 1),
            Map.entry("1", 1),
            Map.entry("two", 2),
            Map.entry("2", 2),
            Map.entry("three", 3),
            Map.entry("3", 3),
            Map.entry("four", 4),
            Map.entry("4", 4),
            Map.entry("five", 5),
            Map.entry("5", 5),
            Map.entry("six", 6),
            Map.entry("6", 6),
            Map.entry("seven", 7),
            Map.entry("7", 7),
            Map.entry("eight", 8),
            Map.entry("8", 8),
            Map.entry("nine", 9),
            Map.entry("9", 9)
    );

    private int parseLineForDigits(String line) {
        int firstCharIndex = line.length();
        int lastCharIndex = -1;
        int firstCharValue = 0;
        int lastCharValue = 0;
        for (String digitString : digitStringMap.keySet()) {
            int index = line.indexOf(digitString);
            if (index != -1 && index < firstCharIndex) {
                firstCharIndex = index;
                firstCharValue = digitStringMap.get(digitString);
            }
            index = line.lastIndexOf(digitString);
            if (index != -1 && index > lastCharIndex) {
                lastCharIndex = index;
                lastCharValue = digitStringMap.get(digitString);
            }
        }
        return 10 * firstCharValue + lastCharValue;
    }
    public static void main(String[] args) {
        Trebuchet test = new Trebuchet();
        test.processInputFile(TEST_INPUT);
        test.processInputFile(INPUT);
        test.processInputFile(TEST_INPUT2);
        test.processInputFile(INPUT2);
    }
}
