import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.Scene;
import javafx.stage.Stage;

// Main class to run the application
public class App extends Application {
    // Start method to load the FXML file and set the stage
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("App.fxml"));
        primaryStage.setTitle("Weather Information App");
        primaryStage.setScene(new Scene(root, 400, 600)); // Changed the width to 400
        primaryStage.show();
    }
    // Main method to launch the application
    public static void main(String[] args) {
        launch(args);
    }
}
