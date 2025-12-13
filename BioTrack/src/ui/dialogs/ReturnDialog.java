import java.time.LocalDateTime;
import javax.swing.*;

public class ReturnDialog extends JDialog {

    public ReturnDialog(JFrame parent, Equipment equipment) {
        super(parent, "Return Equipment", true);
        setSize(350, 200);
        setLocationRelativeTo(parent);
        setLayout(null);

        JLabel lblInfo = new JLabel("Returning: " + equipment.getName());
        lblInfo.setBounds(30, 30, 300, 25);
        add(lblInfo);

        JButton btnReturn = new JButton("Confirm Return");
        btnReturn.setBounds(100, 80, 150, 30);
        add(btnReturn);

        btnReturn.addActionListener(e -> {

            // Check if the equipment is currently borrowed
            if (!"Borrowed".equals(equipment.getStatus())) {
                JOptionPane.showMessageDialog(this, "Equipment was not borrowed!");
                return;  // Do not continue
            }

            // Update status
            equipment.setStatus("Available");

            // Prepare timestamp
            String dateTime = LocalDateTime.now().toString();

            // Create transaction record
            Transaction t = new Transaction(
                    equipment.getId(),
                    equipment.getName(),
                    "Returned",
                    "N/A",      
                    dateTime
            );

            // Save to transactions.txt
            TransactionManager.saveTransaction(t);

            JOptionPane.showMessageDialog(this,
                    equipment.getName() + " returned successfully!");

            dispose();
        });
    }
}
