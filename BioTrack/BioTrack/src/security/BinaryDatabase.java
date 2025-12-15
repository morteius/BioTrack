import java.io.*;
import java.util.zip.GZIPOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.List;

public class BinaryDatabase {
    private static final byte[] SIGNATURE = {0x42, 0x49, 0x4F, 0x54, 0x52, 0x41, 0x43, 0x4B}; // "BIOTRACK"
    
    public static void saveToBinary(List<Equipment> equipmentList, String filename) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(
                new GZIPOutputStream(new FileOutputStream(filename)))) {
            
            // Write signature
            dos.write(SIGNATURE);
            
            // Write version
            dos.writeInt(1);
            
            // Write number of records
            dos.writeInt(equipmentList.size());
            
            // Write each equipment
            for (Equipment eq : equipmentList) {
                writeString(dos, eq.getId());
                writeString(dos, eq.getName());
                writeString(dos, eq.getCategory());
                writeString(dos, eq.getStatus());
                writeString(dos, eq.getLocation());
                dos.writeInt(eq.getQuantity());
                dos.writeInt(eq.getAvailableQuantity());
                writeString(dos, eq.getLastMaintenance());
            }
        }
    }
    
    private static void writeString(DataOutputStream dos, String str) throws IOException {
        if (str == null) {
            dos.writeInt(0);
        } else {
            byte[] bytes = str.getBytes("UTF-8");
            dos.writeInt(bytes.length);
            dos.write(bytes);
        }
    }
}