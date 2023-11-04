package preferences;

import java.awt.BorderLayout;
import javax.swing.*;

import main.MainFrame;

/**
 * An instance of class functions as the view for the application preferences.
 * It holds panels for the various preference groups in a tabbed pane.
 */
public class PreferencesDialog extends JDialog {
    private DataProviderPreferencesPanel dataProviderPreferencesPanel;
    private JButton saveAndCloseButton;
    private JButton cancelButton;
    
    /**
     * Constructs the preferences dialog.
     *
     * @param mainFrame the main frame to associate this dialog with
     * @param dataProviderPreferencesPanel the data provider preferences panel to add to this dialogs tabbed pane
     */
    public PreferencesDialog(MainFrame mainFrame, DataProviderPreferencesPanel dataProviderPreferencesPanel) {
        super(mainFrame, "Preferences", true);
        this.dataProviderPreferencesPanel = dataProviderPreferencesPanel;
        
        // Construct tabbed pane
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Data Providers", dataProviderPreferencesPanel);
        
        // Construct button panel for apply and cancel buttons
        JPanel buttonPanel = new JPanel();
        saveAndCloseButton = new JButton("Save and Close");
        saveAndCloseButton.setActionCommand("Preferences.saveAndClose"); 
        cancelButton = new JButton("Cancel");
        cancelButton.setActionCommand("Preferences.cancel");
        buttonPanel.add(saveAndCloseButton);
        buttonPanel.add(cancelButton);
        
        add(tabbedPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        
        // window settings
        setResizable(false);
        pack();
        setLocationRelativeTo(mainFrame);
    }

    /**
     * Gets the data provider preferences panel.
     *
     * @return the data provider preferences panel
     */
    public DataProviderPreferencesPanel getDataProviderPreferencesPanel() {
        return dataProviderPreferencesPanel;
    }

    /**
     * Registers the preferences controller as an action listener at the "Save and Close" and 
     * "Cancel" buttons and as a window listener at the dialog itself to handle user interaction.
     *
     * @param preferencesController the preferences controller
     */
    public void addController(PreferencesController preferencesController) {
        saveAndCloseButton.addActionListener(preferencesController);
        cancelButton.addActionListener(preferencesController);
        addWindowListener(preferencesController);
    }

    /**
     * Gets a reference to this dialogs "Save and Close" button. Needed to also register the main Controller
     * as an action listener at this button in order to handle changes to the data providers that may
     * necessitate a reconnect. 
     *
     * @return the save and close button
     */
    public JButton getSaveAndCloseButton() {
        return saveAndCloseButton;
    }
   
}
