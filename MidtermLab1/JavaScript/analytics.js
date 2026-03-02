const fs = require('fs');
const path = require('path');
const dialog = require('dialog');
const prompt = require('prompt-sync')();

/**
 * CSV Analytics Report Generator
 * Generates analytics summary from CSV files and exports to summary_report.csv
 */

async function main() {
    let continueProcessing = true;

    while (continueProcessing) {
        const selectedFile = await selectFile();

        if (!selectedFile) {
            console.log("No file selected. Exiting.");
            break;
        }

        try {
            const content = fs.readFileSync(selectedFile, 'utf-8');
            const lines = content.split(/\r?\n/).filter(l => l.length > 0);

            if (lines.length === 0) {
                console.log("Dataset is empty – nothing to analyze.");
                continue;
            }

            const headers = lines[0].split(',');
            const rows = lines.slice(1).map(l => l.split(','));
            const totalRecords = rows.length;

            // Accumulate sums and counts for numeric columns
            const sums = new Array(headers.length).fill(0);
            const counts = new Array(headers.length).fill(0);

            rows.forEach(row => {
                row.forEach((cell, i) => {
                    const v = parseFloat(cell);
                    if (!isNaN(v)) {
                        sums[i] += v;
                        counts[i]++;
                    }
                });
            });

            // Generate summary report
            let report = "=== EXECUTIVE SALES SUMMARY REPORT ===\n\n";
            report += "DATASET OVERVIEW\n";
            report += "Total Games Analyzed," + totalRecords + "\n\n";
            report += "SALES PERFORMANCE METRICS\n";

            // Only show relevant numeric columns with readable names
            if (counts[7] > 0) { // total_sales
                report += "Average Total Sales (Millions),$" + (sums[7] / counts[7]).toFixed(2) + "\n";
            }
            if (counts[8] > 0) { // na_sales
                report += "Average North America Sales (Millions),$" + (sums[8] / counts[8]).toFixed(2) + "\n";
            }
            if (counts[9] > 0) { // jp_sales
                report += "Average Japan Sales (Millions),$" + (sums[9] / counts[9]).toFixed(2) + "\n";
            }
            if (counts[10] > 0) { // pal_sales
                report += "Average PAL Regions Sales (Millions),$" + (sums[10] / counts[10]).toFixed(2) + "\n";
            }
            if (counts[11] > 0) { // other_sales
                report += "Average Other Regions Sales (Millions),$" + (sums[11] / counts[11]).toFixed(2) + "\n";
            }

            report += "\n";
            report += "QUALITY METRICS\n";
            if (counts[6] > 0) { // critic_score
                report += "Average Critic Score (out of 10)," + (sums[6] / counts[6]).toFixed(2) + "\n";
            }

            report += "\n";
            report += "Report Generated: " + new Date().toString() + "\n";

            // Write summary report using fs.writeFile (async)
            fs.writeFile("summary_report.csv", report, (err) => {
                if (err) {
                    console.error("Error writing file: " + err.message);
                    return;
                }
                
                console.log("Summary report written to summary_report.csv");

                // Ask user if they want to process another file
                const choice = prompt("File processed successfully! Process another file? (yes/no): ").toLowerCase();
                if (choice !== 'yes' && choice !== 'y') {
                    continueProcessing = false;
                }
            });
        } catch (error) {
            console.error("Error reading or writing file: " + error.message);
        }
    }
}

/**
 * Opens a file dialog to select a CSV file
 */
async function selectFile() {
    while (true) {
        const userHome = process.env.USERPROFILE || process.env.HOME;
        
        try {
            const files = dialog.showOpenDialog({
                defaultPath: userHome,
                filters: [
                    { name: 'CSV Files', extensions: ['csv'] },
                    { name: 'All Files', extensions: ['*'] }
                ],
                properties: ['openFile']
            });

            if (!files || files.length === 0) {
                return null;
            }

            const selectedFile = files[0];

            // Validate file
            if (!fs.existsSync(selectedFile)) {
                // File not found, try recursive search
                const found = findFileRecursive(userHome, path.basename(selectedFile));
                if (found) {
                    return found;
                } else {
                    const retry = prompt("File not found: " + path.basename(selectedFile) + "\nSearched in all folders.\nTry again? (yes/no): ").toLowerCase();
                    if (retry !== 'yes' && retry !== 'y') {
                        return null;
                    }
                    continue;
                }
            }

            if (!fs.lstatSync(selectedFile).isFile()) {
                const retry = prompt("Selected item is not a file: " + selectedFile + "\nTry again? (yes/no): ").toLowerCase();
                if (retry !== 'yes' && retry !== 'y') {
                    return null;
                }
                continue;
            }

            return selectedFile;
        } catch (error) {
            console.error("Error opening file dialog: " + error.message);
            const retry = prompt("Error selecting file. Try again? (yes/no): ").toLowerCase();
            if (retry !== 'yes' && retry !== 'y') {
                return null;
            }
        }
    }
}

/**
 * Recursively search for a file by name
 */
function findFileRecursive(directory, targetName) {
    try {
        const files = fs.readdirSync(directory);

        for (const file of files) {
            const fullPath = path.join(directory, file);

            if (path.basename(fullPath).toLowerCase() === targetName.toLowerCase()) {
                if (fs.lstatSync(fullPath).isFile()) {
                    return fullPath;
                }
            }

            if (fs.lstatSync(fullPath).isDirectory() && !file.startsWith('.')) {
                const found = findFileRecursive(fullPath, targetName);
                if (found) {
                    return found;
                }
            }
        }
    } catch (error) {
        // Permission denied or other error, skip this directory
    }

    return null;
}

// Run the program
main().catch(error => {
    console.error("Fatal error: " + error.message);
    process.exit(1);
});
