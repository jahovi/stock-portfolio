package watchlist;

import java.io.*;
import java.net.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import main.Stock;
import preferences.DataProvider;

/**
 * This utility class provides a static method to get a stock quote from the specified data provider.
 * Since no internal state is needed, this class was implemented with static methods only.
 */
public class StaticStockQuoteRequester {
    private static Gson gson = new GsonBuilder().create();


    /**
     * Sets the current and previous close price of the specified stock by querying the specified 
     * data provider's quote endpoint.
     *
     * @param stock the stock to set the current and previous close price on
     * @param dataProvider the data provider to query for the quote
     */
    public static void setCurrentAndPreviousClosePrice(Stock stock, DataProvider dataProvider) {
        // construct request URL
        String requestURL = constructRequestURL(stock, dataProvider);
        String jsonResponse = queryDataProvider(requestURL);
        
        if (jsonResponse != null) {
            QuoteResponse quoteResponse = gson.fromJson(jsonResponse, QuoteResponse.class);
            stock.setPreviousClosePrice(quoteResponse.previousClosePrice);
            stock.setOpenPrice(quoteResponse.openPrice);
            stock.setPrice(quoteResponse.currentPrice);
            
            // For debugging:
//            System.out.println(stock.getSymbol() + " price: " + quoteResponse.currentPrice);
//            System.out.println(stock.getSymbol() + " previousClosePrice: " + quoteResponse.previousClosePrice);
//            System.out.println(stock.getSymbol() + " openPrice: " + quoteResponse.openPrice);
//            System.out.println(stock.getSymbol() + " high: " + quoteResponse.highPrice);
//            System.out.println(stock.getSymbol() + " low: " + quoteResponse.lowPrice);
            
        }

    }

    private static String queryDataProvider(String requestUrl) {
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
//                System.out.println("Quote: " + jsonResponse);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return jsonResponse;
    }

    private static String constructRequestURL(Stock stock, DataProvider dataProvider) {
        DataProvider activeDataProvider = dataProvider;
        String baseURL = activeDataProvider.getPullUrl();
        String apiKey = activeDataProvider.getApiKey();
        String functionIdentifier = "/quote";
        String querySeparator = "?";
        String queryPart = "symbol=" + stock.getSymbol();
        String tokenPart = "token=" + apiKey;
        String requestURL = baseURL + functionIdentifier + querySeparator + queryPart + "&" + tokenPart;
        return requestURL;
    }
    
    private static class QuoteResponse {
        @SerializedName("o") private double openPrice; 
        @SerializedName("h") private double highPrice; 
        @SerializedName("l") private double lowPrice; 
        @SerializedName("c") private double currentPrice;
        @SerializedName("pc") private double previousClosePrice;
    }
}
