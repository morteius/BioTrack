import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main().showWelcomeScreen());
    }
    
    private void showWelcomeScreen() {
        JFrame frame = new JFrame("BioTrack");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 650);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 245, 250));
        frame.add(mainPanel);
        
        showWelcomeContent(mainPanel, frame);
        
        frame.setVisible(true);
    }
    
    private void showWelcomeContent(JPanel mainPanel, JFrame frame) {
        mainPanel.removeAll();
        mainPanel.setLayout(new BorderLayout());
        
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(240, 245, 250));
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 100, 10, 100);
        
        // Title 
        JLabel title = new JLabel("Welcome to BioTrack", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 42));
        title.setForeground(new Color(66, 133, 244));
        gbc.insets = new Insets(20, 100, 10, 100);
        centerPanel.add(title, gbc);
        
        // Subtitle
        JLabel subtitle = new JLabel("Laboratory Inventory Management System", SwingConstants.CENTER);
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        subtitle.setForeground(Color.DARK_GRAY);
        gbc.insets = new Insets(0, 100, 50, 100);
        centerPanel.add(subtitle, gbc);
        
        // Buttons panel
        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 0, 15));
        buttonPanel.setOpaque(false);
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(10, 150, 10, 150);
        
        JButton btnLogin = createButton("Log In", 250, 50);
        JButton btnAbout = createButton("About", 250, 50);
        JButton btnCredit = createButton("Credit", 250, 50);
        JButton btnExit = createButton("Exit", 250, 50);
        
        buttonPanel.add(btnLogin);
        buttonPanel.add(btnAbout);
        buttonPanel.add(btnCredit);
        buttonPanel.add(btnExit);
        
        centerPanel.add(buttonPanel, gbc);
        
        // Button actions
        btnLogin.addActionListener(e -> new LoginFrame(frame).showLogin());
        btnAbout.addActionListener(e -> showAboutContent(mainPanel, frame));
        btnCredit.addActionListener(e -> showCreditContent(mainPanel, frame));
        btnExit.addActionListener(e -> System.exit(0));
        
        mainPanel.revalidate();
        mainPanel.repaint();
    }
    
    private void showAboutContent(JPanel mainPanel, JFrame frame) {
        mainPanel.removeAll();
        mainPanel.setLayout(new BorderLayout());
        
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(240, 245, 250));
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        // Title - made bigger
        JLabel title = new JLabel("About BioTrack", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 42)); // Increased from 36 to 42
        title.setForeground(new Color(66, 133, 244));
        title.setBorder(BorderFactory.createEmptyBorder(40, 0, 30, 0));
        contentPanel.add(title, BorderLayout.NORTH);
        
        // About text
        JTextArea aboutText = new JTextArea();
        aboutText.setText("BioTrack is a Laboratory Inventory Management System " +
                         "designed to track equipment borrowing in academic and " +
                         "research laboratories.\n\n" +
                         "Features:\n" +
                         "• Track equipment availability\n" +
                         "• Manage user borrowings\n" +
                         "• Generate transaction reports\n" +
                         "• Monitor equipment status\n\n" +
                         "Version: 1.0.0");
        aboutText.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        aboutText.setBackground(new Color(240, 245, 250));
        aboutText.setEditable(false);
        aboutText.setLineWrap(true);
        aboutText.setWrapStyleWord(true);
        aboutText.setBorder(BorderFactory.createEmptyBorder(0, 50, 0, 50));
        
        JScrollPane scrollPane = new JScrollPane(aboutText);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(new Color(240, 245, 250));
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Back button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 40, 0));
        
        JButton backBtn = createButton("Back to Main", 200, 50);
        backBtn.addActionListener(e -> showWelcomeContent(mainPanel, frame));
        bottomPanel.add(backBtn);
        
        contentPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        mainPanel.revalidate();
        mainPanel.repaint();
    }
    
    private void showCreditContent(JPanel mainPanel, JFrame frame) {
        mainPanel.removeAll();
        mainPanel.setLayout(new BorderLayout());
        
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(new Color(240, 245, 250));
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(15, 0, 15, 0);
        
        // Title - made bigger
        JLabel title = new JLabel("Credits", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 42)); // Increased from 36 to 42
        title.setForeground(new Color(66, 133, 244));
        gbc.insets = new Insets(0, 0, 50, 0);
        contentPanel.add(title, gbc);
        
        gbc.insets = new Insets(15, 0, 15, 0);
        
        // Credits content
        JLabel credit1 = new JLabel(
            "<html><center>" +
            "<b>Developers:</b><br><br>" +
            "MAPE, C.J.<br>" +
            "BESIMO, M.<br>" +
            "POSTRADO, M.V.<br>" +
            "SECILLANO, L.H.<br>" +
            "SULPICO, S." +
            "</center></html>",
            SwingConstants.CENTER
        );
        credit1.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        contentPanel.add(credit1, gbc);
        
        JLabel credit2 = new JLabel("Language: Java", SwingConstants.CENTER);
        credit2.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        contentPanel.add(credit2, gbc);
        
        JLabel credit3 = new JLabel("Project: BioTrack Inventory System", SwingConstants.CENTER);
        credit3.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        contentPanel.add(credit3, gbc);
        
        // Back button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(50, 0, 50, 0));
        
        JButton backBtn = createButton("Back to Main", 200, 50);
        backBtn.addActionListener(e -> showWelcomeContent(mainPanel, frame));
        bottomPanel.add(backBtn);
        
        contentPanel.add(bottomPanel, gbc);
        
        mainPanel.revalidate();
        mainPanel.repaint();
    }
    
    private JButton createButton(String text, int width, int height) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(width, height));
        button.setFont(new Font("Segoe UI", Font.BOLD, 18)); // Changed to Segoe UI
        button.setBackground(new Color(66, 133, 244));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(50, 110, 200), 1),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(50, 110, 200));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(66, 133, 244));
            }
        });
        
        return button;
    }
}