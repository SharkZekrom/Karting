package viewPackage;

import controllerPackage.ApplicationController;
import exceptionPackage.ConnectionException;
import exceptionPackage.UnfoundResearchException;
import modelPackage.Category;
import modelPackage.Customer;
import modelPackage.Race;
import modelPackage.Track;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

public class RemoveSlotRaceFormPanel extends JPanel {

    private ApplicationController controller;
    private JPanel formPanel;
    private JPanel buttonsPanel;

    private JButton validationButton;
    private JButton resetButton;

    private JComboBox<Track> trackField;
    private JComboBox<Category> categoryField;
    private JTextField dateField;
    private JComboBox<Race> raceField;


    public RemoveSlotRaceFormPanel() {
        setController(new ApplicationController());
        formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(0, 2, 10, 15));

        this.buttonsPanel = new JPanel();

        this.validationButton = new JButton("Validate");
        this.resetButton = new JButton("Reset");

        dateField = new JTextField();
        trackField = new JComboBox<>();
        categoryField = new JComboBox<>();
        raceField = new JComboBox<>();

        try {
            for (Track track : controller.getAllTracks()) {
                trackField.addItem(track);
            }
            for (Category category : controller.getAllCategories()) {
                categoryField.addItem(category);
            }

            dateField.addActionListener(e -> {
                try {
                    updateRaceField();
                } catch (UnfoundResearchException | ConnectionException ex) {
                    throw new RuntimeException(ex);
                }
            });
            trackField.addActionListener(e -> {
                try {
                    updateRaceField();
                } catch (UnfoundResearchException | ConnectionException ex) {
                    throw new RuntimeException(ex);
                }
            });
            categoryField.addActionListener(e -> {
                try {
                    updateRaceField();
                } catch (UnfoundResearchException | ConnectionException ex) {
                    throw new RuntimeException(ex);
                }
            });

        } catch (UnfoundResearchException | ConnectionException | SQLException exception) {
            JOptionPane.showMessageDialog(null, exception.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }

        formPanel.add(new JLabel("Date (yyyy-MM-dd)"));  // Instruction for date format
        formPanel.add(dateField);

        formPanel.add(new JLabel("Track"));
        trackField.setSelectedItem(null);
        formPanel.add(trackField);

        formPanel.add(new JLabel("Category"));
        categoryField.setSelectedItem(null);
        formPanel.add(categoryField);

        formPanel.add(new JLabel("Race"));
        raceField.setSelectedItem(null);
        formPanel.add(raceField);

        this.buttonsPanel.add(this.validationButton);
        this.buttonsPanel.add(this.resetButton);

        this.add(this.formPanel);
        this.add(this.buttonsPanel);

        // Implementation des actions effectuées avec le bouton validation
        validationButton.addActionListener(e -> {
            String errorMessage = checkForm();
            if (!errorMessage.isEmpty()) {
                JOptionPane.showMessageDialog(null, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Race selectedRace = (Race) raceField.getSelectedItem();
            Category selectedCategory = (Category) categoryField.getSelectedItem();
            Track selectedTrack = (Track) trackField.getSelectedItem();

            try {
                controller.removeRace(selectedRace, selectedCategory, selectedTrack);
                JOptionPane.showMessageDialog(null, "Race removed from the database", "Success", JOptionPane.INFORMATION_MESSAGE);

                // Clear the form after successful action
                resetForm();
            } catch (UnfoundResearchException | ConnectionException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Implementation des actions effectuées avec le bouton reset
        resetButton.addActionListener(e -> {
            trackField.setSelectedItem(null);
            categoryField.setSelectedItem(null);
            dateField.setText("");
            raceField.setSelectedItem(null);

        });
    }

    private void updateRaceField() throws UnfoundResearchException, ConnectionException {
        Track selectedTrack = (Track) trackField.getSelectedItem();
        Category selectedCategory = (Category) categoryField.getSelectedItem();
        String dateString = dateField.getText();

        if (selectedTrack == null || selectedCategory == null || dateString.isEmpty()) {
            // Clear the raceField if date is not selected
            raceField.removeAllItems();
            return;
        }

        // Convert dateString to LocalDate
        LocalDate date;
        try {
            date = LocalDate.parse(dateString);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Invalid date format. Please use yyyy-MM-dd.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Fetch races for the selected date
        List<Race> races = controller.getRacesByTrackAndCategory(selectedTrack, selectedCategory, date);
        raceField.removeAllItems();
        for (Race race : races) {
            if (race.getDate().equals(date)) {
                raceField.addItem(race);
            }
        }
    }

    public String checkForm() {
        StringBuilder errorMessage = new StringBuilder();

        // Vérification de la date
        if (dateField.getText().isEmpty()) {
            errorMessage.append("Date field is mandatory\n");
        } else {
            try {
                LocalDate.parse(dateField.getText()); // Check if the date is in valid format
            } catch (DateTimeParseException e) {
                errorMessage.append("Invalid date format. Please use yyyy-MM-dd.\n");
            }
        }

        // Vérification que les champs track, category, et race sont sélectionnés
        if (trackField.getSelectedItem() == null) {
            errorMessage.append("Track field is mandatory\n");
        }
        if (categoryField.getSelectedItem() == null) {
            errorMessage.append("Category field is mandatory\n");
        }
        if (raceField.getSelectedItem() == null) {
            errorMessage.append("Race field is mandatory\n");
        }

        return errorMessage.toString();
    }

    public void resetForm() {
        trackField.setSelectedItem(null);
        categoryField.setSelectedItem(null);
        dateField.setText("");
        raceField.setSelectedItem(null);
    }



    public void setController(ApplicationController controller) {
        this.controller = controller;
    }
}
