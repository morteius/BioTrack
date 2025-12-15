import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

public class TransactionManager {
    private static Set<String> existingIds = new HashSet<>();
    
    static {
        loadExistingIds();
    }
    
    public static void saveTransaction(Transaction t) {
        try {
            // Ensure data directory exists
            java.io.File dataDir = new java.io.File("data");
            if (!dataDir.exists()) {
                dataDir.mkdir();
            }
            
            // Save transaction
            try (PrintWriter pw = new PrintWriter(new FileWriter("data/transactions.txt", true))) {
                pw.println(t.toCSV());
            }
            
            // Add to existing IDs
            existingIds.add(t.getTransactionId());
        } catch (IOException e) {
            System.err.println("Error saving transaction: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // Alternative method with transaction details
    public static void saveTransaction(String equipmentId, String equipmentName, 
                                       String person, int quantity, String action) {
        try {
            // Ensure data directory exists
            java.io.File dataDir = new java.io.File("data");
            if (!dataDir.exists()) {
                dataDir.mkdir();
            }
            
            // Create transaction ID
            String transactionId = generateTransactionId();
            
            // Format date as MM-dd-yyyy hh:mm a
            String date = LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("MM-dd-yyyy hh:mm a")
            );
            
            // Create CSV line
            String csvLine = String.join(",",
                transactionId,
                equipmentId,
                equipmentName,
                person,
                String.valueOf(quantity),
                date,
                action
            );
            
            // Save to file
            try (PrintWriter pw = new PrintWriter(new FileWriter("data/transactions.txt", true))) {
                pw.println(csvLine);
            }
            
            // Add to existing IDs
            existingIds.add(transactionId);
        } catch (IOException e) {
            System.err.println("Error saving transaction: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // Generate 6-digit transaction ID
    private static String generateTransactionId() {
        String id;
        do {
            // Generate random 6-digit number
            int num = 100000 + (int)(Math.random() * 900000); // 100000 to 999999
            id = "TXN" + num;
        } while (existingIds.contains(id)); // Ensure uniqueness
        return id;
    }
    
    // Load existing transaction IDs from file
    private static void loadExistingIds() {
        try {
            java.io.File file = new java.io.File("data/transactions.txt");
            if (!file.exists()) return;
            
            try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length > 0) {
                        existingIds.add(parts[0].trim());
                    }
                }
            }
        } catch (IOException e) {
            // Silently fail
        }
    }
}