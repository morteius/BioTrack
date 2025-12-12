import javax.swing.*;

public class AddEquipmentDialog extends JDialog {

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

        JTextField txtCategory = new JTextField();
        txtCategory.setBounds(120, 70, 180, 25);
        add(txtCategory);

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

        btnAdd.addActionListener(e -> {
            String name = txtName.getText().trim();
            String category = txtCategory.getText().trim();
            String status = (String) cmbStatus.getSelectedItem();

            if (!name.isEmpty() && !category.isEmpty()) {
                Equipment eq = new Equipment("E" + System.currentTimeMillis(), name, category, status);
                JOptionPane.showMessageDialog(this, "Equipment Added: " + eq);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Please fill all fields!");
            }
        });
    }
}
