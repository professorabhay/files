import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

public class TokenGeneration {
    
    public static void main(String[] args) {
        String username = "your_username";
        String password = "your_password";

        try {
            String token = generateToken(username, password);
            makeOriginalRequest(token);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String generateToken(String username, String password) throws IOException {
        // Construct the authentication endpoint URL
        URL authEndpoint = new URL("https://your-authentication-endpoint.com/token");

        // Create the authentication request
        HttpURLConnection connection = (HttpURLConnection) authEndpoint.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        // Encode the username and password
        String credentials = username + ":" + password;
        String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());
        
        // Set the Authorization header
        connection.setRequestProperty("Authorization", "Basic " + encodedCredentials);

        // Send the request
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            // Read the response
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Extract and return the token from the response
            return response.toString();
        } else {
            throw new IOException("Failed to generate token. Status code: " + responseCode);
        }
    }

    public static void makeOriginalRequest(String token) throws IOException {
        // Construct the original request URL
        URL originalEndpoint = new URL("https://your-original-endpoint.com/data");

        // Create the original request
        HttpURLConnection connection = (HttpURLConnection) originalEndpoint.openConnection();
        connection.setRequestMethod("GET");

        // Set the Authorization header with the token
        connection.setRequestProperty("Authorization", "Bearer " + token);

        // Send the request
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            // Read the response
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Process the response as needed
            System.out.println(response.toString());
        } else {
            throw new IOException("Original request failed. Status code: " + responseCode);
        }
    }
}
