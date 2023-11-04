package charts;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

/**
 * Represents an item in the Windows menu of the main frame menu bar. Each item is associated with an 
 * existing chart frame. Clicking the item brings the frame to the front. Closing the frame also removes
 * the item from the menu.
 */
public class WindowsMenuChartItem extends JMenuItem implements ActionListener, InternalFrameListener{
    private ChartFrame associatedChartFrame;
    
    /**
     * Constructs a new item in the windows menu and associates it with a chart frame.
     *
     * @param chartFrame the chart frame this item corresponds to
     */
    public WindowsMenuChartItem(ChartFrame chartFrame) {
        super(chartFrame.getStock().getDescription());
        this.associatedChartFrame = chartFrame;
        
        addActionListener(this);
        associatedChartFrame.addInternalFrameListener(this);
    }

    /**
     * Brings the chart from to the front when the user clicks the item.
     *
     * @param e ActionEvent passed in by the framework
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        associatedChartFrame.toFront();
    }

    /**
     * Removes the item when the associated chart is closed.
     *
     * @param e the InternalFrame event passed in by the framework 
     */
    @Override
    public void internalFrameClosed(InternalFrameEvent e) {
        // removes the menu item from the windows menu when the associated chart frame is closed
        getParent().remove(this);
    }
    
    // ------------ unused InternalFrameListener methods  --------------

    @Override
    public void internalFrameOpened(InternalFrameEvent e) {}

    @Override
    public void internalFrameClosing(InternalFrameEvent e) {}

    @Override
    public void internalFrameIconified(InternalFrameEvent e) {}

    @Override
    public void internalFrameDeiconified(InternalFrameEvent e) {}

    @Override
    public void internalFrameActivated(InternalFrameEvent e) {}

    @Override
    public void internalFrameDeactivated(InternalFrameEvent e) {}
    
    

}
