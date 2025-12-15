import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AuditLogger {
    private static final String AUDIT_FILE = "data/audit.log";
    
    public static void logEvent(String username, String action, String details) {
        try {
            File dataDir = new File("data");
            if (!dataDir.exists()) {
                dataDir.mkdir();
            }
            
            String timestamp = LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("MM-dd-yyyy hh:mm:ss a")
            );
            
            String logEntry = String.format("[%s] User: %s | Action: %s | Details: %s\n",
                timestamp, username, action, details);
            
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(AUDIT_FILE, true))) {
                bw.write(logEntry);
            }
        } catch (IOException e) {
            System.err.println("Failed to write audit log: " + e.getMessage());
        }
    }
    
    // Call this in your application:
    // AuditLogger.logEvent(User.getCurrentUsername(), "ADD_EQUIPMENT", "Added EQ123456");
    // AuditLogger.logEvent(User.getCurrentUsername(), "BORROW", "Borrowed 3 of EQ123456");
    // AuditLogger.logEvent(User.getCurrentUsername(), "EDIT", "Edited EQ123456 status");
}