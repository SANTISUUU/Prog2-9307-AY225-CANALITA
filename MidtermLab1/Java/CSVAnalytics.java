package Java;

import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class CSVAnalytics {

    public static void main(String[] args) {

        boolean continueProcessing = true;

        while (continueProcessing) {

            File file = selectFile();
            if (file == null) {
                System.out.println("No file selected. Exiting.");
                break;
            }

            List<String[]> rows = new ArrayList<>();
            String[] headers;

            try (BufferedReader br = new BufferedReader(new FileReader(file))) {

                String headerLine = br.readLine();
                if (headerLine == null) {
                    System.out.println("Dataset is empty.");
                    continue;
                }

                headers = headerLine.split(",");

                String line;
                while ((line = br.readLine()) != null) {
                    rows.add(line.split(","));
                }

                int totalRecords = rows.size();

                // ===== DYNAMIC COLUMN DETECTION =====
                Map<String, Integer> columnIndex = new HashMap<>();
                for (int i = 0; i < headers.length; i++) {
                    columnIndex.put(headers[i].trim().toLowerCase(), i);
                }

                int idxTitle = columnIndex.getOrDefault("title", -1);
                int idxGenre = columnIndex.getOrDefault("genre", -1);
                int idxPublisher = columnIndex.getOrDefault("publisher", -1);
                int idxCritic = columnIndex.getOrDefault("critic_score", -1);
                int idxTotal = columnIndex.getOrDefault("total_sales", -1);
                int idxNA = columnIndex.getOrDefault("na_sales", -1);
                int idxJP = columnIndex.getOrDefault("jp_sales", -1);
                int idxPAL = columnIndex.getOrDefault("pal_sales", -1);
                int idxOther = columnIndex.getOrDefault("other_sales", -1);

                double totalSales = 0, totalNA = 0, totalJP = 0, totalPAL = 0, totalOther = 0;
                double criticSum = 0;
                int criticCount = 0;

                String topGame = "N/A";
                double topGameSales = 0;

                Map<String, Double> genreSales = new HashMap<>();
                Map<String, Double> publisherSales = new HashMap<>();

                for (String[] row : rows) {

                    double sales = (idxTotal >= 0 && idxTotal < row.length) ? parseDouble(row[idxTotal]) : 0;
                    double na = (idxNA >= 0 && idxNA < row.length) ? parseDouble(row[idxNA]) : 0;
                    double jp = (idxJP >= 0 && idxJP < row.length) ? parseDouble(row[idxJP]) : 0;
                    double pal = (idxPAL >= 0 && idxPAL < row.length) ? parseDouble(row[idxPAL]) : 0;
                    double other = (idxOther >= 0 && idxOther < row.length) ? parseDouble(row[idxOther]) : 0;

                    totalSales += sales;
                    totalNA += na;
                    totalJP += jp;
                    totalPAL += pal;
                    totalOther += other;

                    if (sales > topGameSales && idxTitle >= 0 && idxTitle < row.length) {
                        topGameSales = sales;
                        topGame = row[idxTitle];
                    }

                    if (idxGenre >= 0 && idxGenre < row.length) {
                        String genre = row[idxGenre];
                        genreSales.put(genre, genreSales.getOrDefault(genre, 0.0) + sales);
                    }

                    if (idxPublisher >= 0 && idxPublisher < row.length) {
                        String publisher = row[idxPublisher];
                        publisherSales.put(publisher, publisherSales.getOrDefault(publisher, 0.0) + sales);
                    }

                    if (idxCritic >= 0 && idxCritic < row.length) {
                        try {
                            criticSum += Double.parseDouble(row[idxCritic]);
                            criticCount++;
                        } catch (NumberFormatException ignored) {}
                    }
                }

                String topGenre = genreSales.entrySet()
                        .stream()
                        .max(Map.Entry.comparingByValue())
                        .map(Map.Entry::getKey)
                        .orElse("N/A");

                double topGenreSales = genreSales.getOrDefault(topGenre, 0.0);

                String topPublisher = publisherSales.entrySet()
                        .stream()
                        .max(Map.Entry.comparingByValue())
                        .map(Map.Entry::getKey)
                        .orElse("N/A");

                double topPublisherSales = publisherSales.getOrDefault(topPublisher, 0.0);

                double totalRegional = totalNA + totalJP + totalPAL + totalOther;

                // ===== WRITE EXECUTIVE REPORT =====
                try (PrintWriter pw = new PrintWriter(new FileWriter("summary_report.csv"))) {

                    pw.println("=== EXECUTIVE SALES ANALYTICS SUMMARY REPORT ===");
                    pw.println();

                    pw.println("DATASET OVERVIEW");
                    pw.println("Total Records," + totalRecords);
                    pw.println("Total Genres," + genreSales.size());
                    pw.println("Total Publishers," + publisherSales.size());
                    pw.println();

                    pw.println("SALES PERFORMANCE METRICS (Millions)");
                    pw.printf("Total Global Sales,$%.2f%n", totalSales);
                    pw.printf("North America,$%.2f%n", totalNA);
                    pw.printf("Japan,$%.2f%n", totalJP);
                    pw.printf("PAL Regions,$%.2f%n", totalPAL);
                    pw.printf("Other Regions,$%.2f%n", totalOther);
                    pw.println();

                    pw.println("QUALITY METRICS");
                    if (criticCount > 0) {
                        pw.printf("Average Critic Score,%.2f%n", criticSum / criticCount);
                    }
                    pw.println();

                    pw.println("PERFORMANCE LEADERS");
                    pw.printf("Top Selling Game,%s ($%.2fM)%n", topGame, topGameSales);
                    pw.printf("Top Genre,%s ($%.2fM)%n", topGenre, topGenreSales);
                    pw.printf("Top Publisher,%s ($%.2fM)%n", topPublisher, topPublisherSales);
                    pw.println();

                    pw.println("REGIONAL SALES BREAKDOWN (%)");
                    if (totalRegional > 0) {
                        pw.printf("North America,%.1f%%%n", (totalNA / totalRegional) * 100);
                        pw.printf("Japan,%.1f%%%n", (totalJP / totalRegional) * 100);
                        pw.printf("PAL Regions,%.1f%%%n", (totalPAL / totalRegional) * 100);
                        pw.printf("Other Regions,%.1f%%%n", (totalOther / totalRegional) * 100);
                    }

                    pw.println();
                    pw.println("Report Generated," + new java.util.Date());
                }

                System.out.println("Summary report written to summary_report.csv");

                Object[] options = {"Process Another", "Exit"};
                int choice = JOptionPane.showOptionDialog(null,
                        "File processed successfully!\nWould you like to process another file?",
                        "Success",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        options,
                        options[0]);

                if (choice != 0) {
                    continueProcessing = false;
                }

            } catch (IOException e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }

    private static double parseDouble(String value) {
        try {
            return Double.parseDouble(value);
        } catch (Exception e) {
            return 0;
        }
    }

    private static File selectFile() {

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select CSV File");
        fileChooser.setFileFilter(new FileNameExtensionFilter("CSV Files (*.csv)", "csv"));

        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        }

        return null;
    }
}