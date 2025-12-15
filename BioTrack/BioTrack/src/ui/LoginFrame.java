import java.awt.*;
import javax.swing.*;

public class LoginFrame {
    private final Authentication auth = new Authentication();
    private JFrame parentFrame;
    
    public LoginFrame(JFrame parentFrame) {
        this.parentFrame = parentFrame;
    }
    
    public void showLogin() {
        JFrame frame = new JFrame("BioTrack Login");
        frame.setSize(500, 600);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        JPanel main = new JPanel(new GridBagLayout());
        main.setBackground(new Color(240, 245, 250));
        frame.add(main);
        
        JPanel card = new JPanel(new GridBagLayout());
        card.setPreferredSize(new Dimension(380, 450));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(210, 210, 210), 1));
        main.add(card);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 40, 10, 40);
        
        // Title
        JLabel title = new JLabel("Login", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(66, 133, 244));
        gbc.insets = new Insets(30, 0, 30, 0);
        card.add(title, gbc);
        
        // Username field with placeholder
        gbc.insets = new Insets(10, 40, 10, 40);
        PlaceholderTextField txtUser = new PlaceholderTextField("Username", 260, 40);
        txtUser.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtUser.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        card.add(txtUser, gbc);
        
        // Password field with placeholder
        PlaceholderPasswordField txtPass = new PlaceholderPasswordField("Password", 260, 40);
        txtPass.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPass.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        txtPass.setEchoChar('•');
        card.add(txtPass, gbc);
        
        // Error label
        JLabel errorLabel = new JLabel(" ", SwingConstants.CENTER);
        errorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        errorLabel.setForeground(Color.RED);
        gbc.insets = new Insets(5, 40, 5, 40);
        card.add(errorLabel, gbc);
        
        // Login button
        JButton btnLogin = new JButton("LOGIN");
        btnLogin.setPreferredSize(new Dimension(260, 40));
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setBackground(new Color(66, 133, 244));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        gbc.insets = new Insets(20, 40, 20, 40);
        card.add(btnLogin, gbc);
        
        btnLogin.addActionListener(e -> {
            String user = txtUser.getRealText().trim();
            String pass = txtPass.getRealText().trim();
            
            User login = auth.validateLogin(user, pass);
            
            if (login == null) {
                errorLabel.setText("Invalid username or password");
            } else {
                User.setCurrentUser(login);
                if (parentFrame != null) parentFrame.dispose();
                frame.dispose();
                new MainAppFrame(login.getUsername()).showMain();
            }
        });
        
        frame.setVisible(true);
    }
    
    // Placeholder Text Field
    static class PlaceholderTextField extends JTextField {
        private final String placeholder;
        
        public PlaceholderTextField(String placeholder, int w, int h) {
            this.placeholder = placeholder;
            setPreferredSize(new Dimension(w, h));
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (getText().isEmpty() && !isFocusOwner()) {
                g.setColor(Color.GRAY);
                g.setFont(getFont().deriveFont(Font.ITALIC));
                Insets ins = getInsets();
                g.drawString(placeholder, ins.left + 8, getHeight() / 2 + getFont().getSize() / 2 - 2);
            }
        }
        
        public String getRealText() {
            return getText();
        }
    }
    
    // Placeholder Password Field
    static class PlaceholderPasswordField extends JPasswordField {
        private final String placeholder;
        
        public PlaceholderPasswordField(String placeholder, int w, int h) {
            this.placeholder = placeholder;
            setPreferredSize(new Dimension(w, h));
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (getPassword().length == 0 && !isFocusOwner()) {
                g.setColor(Color.GRAY);
                g.setFont(getFont().deriveFont(Font.ITALIC));
                Insets ins = getInsets();
                g.drawString(placeholder, ins.left + 8, getHeight() / 2 + getFont().getSize() / 2 - 2);
            }
        }
        
        public String getRealText() {
            return new String(getPassword());
        }
    }
}