import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.*;

public class EditEquipmentDialog extends JDialog {

    public EditEquipmentDialog(JFrame parent, Equipment equipment) {
        super(parent, "Edit Equipment", true);
        setSize(350, 350);
        setLocationRelativeTo(parent);
        setLayout(null);

        // -------------------- NAME --------------------
        JLabel lblName = new JLabel("Name:");
        lblName.setBounds(30, 30, 80, 25);
        add(lblName);

        JTextField txtName = new JTextField(equipment.getName());
        txtName.setBounds(120, 30, 180, 25);
        add(txtName);

        // -------------------- CATEGORY --------------------
        JLabel lblCategory = new JLabel("Category:");
        lblCategory.setBounds(30, 70, 80, 25);
        add(lblCategory);

        String[] categories = {
            "Glassware", "Measuring Instruments", "Heating Equipment",
            "Cooling Equipment", "Electronic Equipment", "General Tools",
            "Safety Equipment", "Cleaning & Sterilization", "Chemical Reagents",
            "Consumables", "Storage Containers", "Specimen/Testing Equipment",
            "Microscopy Equipment", "Power/Mechanical Tools"
        };

        JComboBox<String> cmbCategory = new JComboBox<>(categories);
        cmbCategory.setBounds(120, 70, 180, 25);
        cmbCategory.setSelectedItem(equipment.getCategory());
        add(cmbCategory);

        // -------------------- STATUS --------------------
        JLabel lblStatus = new JLabel("Status:");
        lblStatus.setBounds(30, 110, 80, 25);
        add(lblStatus);

        String[] statusOptions = {"Available", "Borrowed", "Damaged"};
        JComboBox<String> cmbStatus = new JComboBox<>(statusOptions);
        cmbStatus.setBounds(120, 110, 180, 25);
        cmbStatus.setSelectedItem(equipment.getStatus());
        add(cmbStatus);

        // -------------------- SAVE BUTTON --------------------
        JButton btnSave = new JButton("Save");
        btnSave.setBounds(120, 180, 100, 30);
        add(btnSave);

        btnSave.addActionListener(e -> {
            String newName = txtName.getText().trim();
            String newCategory = (String) cmbCategory.getSelectedItem();
            String newStatus = (String) cmbStatus.getSelectedItem();

            if (!newName.isEmpty() && newCategory != null) {

                // ——— APPLY CHANGES TO EQUIPMENT ———
                String oldStatus = equipment.getStatus();
                equipment.setName(newName);
                equipment.setCategory(newCategory);
                equipment.setStatus(newStatus);

                // ——— AUTOMATIC TRANSACTION ———
                addTransactionHistory(
                    "Edited",
                    equipment.getId(),
                    equipment.getName(),
                    oldStatus,
                    newStatus
                );

                JOptionPane.showMessageDialog(this, "Equipment Updated!");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Please fill all fields!");
            }
        });
    }

    // -------------------- WRITE TO TRANSACTION FILE --------------------
    private void addTransactionHistory(String action, String id, String name,
                                       String oldStatus, String newStatus) {
        try (FileWriter writer = new FileWriter("data/transactions.txt", true)) {

            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            writer.write(
                timestamp + " | " +
                action + " | " +
                "ID: " + id + " | " +
                name + " | " +
                "Status: " + oldStatus + " -> " + newStatus + "\n"
            );

        } catch (IOException e) {
            System.out.println("Error writing transaction: " + e.getMessage());
        }
    }
}
