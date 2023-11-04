package preferences;

import java.io.IOException;
import java.nio.file.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * An instance of this class handles storing application preferences in a  persistent configuration file on disk. 
 * In its constructor it create a default config file if the file does not exist. It provides methods for serializing 
 * an instance of the application preferences model into the config file as well as restoring a serialized prefereces model
 * from said file. It uses JSON for serialization.
 */
public class PreferencesPersistenceWithJSON implements IPreferencesPersistence {
    private Path jsonFilePath = Paths.get(System.getProperty("user.dir"), "stocker_3297810.json");
    private Gson gson = new GsonBuilder().create();
    private boolean preferencesFileExistent;
    
    /**
     * This constructor creates a default configuration file if no config file exists.
     */
    public PreferencesPersistenceWithJSON() {
        if (!jsonFilePath.toFile().exists()) {
            // create json file with default preferences, only invoked on first start
            preferencesFileExistent = false;
            ApplicationPreferencesModel defaultPreferences = new ApplicationPreferencesModel();
            defaultPreferences.addDataProvider(new DataProvider("Finnhub", "API_Key", "https://finnhub.io/api/v1", "wss://ws.finnhub.io"));
            defaultPreferences.addDataProvider(new DataProvider("Generator", "3297810", "http://localhost:8080", "ws://localhost:8090"));
            try {
                jsonFilePath.toFile().createNewFile();
                storeApplicationPreferences(defaultPreferences);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            preferencesFileExistent = true;
        }
    }   

    /**
     * Load application preferences by deserializing an ApplicationPreferencesModel instance from 
     * the configuration file.
     *
     * @return the application preferences model that was restored
     */
    @Override
    public ApplicationPreferencesModel loadApplicationPreferences() {
        ApplicationPreferencesModel restoredPreferences = new ApplicationPreferencesModel();
        try {
            String jsonString = Files.readString(jsonFilePath);
            restoredPreferences = gson.fromJson(jsonString, ApplicationPreferencesModel.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return restoredPreferences;
    }

    /**
     * Store application preferences by serializing an ApplicationPreferncesModel instance to a json string 
     * and writing it to the configuration file.
     *
     * @param applicationPreferences the ApplicationPreferencesModel instance to be stored
     */
    @Override
    public void storeApplicationPreferences(ApplicationPreferencesModel applicationPreferences) {
        String jsonString = gson.toJson(applicationPreferences);
        try {
            Files.writeString(jsonFilePath, jsonString, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Checks if preferences file exists. Used to detect whether the program is started for the first time
     * and show greeting if so.
     *
     * @return true, if the preferences file exists, false otherwise
     */
    @Override
    public boolean isPreferencesFileExistent() {
        return preferencesFileExistent;
    }

}
