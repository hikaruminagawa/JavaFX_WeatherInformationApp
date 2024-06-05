
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

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

            // Create a new URI object with the API URL
            URI uri = URI.create(apiUrl);

            // Create a new HttpClient
            HttpClient client = HttpClient.newHttpClient();

            // Create a new HttpRequest with the URI
            HttpRequest request = HttpRequest.newBuilder(uri).build();

            // Send the request and get the response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Parse the JSON response and get emperature, humidity, and wind speed
            String responseBody = response.body();
            String temperature = responseBody.split("\"temp\":")[1].split(",")[0];
            String humidity = responseBody.split("\"humidity\":")[1].split(",")[0].replaceAll("\\D+","");
            String windSpeed = responseBody.split("\"speed\":")[1].split(",")[0];

            // Display the temperature, humidity, and wind speed in weatherOutput label in App.fxml
            weatherOutput.setText("Temperature: " + temperature + "°C\nHumidity: " + humidity + "%\nWind Speed: " + windSpeed + "m/s");


        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }
}