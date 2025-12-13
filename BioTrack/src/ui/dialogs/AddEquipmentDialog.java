import java.awt.event.ActionEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.*;

public class AddEquipmentDialog extends JDialog {

    private Equipment newEquipment;  // to store the added equipment

    public AddEquipmentDialog(JFrame parent) {
        super(parent, "Add Equipment", true);
        setSize(350, 350);
        setLocationRelativeTo(parent);
        setLayout(null);

        JLabel lblName = new JLabel("Name:");
        lblName.setBounds(30, 30, 80, 25);
        add(lblName);

        JTextField txtName = new JTextField();
        txtName.setBounds(120, 30, 180, 25);
        add(txtName);

        JLabel lblCategory = new JLabel("Category:");
        lblCategory.setBounds(30, 70, 80, 25);
        add(lblCategory);

        String[] categories = {
            "Glassware",
            "Measuring Instruments",
            "Heating Equipment",
            "Cooling Equipment",
            "Electronic Equipment",
            "General Tools",
            "Safety Equipment",
            "Cleaning & Sterilization",
            "Chemical Reagents",
            "Consumables",
            "Storage Containers",
            "Specimen/Testing Equipment",
            "Microscopy Equipment",
            "Power/Mechanical Tools"
        };

        JComboBox<String> cmbCategory = new JComboBox<>(categories);
        cmbCategory.setBounds(120, 70, 180, 25);
        add(cmbCategory);

        JLabel lblStatus = new JLabel("Status:");
        lblStatus.setBounds(30, 110, 80, 25);
        add(lblStatus);

        String[] statusOptions = {"Available", "Borrowed", "Damaged"};
        JComboBox<String> cmbStatus = new JComboBox<>(statusOptions);
        cmbStatus.setBounds(120, 110, 180, 25);
        add(cmbStatus);

        JButton btnAdd = new JButton("Add");
        btnAdd.setBounds(120, 180, 100, 30);
        add(btnAdd);

        // Button action
        btnAdd.addActionListener((ActionEvent e) -> {
            String name = txtName.getText().trim();
            String category = (String) cmbCategory.getSelectedItem();
            String status = (String) cmbStatus.getSelectedItem();

            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name cannot be empty!");
                return;
            }

            // Auto-generate ID
            String id = "EQ" + System.currentTimeMillis();

            // Create Equipment object
            newEquipment = new Equipment(id, name, category, status);

            // --- Add transaction automatically ---
            addTransactionHistory("Added", id, name, "", status);

            dispose(); // close dialog
        });
    }

    // Getter for the new equipment
    public Equipment getNewEquipment() {
        return newEquipment;
    }

    // Write transaction to file
    private void addTransactionHistory(String action, String id, String name, String oldStatus, String newStatus) {
        try (FileWriter writer = new FileWriter("data/transactions.txt", true)) {

            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            writer.write(timestamp + " | " +
                    action + " | ID: " + id + " | " +
                    name + " | Status: " + oldStatus + " -> " + newStatus + "\n");

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Failed to log transaction: " + ex.getMessage());
        }
    }
}
