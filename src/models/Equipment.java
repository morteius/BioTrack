public class Equipment {
    private String id, name, category, status, location, lastMaintenance;
    private int quantity;  // Total quantity
    private int availableQuantity;  // Available to borrow
    
    public Equipment(String id, String name, String category, String status, 
                     String location, int quantity, String lastMaintenance) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.status = status;
        this.location = location;
        this.quantity = quantity;
        this.lastMaintenance = lastMaintenance;
        
        // Initialize availableQuantity based on status
        if (status.equals("Available")) {
            this.availableQuantity = quantity;
        } else if (status.equals("Maintenance") || status.equals("Damaged")) {
            this.availableQuantity = 0;
        } else if (status.equals("Borrowed")) {
            this.availableQuantity = 0;
        } else if (status.equals("Partially Available")) {
            // For existing data, we need to calculate
            this.availableQuantity = quantity > 0 ? quantity - 1 : 0;
        } else {
            this.availableQuantity = quantity; // Default
        }
    }
    
    // Alternative constructor for loading from file with availableQuantity
    public Equipment(String id, String name, String category, String status, 
                     String location, int quantity, int availableQuantity, String lastMaintenance) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.status = status;
        this.location = location;
        this.quantity = quantity;
        this.availableQuantity = availableQuantity;
        this.lastMaintenance = lastMaintenance;
    }
    
    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public String getStatus() { return status; }
    public String getLocation() { return location; }
    public String getLastMaintenance() { return lastMaintenance; }
    public int getQuantity() { return quantity; }
    public int getAvailableQuantity() { return availableQuantity; }
    
    // Setters
    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setCategory(String category) { this.category = category; }
    
    public void setStatus(String status) { 
        this.status = status;
        // Update available quantity based on status
        if (status.equals("Available")) {
            this.availableQuantity = this.quantity;
        } else if (status.equals("Maintenance") || status.equals("Damaged") || status.equals("Borrowed")) {
            this.availableQuantity = 0;
        }
        // For "Partially Available", keep current availableQuantity
    }
    
    public void setLocation(String location) { this.location = location; }
    public void setLastMaintenance(String date) { this.lastMaintenance = date; }
    
    public void setQuantity(int quantity) { 
        this.quantity = quantity; 
        // Adjust available quantity if needed
        if (this.availableQuantity > quantity) {
            this.availableQuantity = quantity;
        }
    }
    
    public void setAvailableQuantity(int qty) { 
        this.availableQuantity = qty; 
        // Update status based on available quantity
        if (this.availableQuantity == 0) {
            this.status = quantity > 0 ? "Borrowed" : "Unavailable";
        } else if (this.availableQuantity == this.quantity) {
            this.status = "Available";
        } else {
            this.status = "Partially Available";
        }
    }
    
    // Borrow quantity
    public boolean borrow(int qty) {
        if (qty <= availableQuantity && availableQuantity > 0) {
            availableQuantity -= qty;
            // Update status
            if (availableQuantity == 0) {
                status = quantity > 0 ? "Borrowed" : "Unavailable";
            } else if (availableQuantity < quantity) {
                status = "Partially Available";
            }
            return true;
        }
        return false;
    }
    
    // Return quantity
    public boolean returnItem(int qty) {
        int borrowed = quantity - availableQuantity;
        if (qty <= borrowed && qty > 0) {
            availableQuantity += qty;
            // Update status
            if (availableQuantity == quantity) {
                status = "Available";
            } else if (availableQuantity > 0) {
                status = "Partially Available";
            }
            return true;
        }
        return false;
    }
    
    // For saving to equipment file (with availableQuantity)
    public String toCSV() {
        return String.join(",",
            id, 
            name, 
            category, 
            status, 
            location,
            String.valueOf(quantity), 
            String.valueOf(availableQuantity), 
            lastMaintenance
        );
    }
    
    // For backward compatibility (old format without availableQuantity)
    public String toLegacyCSV() {
        return String.join(",",
            id, 
            name, 
            category, 
            status, 
            location,
            String.valueOf(quantity), 
            lastMaintenance
        );
    }
    
    @Override
    public String toString() {
        return name + " (" + id + ") - " + status + " [" + availableQuantity + "/" + quantity + " available]";
    }
}