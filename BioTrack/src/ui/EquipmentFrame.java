import java.awt.*;
import java.io.*;
import javax.swing.*;

public class EquipmentFrame {

    private JFrame frame;
    private DefaultListModel<Equipment> equipmentModel;
    private JList<Equipment> equipmentList;

    private static final String DATA_DIR = "data";
    private static final String EQUIPMENT_FILE = DATA_DIR + File.separator + "equipments.txt";

    public void showEquipmentFrame() {
        frame = new JFrame("Equipment Manager");
        frame.setSize(600, 450);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel title = new JLabel("Equipment List");
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setBounds(20, 20, 200, 30);
        frame.add(title);

        equipmentModel = new DefaultListModel<>();
        equipmentList = new JList<>(equipmentModel);
        JScrollPane scroll = new JScrollPane(equipmentList);
        scroll.setBounds(20, 70, 350, 300);
        frame.add(scroll);

        loadEquipment();   // ✅ NOW WORKS

        JButton btnAdd = new JButton("Add Equipment");
        btnAdd.setBounds(400, 70, 150, 35);
        frame.add(btnAdd);

        JButton btnEdit = new JButton("Edit");
        btnEdit.setBounds(400, 120, 150, 35);
        frame.add(btnEdit);

        JButton btnBorrow = new JButton("Borrow");
        btnBorrow.setBounds(400, 170, 150, 35);
        frame.add(btnBorrow);

        JButton btnReturn = new JButton("Return");
        btnReturn.setBounds(400, 220, 150, 35);
        frame.add(btnReturn);

        JButton btnDelete = new JButton("Delete");
        btnDelete.setBounds(400, 270, 150, 35);
        frame.add(btnDelete);

        btnAdd.addActionListener(e -> {
            AddEquipmentDialog dialog = new AddEquipmentDialog(frame);
            dialog.setVisible(true);

            Equipment eq = dialog.getNewEquipment();
            if (eq != null) {
                equipmentModel.addElement(eq);
                saveEquipment();
            }
        });

        btnEdit.addActionListener(e -> {
            Equipment selected = equipmentList.getSelectedValue();
            if (selected != null) {
                new EditEquipmentDialog(frame, selected).setVisible(true);
                equipmentList.repaint();
                saveEquipment();
            } else {
                JOptionPane.showMessageDialog(frame, "Select an equipment first.");
            }
        });

        btnBorrow.addActionListener(e -> {
            Equipment selected = equipmentList.getSelectedValue();
            if (selected != null) {
                new BorrowDialog(frame, selected).setVisible(true);
                equipmentList.repaint();
                saveEquipment();
            } else {
                JOptionPane.showMessageDialog(frame, "Select an equipment first.");
            }
        });

        btnReturn.addActionListener(e -> {
            Equipment selected = equipmentList.getSelectedValue();
            if (selected != null) {
                new ReturnDialog(frame, selected).setVisible(true);
                equipmentList.repaint();
                saveEquipment();
            } else {
                JOptionPane.showMessageDialog(frame, "Select an equipment first.");
            }
        });

        btnDelete.addActionListener(e -> {
            Equipment selected = equipmentList.getSelectedValue();
            if (selected != null) {
                equipmentModel.removeElement(selected);
                saveEquipment();
            } else {
                JOptionPane.showMessageDialog(frame, "Select an equipment first.");
            }
        });

        frame.setVisible(true);
    }

    // -------------------------------------------------------
    // LOAD EQUIPMENT
    // -------------------------------------------------------
    @SuppressWarnings("CallToPrintStackTrace")
private void loadEquipment() {
    equipmentModel.clear();

    File file = new File("data/equipments.txt");

    System.out.println("WORKING DIR: " + System.getProperty("user.dir"));
    System.out.println("LOOKING FOR: " + file.getAbsolutePath());

    if (!file.exists()) {
        JOptionPane.showMessageDialog(frame,
                "equipments.txt NOT FOUND!\n\nExpected at:\n" + file.getAbsolutePath());
        return;
    }

    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
        String line;

        while ((line = br.readLine()) != null) {
            System.out.println("READ: " + line);

            String[] parts = line.split("\\|");
            if (parts.length == 4) {
                equipmentModel.addElement(new Equipment(
                        parts[0],
                        parts[1],
                        parts[2],
                        parts[3]
                ));
            }
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
}


    // -------------------------------------------------------
    // SAVE EQUIPMENT
    // -------------------------------------------------------
    @SuppressWarnings("CallToPrintStackTrace")
    private void saveEquipment() {

        File dir = new File(DATA_DIR);
        if (!dir.exists()) dir.mkdir();

        try (PrintWriter pw = new PrintWriter(
                new FileWriter(EQUIPMENT_FILE))) {

            for (int i = 0; i < equipmentModel.getSize(); i++) {
                Equipment eq = equipmentModel.get(i);

                pw.println(
                        eq.getId() + "|" +
                        eq.getName() + "|" +
                        eq.getCategory() + "|" +
                        eq.getStatus()
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
