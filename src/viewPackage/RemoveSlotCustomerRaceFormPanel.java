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
import java.time.format.DateTimeParseException;
import java.util.List;

public class RemoveSlotCustomerRaceFormPanel extends JPanel {

    private ApplicationController controller;
    private JPanel formPanel;
    private JPanel buttonsPanel;

    private JButton validationButton;
    private JButton resetButton;

    private JComboBox<Customer> customerField;
    private JTextField dateField;
    private JComboBox<Race> raceField;

    public RemoveSlotCustomerRaceFormPanel() {
        setController(new ApplicationController());
        formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(0, 2, 10, 15));

        this.buttonsPanel = new JPanel();

        this.validationButton = new JButton("Validate");
        this.resetButton = new JButton("Reset");

        customerField = new JComboBox<>();
        dateField = new JTextField();
        raceField = new JComboBox<>();

        try {
            // Load customers, tracks, and categories
            for (Customer customer : controller.getAllCustomers()) {
                customerField.addItem(customer);
            }

            // Add listeners to update races based on customer and date inputs

            customerField.addActionListener(e -> {
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


        } catch (UnfoundResearchException | SQLException | ConnectionException e) {
            throw new RuntimeException(e);
        }

        formPanel.add(new JLabel("Customer"));
        customerField.setSelectedItem(null);
        formPanel.add(customerField);

        formPanel.add(new JLabel("Date (yyyy-MM-dd)"));
        formPanel.add(dateField);

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

            Customer selectedCustomer = (Customer) customerField.getSelectedItem();
            Race selectedRace = (Race) raceField.getSelectedItem();

            try {
                controller.removeCustomerFromARace(selectedCustomer, selectedRace);
                JOptionPane.showMessageDialog(null, "Customer " + selectedCustomer.getEmail() + " removed from the race", "Success", JOptionPane.INFORMATION_MESSAGE);

            } catch (UnfoundResearchException | ConnectionException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });



        // Implementation des actions effectuées avec le bouton reset
        resetButton.addActionListener(e -> {
            customerField.setSelectedItem(null);
            dateField.setText("");
            raceField.setSelectedItem(null);
        });
    }

    private void updateRaceField() throws UnfoundResearchException, ConnectionException {
        String dateString = dateField.getText();
        Customer selectedCustomer = (Customer) customerField.getSelectedItem();

        if (selectedCustomer == null || dateString.isEmpty()) {
            // Clear the raceField if track, category, or date is not selected
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

        // Fetch races for the selected customer and date
        List<Race> races = controller.getRacesByCustomer(selectedCustomer.getEmail());
        raceField.removeAllItems();
        for (Race race : races) {
            if (race.getDate().equals(date)) {
                raceField.addItem(race);
            }
        }
    }

    public String checkForm() {
        StringBuilder errorMessage = new StringBuilder();

        // Vérification que le champ customer est sélectionné
        if (customerField.getSelectedItem() == null) {
            errorMessage.append("Customer field is mandatory\n");
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

        // Vérification que le champ race est sélectionné
        if (raceField.getSelectedItem() == null) {
            errorMessage.append("Race field is mandatory\n");
        }

        return errorMessage.toString();
    }


    public void setController(ApplicationController controller) {
        this.controller = controller;
    }
}
