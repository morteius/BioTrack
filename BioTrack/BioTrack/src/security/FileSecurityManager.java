import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermission;
import java.util.HashSet;
import java.util.Set;

public class FileSecurityManager {
    
    public static void makeFileReadOnly(File file) throws IOException {
        // Set file to read-only
        file.setReadOnly();
        
        // For Unix/Linux systems, set permissions
        if (!System.getProperty("os.name").toLowerCase().contains("win")) {
            Path path = file.toPath();
            Set<PosixFilePermission> permissions = new HashSet<>();
            permissions.add(PosixFilePermission.OWNER_READ);
            permissions.add(PosixFilePermission.GROUP_READ);
            permissions.add(PosixFilePermission.OTHERS_READ);
            Files.setPosixFilePermissions(path, permissions);
        }
    }
    
    public static void makeFileWritable(File file) throws IOException {
        // Make file writable temporarily
        file.setWritable(true);
        
        if (!System.getProperty("os.name").toLowerCase().contains("win")) {
            Path path = file.toPath();
            Set<PosixFilePermission> permissions = new HashSet<>();
            permissions.add(PosixFilePermission.OWNER_READ);
            permissions.add(PosixFilePermission.OWNER_WRITE);
            permissions.add(PosixFilePermission.GROUP_READ);
            permissions.add(PosixFilePermission.OTHERS_READ);
            Files.setPosixFilePermissions(path, permissions);
        }
    }
}