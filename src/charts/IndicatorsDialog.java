package charts;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * Represents the dialog that can be opened from the menu bar of a chart frame,
 * where the user defines and creates indicators to be shown in the chart.
 */
public class IndicatorsDialog extends JDialog implements ActionListener{
    
    private DefaultListModel<String> availableIndicatorsListModel;
    private JList<String> availableIndicatorsList;
    private JButton addButton;
    
    private DefaultListModel<String> existingIndicatorsListModel;
    private JList<String> existingIndicatorsList;
    private JButton deleteButton;
    private JButton okButton;
    
    // private Border padding;
    private ChartController chartController; 
    private List<Indicator> indicatorsToDraw = new ArrayList<>();

    /**
     * Constructs a new indicators dialog.
     *
     * @param parent the chart panel this dialog is associated with
     * @param chartController the chart controller that passes the user defined indicators to the chart model
     */
    public IndicatorsDialog(ChartPanel parent, ChartController chartController) {
        super();
        setTitle("Indicators");
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        // padding = BorderFactory.createEmptyBorder(20, 10, 20, 10);
        this.chartController = chartController; 

        
        JPanel indicatorsPanel = new JPanel(new GridLayout(1, 2));
        JPanel availableIndicatorsPanel = createAvailableIndicatorsPanel();
        JPanel existingIndiatorsPanel = createExistingIndicatorsPanel();
        indicatorsPanel.add(availableIndicatorsPanel);
        indicatorsPanel.add(existingIndiatorsPanel);
        add(indicatorsPanel, BorderLayout.NORTH);
        
        okButton = new JButton("OK");
        okButton.addActionListener(this);
        add(okButton, BorderLayout.SOUTH);
        
        setResizable(false);
        pack();
        setVisible(true);
    }
    
    private JPanel createAvailableIndicatorsPanel() {
        // available indicators panel
        JPanel availableIndicatorsPanel = new JPanel(new GridLayout(3, 0));
        JLabel availableIndicatorsLabel = new JLabel("Available Indicators");
        
        availableIndicatorsListModel = new DefaultListModel<>();
        availableIndicatorsList = new JList<>(availableIndicatorsListModel);
        availableIndicatorsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane availableIndicatorsScrollPane = new JScrollPane(availableIndicatorsList);
        addButton = new JButton("Add");
        addButton.addActionListener(this);
        
        availableIndicatorsList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (availableIndicatorsList.getSelectedIndex() == -1) {
                    addButton.setEnabled(false);
                } else {
                    addButton.setEnabled(true);
                }
            }
        });
        
        availableIndicatorsPanel.add(availableIndicatorsLabel);
        availableIndicatorsPanel.add(availableIndicatorsScrollPane);
        availableIndicatorsPanel.add(addButton);
        
        // add available indicators
        String[] availableIndicators = new String[] {"Moving Average", "Bollinger Bands"};
        for (String indicator : availableIndicators) {
            availableIndicatorsListModel.addElement(indicator);
        }
        availableIndicatorsList.setSelectedIndex(0);
        
        return availableIndicatorsPanel;
    }

    private JPanel createExistingIndicatorsPanel() {
        // Existing indicators panel 
        JPanel existingIndicatorsPanel = new JPanel(new GridLayout(3, 0));
        JLabel existingIndicatorsLabel = new JLabel("Indicators shown");
        
        existingIndicatorsListModel = new DefaultListModel<>();
        existingIndicatorsList = new JList<>(existingIndicatorsListModel);
        existingIndicatorsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane existingIndicatorsScrollPane = new JScrollPane(existingIndicatorsList);
        deleteButton = new JButton("Delete");
        deleteButton.setEnabled(false);
        deleteButton.addActionListener(this);
        

        
        existingIndicatorsList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (existingIndicatorsList.getSelectedIndex() == -1) {
                    deleteButton.setEnabled(false);
                } else {
                    deleteButton.setEnabled(true);
                }
            }
        });
        
        existingIndicatorsPanel.add(existingIndicatorsLabel);
        existingIndicatorsPanel.add(existingIndicatorsScrollPane);
        existingIndicatorsPanel.add(deleteButton);
        return existingIndicatorsPanel;
    }

    
    /**
     * Handles user interactions with the "Add", "Delete" and "OK" buttons  of the dialog.
     *
     * @param e the ActionEvent passed in by the framework
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Add")) {
            if (createIndicator() == -1) {
                return;
            }
        } else if (e.getActionCommand().equals("Delete")) {
            removeSelectedIndicator();
        } else if (e.getActionCommand().equals("OK")) {
            passIndicatorsToControllerAndDispose();
        }
        
    }

    private void passIndicatorsToControllerAndDispose() {
        chartController.indicatorsDialogOKButtonClicked(indicatorsToDraw);
        dispose();
    }

    private void removeSelectedIndicator() {
        indicatorsToDraw.remove(existingIndicatorsList.getSelectedIndex());
        existingIndicatorsListModel.removeElement(existingIndicatorsList.getSelectedValue());
    }
    
    private int createIndicator() {
        // decide which indicator to create
        if (availableIndicatorsList.getSelectedValue().equals("Moving Average")) {
            // create moving average
            int lengthParameter = askForLengthParameter();
            if (lengthParameter == -1) {
                // cancel was pressed
                return -1;
            }
            Indicator newIndicator = new MovingAverage(lengthParameter);
            existingIndicatorsListModel.addElement(newIndicator.toString());
            indicatorsToDraw.add(newIndicator);
            
        } else if (availableIndicatorsList.getSelectedValue().equals("Bollinger Bands")) {
            // create bollinger bands
            int lengthParameter = askForLengthParameter();
            if (lengthParameter == -1) {
                // cancel was pressed
                return -1;
            }
            int standardDeviationParameter = aksForStandardDeviationParameter();
            if (standardDeviationParameter == -1) {
                // cancel was pressed
                return -1;
            }
            Indicator newIndicator = new BollingerBands(lengthParameter, standardDeviationParameter);
            existingIndicatorsListModel.addElement(newIndicator.toString());
            indicatorsToDraw.add(newIndicator);
        }
        return 0;   // success
    }

    private int aksForStandardDeviationParameter() {
        String input = (String) JOptionPane.showInputDialog("Enter standard deviation factor");
        
        if (input == null) {
            // cancel was pressed
            return -1;
        } else if(isIntegerGreaterZero(input)) {
            return Integer.parseInt(input);
        } else  {
            // invalid input, ask again
            return askForLengthParameter();
        }
    }

    private int askForLengthParameter() {
        String input = (String) JOptionPane.showInputDialog("Enter length parameter");
      
        if (input == null) {
            // cancel was pressed
            return -1;
        } else if(isIntegerGreaterZero(input)) {
            return Integer.parseInt(input);
        } else  {
            // invalid input, ask again
            return askForLengthParameter();
        }
    }
    
    private boolean isIntegerGreaterZero(String s) {
        try {
            int result = Integer.parseInt(s);
            if (result > 0) {
                return true;
            } else {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
