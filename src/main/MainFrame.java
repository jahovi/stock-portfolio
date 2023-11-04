package main;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.*;

/**
 * Represents the main frame of the application.
 */
public class MainFrame extends JFrame {
    
    private JDesktopPane desktopPane;

    /**
     * Instantiates a new main frame.
     */
    public MainFrame() {
        super("Stocker - Hofmann, Jan - 3297810");

        // Indent frame from the screen edges (copied from InternalFrameDemo.java from Oracle's Swing Tutorial)
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset, screenSize.width - inset * 2, screenSize.height - inset * 2);
        
        desktopPane = new JDesktopPane();
        setContentPane(desktopPane);
        
        setVisible(true);
    }
    
    /**
     * Adds an internal frame.
     *
     * @param internalFrame the internal frame to be added
     */
    public void addInternalFrame(JInternalFrame internalFrame) {
        desktopPane.add(internalFrame);
    }
    
    /**
     * Removes the specified internal frame.
     *
     * @param internalFrame the internal frame to be removed
     */
    public void removeInternalFrame(JInternalFrame internalFrame) {
        desktopPane.remove(internalFrame);
    }
}
