package application;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;

public class Main extends Application {

    // Static variable for holding the single instance of the dashboard1
    private static Main instance;
    private static Stage dashboardStage;

    public Main() {
        // Prevent instantiation of multiple Main objects
        if (instance != null) {
            throw new IllegalStateException("Main instance already created");
        }
        instance = this;
    }

    // Getter for the Singleton instance
    public static Main getInstance() {
        return instance;
    }

    @Override
    public void start(Stage primaryStage) {
        if (dashboardStage != null) {
            // If the dashboard1 is already open, bring it to the front
            dashboardStage.toFront();
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Sample.fxml"));
            AnchorPane root = loader.load();
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Farm Dashboard");

            // Store the stage instance to enforce the Singleton behavior
            dashboardStage = primaryStage;
            primaryStage.setOnCloseRequest(event -> dashboardStage = null);

            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    // Method to ensure the dashboard1 can be shown only once
    public void showDashboard() {
        if (dashboardStage != null) {
            dashboardStage.toFront();
        } else {
            start(new Stage());
        }
    }
}
