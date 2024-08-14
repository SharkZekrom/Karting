package viewPackage;

import controllerPackage.ApplicationController;
import exceptionPackage.ConnectionException;
import exceptionPackage.ExistingElementException;
import exceptionPackage.UnfoundResearchException;
import modelPackage.Category;
import modelPackage.Track;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class AddSlotRaceFormPanel extends JPanel {

    private ApplicationController controller;
    private JPanel formPanel;
    private JPanel buttonsPanel;

    private JButton validationButton;
    private JButton resetButton;

    private JComboBox<Track> trackField;
    private JComboBox<Category> categoryField;

    private JTextField dateField;
    private JTextField startTimeField;

    public AddSlotRaceFormPanel() {
        setController(new ApplicationController());
        formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(0, 2, 10, 15));

        this.buttonsPanel = new JPanel();

        this.validationButton = new JButton("Validate");
        this.resetButton = new JButton("Reset");

        trackField = new JComboBox<>();
        categoryField = new JComboBox<>();
        dateField = new JTextField();
        startTimeField = new JTextField();

        try {
            for (Track track : controller.getAllTracks()) {
                trackField.addItem(track);
            }
            for (Category category : controller.getAllCategories()) {
                categoryField.addItem(category);
            }

        } catch (UnfoundResearchException | ConnectionException | SQLException exception) {
            JOptionPane.showMessageDialog(null, exception.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }

        formPanel.add(new JLabel("Track"));
        trackField.setSelectedItem(null);
        formPanel.add(trackField);

        formPanel.add(new JLabel("Category"));
        categoryField.setSelectedItem(null);
        formPanel.add(categoryField);

        formPanel.add(new JLabel("Date (yyyy-MM-dd)"));
        formPanel.add(dateField);

        formPanel.add(new JLabel("Start Time (HH:mm:ss)"));
        formPanel.add(startTimeField);

        buttonsPanel.add(validationButton);
        buttonsPanel.add(resetButton);

        this.buttonsPanel.add(this.validationButton);
        this.buttonsPanel.add(this.resetButton);

        this.add(this.formPanel);
        this.add(this.buttonsPanel);

        validationButton.addActionListener(e -> {
            String errorMessage = checkForm();
            if (!errorMessage.isEmpty()) {
                JOptionPane.showMessageDialog(this, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                controller.addRace(trackField.getItemAt(trackField.getSelectedIndex()),
                        categoryField.getItemAt(categoryField.getSelectedIndex()),
                        LocalDateTime.parse(dateField.getText() + "T" + startTimeField.getText()));

                JOptionPane.showMessageDialog(null, "Race ajoutée", "Success", JOptionPane.INFORMATION_MESSAGE);

                resetButton.doClick();
            } catch (ConnectionException | ExistingElementException | SQLException ex) {
                JOptionPane.showMessageDialog(null, "La course que vous souhaitez ajouter est déjà répertoriée", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });


        resetButton.addActionListener(e -> {
            trackField.setSelectedItem(null);
            categoryField.setSelectedItem(null);
            dateField.setText("");
            startTimeField.setText("");
        });
    }

    public String checkForm() {
        StringBuilder errorMessage = new StringBuilder();

        if (trackField.getSelectedItem() == null) {
            errorMessage.append("Track field is mandatory\n");
        }
        if (categoryField.getSelectedItem() == null) {
            errorMessage.append("Category field is mandatory\n");
        }
        if (dateField.getText().isEmpty()) {
            errorMessage.append("Date field is mandatory\n");
        } else {
            try {
                LocalDateTime.parse(dateField.getText() + "T00:00:00");
            } catch (Exception e) {
                errorMessage.append("Invalid date format. Please use yyyy-MM-dd.\n");
            }
        }
        if (startTimeField.getText().isEmpty()) {
            errorMessage.append("Start Time field is mandatory\n");
        } else {
            try {
                LocalDateTime.parse("2000-01-01T" + startTimeField.getText());
            } catch (Exception e) {
                errorMessage.append("Invalid start time format. Please use HH:mm:ss.\n");
            }
        }

        return errorMessage.toString();
    }


    public void setController(ApplicationController controller) {
        this.controller = controller;
    }

}
