import java.awt.*;
import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.util.regex.Pattern;

public class EditEquipmentDialog extends JDialog {
    private Equipment equipment;
    
    public EditEquipmentDialog(JFrame parent, Equipment equipment) {
        super(parent, "Edit Equipment", true);
        this.equipment = equipment;
        setSize(400, 350);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        
        // Header
        JPanel header = new JPanel();
        header.setBackground(new Color(155, 89, 182));
        header.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        
        JLabel title = new JLabel("Edit Equipment");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(Color.WHITE);
        header.add(title);
        
        // Form
        JPanel form = new JPanel(new GridLayout(4, 2, 10, 10));
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel lblName = new JLabel("Name:*");
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JTextField txtName = new JTextField(equipment.getName());
        txtName.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtName.setDocument(new NameDocument()); // Name validation
        
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
        cmbCategory.setSelectedItem(equipment.getCategory());
        
        JLabel lblStatus = new JLabel("Status:*");
        lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 14));
        String[] statusOptions = {"Available", "Borrowed", "Maintenance", "Damaged", "Partially Available"};
        JComboBox<String> cmbStatus = new JComboBox<>(statusOptions);
        cmbStatus.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmbStatus.setSelectedItem(equipment.getStatus());
        
        JLabel lblLocation = new JLabel("Location:*");
        lblLocation.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JTextField txtLocation = new JTextField(equipment.getLocation());
        txtLocation.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        form.add(lblName); form.add(txtName);
        form.add(lblCategory); form.add(cmbCategory);
        form.add(lblStatus); form.add(cmbStatus);
        form.add(lblLocation); form.add(txtLocation);
        
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
        
        JButton btnSave = createButton("Save", new Color(66, 133, 244));
        JButton btnCancel = createButton("Cancel", new Color(120, 144, 156));
        
        btnSave.addActionListener(e -> {
            String name = txtName.getText().trim();
            String location = txtLocation.getText().trim();
            String selectedStatus = (String) cmbStatus.getSelectedItem(); // Fixed variable name
            
            // Validation
            StringBuilder errors = new StringBuilder();
            
            if (name.isEmpty()) {
                errors.append("• Equipment name is required\n");
            } else if (name.length() < 2) {
                errors.append("• Equipment name must be at least 2 characters\n");
            }
            
            if (location.isEmpty()) {
                errors.append("• Location is required\n");
            }
            
            if (errors.length() > 0) {
                JOptionPane.showMessageDialog(this, 
                    "Please fix the following errors:\n\n" + errors.toString(),
                    "Validation Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Confirm update
            int confirm = JOptionPane.showConfirmDialog(this,
                "Update Equipment Details:\n\n" +
                "Name: " + name + "\n" +
                "Category: " + cmbCategory.getSelectedItem() + "\n" +
                "Status: " + selectedStatus + "\n" +
                "Location: " + location + "\n\n" +
                "Are you sure?",
                "Confirm Update",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
            
            if (confirm == JOptionPane.YES_OPTION) {
                equipment.setName(name);
                equipment.setCategory((String) cmbCategory.getSelectedItem());
                equipment.setStatus(selectedStatus);
                equipment.setLocation(location);
                
                JOptionPane.showMessageDialog(this, 
                    "✅ Equipment updated successfully!", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
                dispose();
            }
        });
        
        btnCancel.addActionListener(e -> dispose());
        
        buttons.add(btnSave);
        buttons.add(btnCancel);
        
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.setBackground(Color.WHITE);
        southPanel.add(notePanel, BorderLayout.NORTH);
        southPanel.add(buttons, BorderLayout.SOUTH);
        
        add(header, BorderLayout.NORTH);
        add(form, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
    }
    
    // Custom document for name validation
    private class NameDocument extends PlainDocument {
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
}