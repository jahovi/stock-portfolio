package preferences;

/**
 * The Interface IPreferencesPersistence. Implementing classes have the ability to commit 
 * the data in an application preferences model to persistent storage when the program closes 
 * and to reconstruct that data on restart.
 */
public interface IPreferencesPersistence {
    
    /**
     * Load application preferences.
     *
     * @return the application preferences model
     */
    ApplicationPreferencesModel loadApplicationPreferences();
    
    /**
     * Store application preferences.
     *
     * @param applicationPreferences the application preferences model
     */
    void storeApplicationPreferences(ApplicationPreferencesModel applicationPreferences);
    
    /**
     * Checks if there is a preferences file in existence.
     *
     * @return true, if preferences file exists
     */
    boolean isPreferencesFileExistent();
}
