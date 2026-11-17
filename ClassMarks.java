import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * Generates random student marks from text files and exports to CSV.
 *
 * @author  Shem Irekpita
 * @version 1.0
 * @since   2026-11-16
 */
public final class ClassMarks {

    /** Mean value for normal distribution of marks. */
    private static final double MEAN = 75.0;

    /** Standard deviation for normal distribution of marks. */
    private static final double STD_DEV = 10.0;

    /**
     * Private constructor to prevent instantiation.
     *
     * @throws IllegalStateException Utility class cannot be instantiated.
     */
    private ClassMarks() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Reads a text file line by line into a String array.
     *
     * @param fileName The path of the file to read.
     * @return An array of strings containing each line of the file.
     * @throws IOException If the file cannot be read.
     */
    public static String[] readFileToArray(final String fileName)
            throws IOException {
        final List<String> lines = new ArrayList<>();
        final File file = new File(fileName);
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                final String line = scanner.nextLine().trim();
                if (!line.isEmpty()) {
                    lines.add(line);
                }
            }
        }
        return lines.toArray(new String[0]);
    }

    /**
     * Generates a 2D array of random integer marks with a header row.
     *
     * @param studentArray Array of student names.
     * @param assignmentsArray Array of assignment titles.
     * @return A 2D String array containing headers, names, and marks.
     */
    public static String[][] generateMarks(
            final String[] studentArray, final String[] assignmentsArray) {
        final int numRows = studentArray.length + 1;
        final int numCols = assignmentsArray.length + 1;
        final String[][] marksTable = new String[numRows][numCols];
        final Random random = new Random();

        marksTable[0][0] = "Student";
        for (int col = 0; col < assignmentsArray.length; col++) {
            marksTable[0][col + 1] = assignmentsArray[col];
        }

        for (int row = 0; row < studentArray.length; row++) {
            marksTable[row + 1][0] = studentArray[row];
            for (int col = 0; col < assignmentsArray.length; col++) {
                final double rawMark =
                    random.nextGaussian() * STD_DEV + MEAN;
                final int roundedMark = (int) Math.round(rawMark);
                marksTable[row + 1][col + 1] =
                    Integer.toString(roundedMark);
            }
        }
        return marksTable;
    }

    /**
     * Writes a 2D String array to a CSV file.
     *
     * @param data The 2D array to export.
     * @param outputFileName The name of the output CSV file.
     * @throws IOException If writing to the file fails.
     */
    public static void writeToCsv(
            final String[][] data, final String outputFileName)
            throws IOException {
        try (PrintWriter writer =
                new PrintWriter(new FileWriter(outputFileName))) {
            for (String[] row : data) {
                writer.println(String.join(",", row));
            }
        }
    }

    /**
     * Main entry point for the program.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(final String[] args) {
        try {
            final String[] studentArray =
                readFileToArray("Unit2-08-students.txt");
            final String[] assignmentsArray =
                readFileToArray("Unit2-08-assignments.txt");

            final String[][] results =
                generateMarks(studentArray, assignmentsArray);

            writeToCsv(results, "marks.csv");
            System.out.println("Successfully generated marks.csv");
        } catch (IOException error) {
            System.err.println("File error: " + error.getMessage());
        }
    }
}

