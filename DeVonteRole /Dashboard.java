public class Dashboard {
    // Static instance to hold the single Dashboard instance
    private static Dashboard instance;

    // Private constructor to prevent external instantiation
    private Dashboard() {
        // Initialize any dashboard-specific resources here
    }

    // Public method to provide access to the single instance
    public static Dashboard getInstance() {
        if (instance == null) {
            instance = new Dashboard();
        }
        return instance;
    }

    // Method to display the dashboard (or other dashboard-specific methods)
    public void display() {
        System.out.println("Dashboard displayed");
    }

    // Optional: main method for quick testing
    public static void main(String[] args) {
        Dashboard dashboard1 = Dashboard.getInstance();
        Dashboard dashboard2 = Dashboard.getInstance();

        // Test if both references are the same instance
        if (dashboard1 == dashboard2) {
            System.out.println("Singleton test passed. Only one instance exists.");
        } else {
            System.out.println("Singleton test failed. Multiple instances created.");
        }
    }
}