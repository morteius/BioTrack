import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.*;
import java.util.Base64;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/ECB/PKCS5Padding";
    private static final byte[] KEY = "BioTrackSecretKey".getBytes(); // 16 bytes for AES
    
    // Save equipment with encryption
    public static void saveEquipment(List<Equipment> equipmentList) {
        try {
            StringBuilder data = new StringBuilder();
            for (Equipment eq : equipmentList) {
                data.append(eq.toCSV()).append("\n");
            }
            
            String encrypted = encrypt(data.toString());
            writeToFile("data/equipments.enc", encrypted);
            
            // Also keep a backup non-encrypted version (optional)
            writeToFile("data/equipments_backup.txt", data.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // Load equipment with decryption
    public static List<Equipment> loadEquipment() {
        List<Equipment> equipmentList = new ArrayList<>();
        
        try {
            File encryptedFile = new File("data/equipments.enc");
            if (!encryptedFile.exists()) {
                return equipmentList;
            }
            
            String encryptedData = readFromFile("data/equipments.enc");
            String decryptedData = decrypt(encryptedData);
            
            String[] lines = decryptedData.split("\n");
            for (String line : lines) {
                if (line.trim().isEmpty()) continue;
                
                String[] parts = line.split(",");
                if (parts.length >= 8) {
                    Equipment eq = new Equipment(
                        parts[0].trim(), parts[1].trim(), parts[2].trim(),
                        parts[3].trim(), parts[4].trim(), 
                        Integer.parseInt(parts[5].trim()),
                        Integer.parseInt(parts[6].trim()),
                        parts[7].trim()
                    );
                    equipmentList.add(eq);
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading encrypted data: " + e.getMessage());
            // Fallback to backup if exists
            return loadBackupEquipment();
        }
        
        return equipmentList;
    }
    
    // Fallback to backup file
    private static List<Equipment> loadBackupEquipment() {
        List<Equipment> equipmentList = new ArrayList<>();
        
        try {
            File backupFile = new File("data/equipments_backup.txt");
            if (!backupFile.exists()) {
                return equipmentList;
            }
            
            try (BufferedReader br = new BufferedReader(new FileReader(backupFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;
                    
                    String[] parts = line.split(",");
                    if (parts.length >= 8) {
                        Equipment eq = new Equipment(
                            parts[0].trim(), parts[1].trim(), parts[2].trim(),
                            parts[3].trim(), parts[4].trim(), 
                            Integer.parseInt(parts[5].trim()),
                            Integer.parseInt(parts[6].trim()),
                            parts[7].trim()
                        );
                        equipmentList.add(eq);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return equipmentList;
    }
    
    // Encryption methods
    private static String encrypt(String data) throws Exception {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(KEY, ALGORITHM));
        byte[] encryptedBytes = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }
    
    private static String decrypt(String encryptedData) throws Exception {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(KEY, ALGORITHM));
        byte[] decodedBytes = Base64.getDecoder().decode(encryptedData);
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);
        return new String(decryptedBytes);
    }
    
    // File operations
    private static void writeToFile(String filename, String data) throws IOException {
        File file = new File(filename);
        file.getParentFile().mkdirs();
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(data);
        }
    }
    
    private static String readFromFile(String filename) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
        }
        return content.toString();
    }
    
    // Save transaction with encryption
    public static void saveTransaction(String transactionData) {
        try {
            String encrypted = encrypt(transactionData);
            writeToFile("data/transactions.enc", encrypted);
            
            // Backup
            appendToFile("data/transactions_backup.txt", transactionData + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static void appendToFile(String filename, String data) throws IOException {
        File file = new File(filename);
        file.getParentFile().mkdirs();
        try (FileWriter writer = new FileWriter(file, true)) {
            writer.write(data);
        }
    }
}