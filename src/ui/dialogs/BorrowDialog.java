import java.awt.*;
import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.util.regex.Pattern;

public class BorrowDialog extends JDialog {
    private boolean confirmed = false;
    private int quantity;
    private String borrowerName;
    private Equipment equipment;
    
    public BorrowDialog(JFrame parent, Equipment equipment) {
        super(parent, "Borrow Equipment", true);
        this.equipment = equipment;
        setSize(400, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        
        // Header
        JPanel header = new JPanel();
        header.setBackground(new Color(251, 188, 5));
        header.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        
        JLabel title = new JLabel("Borrow: " + equipment.getName());
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(Color.BLACK);
        header.add(title);
        
        // Info panel
        JPanel info = new JPanel(new GridLayout(5, 2, 10, 10));
        info.setBackground(Color.WHITE);
        info.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel lblAvailable = new JLabel("Available:");
        lblAvailable.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JLabel lblAvailableValue = new JLabel(String.valueOf(equipment.getAvailableQuantity()));
        lblAvailableValue.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblAvailableValue.setForeground(new Color(52, 168, 83)); // Green
        
        JLabel lblTotal = new JLabel("Total Quantity:");
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JLabel lblTotalValue = new JLabel(String.valueOf(equipment.getQuantity()));
        lblTotalValue.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        JLabel lblQuantity = new JLabel("Borrow Quantity:*");
        lblQuantity.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JSpinner spnQuantity = new JSpinner(new SpinnerNumberModel(1, 1, equipment.getAvailableQuantity(), 1));
        spnQuantity.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JSpinner.NumberEditor editor = new JSpinner.NumberEditor(spnQuantity, "#");
        spnQuantity.setEditor(editor);
        
        JLabel lblBorrower = new JLabel("Borrower Name:*");
        lblBorrower.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JTextField txtBorrower = createTextField();
        txtBorrower.setDocument(new NameDocument()); // Name validation
        
        info.add(lblAvailable); info.add(lblAvailableValue);
        info.add(lblTotal); info.add(lblTotalValue);
        info.add(lblQuantity); info.add(spnQuantity);
        info.add(lblBorrower); info.add(txtBorrower);
        
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
        
        JButton btnBorrow = createButton("📥 Borrow", new Color(66, 133, 244));
        JButton btnCancel = createButton("Cancel", new Color(120, 144, 156));
        
        btnBorrow.addActionListener(e -> {
            borrowerName = txtBorrower.getText().trim();
            quantity = (int) spnQuantity.getValue();
            
            // Validation
            StringBuilder errors = new StringBuilder();
            
            if (borrowerName.isEmpty()) {
                errors.append("• Borrower name is required\n");
            } else if (borrowerName.length() < 2) {
                errors.append("• Borrower name must be at least 2 characters\n");
            } else if (!isValidName(borrowerName)) {
                errors.append("• Borrower name can only contain letters, spaces, hyphens, and apostrophes\n");
            }
            
            if (quantity <= 0) {
                errors.append("• Quantity must be greater than 0\n");
            } else if (quantity > equipment.getAvailableQuantity()) {
                errors.append("• Cannot borrow more than available quantity (" + equipment.getAvailableQuantity() + ")\n");
            }
            
            if (errors.length() > 0) {
                JOptionPane.showMessageDialog(this, 
                    "Please fix the following errors:\n\n" + errors.toString(),
                    "Validation Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Confirm borrow
            int confirm = JOptionPane.showConfirmDialog(this,
                "Confirm Borrow:\n\n" +
                "Equipment: " + equipment.getName() + "\n" +
                "Borrower: " + borrowerName + "\n" +
                "Quantity: " + quantity + "\n\n" +
                "Are you sure?",
                "Confirm Borrow",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
            
            if (confirm == JOptionPane.YES_OPTION) {
                confirmed = true;
                dispose();
            }
        });
        
        btnCancel.addActionListener(e -> dispose());
        
        buttons.add(btnBorrow);
        buttons.add(btnCancel);
        
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.setBackground(Color.WHITE);
        southPanel.add(notePanel, BorderLayout.NORTH);
        southPanel.add(buttons, BorderLayout.SOUTH);
        
        add(header, BorderLayout.NORTH);
        add(info, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
    }
    
    // Custom document for name validation
    private class NameDocument extends PlainDocument {
        private final Pattern pattern = Pattern.compile("^[a-zA-Z\\s\\-\\'\\,\\.]*$");
        
        @Override
        public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
            if (str == null) return;
            
            String current = getText(0, getLength());
            String proposed = current.substring(0, offset) + str + current.substring(offset);
            
            if (pattern.matcher(proposed).matches() && proposed.length() <= 50) {
                super.insertString(offset, str, attr);
            } else {
                Toolkit.getDefaultToolkit().beep();
            }
        }
    }
    
    // Helper method to validate name
    private boolean isValidName(String name) {
        return name.matches("^[a-zA-Z\\s\\-\\'\\,\\.]+$");
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
    
    public boolean isConfirmed() {
        return confirmed;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public String getBorrowerName() {
        return borrowerName;
    }
}