import java.awt.*;
import javax.swing.*;

public class MainAppFrame {

    private JFrame frame;
    @SuppressWarnings("FieldMayBeFinal")
    private String username;

    // Custom panel with background image
    class BackgroundPanel extends JPanel {
        @SuppressWarnings("FieldMayBeFinal")
        private Image bg;

        public BackgroundPanel(String imagePath) {
            bg = new ImageIcon(imagePath).getImage();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
        }
    }

    public MainAppFrame(String username) {
        this.username = username;
    }

    public void showMain() {

        frame = new JFrame("BioTrack - Main Menu");
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        // Background panel
        BackgroundPanel bgPanel = new BackgroundPanel("BioTrack/src/logo.jpg");
        bgPanel.setLayout(null);  // keep your absolute positions
        frame.setContentPane(bgPanel);

        JLabel lbl = new JLabel("Welcome " + username + "!");
        lbl.setFont(new Font("SansSerif", Font.BOLD, 20));
        lbl.setForeground(Color.BLACK); // text readable on bg
        lbl.setBounds(30, 20, 300, 40);
        bgPanel.add(lbl);

        JButton equipmentBtn = new JButton("Equipment Management");
        equipmentBtn.setBounds(50, 100, 200, 50);
        bgPanel.add(equipmentBtn);

        JButton transactionBtn = new JButton("Transaction Records");
        transactionBtn.setBounds(50, 170, 200, 50);
        bgPanel.add(transactionBtn);

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setBounds(50, 240, 200, 50);
        bgPanel.add(logoutBtn);

        // Actions
        equipmentBtn.addActionListener(e -> new EquipmentFrame().showEquipmentFrame());
        transactionBtn.addActionListener(e -> new TransactionFrame().showTransactionFrame());
        logoutBtn.addActionListener(e -> {
            frame.dispose();
            new LoginFrame().showLogin();
        });

        frame.setVisible(true);
    }
}
