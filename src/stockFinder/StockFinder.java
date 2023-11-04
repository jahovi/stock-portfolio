package stockFinder;

import java.io.*;
import java.net.*;

import com.google.gson.*;

import main.Stock;
import preferences.ApplicationPreferencesModel;
import preferences.DataProvider;

/**
 * This class implements the IStockFinder interface. An instance is therefore used 
 * to query the data providers with search terms from the stock finder dialog.
 */
public class StockFinder implements IStockFinder {
    private Gson gson = new GsonBuilder().create();
    private ApplicationPreferencesModel preferences;

    private static class SymbolLookupResponse {
        // The json response from the data provider is deserialized into this type
        private Stock[] result;
    }

    /**
     * Constructs a stock finder. Passes in a reference to the preferences model
     * in order to get the active data provider to query.
     *
     * @param preferences the ApplicationPreferencesModel instance
     */
    public StockFinder(ApplicationPreferencesModel preferences) {
        this.preferences = preferences;
    }

    /**
     * Finds stocks matching the specified search term by querying the 
     * active data provider. Returns an empty array if the search is without result.
     *
     * @param searchterm the search term to look up
     * @return an array of stocks  that match the search term, as returned by the data provider
     */
    @Override
    // lookup searchterm and return a list of matching stocks. returns empty array
    // if not found.
    public Stock[] findMatchingStocks(String searchterm) {
        // construct request url
        String requestURL = constructRequestURL(searchterm);
        String jsonResponse = queryDataProvider(requestURL);

        Stock[] stocks = new Stock[0];
        if (jsonResponse != null) {
            stocks = getStocksFromJsonResponse(jsonResponse);
        }

        return stocks;
    }

    private String constructRequestURL(String searchterm) {
        DataProvider activeDataProvider = preferences.getActiveDataProvider();
        String baseURL = activeDataProvider.getPullUrl();
        String apiKey = activeDataProvider.getApiKey();
        String functionIdentifier = "/search";
        String querySeparator = "?";
        String queryPart = "q=" + searchterm;
        String tokenPart = "token=" + apiKey;
        String requestURL = baseURL + functionIdentifier + querySeparator + queryPart + "&" + tokenPart;
        return requestURL;
    }

    private String queryDataProvider(String requestUrl) {
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
            System.out.println("Unable to query data provider. Check data provider data in preferences. The provider may also be unreachable.");
            e.printStackTrace();
        } 

        return jsonResponse;
    }

    private Stock[] getStocksFromJsonResponse(String json) {
        Stock[] stocks = gson.fromJson(json, SymbolLookupResponse.class).result;
        return stocks;
    }

}
