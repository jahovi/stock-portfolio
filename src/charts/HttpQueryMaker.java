package charts;

import java.io.*;
import java.net.*;

/**
 * This utility class contains a static method to make a REST http query to the 
 * provided URL and receive a String as response. This string is parsed for
 * candle data in the StaticCandleDataRequester.
 */
public class HttpQueryMaker {
    
 /**
 * Send an http request to the specified URL and return the response string
 *
 * @param requestUrl the request url
 * @return the string that was received in response
 */
public static String queryDataProvider(String requestUrl) {
        String jsonResponse = null;
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(requestUrl).openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            int code = conn.getResponseCode();
            if (code >= 400) {
                System.err.println("Request failed. Code: " + code);
                // check for null return in caller of this method
                return null;
            }
            try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                jsonResponse = in.readLine();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return jsonResponse;
    }
}
