import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction {
    private String transactionId;
    private String equipmentId;
    private String equipmentName;
    private String person;
    private int quantity;
    private String date;
    private String action; // "Borrow" or "Return"
    
    public Transaction(String equipmentId, String equipmentName, String person, 
                      int quantity, String action) {
        this.transactionId = generateTransactionId();
        this.equipmentId = equipmentId;
        this.equipmentName = equipmentName;
        this.person = person;
        this.quantity = quantity;
        this.date = LocalDateTime.now().format(
            DateTimeFormatter.ofPattern("MM-dd-yyyy hh:mm a")
        );
        this.action = action;
    }
    
    // Generate 6-digit transaction ID
    private String generateTransactionId() {
        // Generate random 6-digit number
        int num = 100000 + (int)(Math.random() * 900000); // 100000 to 999999
        return "TXN" + num;
    }
    
    // Getters
    public String getTransactionId() { return transactionId; }
    public String getEquipmentId() { return equipmentId; }
    public String getEquipmentName() { return equipmentName; }
    public String getPerson() { return person; }
    public int getQuantity() { return quantity; }
    public String getDate() { return date; }
    public String getAction() { return action; }
    
    // For saving to CSV file
    public String toCSV() {
        return String.join(",",
            transactionId,
            equipmentId,
            equipmentName,
            person,
            String.valueOf(quantity),
            date,
            action
        );
    }
    
    @Override
    public String toString() {
        return String.format("%s: %s %s %d units of %s on %s",
            transactionId, person, action, quantity, equipmentName, date);
    }
}