package main;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import preferences.*;
import stockFinder.StockFinderDialog;
import watchlist.*;


public class Stocker {
    
    private IPreferencesPersistence preferencesPersistence;
    private ApplicationPreferencesModel preferences;
    private PreferencesDialog preferencesDialog;
    private StockFinderDialog stockFinderDialog;
    private MainFrame mainFrame;
    private MainMenuBar mainMenuBar;

    public Stocker() {
        
        // Remove this to enable full logging
        System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "ERROR");    
        
        
        restoreApplicationPreferences();
        
        
        mainFrame = new MainFrame();
        mainMenuBar = new MainMenuBar();
        mainFrame.setJMenuBar(mainMenuBar);
        
        
        // Create preferences dialog
        DataProviderPreferencesPanel dataProviderPreferencesPanel = new DataProviderPreferencesPanel(preferences);
        preferencesDialog = new PreferencesDialog(mainFrame, dataProviderPreferencesPanel);
        // PreferencesController preferencesController = new PreferencesController(preferences, preferencesDialog, preferencesPersistence);
        new PreferencesController(preferences, preferencesDialog, preferencesPersistence);
        
        // create stock finder dialog
        stockFinderDialog = new StockFinderDialog(mainFrame, preferences);
        
        
        // Watchlist
        WatchlistModel watchlistModel = new WatchlistModel();
        WatchlistFrame watchlist = new WatchlistFrame(watchlistModel);
        mainFrame.addInternalFrame(watchlist);
        
        MainController mainController = new MainController(preferencesDialog, stockFinderDialog, watchlist, watchlistModel, preferences, preferencesPersistence, mainFrame, mainMenuBar);
        mainMenuBar.addController(mainController);
        watchlist.getAddButton().addActionListener(mainController);
        watchlist.getRemoveButton().addActionListener(mainController);
        watchlist.getShowButton().addActionListener(mainController);
        stockFinderDialog.getAddToWatchlistButton().addActionListener(mainController);
        stockFinderDialog.getOpenChartButton().addActionListener(mainController);
        mainFrame.addWindowListener(mainController);
        
        // Add listener to buttons that effect changes to data provider and necessitate a reconnect
        preferencesDialog.getSaveAndCloseButton().addActionListener(mainController);
        
        
        showGreeting();
        
    }


    private void showGreeting() {
        if (!preferencesPersistence.isPreferencesFileExistent()) {
            String greeting = "Welcome to Stocker! \nSince this is your first session, please head over to Tools -> Preferences. \nIn the Data Providers tab, select Finnhub and hit Edit to enter your API key. \n"
                    + "You can also set another data provider active by selecting it and hitting To Top."
                    + " \n\nA few tips: \n- In case of connection loss hit Tools -> Reconnect to reconnect to the currently active data provider. \n"
                    + "- In case of irrecoverable errors, delete the configuration file ‘stocker_3297810.json’ in the main program directory and try again. \n" 
                    + " \nI hope you enjoy working with Stocker. Have a nice day! \n";
            String greetingTitle = "Welcome to Stocker!";
            JOptionPane.showMessageDialog(mainFrame, greeting, greetingTitle, JOptionPane.INFORMATION_MESSAGE);
        }
    }


    private void restoreApplicationPreferences() {
        preferencesPersistence = new PreferencesPersistenceWithJSON();
        preferences = preferencesPersistence.loadApplicationPreferences();
    }


    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Stocker();

            }
        });
    }

}
