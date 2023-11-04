package main;

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.*;

/**
 * Represents the menu bar of the main application frame.
 */
public class MainMenuBar extends JMenuBar{
    private JMenuItem preferencesItem;
    private JMenuItem stockFinderItem;
    private JMenuItem watchlistItem;
    private JMenuItem exitItem;
    private JMenu windowsMenu;
    
    private JMenuItem reconnectItem;

    /**
     * Creates the main menu bar.
     */
    public MainMenuBar() {
        createMainMenuBar();
    }

    private void createMainMenuBar() {
        // Create "Tools" Menu
        JMenu toolsMenu = new JMenu("Tools");
        toolsMenu.setMnemonic(KeyEvent.VK_T);
        add(toolsMenu);
        // Create "Preferences" Item
        preferencesItem = new JMenuItem("Preferences");
        preferencesItem.setActionCommand("Preferences");
        preferencesItem.setMnemonic(KeyEvent.VK_P);
        toolsMenu.add(preferencesItem);
        // Create "Stockfinder" Item
        stockFinderItem = new JMenuItem("Stock Finder");
        stockFinderItem.setMnemonic(KeyEvent.VK_F);
        toolsMenu.add(stockFinderItem);
        // Create Reconnect item
        reconnectItem= new JMenuItem("Reconnect");
        reconnectItem.setMnemonic(KeyEvent.VK_R);
        toolsMenu.add(reconnectItem);
        // Create "Exit" Item
        exitItem = new JMenuItem("Exit");
        exitItem.setMnemonic(KeyEvent.VK_X);
        toolsMenu.add(exitItem);
        
        // Create windows menu
        windowsMenu = new JMenu("Windows");
        windowsMenu.setMnemonic(KeyEvent.VK_W);
        add(windowsMenu);
        // Create "Watchlist" Item
        watchlistItem = new JMenuItem("Watchlist");
        watchlistItem.setMnemonic(KeyEvent.VK_L);
        windowsMenu.add(watchlistItem);
        
    }
    
    /**
     * Registers the main application controller with the buttons on the menu bar.
     *
     * @param controller the controller to register
     */
    public void addController(ActionListener controller) {
        preferencesItem.addActionListener(controller);
        stockFinderItem.addActionListener(controller);
        watchlistItem.addActionListener(controller);
        exitItem.addActionListener(controller);
        reconnectItem.addActionListener(controller);
    }

    /**
     * Gets a reference to the windows menu. Used by the main controller
     * to add new items for chart frames.
     *
     * @return the windows menu of the main menu bar
     */
    public JMenu getWindowsMenu() {
        return windowsMenu;
    }
}
