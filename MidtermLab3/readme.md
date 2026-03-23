# MidtermLab3 — README

## Overview

A Java and Javascript console applications that processes a CSV dataset (Pearson VUE Exam Results) and performs three machine problem functions:

- **MP05** — Extract and display all values from a selected column
- **MP06** — Display unique/distinct values from a selected column
- **MP07** — Sort all records alphabetically by a selected column

---

## Requirements

| Requirement | Details |
|-------------|---------|
| Language | Java |
| Java Version | Java 8 or higher |
| External Libraries | None — uses built-in Java only |
| Dataset | Pearson VUE Exam Results (`.csv`) |

---

## How to Run

### Step 1 — Make sure Java is installed
Open your terminal or Command Prompt and check:
```
java -version
```
If no version appears, download and install Java from [https://java.com](https://java.com).

---

### Step 2 — Save the file
Place `MachineProblem.java` in a folder, for example:
```
C:\Users\YourName\Desktop\MP\MachineProblem.java
```

---

### Step 3 — Open terminal in that folder
On Windows:
1. Open the folder in File Explorer
2. Click the address bar, type `cmd`, and press **Enter**

On Mac/Linux:
```
cd /path/to/your/folder
```

---

### Step 4 — Compile the program
```
javac MachineProblem.java
```
This creates a `MachineProblem.class` file in the same folder.

---

### Step 5 — Run the program
```
java MachineProblem
```

---

### Step 6 — Enter the CSV file path when prompted
The program will ask:
```
Enter the full path to your CSV dataset file:
```
Enter the full path to your CSV file, for example:

- **Windows:** `C:\Users\YourName\Downloads\Sample_Data-Prog-2-csv.csv`
- **Mac/Linux:** `/home/yourname/Downloads/Sample_Data-Prog-2-csv.csv`

---

### Step 7 — Choose a Machine Problem from the menu
```
--------------------------------------------
                 MAIN MENU
--------------------------------------------
 [5] MP05 - Extract and Display a Column
 [6] MP06 - Display Unique Values in a Column
 [7] MP07 - Sort Records Alphabetically by Column
 [0] Exit
--------------------------------------------
Choose an option:
```
Type `5`, `6`, or `7` and press **Enter**.

---

### Step 8 — Select a column
The program will display the available columns:
```
Available Columns:
  [0] Candidate
  [1] Type
  [2] Column1
  [3] Exam
  [4] Language
  [5] Exam Date
  [6] Score
  [7] Result
  [8] Time Used
```
Type a column number (e.g. `3` for Exam) and press **Enter**.

---

## Program Logic

1. The program asks the user for the CSV file path before doing anything else.
2. It reads the file using `BufferedReader` and `FileReader`, skipping metadata rows at the top until it finds the real column header row.
3. Each valid data row is parsed using a custom CSV parser that handles quoted fields containing commas (e.g. `"Last, First"`).
4. Parsed rows are stored in an `ArrayList` of `String[]` arrays.
5. The user picks a machine problem and a column from a menu, and the selected operation runs on the stored records.
6. Results are printed to the console in a formatted table.
7. Errors such as missing files or invalid input are caught and displayed with a clear message.

---

## Dataset Structure

| Column Index | Column Name |
|---|---|
| 0 | Candidate |
| 1 | Type (Student / Faculty / NTE) |
| 2 | Column1 |
| 3 | Exam |
| 4 | Language |
| 5 | Exam Date |
| 6 | Score |
| 7 | Result (PASS / FAIL) |
| 8 | Time Used |

---

## Machine Problems Explained

### MP05 — Extract and Display a Column
Displays every value in the selected column, numbered row by row.

**Example output (Column: Exam):**
```
No.   | Exam
----------------------------------------------------------
1     | Python
2     | Cybersecurity
3     | Artificial Intelligence
...
```

---

### MP06 — Display Unique Values in a Column
Collects all values from the selected column, removes duplicates using a `LinkedHashSet`, and displays only unique entries.

**Example output (Column: Result):**
```
No.   | Result
----------------------------------------------------------
1     | PASS
2     | FAIL
```

---

### MP07 — Sort Records Alphabetically by a Column
Sorts a copy of all records by the selected column in case-insensitive alphabetical order using `compareToIgnoreCase()`, then displays the full sorted table.

**Example output (Sorted by: Candidate):**
```
No.   | Candidate                  | Type       | Exam                      | Result   | Score
1     | Agna,Euels                 | Student    | Device Configuration...   | FAIL     | 672
2     | Aldous,Chapiro             | NTE        | IT Specialist Networking  | PASS     | 980
...
```

---

## Error Handling

| Error | How it's handled |
|---|---|
| File not found | Prints `[ERROR] File not found` and exits |
| Unreadable file | Prints `[ERROR] Could not read file` and exits |
| Invalid menu input | Prints `[WARNING]` and re-shows the menu |
| Invalid column number | Prints `[WARNING]` and defaults to column 0 |

---

## Files Included

```
MachineProblem.java     — Main source code
README_Java.md          — This file
```

---

## Author Notes

- All comments in the source code explain variables, functions, and processing logic as required.
- No external libraries or dependencies are needed.
- The program can be re-run multiple times without restarting — just choose `0` to exit.




 is a Node.js console application that processes a CSV dataset (Pearson VUE Exam Results) and performs three machine problem functions:

- **MP05** — Extract and display all values from a selected column
- **MP06** — Display unique/distinct values from a selected column
- **MP07** — Sort all records alphabetically by a selected column

---

## Requirements

| Requirement | Details |
|-------------|---------|
| Language | JavaScript (Node.js) |
| Node.js Version | v14 or higher |
| External Libraries | None — uses built-in Node.js modules only |
| Modules Used | `fs` (File System), `readline` |
| Dataset | Pearson VUE Exam Results (`.csv`) |

---

## How to Run

### Step 1 — Make sure Node.js is installed
Open your terminal or Command Prompt and check:
```
node -v
```
If no version appears, download and install Node.js from [https://nodejs.org](https://nodejs.org).

---

### Step 2 — Save the file
Place `MachineProblem.js` in a folder, for example:
```
C:\Users\YourName\Desktop\MP\MachineProblem.js
```

---

### Step 3 — Open terminal in that folder
On Windows:
1. Open the folder in File Explorer
2. Click the address bar, type `cmd`, and press **Enter**

On Mac/Linux:
```
cd /path/to/your/folder
```

---

### Step 4 — Run the program
```
node MachineProblem.js
```
> No compilation step needed — Node.js runs the file directly!

---

### Step 5 — Enter the CSV file path when prompted
The program will ask:
```
Enter the full path to your CSV dataset file:
```
Enter the full path to your CSV file, for example:

- **Windows:** `C:\Users\YourName\Downloads\Sample_Data-Prog-2-csv.csv`
- **Mac/Linux:** `/home/yourname/Downloads/Sample_Data-Prog-2-csv.csv`

---

### Step 6 — Choose a Machine Problem from the menu
```
--------------------------------------------
               MAIN MENU
--------------------------------------------
 [5] MP05 - Extract and Display a Column
 [6] MP06 - Display Unique Values in a Column
 [7] MP07 - Sort Records Alphabetically by Column
 [0] Exit
--------------------------------------------
Choose an option:
```
Type `5`, `6`, or `7` and press **Enter**.

---

### Step 7 — Select a column
The program displays all available columns:
```
Available Columns:
  [0] Candidate
  [1] Type
  [2] Column1
  [3] Exam
  [4] Language
  [5] Exam Date
  [6] Score
  [7] Result
  [8] Time Used
```
Type a column number (e.g. `3` for Exam) and press **Enter**.

---

## Program Logic

1. The program prompts the user for the CSV file path before any processing begins.
2. It reads the file using the **Node.js `fs` (File System) module** via `fs.readFileSync()`.
3. The file content is split into lines and scanned for the real header row, skipping metadata rows at the top.
4. Each valid data row is parsed using a custom CSV parser that handles quoted fields containing commas (e.g. `"Last, First"`).
5. Parsed rows are stored in a JavaScript array of string arrays.
6. User input is handled using the **`readline` module** wrapped in `async/await` Promises for clean sequential prompts.
7. The selected MP runs on the stored records and prints a formatted table to the console.
8. Errors such as missing files or invalid inputs are caught and displayed with clear messages.

---

## Dataset Structure

| Column Index | Column Name |
|---|---|
| 0 | Candidate |
| 1 | Type (Student / Faculty / NTE) |
| 2 | Column1 |
| 3 | Exam |
| 4 | Language |
| 5 | Exam Date |
| 6 | Score |
| 7 | Result (PASS / FAIL) |
| 8 | Time Used |

---

## Machine Problems Explained

### MP05 — Extract and Display a Column
Displays every value in the selected column, numbered row by row.

**Example output (Column: Exam):**
```
No.   | Exam
----------------------------------------------------------
1     | Python
2     | Cybersecurity
3     | Artificial Intelligence
...
```

---

### MP06 — Display Unique Values in a Column
Collects all values from the selected column, removes duplicates using a JavaScript `Set`, and displays only unique entries.

**Example output (Column: Result):**
```
No.   | Result
----------------------------------------------------------
1     | PASS
2     | FAIL
```

---

### MP07 — Sort Records Alphabetically by a Column
Creates a sorted copy of all records using `Array.sort()` with `localeCompare()` for case-insensitive alphabetical ordering, then displays the full sorted table.

**Example output (Sorted by: Candidate):**
```
No.   | Candidate                  | Type       | Exam                      | Result   | Score
1     | Agna,Euels                 | Student    | Device Configuration...   | FAIL     | 672
2     | Aldous,Chapiro             | NTE        | IT Specialist Networking  | PASS     | 980
...
```

---

## Error Handling

| Error | How it's handled |
|---|---|
| File not found | Prints `[ERROR] File not found` and exits |
| Empty/no records | Prints `[ERROR] No valid records found` and exits |
| Invalid menu input | Prints `[WARNING]` and re-shows the menu |
| Invalid column number | Prints `[WARNING]` and defaults to column 0 |
| Unexpected crash | Caught by `.catch()` on the main async function |

---

## Key Node.js Modules Used

### `fs` — File System Module
Used to read the CSV file from disk:
```javascript
const fs = require("fs");
const content = fs.readFileSync(filePath, "utf-8");
```

### `readline` — Readline Module
Used to interactively prompt the user for input in the terminal:
```javascript
const readline = require("readline");
const rl = readline.createInterface({ input: process.stdin, output: process.stdout });
```

---

## Files Included

```
MachineProblem.js       — Main source code
MachineProblem.html     — Browser-based version (bonus)
README_JavaScript.md    — This file
```

---

## Author Notes

- All comments in the source code explain variables, functions, and processing logic as required.
- No `npm install` or external packages needed — runs out of the box.
- The program loops the menu so multiple MPs can be tested in one session. Type `0` to exit.
- The HTML version (`MachineProblem.html`) is a browser-based alternative that also supports CSV export.