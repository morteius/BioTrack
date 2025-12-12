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
            if ("Borrowed".equals(equipment.getStatus())) {
                equipment.setStatus("Available");
                JOptionPane.showMessageDialog(this, equipment.getName() + " returned successfully!");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Equipment was not borrowed!");
            }
        });
    }
}
