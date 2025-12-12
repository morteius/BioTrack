import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class EquipmentFrame extends JFrame {

    private JTable table;
    private DefaultTableModel model;
    private ArrayList<Equipment> equipmentList = new ArrayList<>();

    private final String FILE_PATH = "biotrack/data/equipment.txt"; // updated path

    public EquipmentFrame() {
        setTitle("Equipment Management System");
        setSize(800, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Table model
        model = new DefaultTableModel(new String[] { "ID", "Name", "Category", "Status" }, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        // Ensure file exists and load equipment data
        ensureEquipmentFileExists(FILE_PATH);
        loadEquipmentFile(FILE_PATH);
        refreshTable();

        // Buttons
        JButton btnAdd = new JButton("Add Equipment");
        JButton btnBorrow = new JButton("Borrow");
        JButton btnEdit = new JButton("Edit");
        JButton btnReturn = new JButton("Return");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnBorrow);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnReturn);

        // Button Actions (replace with your dialogs)
        btnAdd.addActionListener(e -> openAddDialog());
        btnBorrow.addActionListener(e -> openBorrowDialog());
        btnEdit.addActionListener(e -> openEditDialog());
        btnReturn.addActionListener(e -> openReturnDialog());

        // Layout
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // -----------------------------
    // Ensure file exists
    // -----------------------------
    private void ensureEquipmentFileExists(String filename) {
        File file = new File(filename);
        if (!file.exists()) {
            file.getParentFile().mkdirs(); // create folder if missing
            try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
                pw.println("E1001,Projector,Electronics,Available");
                pw.println("E1002,Laptop,Electronics,Borrowed");
                pw.println("E1003,Microphone,Audio,Available");
                pw.println("E1004,Tripod,Accessories,Available");
                pw.println("E1005,HDMI Cable,Cables,Damaged");
                pw.println("E1006,Speakers,Audio,Borrowed");
                pw.println("E1007,Webcam,Electronics,Available");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Could not create file: " + filename);
            }
        }
    }

    // -----------------------------
    // Load equipment data from TXT
    // -----------------------------
    private void loadEquipmentFile(String filename) {
        equipmentList.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 4) {
                    Equipment eq = new Equipment(
                            data[0].trim(),
                            data[1].trim(),
                            data[2].trim(),
                            data[3].trim());
                    equipmentList.add(eq);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Could not load file: " + filename);
        }
    }

    // -----------------------------
    // Refresh table
    // -----------------------------
    private void refreshTable() {
        model.setRowCount(0);
        for (Equipment eq : equipmentList) {
            model.addRow(new Object[] {
                    eq.getId(),
                    eq.getName(),
                    eq.getCategory(),
                    eq.getStatus()
            });
        }
    }

    // -----------------------------
    // Dialog placeholders
    // -----------------------------
    private void openAddDialog() {
        JOptionPane.showMessageDialog(this, "AddEquipmentDialog goes here.");
    }

    private void openBorrowDialog() {
        JOptionPane.showMessageDialog(this, "BorrowDialog goes here.");
    }

    private void openEditDialog() {
        JOptionPane.showMessageDialog(this, "EditEquipmentDialog goes here.");
    }

    private void openReturnDialog() {
        JOptionPane.showMessageDialog(this, "ReturnDialog goes here.");
    }

    // -----------------------------
    // Main for testing
    // -----------------------------
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EquipmentFrame().setVisible(true));
    }
}
