package viewPackage;

import controllerPackage.ApplicationController;
import exceptionPackage.ConnectionException;
import exceptionPackage.UnfoundResearchException;
import modelPackage.Category;
import modelPackage.Race;
import modelPackage.Track;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class EditSlotRaceFormPanel extends JPanel {

    private ApplicationController controller;
    private JPanel formPanel;
    private JPanel buttonsPanel;

    private JButton validationButton;

    private JComboBox<Race> raceField;    // ComboBox pour sélectionner la course
    private JComboBox<Track> trackField;
    private JComboBox<Category> categoryField;

    private JTextField dateField;
    private JTextField startTimeField;

    public EditSlotRaceFormPanel() {
        setController(new ApplicationController());
        formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(0, 2, 10, 30));

        this.buttonsPanel = new JPanel();

        this.validationButton = new JButton("Validate");

        raceField = new JComboBox<>();
        trackField = new JComboBox<>();
        categoryField = new JComboBox<>();
        dateField = new JTextField();
        startTimeField = new JTextField();

        try {
            // Remplir la JComboBox avec toutes les courses
            for (Race race : controller.getAllRaces()) {
                raceField.addItem(race);
            }

            for (Track track : controller.getAllTracks()) {
                trackField.addItem(track);
            }
            for (Category category : controller.getAllCategories()) {
                categoryField.addItem(category);
            }
        } catch (UnfoundResearchException | ConnectionException | SQLException exception) {
            JOptionPane.showMessageDialog(null, exception.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }

        formPanel.add(new JLabel("Select Race"));
        formPanel.add(raceField);

        formPanel.add(new JLabel("Track"));
        formPanel.add(trackField);

        formPanel.add(new JLabel("Category"));
        formPanel.add(categoryField);

        formPanel.add(new JLabel("Date (yyyy-MM-dd)"));
        formPanel.add(dateField);

        formPanel.add(new JLabel("Start Time (HH:mm:ss)"));
        formPanel.add(startTimeField);

        buttonsPanel.add(validationButton);

        this.add(this.formPanel);
        this.add(this.buttonsPanel);

        // Désactiver les champs et le bouton de validation au démarrage
        setFieldsEnabled(false);
        raceField.setSelectedItem(null);
        trackField.setSelectedItem(null);
        categoryField.setSelectedItem(null);
        // Ajouter un écouteur d'action pour la JComboBox raceField
        raceField.addActionListener(e -> {
            Race selectedRace = (Race) raceField.getSelectedItem();
            if (selectedRace != null) {
                // Activer les champs
                setFieldsEnabled(true);

                // Remplir les champs avec les valeurs actuelles de la course sélectionnée
                trackField.setSelectedItem(selectedRace.getTrack());
                categoryField.setSelectedItem(selectedRace.getCategory());
                dateField.setText(selectedRace.getDate().toString());
                startTimeField.setText(selectedRace.getStartTime().toString());
            } else {
                // Désactiver les champs si aucune course n'est sélectionnée
                setFieldsEnabled(false);
                clearFields();
            }
        });

        validationButton.addActionListener(e -> {
            String errorMessage = checkForm();
            if (!errorMessage.isEmpty()) {
                JOptionPane.showMessageDialog(null, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                Race selectedRace = (Race) raceField.getSelectedItem();
                LocalDate date = LocalDate.parse(dateField.getText());
                LocalTime startTime = LocalTime.parse(startTimeField.getText());
                Track selectedTrack = (Track) trackField.getSelectedItem();
                Category selectedCategory = (Category) categoryField.getSelectedItem();

                controller.editRace(selectedRace.getId(), selectedTrack, selectedCategory, date, startTime);
                JOptionPane.showMessageDialog(null, "Race mise à jour avec succès", "Succès", JOptionPane.INFORMATION_MESSAGE);

                // Réinitialiser le formulaire après l'action réussie
                clearFields();
                setFieldsEnabled(false);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Erreur lors de la mise à jour de la course: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

    }

    private void setFieldsEnabled(boolean enabled) {
        trackField.setEnabled(enabled);
        categoryField.setEnabled(enabled);
        dateField.setEnabled(enabled);
        startTimeField.setEnabled(enabled);
        validationButton.setEnabled(enabled);
    }

    private void clearFields() {
        trackField.setSelectedItem(null);
        categoryField.setSelectedItem(null);
        dateField.setText("");
        startTimeField.setText("");
    }

    public String checkForm() {
        StringBuilder errorMessage = new StringBuilder();

        // Vérification que le champ race est sélectionné
        if (raceField.getSelectedItem() == null) {
            errorMessage.append("Race field is mandatory\n");
        }

        // Vérification que le champ track est sélectionné
        if (trackField.getSelectedItem() == null) {
            errorMessage.append("Track field is mandatory\n");
        }

        // Vérification que le champ category est sélectionné
        if (categoryField.getSelectedItem() == null) {
            errorMessage.append("Category field is mandatory\n");
        }

        // Vérification que le champ date est rempli et dans le bon format
        if (dateField.getText().isEmpty()) {
            errorMessage.append("Date field is mandatory\n");
        } else {
            try {
                LocalDate.parse(dateField.getText()); // Vérifie que la date est au bon format
            } catch (DateTimeParseException e) {
                errorMessage.append("Invalid date format. Please use yyyy-MM-dd.\n");
            }
        }

        // Vérification que le champ start time est rempli et dans le bon format
        if (startTimeField.getText().isEmpty()) {
            errorMessage.append("Start time field is mandatory\n");
        } else {
            try {
                LocalTime.parse(startTimeField.getText()); // Vérifie que l'heure est au bon format
            } catch (DateTimeParseException e) {
                errorMessage.append("Invalid start time format. Please use HH:mm:ss.\n");
            }
        }

        return errorMessage.toString();
    }


    public void setController(ApplicationController controller) {
        this.controller = controller;
    }
}
