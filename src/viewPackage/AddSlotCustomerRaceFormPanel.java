package viewPackage;

import controllerPackage.ApplicationController;
import exceptionPackage.ConnectionException;
import exceptionPackage.ExistingElementException;
import exceptionPackage.UnfoundResearchException;
import modelPackage.Category;
import modelPackage.Customer;
import modelPackage.Race;
import modelPackage.Track;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class AddSlotCustomerRaceFormPanel extends JPanel {

    private ApplicationController controller;
    private JPanel formPanel;
    private JPanel buttonsPanel;

    private JButton validationButton;
    private JButton resetButton;

    private JComboBox<Customer> customerField;
    private JComboBox<Track> trackField;
    private JComboBox<Category> categoryField;
    private JComboBox<Race> raceField;

    private JTextField dateField;  // Text field for date

    public AddSlotCustomerRaceFormPanel() {
        setController(new ApplicationController());
        formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(0, 2, 10, 15));

        this.buttonsPanel = new JPanel();

        this.validationButton = new JButton("Validate");
        this.resetButton = new JButton("Reset");

        customerField = new JComboBox<>();
        dateField = new JTextField();
        trackField = new JComboBox<>();
        categoryField = new JComboBox<>();
        raceField = new JComboBox<>();

        try {
            // Load customers, tracks, and categories
            for (Customer customer : controller.getAllCustomers()) {
                customerField.addItem(customer);
            }
            for (Track track : controller.getAllTracks()) {
                trackField.addItem(track);
            }
            for (Category category : controller.getAllCategories()) {
                categoryField.addItem(category);
            }

            // Add listeners to update races based on track, category, and date inputs
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
            dateField.addActionListener(e -> {
                try {
                    updateRaceField();
                } catch (UnfoundResearchException | ConnectionException ex) {
                    throw new RuntimeException(ex);
                }
            });

        } catch (UnfoundResearchException | ConnectionException | SQLException exception) {
            JOptionPane.showMessageDialog(null, exception.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }

        formPanel.add(new JLabel("Customer"));
        customerField.setSelectedItem(null);
        formPanel.add(customerField);

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
                JOptionPane.showMessageDialog(this, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                Customer selectedCustomer = (Customer) customerField.getSelectedItem();
                Race selectedRace = (Race) raceField.getSelectedItem();
                Track selectedTrack = (Track) trackField.getSelectedItem();
                Category selectedCategory = (Category) categoryField.getSelectedItem();

                controller.addCustomerToARace(selectedCustomer, selectedRace, selectedCategory, selectedTrack);
                JOptionPane.showMessageDialog(null, "Customer " + selectedCustomer.getEmail() + " assigned to the race", "Success", JOptionPane.INFORMATION_MESSAGE);

                resetButton.doClick();
            } catch (ExistingElementException | SQLException | ConnectionException ex) {
                JOptionPane.showMessageDialog(this, "Failed to assign customer to race: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });



        // Implementation des actions effectuées avec le bouton reset
        resetButton.addActionListener(e -> {
            customerField.setSelectedItem(null);
            dateField.setText("");
            trackField.setSelectedItem(null);
            categoryField.setSelectedItem(null);
            raceField.setSelectedItem(null);
        });
    }

    private void updateRaceField() throws UnfoundResearchException, ConnectionException {
        Track selectedTrack = (Track) trackField.getSelectedItem();
        Category selectedCategory = (Category) categoryField.getSelectedItem();
        String dateString = dateField.getText();

        if (selectedTrack == null || selectedCategory == null || dateString.isEmpty()) {
            // Clear the raceField if track, category, or date is not selected
            raceField.removeAllItems();
            return;
        }

        LocalDate date;
        try {
            // Attempt to parse the date input
            date = LocalDate.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (DateTimeParseException e) {
            // Inform user if the date format is incorrect
            JOptionPane.showMessageDialog(null, "Invalid date format. Please enter date in yyyy-MM-dd format.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<Race> races = controller.getRacesByTrackAndCategory(selectedTrack, selectedCategory, date);
        raceField.removeAllItems();
        for (Race race : races) {
            if (!race.isFull() && !race.hasCustomer(((Customer) customerField.getSelectedItem()).getEmail())) {
                raceField.addItem(race);
            }
        }
    }

    public String checkForm() {
        StringBuilder errorMessage = new StringBuilder();

        if (customerField.getSelectedItem() == null) {
            errorMessage.append("Customer field is mandatory\n");
        }
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
                LocalDate.parse(dateField.getText(), DateTimeFormatter.ISO_LOCAL_DATE);
            } catch (DateTimeParseException e) {
                errorMessage.append("Invalid date format. Please use yyyy-MM-dd.\n");
            }
        }
        if (raceField.getSelectedItem() == null) {
            errorMessage.append("Race field is mandatory\n");
        }

        return errorMessage.toString();
    }


    public void setController(ApplicationController controller) {
        this.controller = controller;
    }
}
