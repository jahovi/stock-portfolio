package charts;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import main.Stock;
import preferences.DataProvider;

/**
 * This class contains a static method for requesting candle data from a data provider. 
 * It parses the response into a CandleDataResponse object and constructs a list of candles out of it.
 */
public class StaticCandleDataRequester {
    // utility class for getting a list of candles
    private static Gson gson = new GsonBuilder().create();



    /**
     * Gets a list of candle data.
     *
     * @param dataProvider the data provider to query
     * @param stock the stock for which to get candles
     * @param resolution the resolution for the candle data. One of [1, 5, 15, 30, 60, D, W, M]
     * @param from the beginning of the time frame for which to get candles as a UNIX timestamp in seconds
     * @param to the the end of the time frame for which to get candles as a UNIX timestamp in seconds
     * @return a list of Candle objects
     */
    public static List<Candle> getCandleList(DataProvider dataProvider, Stock stock, String resolution, long from, long to) {
        String requestUrl = constructRequestURL(dataProvider, stock, resolution, from, to);
        String jsonResponse = HttpQueryMaker.queryDataProvider(requestUrl);
        ArrayList<Candle> candleList = new ArrayList<>();
        
        if (jsonResponse != null) {
            CandleDataResponse candleDataResponse = gson.fromJson(jsonResponse, CandleDataResponse.class);
            // combine one data point from each array in the response object into a candle
            for (int i = 0; i < candleDataResponse.getArraysLength(); i++) {
                double low = candleDataResponse.getLowPrices()[i];
                double high = candleDataResponse.getHighPrices()[i];
                double open = candleDataResponse.getOpenPrices()[i];
                double close = candleDataResponse.getClosePrices()[i];
                long timestamp = candleDataResponse.getTimestamps()[i];
                Candle candle = new Candle(low, high, open, close, timestamp);
                candleList.add(candle);
            }
        }
        return candleList;
    }
    

    private static String constructRequestURL(DataProvider dataProvider, Stock stock, String resolution, long from, long to) {
        String baseURL = dataProvider.getPullUrl();
        String apiKey = dataProvider.getApiKey();
        
        String functionIdentifier = "/stock/candle";
        String querySeparator = "?";
        String symbolPart = "symbol=" + stock.getSymbol();
        String resolutionPart = "resolution=" + resolution;
        String fromPart = "from=" + from;
        String toPart = "to=" + to;
        String tokenPart = "token=" + apiKey;
        String requestURL = baseURL + functionIdentifier + querySeparator + symbolPart + "&" + resolutionPart + "&" + fromPart + "&" + toPart + "&" + tokenPart;
        
        return requestURL;
    }
}
