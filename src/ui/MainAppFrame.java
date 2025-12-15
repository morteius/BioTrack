import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MainAppFrame {
    private JFrame frame;
    private String username;
    private JPanel contentPanel;
    
    public MainAppFrame(String username) {
        this.username = username;
    }
    
    public void showMain() {
        frame = new JFrame("BioTrack - Equipment Management");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1400, 800);
        frame.setMinimumSize(new Dimension(1100, 650));
        frame.setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 245, 250));
        
        // Top Bar
        mainPanel.add(createTopBar(), BorderLayout.NORTH);
        
        // Sidebar
        mainPanel.add(createSidebar(), BorderLayout.WEST);
        
        // Main Content
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        contentPanel.setBackground(new Color(240, 245, 250));
        contentPanel.add(createDashboardContent());
        
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        frame.add(mainPanel);
        frame.setVisible(true);
    }
    
    private JPanel createTopBar() {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(66, 133, 244));
        topBar.setPreferredSize(new Dimension(frame.getWidth(), 60));
        topBar.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
        
        JLabel logo = new JLabel("🏥 BioTrack");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        logo.setForeground(Color.WHITE);
        
        JPanel userPanel = new JPanel();
        userPanel.setOpaque(false);
        
        JLabel welcomeLabel = new JLabel("Welcome, " + username + "!");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        welcomeLabel.setForeground(Color.WHITE);
        
        JButton btnLogout = createStyledButton("Logout", new Color(234, 67, 53));
        btnLogout.addActionListener(e -> {
            frame.dispose();
            new LoginFrame(null).showLogin();
        });
        
        userPanel.add(welcomeLabel);
        userPanel.add(Box.createHorizontalStrut(15));
        userPanel.add(btnLogout);
        
        topBar.add(logo, BorderLayout.WEST);
        topBar.add(userPanel, BorderLayout.EAST);
        
        return topBar;
    }
    
    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(44, 62, 80));
        sidebar.setPreferredSize(new Dimension(220, frame.getHeight()));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        String[] navItems = {"📊 Dashboard", "🔧 Equipment", "📋 Transactions", "📈 Reports"};
        for (int i = 0; i < navItems.length; i++) {
            JButton btn = createNavButton(navItems[i], i == 0);
            final int index = i;
            btn.addActionListener(e -> handleNavClick(index));
            sidebar.add(Box.createVerticalStrut(10));
            sidebar.add(btn);
        }
        
        sidebar.add(Box.createVerticalGlue());
        return sidebar;
    }
    
    private JButton createNavButton(String text, boolean active) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBackground(active ? new Color(52, 152, 219) : new Color(44, 62, 80));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(220, 50));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                if (!active) btn.setBackground(new Color(52, 152, 219));
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                if (!active) btn.setBackground(new Color(44, 62, 80));
            }
        });
        
        return btn;
    }
    
    private Component createDashboardContent() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        
        JLabel title = new JLabel("Dashboard Overview");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(50, 60, 70));
        
        JLabel dateLabel = new JLabel(LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy")));
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dateLabel.setForeground(new Color(120, 130, 140));
        
        headerPanel.add(title, BorderLayout.WEST);
        headerPanel.add(dateLabel, BorderLayout.EAST);
        
        // Stats Cards
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        statsPanel.setOpaque(false);
        statsPanel.setBorder(BorderFactory.createEmptyBorder(25, 0, 25, 0));
        
        int[] stats = getEquipmentStats();
        String[] statData = {
            "📦", "Total Equipment", String.valueOf(stats[0]), "#4285F4",
            "✅", "Available", String.valueOf(stats[1]), "#34A853",
            "📥", "Borrowed", String.valueOf(stats[2]), "#FBBC05",
            "🔧", "Maintenance", String.valueOf(stats[3]), "#9B59B6"
        };
        
        for (int i = 0; i < 4; i++) {
            statsPanel.add(createStatCard(
                statData[i*4], 
                statData[i*4 + 1], 
                statData[i*4 + 2], 
                Color.decode(statData[i*4 + 3])
            ));
        }
        
        // Recent Activities
        JPanel recentPanel = createRecentActivitiesPanel();
        
        // Quick Actions
        JPanel actionsPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        actionsPanel.setOpaque(false);
        actionsPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        
        String[] actionData = {
            "📥 Quick Borrow", "#4285F4",
            "📤 Quick Return", "#34A853",
            "➕ Add Equipment", "#9B59B6"
        };
        
        for (int i = 0; i < 3; i++) {
            JButton btn = createStyledButton(actionData[i*2], Color.decode(actionData[i*2 + 1]));
            final int action = i;
            btn.addActionListener(e -> handleQuickAction(action));
            actionsPanel.add(btn);
        }
        
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(statsPanel, BorderLayout.CENTER);
        panel.add(recentPanel, BorderLayout.SOUTH);
        
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(panel, BorderLayout.CENTER);
        wrapper.add(actionsPanel, BorderLayout.SOUTH);
        
        return wrapper;
    }
    
    private JPanel createStatCard(String icon, String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(220, 230, 240), 1));
        card.setPreferredSize(new Dimension(200, 120));
        
        JPanel content = new JPanel(new BorderLayout());
        content.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        content.setOpaque(false);
        
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 28));
        iconLabel.setForeground(color);
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        valueLabel.setForeground(color);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        titleLabel.setForeground(new Color(100, 110, 120));
        
        content.add(iconLabel, BorderLayout.NORTH);
        content.add(valueLabel, BorderLayout.CENTER);
        content.add(titleLabel, BorderLayout.SOUTH);
        
        card.add(content, BorderLayout.CENTER);
        return card;
    }
    
    private JPanel createRecentActivitiesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 230, 240), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel title = new JLabel("Recent Activities");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(new Color(50, 60, 70));
        
        String[] columns = {"TIME", "ACTION", "EQUIPMENT", "USER", "QTY"};
        Object[][] data = getRecentActivities();
        
        JTable table = new JTable(data, columns);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(32);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(44, 62, 80));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setShowGrid(false);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 230, 240), 1));
        
        panel.add(title, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JButton createStyledButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(color.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(color);
            }
        });
        
        return btn;
    }
    
    private void handleNavClick(int index) {
        switch (index) {
            case 0:
                contentPanel.removeAll();
                contentPanel.add(createDashboardContent());
                contentPanel.revalidate();
                contentPanel.repaint();
                break;
            case 1:
                frame.dispose();
                new EquipmentFrame().showEquipmentFrame();
                break;
            case 2:
                frame.dispose();
                new TransactionFrame().showTransactionFrame();
                break;
            case 3:
                showReportsDialog();
                break;
        }
    }
    
    private void handleQuickAction(int action) {
        switch (action) {
            case 0: // Borrow
            case 1: // Return
                frame.dispose();
                new EquipmentFrame().showEquipmentFrame();
                break;
            case 2: // Add Equipment
                AddEquipmentDialog dialog = new AddEquipmentDialog(frame);
                dialog.setVisible(true);
                if (dialog.getNewEquipment() != null) {
                    contentPanel.removeAll();
                    contentPanel.add(createDashboardContent());
                    contentPanel.revalidate();
                    contentPanel.repaint();
                }
                break;
        }
    }
    
    private int[] getEquipmentStats() {
        int[] stats = {0, 0, 0, 0};
        
        try (BufferedReader br = new BufferedReader(new FileReader("data/equipments.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 6) {
                    stats[0]++;
                    String status = parts[3].trim().toLowerCase();
                    if (status.contains("available")) stats[1]++;
                    else if (status.contains("borrowed")) stats[2]++;
                    else if (status.contains("maintenance")) stats[3]++;
                }
            }
        } catch (Exception e) {
            // Use default values
        }
        
        return stats;
    }
    
    private Object[][] getRecentActivities() {
        List<Object[]> activities = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader("data/transactions.txt"))) {
            List<String> lines = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) lines.add(line);
            
            int start = Math.max(0, lines.size() - 5);
            for (int i = start; i < lines.size(); i++) {
                String[] parts = lines.get(i).split(",");
                if (parts.length >= 9) {
                    activities.add(new Object[]{
                        parts[6].trim(),
                        parts[7].trim(),
                        parts[4].trim(),
                        parts[1].trim(),
                        parts[8].trim()
                    });
                }
            }
        } catch (Exception e) {
            activities.add(new Object[]{"No activities", "-", "-", "-", "-"});
        }
        
        return activities.toArray(new Object[0][]);
    }
    
    private void showReportsDialog() {
        JDialog dialog = new JDialog(frame, "Generate Reports", true);
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(frame);
        
        JPanel content = new JPanel(new BorderLayout());
        content.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel title = new JLabel("Generate Report", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(new Color(66, 133, 244));
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        JPanel form = new JPanel(new GridLayout(3, 2, 10, 15));
        form.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));
        
        String[] labels = {"Report Type:", "Date Range:", "Format:"};
        String[][] options = {
            {"Equipment", "Transactions", "Maintenance"},
            {"Last 7 Days", "Last 30 Days", "All Time"},
            {"PDF", "CSV", "HTML"}
        };
        
        JComboBox<String>[] combos = new JComboBox[3];
        for (int i = 0; i < 3; i++) {
            JLabel label = new JLabel(labels[i]);
            label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            combos[i] = new JComboBox<>(options[i]);
            combos[i].setFont(new Font("Segoe UI", Font.PLAIN, 14));
            form.add(label);
            form.add(combos[i]);
        }
        
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        JButton generate = createStyledButton("Generate Report", new Color(66, 133, 244));
        JButton cancel = createStyledButton("Cancel", new Color(150, 150, 150));
        
        generate.addActionListener(e -> {
            JOptionPane.showMessageDialog(dialog, "Report generated successfully!", 
                "Success", JOptionPane.INFORMATION_MESSAGE);
            dialog.dispose();
        });
        cancel.addActionListener(e -> dialog.dispose());
        
        buttons.add(generate);
        buttons.add(cancel);
        
        content.add(title, BorderLayout.NORTH);
        content.add(form, BorderLayout.CENTER);
        content.add(buttons, BorderLayout.SOUTH);
        
        dialog.add(content);
        dialog.setVisible(true);
    }
}