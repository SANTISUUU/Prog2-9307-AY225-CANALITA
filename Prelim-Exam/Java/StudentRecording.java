/*
 HELLO HELLO!
 CHINO MA. SANTI F. CANALITA WAS HERE
 25-2205-910
 UPHSD - MOLINO
 BSCS - DS 1ST YR. - 2ND SEM.
 PRELIM EXAM - PROGRAMMING 2
*/

import java.awt.*;
import java.io.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class StudentRecording extends JFrame {

    private JTable table;
    private DefaultTableModel model;

    private JTextField[] fields;
    private JTextField searchField;

    private final String[] columns = {
            "StudentID", "First Name", "Last Name",
            "Lab Work 1", "Lab Work 2", "Lab Work 3",
            "Prelim Exam", "Attendance Grade"
    };

    private final String FILE_PATH = 
        "c:\\Users\\SANTISIM0\\OneDrive\\Documents\\Coding\\School\\CANALITA\\Fundamentals of PRogramming\\First Year\\2nd Sem\\Prelims\\git\\Prog2-9307-AY225-CANALITA\\Prelim-Exam\\Java\\class_records.csv";

    public StudentRecording() {
        this.setTitle("CHINO MA. SANTI F. CANALITA WAS HERE");
        this.setSize(1000, 500);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        Color bg = new Color(80, 80, 80);
        Color fg = new Color(245, 245, 245);

        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        table.setFillsViewportHeight(true);
        table.setBackground(bg);
        table.setForeground(fg);
        table.getTableHeader().setBackground(bg);
        table.getTableHeader().setForeground(fg);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBackground(bg);
        scrollPane.getViewport().setBackground(bg);

        JPanel inputPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        inputPanel.setBackground(bg);

        fields = new JTextField[columns.length];
        for (int i = 0; i < columns.length; i++) {
            fields[i] = new JTextField();
            fields[i].setBackground(new Color(100, 100, 100));
            fields[i].setForeground(fg);
            inputPanel.add(labeled(columns[i], fields[i], fg));
        }

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(bg);

        JButton btnAdd = new JButton("Add");
        JButton btnDelete = new JButton("Delete");
        JButton btnSave = new JButton("Save CSV");
        JButton btnSearch = new JButton("Search");

        searchField = new JTextField(15);
        searchField.setBackground(new Color(100, 100, 100));
        searchField.setForeground(fg);

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnSave);
        buttonPanel.add(new JLabel("Search:", JLabel.RIGHT));
        buttonPanel.add(searchField);
        buttonPanel.add(btnSearch);

        this.setLayout(new BorderLayout(10, 10));
        this.setBackground(bg);
        this.getContentPane().setBackground(bg);
        this.add(scrollPane, BorderLayout.CENTER);
        this.add(inputPanel, BorderLayout.NORTH);
        this.add(buttonPanel, BorderLayout.SOUTH);

        loadCSV();

        // ADD
        btnAdd.addActionListener(e -> {
            String[] row = new String[columns.length];
            for (int i = 0; i < fields.length; i++) {
                if (fields[i].getText().isEmpty()) {
                    showMessage("Please fill in all fields.");
                    return;
                }
                row[i] = fields[i].getText();
            }
            model.addRow(row);
            clearFields();
        });

        // DELETE
        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                model.removeRow(row);
            } else {
                showMessage("Select a student to delete.");
            }
        });

        // SAVE
        btnSave.addActionListener(e -> saveCSV());

        // SEARCH
        btnSearch.addActionListener(e -> searchStudent());
    }

    private JPanel labeled(String label, JTextField field, Color fg) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(200, 200, 200));

        JLabel lbl = new JLabel(label);
        lbl.setForeground(fg);

        panel.add(lbl, BorderLayout.NORTH);
        panel.add(field, BorderLayout.CENTER);
        return panel;
    }

    private void loadCSV() {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                model.addRow(line.split(","));
            }
        } catch (IOException e) {
            showMessage("CSV file not found. A new one will be created.");
        }
    }

    private void saveCSV() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_PATH))) {
            for (int i = 0; i < model.getRowCount(); i++) {
                for (int j = 0; j < model.getColumnCount(); j++) {
                    pw.print(model.getValueAt(i, j));
                    if (j < model.getColumnCount() - 1) pw.print(",");
                }
                pw.println();
            }
            showMessage("CSV saved successfully.");
        } catch (IOException e) {
            showMessage("Error saving CSV.");
        }
    }

    private void searchStudent() {
        String query = searchField.getText().toLowerCase();

        for (int i = 0; i < model.getRowCount(); i++) {
            String id = model.getValueAt(i, 0).toString().toLowerCase();
            String fname = model.getValueAt(i, 1).toString().toLowerCase();
            String lname = model.getValueAt(i, 2).toString().toLowerCase();

            if (id.contains(query) || fname.contains(query) || lname.contains(query)) {
                table.setRowSelectionInterval(i, i);
                table.scrollRectToVisible(table.getCellRect(i, 0, true));
                return;
            }
        }
        showMessage("Student not found.");
    }

    private void clearFields() {
        for (JTextField f : fields) f.setText("");
    }

    private void showMessage(String msg) {
        UIManager.put("OptionPane.background", new Color(180, 180, 180));
        UIManager.put("Panel.background", new Color(180, 180, 180));
        UIManager.put("OptionPane.messageForeground", Color.WHITE);

        JOptionPane.showMessageDialog(this, msg);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StudentRecording().setVisible(true));
    }
}

/*
 HELLO HELLO!
 CHINO MA. SANTI F. CANALITA WAS HERE
 25-2205-910
 UPHSD - MOLINO
 BSCS - DS 1ST YR. - 2ND SEM.
 PRELIM EXAM - PROGRAMMING 2
*/