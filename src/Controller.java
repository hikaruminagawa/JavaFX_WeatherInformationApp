import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;



public class Controller {

    @FXML
    private VBox mainScreen;

    @FXML       
    private Label timeLabel;

    @FXML
    private TextField locationInput;

    @FXML
    private ImageView weatherIcon;

    @FXML
    private Label weatherOutput;

    @FXML
    private Label searchHistoryLabel;

    // Initialize HashMap to store the search history with timestamp and location
    HashMap<String, String> searchHistory = new HashMap<>();

    // Get current time and date and keep updating it every second, then display it on timeLabel
    @FXML
    public void initialize() {
        Thread clock = new Thread(() -> {
            while (true) {
                Date date = new Date();
                DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                String time = timeFormat.format(date);
                Platform.runLater(() -> timeLabel.setText(time));

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        clock.setDaemon(true);
        clock.start();

        // Set the background image based on the time of day
        BackgroundImage morningImage = new BackgroundImage(new Image("images\\Morning.png", 400, 600, false, true),
                null, null, null, null);
        BackgroundImage afternoonImage = new BackgroundImage(new Image("images\\Afternoon.png", 400, 600, false, true),
                null, null, null, null);
        BackgroundImage eveningImage = new BackgroundImage(new Image("images\\Evening.png", 400, 600, false, true),
                null, null, null, null);
        BackgroundImage nightImage = new BackgroundImage(new Image("images\\Night.png", 400, 600, false, true), null, null,
                null, null);
         
        mainScreen.setBackground(new Background(morningImage));
    }

    // Initialize variables to store the weather data
    String responseBody = "";
    String condition = "";
    String temperature = "";
    String humidity = "";
    String windSpeed = "";
    String unit = "C";

    @FXML
    private Label weatherForecast;

    // Initialize variables to store the forecast data
    String forecastDate = "";
    String forecastTemperature = "";
    String forecastCondition = "";
    String forecastOutput = "";

    // API key for OpenWeatherMap
    String key = "6fa085713e4974b450750c580a20857a";


    // Method to get the weather data for the location entered in the locationInput TextField
    @FXML
    private void getWeather() {
        // Get the location from the locationInput TextField
        String location = locationInput.getText();
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
            responseBody = response.body();
            condition = responseBody.split("\"main\":\"")[1].split("\"")[0];
            temperature = responseBody.split("\"temp\":")[1].split(",")[0];
            humidity = responseBody.split("\"humidity\":")[1].split(",")[0].replaceAll("\\D+","");
            windSpeed = responseBody.split("\"speed\":")[1].split(",")[0];

            // Display the temperature, humidity, and wind speed in weatherOutput label in App.fxml
            weatherOutput.setText("Condition: " + condition + "\nTemperature: " + temperature + "°C\nHumidity: " + humidity + "%\nWind Speed: " + windSpeed + "m/s");
            System.out.println(responseBody);

            // Set weatherIcon to the appropriate image based on the condition
            Image image = null;
            
            switch (condition) {
                case "Clear":
                    image = new Image ("images\\Clear.png");
                    break;
                case "Clouds":
                    image = new Image ("images\\Clouds.png");
                    break;
                case "Rain":
                    image = new Image ("images\\Rain.png");
                    break;
                case "Snow":
                    image = new Image ("images\\Snow.png");
                    break;
                default:
                    image = new Image ("images\\Unknown.png");
                    break;
            }
            weatherIcon.setImage(image);

            // Add the location to the searchHistory HashMap with the current timestamp
            searchHistory.put(location, new Date().toString());

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }

    // Method to switch the temperature unit between Celsius and Fahrenheit
    @FXML
    private void switchUnit(){
        double temp = Double.parseDouble(temperature);
        double convertedTemp;

        if (unit.equals("C")) {
            convertedTemp = (temp * 9/5) + 32;
            unit = "F";
        } else {
            convertedTemp = (temp - 32) * 5/9;
            unit = "C";
        }
        
        // Display the temperature, humidity, and wind speed in weatherOutput label in App.fxml
        temperature = String.format("%.2f", convertedTemp);
        weatherOutput.setText("Condition: " + condition + "\nTemperature: " + temperature + "°" + unit + "\nHumidity: " + humidity + "%\nWind Speed: " + windSpeed + "m/s");

        // Update the forecast data with the new unit
        if(forecastOutput != ""){
            forecastOutput = "";
            getForecast();
        }
        

    }

    // Method to get the forecast for the next 3 days
    @FXML
    private void getForecast(){
        // Get the location from the locationInput TextField
        String location = locationInput.getText();

        // Call the forecast API here with the location
        try{
            // Create the URL for the API request
            String apiUrl = "https://api.openweathermap.org/data/2.5/forecast?q=" + location + "&units=metric&appid=" + key;
            
            // Create a new URI object with the API URL
            URI uri = URI.create(apiUrl);

            // Create a new HttpClient
            HttpClient client = HttpClient.newHttpClient();

            // Create a new HttpRequest with the URI
            HttpRequest request = HttpRequest.newBuilder(uri).build();

            // Send the request and get the response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Get the response body
            String forecastResponseBody = response.body();
            
            // Parse the JSON response and get the forecast data for the next 3 days
            for (int i = 0; i < 3; i++) {
                // Get the forecast date, temperature, and condition
                forecastDate = forecastResponseBody.split("\"dt_txt\":\"")[i+1].split("\"")[0];
                forecastTemperature = forecastResponseBody.split("\"temp\":")[i+1].split(",")[0];
                forecastCondition = forecastResponseBody.split("\"main\":\"")[i+1].split("\"")[0];
                
                // Convert forecastTemperature to double
                double temp = Double.parseDouble(forecastTemperature);

                // Add the forecast data to the forecastOutput string
                if (unit.equals("F")) {
                    forecastOutput += forecastDate + "\n" + "Temperature: " + String.format("%.2f", (temp * 9/5 + 32)) + "°F\nCondition: " + forecastCondition + "\n\n";
                }
                else if (unit.equals("C")) {
                    forecastOutput += forecastDate + "\n" + "Temperature: " + temp + "°C\nCondition: " + forecastCondition + "\n\n";
                }
            }
            
            // Display the forecast data in the weatherForecast label in App.fxml
            weatherForecast.setText(forecastOutput);  
            weatherForecast.setTextAlignment(TextAlignment.CENTER); 
        
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showSearchHistory(){
        // Display the search history in the searchHistory label in App.fxml by formatting the searchHistory HashMap
        String searchHistoryOutput = "";
        for (Map.Entry<String, String> entry : searchHistory.entrySet()) {
            searchHistoryOutput += entry.getKey() + " - " + entry.getValue() + "\n";
        }
        searchHistoryLabel.setText(searchHistoryOutput);
    }
}
