import java.awt.*;
import java.io.*;
import javax.swing.*;
import javax.swing.table.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EquipmentFrame {
    private JFrame frame;
    private DefaultTableModel model;
    private List<Equipment> data;
    private Set<String> existingTransactionIds = new HashSet<>();
    
    public void showEquipmentFrame() {
        frame = new JFrame("Equipment Management");
        frame.setSize(1400, 800);
        frame.setLocationRelativeTo(null);
        
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(new Color(240, 245, 250));
        
        // Top bar
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(66, 133, 244));
        topBar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        JLabel title = new JLabel("Equipment Management");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(Color.WHITE);
        
        JButton backBtn = createHoverButton("← Back", Color.WHITE, new Color(66, 133, 244));
        backBtn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        backBtn.setForeground(new Color(66, 133, 244));
        backBtn.addActionListener(e -> {
            frame.dispose();
            new MainAppFrame(User.getCurrentUsername()).showMain();
        });
        
        topBar.add(title, BorderLayout.WEST);
        topBar.add(backBtn, BorderLayout.EAST);
        
        // Main content
        JPanel content = new JPanel(new BorderLayout(15, 15));
        content.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        content.setOpaque(false);
        
        // Table with custom renderer for colors
        String[] columns = {"ID", "Name", "Category", "Status", "Location", "Qty", "Available", "Last Maintenance"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells non-editable
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                // Sort columns properly
                if (columnIndex == 5 || columnIndex == 6) {
                    return Integer.class;
                }
                return String.class;
            }
        };
        
        JTable table = new JTable(model);
        styleTable(table);
        
        // Set custom renderer for Status column (column 3)
        table.getColumnModel().getColumn(3).setCellRenderer(new StatusCellRenderer());
        
        // Add table sorter
        table.setAutoCreateRowSorter(true);
        table.getRowSorter().toggleSortOrder(1); // Sort by Name column initially
        
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(220, 230, 240), 1));
        
        // Buttons panel with hover effects
        JPanel buttons = new JPanel(new GridLayout(7, 1, 0, 10));
        buttons.setOpaque(false);
        buttons.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        
        Color[] btnColors = {
            new Color(66, 133, 244), new Color(52, 168, 83),
            new Color(251, 188, 5), new Color(234, 67, 53),
            new Color(155, 89, 182), new Color(192, 57, 43),
            new Color(120, 144, 156)
        };
        
        String[] btnTexts = {
            "➕ Add", "✏️ Edit", "📥 Borrow", "📤 Return",
            "🔧 Maintenance", "🗑️ Remove", "🔄 Refresh"
        };
        
        for (int i = 0; i < 7; i++) {
            JButton btn = createHoverButton(btnTexts[i], btnColors[i], btnColors[i].darker());
            final int index = i;
            btn.addActionListener(e -> handleButtonClick(index, table));
            buttons.add(btn);
        }
        
        content.add(scroll, BorderLayout.CENTER);
        content.add(buttons, BorderLayout.EAST);
        
        main.add(topBar, BorderLayout.NORTH);
        main.add(content, BorderLayout.CENTER);
        
        frame.add(main);
        loadData();
        frame.setVisible(true);
    }
    
    // Custom cell renderer for Status column
    private class StatusCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, 
                    isSelected, hasFocus, row, column);
            
            String status = value.toString();
            
            // Set colors based on status
            if (status.equalsIgnoreCase("Available")) {
                c.setForeground(new Color(52, 168, 83)); // Green
                c.setFont(c.getFont().deriveFont(Font.BOLD));
            } else if (status.equalsIgnoreCase("Borrowed")) {
                c.setForeground(new Color(234, 67, 53)); // Red
                c.setFont(c.getFont().deriveFont(Font.BOLD));
            } else if (status.equalsIgnoreCase("Maintenance")) {
                c.setForeground(new Color(155, 89, 182)); // Purple
                c.setFont(c.getFont().deriveFont(Font.BOLD));
            } else if (status.equalsIgnoreCase("Partially Available")) {
                c.setForeground(new Color(251, 188, 5)); // Yellow/Orange
                c.setFont(c.getFont().deriveFont(Font.BOLD));
            } else if (status.equalsIgnoreCase("Damaged")) {
                c.setForeground(new Color(192, 57, 43)); // Dark Red
                c.setFont(c.getFont().deriveFont(Font.BOLD));
            } else {
                c.setForeground(Color.BLACK);
            }
            
            // Center align the text
            setHorizontalAlignment(SwingConstants.CENTER);
            
            return c;
        }
    }
    
    // Create button with hover effect
    private JButton createHoverButton(String text, Color normalColor, Color hoverColor) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                if (getModel().isRollover()) {
                    setBackground(hoverColor);
                } else {
                    setBackground(normalColor);
                }
                super.paintComponent(g);
            }
        };
        
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setBackground(normalColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
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
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Increased font size
        table.setRowHeight(40); // Increased row height for better readability
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(44, 62, 80));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setReorderingAllowed(false);
        
        // Additional protection against editing
        table.setDefaultEditor(Object.class, null);
        
        // Center align all columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Set column widths for better readability
        table.getColumnModel().getColumn(0).setPreferredWidth(100); // ID
        table.getColumnModel().getColumn(1).setPreferredWidth(200); // Name (wider)
        table.getColumnModel().getColumn(2).setPreferredWidth(180); // Category
        table.getColumnModel().getColumn(3).setPreferredWidth(120); // Status
        table.getColumnModel().getColumn(4).setPreferredWidth(150); // Location
        table.getColumnModel().getColumn(5).setPreferredWidth(70);  // Qty
        table.getColumnModel().getColumn(6).setPreferredWidth(90);  // Available
        table.getColumnModel().getColumn(7).setPreferredWidth(180); // Last Maintenance
        
        // Center align specific columns
        for (int i = 0; i < table.getColumnCount(); i++) {
            if (i != 1 && i != 2 && i != 4) { // Name, Category, Location remain left-aligned
                table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
        }
    }
    
    private void loadData() {
        model.setRowCount(0);
        data = new ArrayList<>();
        
        try {
            File file = new File("data/equipments.txt");
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
                return;
            }
            
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    Equipment eq;
                    
                    if (parts.length >= 8) {
                        // New format with availableQuantity
                        eq = new Equipment(
                            parts[0].trim(), parts[1].trim(), parts[2].trim(),
                            parts[3].trim(), parts[4].trim(), 
                            Integer.parseInt(parts[5].trim()),
                            Integer.parseInt(parts[6].trim()),
                            parts[7].trim()
                        );
                    } else if (parts.length >= 7) {
                        // Old format without availableQuantity
                        eq = new Equipment(
                            parts[0].trim(), parts[1].trim(), parts[2].trim(),
                            parts[3].trim(), parts[4].trim(), 
                            Integer.parseInt(parts[5].trim()),
                            parts[6].trim()
                        );
                    } else {
                        continue; // Skip invalid lines
                    }
                    
                    data.add(eq);
                }
            }
            
            // Sort equipment alphabetically by name
            Collections.sort(data, new Comparator<Equipment>() {
                @Override
                public int compare(Equipment e1, Equipment e2) {
                    return e1.getName().compareToIgnoreCase(e2.getName());
                }
            });
            
            // Add sorted data to table
            for (Equipment eq : data) {
                model.addRow(new Object[]{
                    eq.getId(), eq.getName(), eq.getCategory(),
                    eq.getStatus(), eq.getLocation(), eq.getQuantity(),
                    eq.getAvailableQuantity(), eq.getLastMaintenance()
                });
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Error loading data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void handleButtonClick(int action, JTable table) {
        int row = table.getSelectedRow();
        if (row < 0 && action != 0 && action != 6) {
            JOptionPane.showMessageDialog(frame, "Please select equipment first", "Info", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        switch (action) {
            case 0: // Add
                AddEquipmentDialog dialog = new AddEquipmentDialog(frame);
                dialog.setVisible(true);
                loadData(); // Refresh after adding
                break;
                
            case 1: // Edit
                Equipment selectedEq = data.get(row);
                EditEquipmentDialog editDialog = new EditEquipmentDialog(frame, selectedEq);
                editDialog.setVisible(true);
                saveAllEquipment();
                loadData();
                break;
                
            case 2: // Borrow
                Equipment borrowEq = data.get(row);
                if (borrowEq.getAvailableQuantity() <= 0) {
                    JOptionPane.showMessageDialog(frame, "No items available to borrow", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                BorrowDialog borrowDialog = new BorrowDialog(frame, borrowEq);
                borrowDialog.setVisible(true);
                if (borrowDialog.isConfirmed()) {
                    int quantity = borrowDialog.getQuantity();
                    borrowEq.setAvailableQuantity(borrowEq.getAvailableQuantity() - quantity);
                    saveAllEquipment();
                    loadData();
                    
                    // Create transaction record
                    createTransaction(borrowEq.getId(), borrowEq.getName(), 
                                    borrowDialog.getBorrowerName(), quantity, "Borrow");
                    JOptionPane.showMessageDialog(frame, "Successfully borrowed " + quantity + " items");
                }
                break;
                
            case 3: // Return
                Equipment returnEq = data.get(row);
                int borrowedCount = returnEq.getQuantity() - returnEq.getAvailableQuantity();
                if (borrowedCount <= 0) {
                    JOptionPane.showMessageDialog(frame, "No items to return", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                ReturnDialog returnDialog = new ReturnDialog(frame, returnEq);
                returnDialog.setVisible(true);
                if (returnDialog.isConfirmed()) {
                    int quantity = returnDialog.getQuantity();
                    returnEq.setAvailableQuantity(returnEq.getAvailableQuantity() + quantity);
                    saveAllEquipment();
                    loadData();
                    
                    // Create transaction record
                    createTransaction(returnEq.getId(), returnEq.getName(), 
                                    returnDialog.getReturnerName(), quantity, "Return");
                    JOptionPane.showMessageDialog(frame, "Successfully returned " + quantity + " items");
                }
                break;
                
            case 4: // Maintenance
                if (row >= 0) {
                    Equipment eq = data.get(row);
                    String[] options = {"Mark as Maintenance", "Mark as Available", "Mark as Damaged", "Cancel"};
                    int choice = JOptionPane.showOptionDialog(frame,
                        "Select status for " + eq.getName(),
                        "Change Status",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        options,
                        options[0]);
                    
                    if (choice == 0) {
                        eq.setStatus("Maintenance");
                        saveAllEquipment();
                        loadData();
                        JOptionPane.showMessageDialog(frame, "Equipment marked for maintenance");
                    } else if (choice == 1) {
                        eq.setStatus("Available");
                        saveAllEquipment();
                        loadData();
                        JOptionPane.showMessageDialog(frame, "Equipment marked as available");
                    } else if (choice == 2) {
                        eq.setStatus("Damaged");
                        saveAllEquipment();
                        loadData();
                        JOptionPane.showMessageDialog(frame, "Equipment marked as damaged");
                    }
                }
                break;
                
            case 5: // Remove
                if (row >= 0) {
                    int confirm = JOptionPane.showConfirmDialog(frame,
                        "Are you sure you want to remove this equipment?",
                        "Confirm Removal",
                        JOptionPane.YES_NO_OPTION);
                    
                    if (confirm == JOptionPane.YES_OPTION) {
                        data.remove(row);
                        saveAllEquipment();
                        loadData();
                        JOptionPane.showMessageDialog(frame, "Equipment removed successfully");
                    }
                }
                break;
                
            case 6: // Refresh
                loadData();
                break;
        }
    }
    
    private void saveAllEquipment() {
        // Sort before saving
        Collections.sort(data, new Comparator<Equipment>() {
            @Override
            public int compare(Equipment e1, Equipment e2) {
                return e1.getName().compareToIgnoreCase(e2.getName());
            }
        });
        
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("data/equipments.txt"))) {
            for (Equipment eq : data) {
                // Save with new format including availableQuantity
                bw.write(eq.getId() + "," +
                        eq.getName() + "," +
                        eq.getCategory() + "," +
                        eq.getStatus() + "," +
                        eq.getLocation() + "," +
                        eq.getQuantity() + "," +
                        eq.getAvailableQuantity() + "," +
                        eq.getLastMaintenance());
                bw.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error saving data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void createTransaction(String equipmentId, String equipmentName, String person, int quantity, String action) {
        try {
            File dataDir = new File("data");
            if (!dataDir.exists()) {
                dataDir.mkdir();
            }
            
            try (BufferedWriter bw = new BufferedWriter(new FileWriter("data/transactions.txt", true))) {
                // Generate 6-digit transaction ID
                String transactionId = generateTransactionId();
                
                // Format date as MM-dd-yyyy hh:mm a
                String date = LocalDateTime.now().format(
                    DateTimeFormatter.ofPattern("MM-dd-yyyy hh:mm a")
                );
                
                bw.write(transactionId + "," +
                        equipmentId + "," +
                        equipmentName + "," +
                        person + "," +
                        quantity + "," +
                        date + "," +
                        action);
                bw.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error recording transaction: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Generate 6-digit transaction ID
    private String generateTransactionId() {
        // Load existing transaction IDs
        loadExistingTransactionIds();
        
        String id;
        do {
            // Generate random 6-digit number
            int num = 100000 + (int)(Math.random() * 900000); // 100000 to 999999
            id = "TXN" + num;
        } while (existingTransactionIds.contains(id)); // Ensure uniqueness
        
        existingTransactionIds.add(id);
        return id;
    }
    
    // Load existing transaction IDs from file
    private void loadExistingTransactionIds() {
        try {
            File file = new File("data/transactions.txt");
            if (!file.exists()) return;
            
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length > 0) {
                        existingTransactionIds.add(parts[0].trim());
                    }
                }
            }
        } catch (IOException e) {
            // Silently fail
        }
    }
}