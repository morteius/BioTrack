import java.awt.*;
import java.io.*;
import javax.swing.*;
import javax.swing.table.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TransactionFrame {
    private JFrame frame;
    private DefaultTableModel model;
    private List<TransactionData> transactionData;
    
    public void showTransactionFrame() {
        frame = new JFrame("Transaction History");
        frame.setSize(1400, 800);
        frame.setLocationRelativeTo(null);
        
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(new Color(240, 245, 250));
        
        // Top bar
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(66, 133, 244));
        topBar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        JLabel title = new JLabel("Transaction History");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(Color.WHITE);
        
        JButton backBtn = createHoverButton("← Back", Color.WHITE, new Color(220, 220, 220));
        backBtn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        backBtn.setForeground(new Color(66, 133, 244));
        backBtn.addActionListener(e -> {
            frame.dispose();
            new MainAppFrame(User.getCurrentUsername()).showMain();
        });
        
        topBar.add(title, BorderLayout.WEST);
        topBar.add(backBtn, BorderLayout.EAST);
        
        // Main content
        JPanel content = new JPanel(new BorderLayout());
        content.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        content.setOpaque(false);
        
        // Table
        String[] columns = {"Transaction ID", "Equipment ID", "Equipment Name", "Person", "Quantity", "Date & Time", "Action"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 4) { // Quantity column
                    return Integer.class;
                }
                return String.class;
            }
        };
        
        JTable table = new JTable(model);
        styleTable(table);
        
        // Add hover effects
        addTableHoverEffects(table);
        
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(220, 230, 240), 1));
        
        // Filter panel with hover effects
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setOpaque(false);
        
        JLabel filterLabel = new JLabel("Filter:");
        filterLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        JComboBox<String> filter = new JComboBox<>(new String[]{"All", "Borrow", "Return"});
        filter.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        filter.addActionListener(e -> applyFilter((String)filter.getSelectedItem()));
        
        JButton exportBtn = createHoverButton("Export CSV", new Color(66, 133, 244), new Color(53, 115, 220));
        exportBtn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        exportBtn.setForeground(Color.WHITE);
        
        exportBtn.addActionListener(e -> exportCSV());
        
        filterPanel.add(filterLabel);
        filterPanel.add(Box.createHorizontalStrut(10));
        filterPanel.add(filter);
        filterPanel.add(Box.createHorizontalStrut(20));
        filterPanel.add(exportBtn);
        
        content.add(scroll, BorderLayout.CENTER);
        content.add(filterPanel, BorderLayout.SOUTH);
        
        main.add(topBar, BorderLayout.NORTH);
        main.add(content, BorderLayout.CENTER);
        
        frame.add(main);
        loadData();
        frame.setVisible(true);
    }
    
    // Create button with hover effect
    private JButton createHoverButton(String text, Color normalColor, Color hoverColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setBackground(normalColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        // Add hover effect
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(hoverColor);
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(normalColor);
            }
        });
        
        return btn;
    }
    
    private void styleTable(JTable table) {
        // Set font - BIGGER and BOLDER
        table.setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.setRowHeight(40);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(44, 62, 80));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setReorderingAllowed(false);
        
        // Custom cell renderer with colors
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                
                Component c = super.getTableCellRendererComponent(table, value, 
                        isSelected, hasFocus, row, column);
                
                // Set bold font for all data
                setFont(getFont().deriveFont(Font.BOLD));
                
                // Set alignment
                if (column == 0 || column == 1 || column == 4 || column == 6) {
                    setHorizontalAlignment(SwingConstants.CENTER);
                } else {
                    setHorizontalAlignment(SwingConstants.LEFT);
                }
                
                // Set colors based on column
                if (column == 0) { // Transaction ID - Blue
                    c.setForeground(new Color(66, 133, 244));
                } else if (column == 1) { // Equipment ID - Dark Blue
                    c.setForeground(new Color(52, 152, 219));
                } else if (column == 2) { // Equipment Name - Black (bold)
                    c.setForeground(Color.BLACK);
                } else if (column == 3) { // Person - Dark Gray
                    c.setForeground(new Color(80, 90, 100));
                } else if (column == 4) { // Quantity - Orange
                    c.setForeground(new Color(251, 188, 5));
                } else if (column == 5) { // Date & Time - IMPORTANT COLOR (Purple)
                    c.setForeground(new Color(155, 89, 182));
                } else if (column == 6) { // Action column
                    String action = value.toString();
                    if (action.equalsIgnoreCase("Borrow")) {
                        c.setForeground(new Color(234, 67, 53)); // Red for Borrow
                    } else if (action.equalsIgnoreCase("Return")) {
                        c.setForeground(new Color(52, 168, 83)); // Green for Return
                    } else if (action.equalsIgnoreCase("Maintenance")) {
                        c.setForeground(new Color(155, 89, 182)); // Purple for Maintenance
                    } else {
                        c.setForeground(new Color(251, 188, 5)); // Yellow for others
                    }
                }
                
                // Background colors
                if (isSelected) {
                    c.setBackground(new Color(52, 152, 219)); // Blue selection
                    c.setForeground(Color.WHITE);
                } else if (row % 2 == 0) {
                    c.setBackground(new Color(250, 250, 250)); // Light gray
                } else {
                    c.setBackground(Color.WHITE);
                }
                
                // Add cell borders
                setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 1, new Color(221, 221, 221)),
                    BorderFactory.createEmptyBorder(5, 10, 5, 10)
                ));
                
                return c;
            }
        });
        
        // Set column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(140); // Transaction ID
        table.getColumnModel().getColumn(1).setPreferredWidth(100); // Equipment ID
        table.getColumnModel().getColumn(2).setPreferredWidth(200); // Equipment Name
        table.getColumnModel().getColumn(3).setPreferredWidth(150); // Person
        table.getColumnModel().getColumn(4).setPreferredWidth(80);  // Quantity
        table.getColumnModel().getColumn(5).setPreferredWidth(180); // Date & Time (wider)
        table.getColumnModel().getColumn(6).setPreferredWidth(100); // Action
    }
    
    private void addTableHoverEffects(JTable table) {
        // Add hover effect
        table.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            private int hoveredRow = -1;
            private int hoveredColumn = -1;
            
            @Override
            public void mouseMoved(java.awt.event.MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());
                
                // Change cursor to hand when over table
                if (row >= 0 && col >= 0) {
                    table.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                } else {
                    table.setCursor(Cursor.getDefaultCursor());
                }
                
                // Repaint to show hover effect
                if (row != hoveredRow || col != hoveredColumn) {
                    hoveredRow = row;
                    hoveredColumn = col;
                    table.repaint();
                }
            }
        });
        
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                table.setCursor(Cursor.getDefaultCursor());
                table.repaint();
            }
        });
    }
    
    // Data class for transactions
    private class TransactionData {
        String transactionId;
        String equipmentId;
        String equipmentName;
        String person;
        int quantity;
        String dateTime;
        String action;
        
        TransactionData(String[] parts) {
            this.transactionId = parts[0].trim();
            this.equipmentId = parts[1].trim();
            this.equipmentName = parts[2].trim();
            this.person = parts[3].trim();
            this.quantity = Integer.parseInt(parts[4].trim());
            this.dateTime = parts[5].trim();
            this.action = parts[6].trim();
        }
    }
    
    private void loadData() {
        model.setRowCount(0);
        transactionData = new ArrayList<>();
        
        try {
            File file = new File("data/transactions.txt");
            if (!file.exists()) {
                return; // No transactions yet
            }
            
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length >= 7) {
                        transactionData.add(new TransactionData(parts));
                    }
                }
            }
            
            // Sort transactions by date (most recent first)
            Collections.sort(transactionData, new Comparator<TransactionData>() {
                @Override
                public int compare(TransactionData t1, TransactionData t2) {
                    return t2.dateTime.compareTo(t1.dateTime); // Reverse order for most recent first
                }
            });
            
            // Add sorted data to table
            for (TransactionData td : transactionData) {
                model.addRow(new Object[]{
                    td.transactionId, td.equipmentId, td.equipmentName,
                    td.person, td.quantity, td.dateTime, td.action
                });
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "No transactions found", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void applyFilter(String filter) {
        loadData();
        if (!filter.equals("All")) {
            for (int i = model.getRowCount() - 1; i >= 0; i--) {
                String action = (String) model.getValueAt(i, 6); // Action is in column 6
                if (!action.equalsIgnoreCase(filter)) {
                    model.removeRow(i);
                }
            }
        }
    }
    
    private void exportCSV() {
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File("transactions.csv"));
        if (chooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
            try (PrintWriter pw = new PrintWriter(chooser.getSelectedFile())) {
                // Write headers
                for (int i = 0; i < model.getColumnCount(); i++) {
                    pw.print(model.getColumnName(i));
                    if (i < model.getColumnCount() - 1) pw.print(",");
                }
                pw.println();
                
                // Write data
                for (int i = 0; i < model.getRowCount(); i++) {
                    for (int j = 0; j < model.getColumnCount(); j++) {
                        pw.print(model.getValueAt(i, j));
                        if (j < model.getColumnCount() - 1) pw.print(",");
                    }
                    pw.println();
                }
                JOptionPane.showMessageDialog(frame, "Exported successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(frame, "Export failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}