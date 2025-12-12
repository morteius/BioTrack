import javax.swing.*;

public class BorrowDialog extends JDialog {

    public BorrowDialog(JFrame parent, Equipment equipment) {
        super(parent, "Borrow Equipment", true);
        setSize(350, 200);
        setLocationRelativeTo(parent);
        setLayout(null);

        JLabel lblInfo = new JLabel("Borrowing: " + equipment.getName());
        lblInfo.setBounds(30, 30, 300, 25);
        add(lblInfo);

        JButton btnBorrow = new JButton("Confirm Borrow");
        btnBorrow.setBounds(100, 80, 150, 30);
        add(btnBorrow);

        btnBorrow.addActionListener(e -> {
            if ("Available".equals(equipment.getStatus())) {
                equipment.setStatus("Borrowed");
                JOptionPane.showMessageDialog(this, equipment.getName() + " borrowed successfully!");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Equipment is not available!");
            }
        });
    }
}
