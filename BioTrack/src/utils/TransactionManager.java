import java.io.FileWriter;
import java.io.PrintWriter;

public class TransactionManager {

    @SuppressWarnings("CallToPrintStackTrace")
    public static void saveTransaction(Transaction t) {
        try (PrintWriter pw = new PrintWriter(new FileWriter("data/transactions.txt", true))) {
            pw.println(t.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

