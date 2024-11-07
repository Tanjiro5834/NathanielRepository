import javax.swing.*;
import org.json.JSONObject;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherApp extends JFrame{
	private JTextField cityInput;
    private JLabel weatherLabel;
    private JLabel tempLabel;
    private JLabel iconLabel;
    
    public WeatherApp() {
    	new JFrame("Weather app");
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLayout(new BorderLayout());
        setResizable(false);
        
        Font buttonFont = new Font("Georgia", Font.BOLD, 18);
        Font labelFont = new Font("Georgia", Font.BOLD, 20);
        
        ImageIcon icon = new ImageIcon(getClass().getResource("/icons/weather_logo.png"));
        setIconImage(icon.getImage());
        
        JPanel topPanel = new JPanel();
        cityInput = new JTextField(16);
        
        JButton searchButton = new JButton("Search");
        searchButton.setFont(buttonFont);
        searchButton.setBackground(new Color(144, 238, 144));
        searchButton.setForeground(Color.WHITE);
        
        JLabel enterCityLabel = new JLabel("City: ");
        enterCityLabel.setFont(labelFont);
        
        topPanel.add(enterCityLabel);
        topPanel.add(cityInput);
        topPanel.add(searchButton);
        add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(3, 1));    
        
        tempLabel = new JLabel("Temperature: --", SwingConstants.CENTER);
        tempLabel.setFont(labelFont);
        weatherLabel = new JLabel("Weather: --", SwingConstants.CENTER);
        weatherLabel.setFont(labelFont);
        iconLabel = new JLabel("", SwingConstants.CENTER);

        centerPanel.add(weatherLabel);
        centerPanel.add(tempLabel);
        centerPanel.add(iconLabel);

        add(centerPanel, BorderLayout.CENTER);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                String city = cityInput.getText();
                if(!city.isEmpty()){
                    fetchWeatherData(city);
                }
            }
        });
    }

    private void fetchWeatherData(String city) {
    	String apiKey = System.getenv("WEATHER_API_KEY");
    	String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey + "&units=metric";
    	
    	try{
    		@SuppressWarnings("deprecation")
			URL url = new URL(apiUrl);
    		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    		conn.setRequestMethod("GET");
    		BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
    		String line;
    		
    		StringBuilder content = new StringBuilder();
    		while((line = reader.readLine()) != null){
    			content.append(line);
    		}
    		reader.close();
    		conn.disconnect();
    		
    		JSONObject json = new JSONObject(content.toString());
            String weather = json.getJSONArray("weather").getJSONObject(0).getString("main");
            double temp = json.getJSONObject("main").getDouble("temp");

            weatherLabel.setText("Weather: " + weather);
            tempLabel.setText("Temperature: " + temp + "°C");
            
            updateIcon(weather);
    		
    	}catch(Exception e){
    		e.getMessage();
    	}
    }
    
    void updateIcon(String weather){
    	String iconPath = "";
    	
    	switch(weather.toLowerCase()){
        case "clear":
            iconPath = "icons/clear.png";  
            break;
        case "clouds":
            iconPath = "icons/cloudy.png";  
            break;
        case "rain":
            iconPath = "icons/rain.png";   
            break;
        case "snow":
            iconPath = "icons/snow.png";  
            break;
        case "thunderstorm":
            iconPath = "icons/thunderstorm.png"; 
            break;
        default:
            iconPath = "icons/default.png"; 
            break;
    	}
    	
        ImageIcon originalIcon = new ImageIcon(getClass().getResource(iconPath));
        
        Image originalImage = originalIcon.getImage();
        Image resizedImage = originalImage.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        
        ImageIcon resizedIcon = new ImageIcon(resizedImage);
        
        iconLabel.setIcon(resizedIcon);
    }

    public static void main(String[] args) {
    	WeatherApp app = new WeatherApp();
    	app.setVisible(true);
    }
}
