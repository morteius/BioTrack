import java.security.MessageDigest;
import java.util.zip.CRC32;

public class DataIntegrityChecker {
    
    public static String calculateChecksum(String data) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(data.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
    
    public static boolean verifyData(String data, String expectedChecksum) {
        String actualChecksum = calculateChecksum(data);
        return actualChecksum.equals(expectedChecksum);
    }
}