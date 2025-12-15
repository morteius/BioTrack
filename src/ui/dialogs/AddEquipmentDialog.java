import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.io.*;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class AddEquipmentDialog extends JDialog {
    private Equipment newEquipment;
    private static Set<String> existingIds = new HashSet<>();
    
    public AddEquipmentDialog(JFrame parent) {
        super(parent, "Add Equipment", true);
        setSize(450, 500);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        
        // Load existing IDs
        loadExistingIds();
        
        // Header
        JPanel header = new JPanel();
        header.setBackground(new Color(66, 133, 244));
        header.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        
        JLabel title = new JLabel("Add New Equipment");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(Color.WHITE);
        header.add(title);
        
        // Form
        JPanel form = new JPanel(new GridLayout(6, 2, 10, 10));
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Name - ALLOW ALL CHARACTERS (including numbers and symbols)
        JLabel lblName = new JLabel("Name:*");
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JTextField txtName = createTextField();
        txtName.setDocument(new EquipmentNameDocument()); // Allow all characters
        
        // Category (with all options)
        JLabel lblCategory = new JLabel("Category:*");
        lblCategory.setFont(new Font("Segoe UI", Font.BOLD, 14));
        String[] categories = {
            "Glassware", "Measuring Instruments", "Heating Equipment",
            "Cooling Equipment", "Electronic Equipment", "General Tools",
            "Safety Equipment", "Cleaning & Sterilization", "Chemical Reagents",
            "Consumables", "Storage Containers", "Specimen/Testing Equipment",
            "Microscopy Equipment", "Power/Mechanical Tools", "Lab Furniture",
            "Analytical Instruments", "Biotechnology Equipment"
        };
        JComboBox<String> cmbCategory = new JComboBox<>(categories);
        cmbCategory.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        // Status
        JLabel lblStatus = new JLabel("Status:*");
        lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 14));
        String[] status = {"Available", "Maintenance", "Damaged"};
        JComboBox<String> cmbStatus = new JComboBox<>(status);
        cmbStatus.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        // Location - Allow all characters
        JLabel lblLocation = new JLabel("Location:*");
        lblLocation.setFont(new Font("Segoe UI", Font.BOLD, 14));
        String[] locations = {
            "Lab Room A", "Lab Room B", "Lab Room C", "Storage Room 1", 
            "Storage Room 2", "Chemistry Lab", "Biology Lab", "Physics Lab",
            "Main Lab", "Equipment Room", "Faculty Room", "Admin Office"
        };
        JComboBox<String> cmbLocation = new JComboBox<>(locations);
        cmbLocation.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmbLocation.setEditable(true);
        
        // QUANTITY - Numbers only
        JLabel lblQuantity = new JLabel("Quantity:*");
        lblQuantity.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JSpinner spnQuantity = new JSpinner(new SpinnerNumberModel(1, 1, 1000, 1));
        spnQuantity.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JSpinner.NumberEditor editor = new JSpinner.NumberEditor(spnQuantity, "#");
        spnQuantity.setEditor(editor);
        
        form.add(lblName); form.add(txtName);
        form.add(lblCategory); form.add(cmbCategory);
        form.add(lblStatus); form.add(cmbStatus);
        form.add(lblLocation); form.add(cmbLocation);
        form.add(lblQuantity); form.add(spnQuantity);
        
        // Example names for guidance
        JPanel examplePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        examplePanel.setBackground(Color.WHITE);
        JLabel example = new JLabel("Examples: Microscope X200, Centrifuge 5000RPM, Pipette Set (10ml)");
        example.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        example.setForeground(new Color(100, 100, 100));
        examplePanel.add(example);
        
        // Required fields note
        JPanel notePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        notePanel.setBackground(Color.WHITE);
        JLabel note = new JLabel("* Required fields");
        note.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        note.setForeground(Color.RED);
        notePanel.add(note);
        
        // Buttons
        JPanel buttons = new JPanel();
        buttons.setBackground(Color.WHITE);
        buttons.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        JButton btnAdd = createButton("➕ Add", new Color(66, 133, 244));
        JButton btnCancel = createButton("Cancel", new Color(120, 144, 156));
        
        btnAdd.addActionListener(e -> {
            String name = txtName.getText().trim();
            String location = cmbLocation.getSelectedItem().toString().trim();
            int quantity = (int) spnQuantity.getValue();
            
            // Validation
            StringBuilder errors = new StringBuilder();
            
            if (name.isEmpty()) {
                errors.append("• Equipment name is required\n");
            } else if (name.length() < 2) {
                errors.append("• Equipment name must be at least 2 characters\n");
            } else if (name.length() > 100) {
                errors.append("• Equipment name cannot exceed 100 characters\n");
            }
            
            if (location.isEmpty()) {
                errors.append("• Location is required\n");
            } else if (location.length() < 2) {
                errors.append("• Location must be at least 2 characters\n");
            }
            
            if (quantity <= 0) {
                errors.append("• Quantity must be greater than 0\n");
            } else if (quantity > 1000) {
                errors.append("• Quantity cannot exceed 1000\n");
            }
            
            if (errors.length() > 0) {
                JOptionPane.showMessageDialog(this, 
                    "Please fix the following errors:\n\n" + errors.toString(),
                    "Validation Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Generate 6-digit unique ID
            String id = generateUniqueId();
            
            // Format date as MM-dd-yyyy hh:mm a (e.g., 12-15-2024 02:30 PM)
            String date = LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("MM-dd-yyyy hh:mm a")
            );
            
            newEquipment = new Equipment(
                id,
                name,
                (String) cmbCategory.getSelectedItem(),
                (String) cmbStatus.getSelectedItem(),
                location,
                quantity,
                date
            );
            
            // Add to existing IDs set
            existingIds.add(id);
            
            // Save equipment to file
            if (saveEquipmentToFile(newEquipment)) {
                JOptionPane.showMessageDialog(this, 
                    "✅ Equipment added successfully!\n\n" +
                    "ID: " + id + "\n" +
                    "Name: " + name + "\n" +
                    "Category: " + cmbCategory.getSelectedItem() + "\n" +
                    "Quantity: " + quantity + "\n" +
                    "Date: " + date,
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
                dispose();
            }
        });
        
        btnCancel.addActionListener(e -> dispose());
        
        buttons.add(btnAdd);
        buttons.add(btnCancel);
        
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.setBackground(Color.WHITE);
        southPanel.add(examplePanel, BorderLayout.NORTH);
        southPanel.add(notePanel, BorderLayout.CENTER);
        southPanel.add(buttons, BorderLayout.SOUTH);
        
        add(header, BorderLayout.NORTH);
        add(form, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
    }
    
    // Custom document for equipment name - ALLOW ALL CHARACTERS
    private class EquipmentNameDocument extends PlainDocument {
        @Override
        public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
            if (str == null) return;
            
            String current = getText(0, getLength());
            String proposed = current.substring(0, offset) + str + current.substring(offset);
            
            // Allow all characters, just check length
            if (proposed.length() <= 100) {
                super.insertString(offset, str, attr);
            } else {
                Toolkit.getDefaultToolkit().beep();
            }
        }
    }
    
    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 180)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return field;
    }
    
    private JButton createButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
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
    
    // Generate 6-digit unique ID (EQ######)
    private String generateUniqueId() {
        String id;
        do {
            // Generate random 6-digit number
            int num = 100000 + (int)(Math.random() * 900000); // 100000 to 999999
            id = "EQ" + num;
        } while (existingIds.contains(id)); // Ensure uniqueness
        return id;
    }
    
    // Load existing IDs from file
    private void loadExistingIds() {
        try {
            File file = new File("data/equipments.txt");
            if (!file.exists()) return;
            
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length > 0) {
                        existingIds.add(parts[0].trim());
                    }
                }
            }
        } catch (IOException e) {
            // Silently fail - we'll generate new IDs anyway
        }
    }
    
    // Save equipment to file
    private boolean saveEquipmentToFile(Equipment equipment) {
        try {
            // Ensure data directory exists
            File dataDir = new File("data");
            if (!dataDir.exists()) {
                dataDir.mkdir();
            }
            
            // Append equipment to file
            try (BufferedWriter bw = new BufferedWriter(new FileWriter("data/equipments.txt", true))) {
                bw.write(equipment.toCSV());
                bw.newLine();
                return true;
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, 
                "❌ Error saving equipment:\n" + e.getMessage(), 
                "Save Error", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    public Equipment getNewEquipment() {
        return newEquipment;
    }
}