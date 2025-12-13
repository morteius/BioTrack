public class Transaction {

    @SuppressWarnings("FieldMayBeFinal")
    private String equipmentId;
    @SuppressWarnings("FieldMayBeFinal")
    private String equipmentName;
    private final String action;      // Borrow / Return
    private final String borrower;    // For borrowing
    private final String dateTime;    // Timestamp

    public Transaction(String equipmentId, String equipmentName, String action, String borrower, String dateTime) {
        this.equipmentId = equipmentId;
        this.equipmentName = equipmentName;
        this.action = action;
        this.borrower = borrower;
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return equipmentId + "|" + equipmentName + "|" + action + "|" + borrower + "|" + dateTime;
    }
}

