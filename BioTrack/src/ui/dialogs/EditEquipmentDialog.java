import javax.swing.*;

public class EditEquipmentDialog extends JDialog {

    public EditEquipmentDialog(JFrame parent, Equipment equipment) {
        super(parent, "Edit Equipment", true);
        setSize(350, 350);
        setLocationRelativeTo(parent);
        setLayout(null);

        JLabel lblName = new JLabel("Name:");
        lblName.setBounds(30, 30, 80, 25);
        add(lblName);

        JTextField txtName = new JTextField(equipment.getName());
        txtName.setBounds(120, 30, 180, 25);
        add(txtName);

        JLabel lblCategory = new JLabel("Category:");
        lblCategory.setBounds(30, 70, 80, 25);
        add(lblCategory);

        JTextField txtCategory = new JTextField(equipment.getCategory());
        txtCategory.setBounds(120, 70, 180, 25);
        add(txtCategory);

        JLabel lblStatus = new JLabel("Status:");
        lblStatus.setBounds(30, 110, 80, 25);
        add(lblStatus);

        String[] statusOptions = {"Available", "Borrowed", "Damaged"};
        JComboBox<String> cmbStatus = new JComboBox<>(statusOptions);
        cmbStatus.setBounds(120, 110, 180, 25);
        cmbStatus.setSelectedItem(equipment.getStatus());
        add(cmbStatus);

        JButton btnSave = new JButton("Save");
        btnSave.setBounds(120, 180, 100, 30);
        add(btnSave);

        btnSave.addActionListener(e -> {
            equipment.setName(txtName.getText().trim());
            equipment.setCategory(txtCategory.getText().trim());
            equipment.setStatus((String) cmbStatus.getSelectedItem());
            JOptionPane.showMessageDialog(this, "Equipment Updated: " + equipment);
            dispose();
        });
    }
}
