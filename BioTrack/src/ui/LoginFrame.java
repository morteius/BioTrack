import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginFrame {

    private final Authentication auth;   
    private boolean passVisible = false; // tracks password visibility

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new BioTrack().showLogin();
            }
        });
    }

    public BioTrack() {
        auth = new Authentication();
    }

    // Load & Scale Icon
    private ImageIcon scaled(String path, int w, int h) {
        ImageIcon icon = new ImageIcon(path);
        Image img = icon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    private void showLogin() {
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
        JLabel topIcon = new JLabel(scaled("src/logo.jpg", 80, 80));
        topIcon.setBounds(130, 20, 80, 80);
        card.add(topIcon);

        JLabel lblLogin = new JLabel("Login", SwingConstants.CENTER);
        lblLogin.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblLogin.setBounds(0, 95, 340, 30);
        card.add(lblLogin);

        // Username
        JLabel iconUser = new JLabel(scaled("src/username.png", 25, 25));
        iconUser.setBounds(40, 150, 30, 30);
        card.add(iconUser);

        JTextField txtUser = new JTextField();
        txtUser.setBounds(80, 150, 200, 30);
        txtUser.setBorder(null);
        txtUser.setFont(new Font("SansSerif", Font.PLAIN, 13));
        txtUser.setForeground(Color.GRAY);
        txtUser.setText("Username");

        // Placeholder for Username
        txtUser.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (txtUser.getText().equals("Username")) {
                    txtUser.setText("");
                    txtUser.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (txtUser.getText().trim().isEmpty()) {
                    txtUser.setText("Username");
                    txtUser.setForeground(Color.GRAY);
                }
            }
        });
        card.add(txtUser);

        JLabel line1 = new JLabel("______________________________________");
        line1.setBounds(80, 170, 200, 20);
        line1.setForeground(Color.LIGHT_GRAY);
        card.add(line1);

        // Password
        JLabel iconPass = new JLabel(scaled("src/password.png", 25, 25));
        iconPass.setBounds(40, 210, 30, 30);
        card.add(iconPass);

        JPasswordField txtPass = new JPasswordField();
        txtPass.setBounds(80, 210, 200, 30);
        txtPass.setBorder(null);
        txtPass.setFont(new Font("SansSerif", Font.PLAIN, 13));
        txtPass.setForeground(Color.GRAY);
        txtPass.setEchoChar((char) 0); // show placeholder
        txtPass.setText("Password");

        // Placeholder for password
        txtPass.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                String current = String.valueOf(txtPass.getPassword());
                if (current.equals("Password")) {

        txtPass.setText("");
                    txtPass.setEchoChar('•');
                    txtPass.setForeground(Color.BLACK);
                    passVisible = false;
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                String current = String.valueOf(txtPass.getPassword()).trim();
                if (current.isEmpty()) {
                    txtPass.setText("Password");
                    txtPass.setEchoChar((char) 0);
                    txtPass.setForeground(Color.GRAY);
                    passVisible = false;
                }
            }
        });
        card.add(txtPass);

        JLabel line2 = new JLabel("______________________________________");
        line2.setBounds(80, 230, 200, 20);
        line2.setForeground(Color.LIGHT_GRAY);
        card.add(line2);

        // Login Button
        JButton btnLogin = new JButton("LOGIN");
        btnLogin.setBounds(90, 300, 160, 40);
        btnLogin.setBackground(new Color(66, 133, 244));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnLogin.setFocusPainted(false);
        card.add(btnLogin);

        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String user = txtUser.getText();
                String pass = String.valueOf(txtPass.getPassword());

                // Ignore placeholders
                if ("Username".equals(user)) {
                    user = "";
                }
                if ("Password".equals(pass)) {
                    pass = "";
                }

                User login = auth.validateLogin(user, pass);

                if (login != null) {
                    JOptionPane.showMessageDialog(frame, "Welcome " + login.getUsername() + "!");
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid credentials!");
                }
            }
        });

        frame.setVisible(true);
    }
}
