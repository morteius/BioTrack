public class Equipment {
    private String id;
    private String name;
    private String category;
    private String status; // Available, Borrowed, Damaged

    public Equipment(String id, String name, String category, String status) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.status = status;
    }

    // Getters & Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return name + " (" + category + ") - " + status;
    }
}
