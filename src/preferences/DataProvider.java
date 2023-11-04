package preferences;

/**
 * Represents a stock data provider (Finnhub, Generator, etc.)
 */
public class DataProvider {
    private String name;
    private String apiKey;
    private String pullUrl;
    private String pushUrl;
    
    /**
     * Constructs a data provider with the specified values.
     *
     * @param name the name
     * @param apiKey the api key
     * @param pullUrl the pull url
     * @param pushUrl the push url
     */
    public DataProvider(String name, String apiKey, String pullUrl, String pushUrl) {
        this.name = name;
        this.apiKey = apiKey;
        this.pullUrl = pullUrl;
        this.pushUrl = pushUrl;
    }
    
    /**
     * Returns this data provider's data as a string array to be displayed in the 
     * data provider table in the preferences dialog.
     *
     * @return the string[]
     */
    public String[] toStringArray() {
        return new String[] {name, apiKey, pullUrl, pushUrl};
    }

    /**
     * Gets the name of the data provider.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the data provider.
     *
     * @param name the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the API key of the data provider.
     *
     * @return the api key
     */
    public String getApiKey() {
        return apiKey;
    }

    /**
     * Sets the API key of the data provider.
     *
     * @param apiKey the new api key
     */
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    /**
     * Gets the pull url for the data provider.
     *
     * @return the pull url
     */
    public String getPullUrl() {
        return pullUrl;
    }

    /**
     * Sets the pull url for the data provider.
     *
     * @param pullUrl the new pull url
     */
    public void setPullUrl(String pullUrl) {
        this.pullUrl = pullUrl;
    }

    /**
     * Gets the push url for the data provider.
     *
     * @return the push url
     */
    public String getPushUrl() {
        return pushUrl;
    }

    /**
     * Sets the push url for the data provider.
     *
     * @param pushUrl the new push url
     */
    public void setPushUrl(String pushUrl) {
        this.pushUrl = pushUrl;
    }
    
    /**
     * Gets the push url with the /?token=apiKey part appended.
     *
     * @return the full push url
     */
    public String getFullPushUrl() {
        return pushUrl + "/?token=" + apiKey;
    }
}
