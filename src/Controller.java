
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Controller {
    @FXML
    private TextField locationInput;

    @FXML
    private Label weatherOutput;

    @FXML
    private void getWeather() {
            String location = locationInput.getText();
            String key = "6fa085713e4974b450750c580a20857a";
            // Call the weather API here with the location
            try {
                // Create the URL for the API request
                String apiUrl = "http://api.openweathermap.org/data/2.5/weather?q=" + location + "&appid=" + key + "&lang=ja&units=metric";
                URL url = new URL(apiUrl);
                
                // Send the HTTP GET request
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                // Update the weatherOutput label with the response
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
}