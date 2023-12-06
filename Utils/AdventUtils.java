package Utils;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Scanner;
import java.util.function.Consumer;

public final class AdventUtils {

    private AdventUtils() {
    }

    public static int findProductOfCollection(Collection<Integer> collection) {
        return collection.stream().mapToInt(x->x).reduce(1, Math::multiplyExact);
    }

    public static int findSumOfCollection(Collection<Integer> collection) {
        return collection.stream().mapToInt(Integer::intValue).sum();
    }

    /**
     *  Function used to read a file line by line and pass those lines into the provided function. Use if you need to
     *  populate values in a class from a file
     * @param filename  The name of the text file to parse
     * @param lineParser    A function that takes a line in the file as a string and will be used on every line of the
     *                      file
     */
    public static void parseFileInput(String filename, Consumer<String> lineParser) {
        File testInput = new File(filename);
        try (Scanner in = new Scanner(testInput)) {
            while (in.hasNextLine()) {
                String line = in.nextLine();
                lineParser.accept(line);
            }
        } catch (IOException e) {
            System.out.println("testInput.txt could not be found. Please ensure this file exists");
        }
    }
}
