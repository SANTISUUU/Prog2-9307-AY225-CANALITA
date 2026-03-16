/**
 * MachineProblem.js
 *
 * A single Node.js program that handles:
 *   MP05 - Extract and display a selected column
 *   MP06 - Display unique values in a column
 *   MP07 - Sort records alphabetically by a column
 *
 * Dataset: Pearson VUE Exam Results (CSV)
 * Columns: Candidate, Type, Column1, Exam, Language, Exam Date, Score, Result, Time Used
 *
 * The program first asks the user for the CSV file path,
 * then shows a menu to choose which machine problem to run.
 *
 * Run with: node MachineProblem.js
 */

// -------------------------------------------------------
// IMPORTS - Node.js built-in modules
// fs       = File System (for reading the CSV file)
// readline = For interactive user input in the terminal
// -------------------------------------------------------
const fs       = require("fs");
const readline = require("readline");

// -------------------------------------------------------
// CONSTANTS - Column index mapping based on the CSV file
// -------------------------------------------------------
const COL_CANDIDATE = 0; // "Candidate" column index
const COL_TYPE      = 1; // "Student/Faculty/NTE" column index
const COL_EXAM      = 3; // "Exam" column index
const COL_LANGUAGE  = 4; // "Language" column index
const COL_DATE      = 5; // "Exam Date" column index
const COL_SCORE     = 6; // "Score" column index
const COL_RESULT    = 7; // "Result" column index
const COL_TIME      = 8; // "Time Used" column index

// Column headers displayed to the user in menus
const COLUMN_NAMES = [
    "Candidate",  // index 0
    "Type",       // index 1
    "Column1",    // index 2
    "Exam",       // index 3
    "Language",   // index 4
    "Exam Date",  // index 5
    "Score",      // index 6
    "Result",     // index 7
    "Time Used"   // index 8
];

// -------------------------------------------------------
// readline interface - used to read user input from terminal
// -------------------------------------------------------
const rl = readline.createInterface({
    input: process.stdin,
    output: process.stdout
});

/**
 * ask()
 * A helper function that wraps rl.question() in a Promise
 * so we can use async/await instead of nested callbacks.
 * @param {string} prompt - The question to show the user
 * @returns {Promise<string>} - The user's answer
 */
function ask(prompt) {
    return new Promise((resolve) => {
        rl.question(prompt, (answer) => resolve(answer.trim()));
    });
}

// -------------------------------------------------------
// parseCSVLine()
// Handles CSV lines where fields may be wrapped in quotes
// (e.g., "Last, First" is one field, not two).
// @param {string} line - A raw line from the CSV file
// @returns {string[]} - Array of field values
// -------------------------------------------------------
function parseCSVLine(line) {
    const fields = [];       // Stores all parsed fields
    let current  = "";       // Builds the current field character by character
    let inQuotes = false;    // Tracks whether we're inside a quoted field

    for (let i = 0; i < line.length; i++) {
        const c = line[i];

        if (c === '"') {
            // Toggle quote mode on/off
            inQuotes = !inQuotes;
        } else if (c === "," && !inQuotes) {
            // Comma outside quotes = field separator
            fields.push(current.trim());
            current = ""; // Reset for next field
        } else {
            // Normal character, append to current field
            current += c;
        }
    }

    // Push the last field after the loop ends
    fields.push(current.trim());
    return fields;
}

// -------------------------------------------------------
// loadCSV()
// Reads the CSV file from disk, skips metadata/header rows,
// and returns an array of parsed record arrays.
// @param {string} filePath - Path to the CSV file
// @returns {string[][] | null} - Array of records, or null on error
// -------------------------------------------------------
function loadCSV(filePath) {
    // Check if the file actually exists before trying to read it
    if (!fs.existsSync(filePath)) {
        console.log(`[ERROR] File not found: ${filePath}`);
        return null;
    }

    // Read the entire file as a UTF-8 string
    // The BOM character (\uFEFF) is removed if present at the start
    const content = fs.readFileSync(filePath, "utf-8").replace(/^\uFEFF/, "");

    // Split the file content into individual lines
    const lines = content.split(/\r?\n/);

    const records     = [];      // Will hold all valid data rows
    let dataStarted   = false;   // Flag: true once we've passed the header row

    for (const line of lines) {
        // Detect the actual column header row and mark data start
        if (line.includes("Candidate") && line.includes("Exam") && line.includes("Score")) {
            dataStarted = true;
            continue; // Skip the header row itself
        }

        // Skip lines before the header row
        if (!dataStarted) continue;

        // Skip blank or whitespace-only lines
        if (line.trim() === "" || line.replace(/,/g, "").trim() === "") continue;

        // Parse the CSV line into fields
        const fields = parseCSVLine(line);

        // Only keep rows with enough columns and a non-empty Candidate name
        if (fields.length >= 9 && fields[COL_CANDIDATE] !== "") {
            records.push(fields);
        }
    }

    return records;
}

// -------------------------------------------------------
// showColumnMenu()
// Displays a numbered list of available columns
// and returns the index the user selected.
// @param {readline.Interface} - For reading user input
// @returns {Promise<number>} - The chosen column index
// -------------------------------------------------------
async function showColumnMenu() {
    console.log("\nAvailable Columns:");
    COLUMN_NAMES.forEach((name, i) => {
        console.log(`  [${i}] ${name}`);
    });

    const input = await ask(`Enter column number (0-${COLUMN_NAMES.length - 1}): `);
    const colIndex = parseInt(input);

    // Validate range
    if (isNaN(colIndex) || colIndex < 0 || colIndex >= COLUMN_NAMES.length) {
        console.log("[WARNING] Invalid column. Defaulting to column 0 (Candidate).");
        return 0;
    }

    return colIndex;
}

// -------------------------------------------------------
// mp05_extractColumn()
// MP05 - Extract and Display a Selected Column
// Asks the user which column to show, then prints
// every value in that column from the dataset.
// @param {string[][]} records - All parsed CSV records
// -------------------------------------------------------
async function mp05_extractColumn(records) {
    console.log("\n========== MP05: Extract and Display a Column ==========");

    // Let the user pick which column to display
    const colIndex = await showColumnMenu();
    const colName  = COLUMN_NAMES[colIndex];

    console.log(`\n--- Values in Column: ${colName} ---`);
    console.log(`${"No.".padEnd(5)} | ${colName.padEnd(50)}`);
    console.log("-".repeat(58));

    // Loop through all records and print the chosen column value
    records.forEach((record, i) => {
        const value = record[colIndex] || "N/A"; // Use "N/A" if field is missing
        console.log(`${String(i + 1).padEnd(5)} | ${value.padEnd(50)}`);
    });

    console.log("-".repeat(58));
    console.log(`Total entries displayed: ${records.length}\n`);
}

// -------------------------------------------------------
// mp06_uniqueValues()
// MP06 - Display Unique Values in a Column
// Collects all values for a chosen column,
// removes duplicates, and prints only unique ones.
// @param {string[][]} records - All parsed CSV records
// -------------------------------------------------------
async function mp06_uniqueValues(records) {
    console.log("\n========== MP06: Display Unique Values in a Column ==========");

    // Let the user pick which column to analyze
    const colIndex = await showColumnMenu();
    const colName  = COLUMN_NAMES[colIndex];

    // Use a Set to automatically filter out duplicate values
    const uniqueValues = new Set();

    records.forEach((record) => {
        const value = record[colIndex];
        if (value && value.trim() !== "") {
            uniqueValues.add(value.trim()); // Sets ignore duplicates automatically
        }
    });

    // Convert Set to Array for display
    const uniqueArray = Array.from(uniqueValues);

    console.log(`\n--- Unique Values in Column: ${colName} ---`);
    console.log(`${"No.".padEnd(5)} | ${colName.padEnd(50)}`);
    console.log("-".repeat(58));

    // Print each unique value with a row number
    uniqueArray.forEach((val, i) => {
        console.log(`${String(i + 1).padEnd(5)} | ${val.padEnd(50)}`);
    });

    console.log("-".repeat(58));
    console.log(`Total unique values: ${uniqueArray.length}\n`);
}

// -------------------------------------------------------
// mp07_sortByColumn()
// MP07 - Sort Records Alphabetically by a Column
// Creates a copy of the records array, sorts it by
// the user's chosen column, and displays the results.
// @param {string[][]} records - All parsed CSV records
// -------------------------------------------------------
async function mp07_sortByColumn(records) {
    console.log("\n========== MP07: Sort Records Alphabetically by Column ==========");

    // Let the user pick which column to sort by
    const colIndex = await showColumnMenu();
    const colName  = COLUMN_NAMES[colIndex];

    // Spread operator creates a shallow copy so original records stay unchanged
    const sorted = [...records];

    // Sort using localeCompare for proper case-insensitive alphabetical order
    sorted.sort((a, b) => {
        const valA = a[colIndex] || "";
        const valB = b[colIndex] || "";
        return valA.localeCompare(valB, undefined, { sensitivity: "base" });
    });

    console.log(`\n--- Records Sorted by: ${colName} ---`);
    console.log(
        `${"No.".padEnd(5)} | ${"Candidate".padEnd(25)} | ${"Type".padEnd(10)} | ${"Exam".padEnd(40)} | ${"Result".padEnd(8)} | ${"Score".padEnd(6)}`
    );
    console.log("-".repeat(102));

    // Print each sorted record with key columns
    sorted.forEach((record, i) => {
        const candidate = (record[COL_CANDIDATE] || "N/A").padEnd(25);
        const type      = (record[COL_TYPE]      || "N/A").padEnd(10);
        const exam      = (record[COL_EXAM]      || "N/A").padEnd(40);
        const result    = (record[COL_RESULT]    || "N/A").padEnd(8);
        const score     = (record[COL_SCORE]     || "N/A").padEnd(6);

        console.log(`${String(i + 1).padEnd(5)} | ${candidate} | ${type} | ${exam} | ${result} | ${score}`);
    });

    console.log("-".repeat(102));
    console.log(`Total records: ${sorted.length}  |  Sorted by: ${colName}\n`);
}

// -------------------------------------------------------
// showMenu()
// Displays the main menu and returns the user's choice.
// @returns {Promise<string>} - The user's menu selection
// -------------------------------------------------------
async function showMenu() {
    console.log("--------------------------------------------");
    console.log("               MAIN MENU                    ");
    console.log("--------------------------------------------");
    console.log(" [5] MP05 - Extract and Display a Column");
    console.log(" [6] MP06 - Display Unique Values in a Column");
    console.log(" [7] MP07 - Sort Records Alphabetically by Column");
    console.log(" [0] Exit");
    console.log("--------------------------------------------");
    return await ask("Choose an option: ");
}

// -------------------------------------------------------
// main()
// Entry point — asks for file path, loads CSV,
// then loops the menu until the user exits.
// -------------------------------------------------------
async function main() {
    console.log("============================================");
    console.log("  Pearson VUE Exam Results - CSV Processor  ");
    console.log("============================================");

    // Step 1: Ask user for the CSV file path
    const filePath = await ask("Enter the full path to your CSV dataset file: ");

    // Step 2: Load and parse the CSV file
    const records = loadCSV(filePath);

    // If loading failed, stop the program
    if (!records || records.length === 0) {
        console.log("[ERROR] No valid records found. Please check your file and try again.");
        rl.close();
        return;
    }

    console.log(`[INFO] Successfully loaded ${records.length} records.\n`);

    // Step 3: Show the main menu in a loop until user exits
    let running = true;
    while (running) {
        const choice = await showMenu();

        switch (choice) {
            case "5":
                await mp05_extractColumn(records);
                break;
            case "6":
                await mp06_uniqueValues(records);
                break;
            case "7":
                await mp07_sortByColumn(records);
                break;
            case "0":
                console.log("Exiting program. Goodbye!");
                running = false;
                break;
            default:
                console.log("[WARNING] Invalid option. Please enter 5, 6, 7, or 0.\n");
        }
    }

    // Close the readline interface when done
    rl.close();
}

// -------------------------------------------------------
// Run the main function and catch any unexpected errors
// -------------------------------------------------------
main().catch((err) => {
    console.error("[FATAL ERROR]", err.message);
    rl.close();
});