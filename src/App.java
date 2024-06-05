import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("App.fxml"));
        primaryStage.setTitle("Weather Information App");
        primaryStage.setScene(new Scene(root, 400, 600)); // Changed the width to 400
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
