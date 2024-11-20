public class DashboardTest {
    public static void main(String[] args) {
        // Obtain two instances of the Dashboard using getInstance()
        Dashboard dashboard1 = Dashboard.getInstance();
        Dashboard dashboard2 = Dashboard.getInstance();

        // Check if both references point to the same instance
        if (dashboard1 == dashboard2) {
            System.out.println("Singleton test passed. Only one instance exists.");
        } else {
            System.out.println("Singleton test failed. Multiple instances created.");
        }
    }
}
