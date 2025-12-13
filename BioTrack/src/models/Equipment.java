public class Equipment {

    private final String id;
    private String name;
    private String category;
    private String status;

    // Correct constructor
    public Equipment(String id, String name, String category, String status) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.status = status;
    }

    // GETTERS
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getStatus() {
        return status;
    }

    // SETTERS
    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // How the equipment appears in JList
    @Override
    public String toString() {
        return id + " | " + name + " | " + category + " | " + status;
    }
}
