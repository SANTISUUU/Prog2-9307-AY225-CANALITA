import java.io.*;
import java.util.*;

/**
 * DatasetProcessing.java
 * 
 * A single Java program that handles:
 *   MP05 - Extract and display a selected column
 *   MP06 - Display unique values in a column
 *   MP07 - Sort records alphabetically by a column
 * 
 * Java and JavaScript CSV Dataset Processing
 * Columns: Candidate, Type, Column1, Exam, Language, Exam Date, Score, Result, Time Used
 * 
 * The program first asks the user for the CSV file path,
 * then shows a menu to choose which machine problem to run.
 */
public class DatasetProcessing {

    // -------------------------------------------------------
    // CONSTANTS - Column index mapping based on the CSV file
    // -------------------------------------------------------
    static final int COL_CANDIDATE  = 0; // "Candidate" column index
    static final int COL_TYPE       = 1; // "Student/Faculty/NTE" column index
    static final int COL_EXAM       = 3; // "Exam" column index
    static final int COL_LANGUAGE   = 4; // "Language" column index
    static final int COL_DATE       = 5; // "Exam Date" column index
    static final int COL_SCORE      = 6; // "Score" column index
    static final int COL_RESULT     = 7; // "Result" column index
    static final int COL_TIME       = 8; // "Time Used" column index

    // Column headers displayed to the user in menus
    static final String[] COLUMN_NAMES = {
        "Candidate",        // index 0
        "Type",             // index 1
        "Column1",          // index 2
        "Exam",             // index 3
        "Language",         // index 4
        "Exam Date",        // index 5
        "Score",            // index 6
        "Result",           // index 7
        "Time Used"         // index 8
    };

    // The number of actual data columns we care about
    static final int TOTAL_COLUMNS = 9;

    // -------------------------------------------------------
    // MAIN METHOD - Entry point of the program
    // -------------------------------------------------------
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Step 1: Ask user for the CSV file path
        System.out.println("============================================");
        System.out.println("   Pearson VUE Exam Results - CSV Processor ");
        System.out.println("============================================");
        System.out.print("Enter the full path to your CSV dataset file: ");
        String filePath = scanner.nextLine().trim();

        // Step 2: Load and parse the CSV file into a list of records
        List<String[]> records = loadCSV(filePath);

        // If loading failed or no records found, stop the program
        if (records == null || records.isEmpty()) {
            System.out.println("[ERROR] No valid records found. Please check your file and try again.");
            return;
        }

        System.out.println("[INFO] Successfully loaded " + records.size() + " records.\n");

        // Step 3: Show the main menu and let user choose an MP
        boolean running = true;
        while (running) {
            System.out.println("--------------------------------------------");
            System.out.println("                 MAIN MENU                  ");
            System.out.println("--------------------------------------------");
            System.out.println(" [5] Extract and Display a Column");
            System.out.println(" [6] Display Unique Values in a Column");
            System.out.println(" [7] Sort Records Alphabetically by Column");
            System.out.println(" [0] Exit");
            System.out.println("--------------------------------------------");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "5":
                    mp05_extractColumn(records, scanner);
                    break;
                case "6":
                    mp06_uniqueValues(records, scanner);
                    break;
                case "7":
                    mp07_sortByColumn(records, scanner);
                    break;
                case "0":
                    System.out.println("Exiting program. Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("[WARNING] Invalid option. Please enter 5, 6, 7, or 0.\n");
            }
        }

        scanner.close();
    }

    // -------------------------------------------------------
    // loadCSV()
    // Reads the CSV file, skips header/metadata rows,
    // and returns a list of String arrays (one per record).
    // -------------------------------------------------------
    static List<String[]> loadCSV(String filePath) {
        // List to store all parsed data rows
        List<String[]> records = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean dataStarted = false; // Flag to mark when actual data rows begin

            while ((line = br.readLine()) != null) {
                // Skip the header row that contains column names
                if (line.contains("Candidate") && line.contains("Exam") && line.contains("Score")) {
                    dataStarted = true; // Data rows come after this header
                    continue;
                }

                // Skip all lines before the header row
                if (!dataStarted) continue;

                // Skip empty or nearly empty lines
                if (line.trim().isEmpty() || line.replace(",", "").trim().isEmpty()) continue;

                // Parse the CSV line (handles quoted fields with commas inside)
                String[] fields = parseCSVLine(line);

                // Only keep rows that have enough columns and a non-empty Candidate name
                if (fields.length >= TOTAL_COLUMNS && !fields[COL_CANDIDATE].trim().isEmpty()) {
                    records.add(fields);
                }
            }

        } catch (FileNotFoundException e) {
            // File path provided does not exist
            System.out.println("[ERROR] File not found: " + filePath);
            return null;
        } catch (IOException e) {
            // Some other file reading error
            System.out.println("[ERROR] Could not read file: " + e.getMessage());
            return null;
        }

        return records;
    }

    // -------------------------------------------------------
    // parseCSVLine()
    // Handles CSV lines where fields may be wrapped in quotes
    // (e.g., "Last, First" is one field, not two).
    // -------------------------------------------------------
    static String[] parseCSVLine(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false; // Tracks if we're inside a quoted field

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (c == '"') {
                // Toggle quote mode on/off
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                // Comma outside quotes = field separator
                fields.add(current.toString().trim());
                current.setLength(0); // Reset buffer
            } else {
                // Normal character, add to current field
                current.append(c);
            }
        }

        // Add the last field after the loop ends
        fields.add(current.toString().trim());

        return fields.toArray(new String[0]);
    }

    // -------------------------------------------------------
    // showColumnMenu()
    // Displays a numbered list of available columns
    // and returns the index chosen by the user.
    // -------------------------------------------------------
    static int showColumnMenu(Scanner scanner) {
        System.out.println("\nAvailable Columns:");
        for (int i = 0; i < COLUMN_NAMES.length; i++) {
            System.out.println("  [" + i + "] " + COLUMN_NAMES[i]);
        }
        System.out.print("Enter column number (0-" + (COLUMN_NAMES.length - 1) + "): ");

        try {
            int colIndex = Integer.parseInt(scanner.nextLine().trim());
            // Validate that the chosen index is within range
            if (colIndex < 0 || colIndex >= COLUMN_NAMES.length) {
                System.out.println("[WARNING] Invalid column number. Defaulting to column 0 (Candidate).");
                return 0;
            }
            return colIndex;
        } catch (NumberFormatException e) {
            System.out.println("[WARNING] Invalid input. Defaulting to column 0 (Candidate).");
            return 0;
        }
    }

    // -------------------------------------------------------
    // MP05 - Extract and Display a Selected Column
    // Asks the user which column to show, then prints
    // every value in that column from the dataset.
    // -------------------------------------------------------
    static void mp05_extractColumn(List<String[]> records, Scanner scanner) {
        System.out.println("\n========== MP05: Extract and Display a Column ==========");

        // Let the user pick which column to display
        int colIndex = showColumnMenu(scanner);
        String colName = COLUMN_NAMES[colIndex];

        System.out.println("\n--- Values in Column: " + colName + " ---");
        System.out.printf("%-5s | %-50s%n", "No.", colName);
        System.out.println("-".repeat(58));

        // Loop through all records and print the chosen column value
        int rowNum = 1;
        for (String[] record : records) {
            // Safety check: make sure this row has the column we need
            String value = (colIndex < record.length) ? record[colIndex] : "N/A";
            System.out.printf("%-5d | %-50s%n", rowNum++, value);
        }

        System.out.println("-".repeat(58));
        System.out.println("Total entries displayed: " + records.size());
        System.out.println();
    }

    // -------------------------------------------------------
    // MP06 - Display Unique Values in a Column
    // Collects all values for a chosen column,
    // removes duplicates, and prints only unique ones.
    // -------------------------------------------------------
    static void mp06_uniqueValues(List<String[]> records, Scanner scanner) {
        System.out.println("\n========== MP06: Display Unique Values in a Column ==========");

        // Let the user pick which column to analyze
        int colIndex = showColumnMenu(scanner);
        String colName = COLUMN_NAMES[colIndex];

        // Use a LinkedHashSet to collect unique values while preserving insertion order
        Set<String> uniqueValues = new LinkedHashSet<>();

        for (String[] record : records) {
            String value = (colIndex < record.length) ? record[colIndex] : "";
            if (!value.isEmpty()) {
                uniqueValues.add(value); // Sets automatically ignore duplicates
            }
        }

        System.out.println("\n--- Unique Values in Column: " + colName + " ---");
        System.out.printf("%-5s | %-50s%n", "No.", colName);
        System.out.println("-".repeat(58));

        // Print each unique value with a row number
        int count = 1;
        for (String val : uniqueValues) {
            System.out.printf("%-5d | %-50s%n", count++, val);
        }

        System.out.println("-".repeat(58));
        System.out.println("Total unique values: " + uniqueValues.size());
        System.out.println();
    }

    // -------------------------------------------------------
    // MP07 - Sort Records Alphabetically by a Column
    // Creates a copy of the records list, sorts it by
    // the user's chosen column, and displays the results.
    // -------------------------------------------------------
    static void mp07_sortByColumn(List<String[]> records, Scanner scanner) {
        System.out.println("\n========== MP07: Sort Records Alphabetically by Column ==========");

        // Let the user pick which column to sort by
        int colIndex = showColumnMenu(scanner);
        String colName = COLUMN_NAMES[colIndex];

        // Make a copy so we don't modify the original list
        List<String[]> sorted = new ArrayList<>(records);

        // Sort using a lambda comparator — case-insensitive alphabetical sort
        final int finalColIndex = colIndex; // Must be effectively final for lambda
        sorted.sort((a, b) -> {
            String valA = (finalColIndex < a.length) ? a[finalColIndex] : "";
            String valB = (finalColIndex < b.length) ? b[finalColIndex] : "";
            return valA.compareToIgnoreCase(valB); // Case-insensitive comparison
        });

        System.out.println("\n--- Records Sorted by: " + colName + " ---");
        // Display header row with all column names
        System.out.printf("%-5s | %-25s | %-10s | %-40s | %-8s | %-6s%n",
                "No.", "Candidate", "Type", "Exam", "Result", "Score");
        System.out.println("-".repeat(102));

        // Print each sorted record with key columns
        int rowNum = 1;
        for (String[] record : sorted) {
            String candidate = record.length > COL_CANDIDATE ? record[COL_CANDIDATE] : "N/A";
            String type      = record.length > COL_TYPE      ? record[COL_TYPE]      : "N/A";
            String exam      = record.length > COL_EXAM      ? record[COL_EXAM]      : "N/A";
            String result    = record.length > COL_RESULT    ? record[COL_RESULT]    : "N/A";
            String score     = record.length > COL_SCORE     ? record[COL_SCORE]     : "N/A";

            System.out.printf("%-5d | %-25s | %-10s | %-40s | %-8s | %-6s%n",
                    rowNum++, candidate, type, exam, result, score);
        }

        System.out.println("-".repeat(102));
        System.out.println("Total records: " + sorted.size() + "  |  Sorted by: " + colName);
        System.out.println();
    }
}