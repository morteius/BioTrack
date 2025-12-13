import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class LoginFrame {

    private final Authentication auth;   

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame().showLogin());
    }

    public LoginFrame() {
        auth = new Authentication();
    }

    private ImageIcon scaled(String path, int w, int h) {
        ImageIcon icon = new ImageIcon(path);
        Image img = icon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    public void showLogin() {

        JFrame frame = new JFrame("BioTrack");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 480);
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);

        JPanel card = new JPanel(null);
        card.setBounds(30, 30, 340, 400);
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 2));
        frame.add(card);

        // Logo
        JLabel topIcon = new JLabel(scaled("BioTrack/src/logo.jpg", 80, 80));
        topIcon.setBounds(130, 20, 80, 80);
        card.add(topIcon);

        JLabel lblLogin = new JLabel("Login", SwingConstants.CENTER);
        lblLogin.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblLogin.setBounds(0, 95, 340, 30);
        card.add(lblLogin);

        // Username
        JLabel iconUser = new JLabel(scaled("BioTrack/src/username.png", 25, 25));
        iconUser.setBounds(40, 150, 30, 30);
        card.add(iconUser);

        JTextField txtUser = new JTextField("Username");
        txtUser.setBounds(80, 150, 200, 30);
        txtUser.setBorder(null);
        txtUser.setForeground(Color.GRAY);

        txtUser.addFocusListener(new FocusAdapter() {
            @SuppressWarnings("override")
            public void focusGained(FocusEvent e) {
                if (txtUser.getText().equals("Username")) {
                    txtUser.setText("");
                    txtUser.setForeground(Color.BLACK);
                }
            }
            @SuppressWarnings("override")
            public void focusLost(FocusEvent e) {
                if (txtUser.getText().trim().isEmpty()) {
                    txtUser.setText("Username");
                    txtUser.setForeground(Color.GRAY);
                }
            }
        });
        card.add(txtUser);

        JLabel line1 = new JLabel("______________________________");
        line1.setForeground(Color.LIGHT_GRAY);
        line1.setBounds(80, 170, 200, 20);
        card.add(line1);

        // Password
        JLabel iconPass = new JLabel(scaled("BioTrack/src/password.png", 25, 25));
        iconPass.setBounds(40, 210, 30, 30);
        card.add(iconPass);

        JPasswordField txtPass = new JPasswordField("Password");
        txtPass.setBounds(80, 210, 200, 30);
        txtPass.setBorder(null);
        txtPass.setForeground(Color.GRAY);
        txtPass.setEchoChar((char) 0);

        txtPass.addFocusListener(new FocusAdapter() {
            @SuppressWarnings("override")
            public void focusGained(FocusEvent e) {
                String pass = new String(txtPass.getPassword());
                if (pass.equals("Password")) {
                    txtPass.setText("");
                    txtPass.setForeground(Color.BLACK);
                    txtPass.setEchoChar('•');
                }
            }

            @SuppressWarnings("override")
            public void focusLost(FocusEvent e) {
                String pass = new String(txtPass.getPassword()).trim();
                if (pass.isEmpty()) {
                    txtPass.setText("Password");
                    txtPass.setForeground(Color.GRAY);
                    txtPass.setEchoChar((char) 0);
                }
            }
        });
        card.add(txtPass);

        JLabel line2 = new JLabel("______________________________");
        line2.setForeground(Color.LIGHT_GRAY);
        line2.setBounds(80, 230, 200, 20);
        card.add(line2);

        // Login Button
        JButton btnLogin = new JButton("LOGIN");
        btnLogin.setBounds(90, 300, 160, 40);
        btnLogin.setBackground(new Color(66, 133, 244));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnLogin.setFocusPainted(false);
        card.add(btnLogin);

        // FINAL WORKING ACTION LISTENER
        btnLogin.addActionListener(e -> {
            String user = txtUser.getText();
            String pass = String.valueOf(txtPass.getPassword());

            if (user.equals("Username")) user = "";
            if (pass.equals("Password")) pass = "";

            User login = auth.validateLogin(user, pass);

            if (login != null) {
                JOptionPane.showMessageDialog(frame, "Welcome " + login.getUsername() + "!");
                frame.dispose();
                new MainAppFrame(login.getUsername()).showMain();
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid credentials!");
            }
        });

        frame.setVisible(true);
    }
}
