import java.time.LocalDateTime;
import javax.swing.*;

public class BorrowDialog extends JDialog {

    public BorrowDialog(JFrame parent, Equipment equipment) {
        super(parent, "Borrow Equipment", true);
        setSize(350, 250);
        setLocationRelativeTo(parent);
        setLayout(null);

        JLabel lblInfo = new JLabel("Borrowing: " + equipment.getName());
        lblInfo.setBounds(30, 20, 300, 25);
        add(lblInfo);

        JLabel lblBorrower = new JLabel("Borrower:");
        lblBorrower.setBounds(30, 60, 80, 25);
        add(lblBorrower);

        JTextField txtBorrower = new JTextField();
        txtBorrower.setBounds(120, 60, 180, 25);
        add(txtBorrower);

        JButton btnBorrow = new JButton("Confirm Borrow");
        btnBorrow.setBounds(100, 120, 150, 30);
        add(btnBorrow);

        btnBorrow.addActionListener(e -> {
            String borrower = txtBorrower.getText().trim();

            if (borrower.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Borrower name is required!");
                return;
            }

            if (!"Available".equals(equipment.getStatus())) {
                JOptionPane.showMessageDialog(this, "Equipment is not available!");
                return;
            }

            // Update equipment status
            equipment.setStatus("Borrowed");

            // SAVE TRANSACTION
            String dateTime = LocalDateTime.now().toString();

            Transaction t = new Transaction(
                    equipment.getId(),
                    equipment.getName(),
                    "Borrowed",
                    borrower,
                    dateTime
            );

            TransactionManager.saveTransaction(t);

            JOptionPane.showMessageDialog(this,
                    equipment.getName() + " borrowed successfully!");

            dispose();
        });
    }
}
