import java.awt.*;
import java.io.*;
import javax.swing.*;

public class TransactionFrame {

    private JFrame frame;
    private DefaultListModel<String> model;

    public void showTransactionFrame() {
        frame = new JFrame("Transaction History");
        frame.setSize(500, 400);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel title = new JLabel("Transaction Records");
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setBounds(20, 20, 300, 30);
        frame.add(title);

        model = new DefaultListModel<>();
        JList<String> list = new JList<>(model);
        JScrollPane scroll = new JScrollPane(list);
        scroll.setBounds(20, 70, 440, 250);
        frame.add(scroll);

        loadTransactions();

        frame.setVisible(true);
    }

    private void loadTransactions() {
        try (BufferedReader br = new BufferedReader(new FileReader("data/transactions.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                model.addElement(line);
            }
        } catch (Exception e) {
            System.out.println("transaction.txt missing");
        }
    }
}
