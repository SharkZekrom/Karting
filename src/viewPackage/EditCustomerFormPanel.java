package viewPackage;

import controllerPackage.ApplicationController;
import exceptionPackage.ConnectionException;
import exceptionPackage.UnfoundResearchException;
import modelPackage.Customer;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class EditCustomerFormPanel extends JPanel {

    private ApplicationController controller;
    private JPanel formPanel;
    private JPanel buttonsPanel;

    private JButton validationButton;

    private JComboBox<Customer> customerField;
    private JTextField emailField;
    private JTextField localityField;
    private JTextField postCodeField;
    private JTextField addressNumberField;
    private JTextField streetField;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField birthDateField;
    private JTextField genderField;
    private JTextField phoneNumberField;
    private JTextField professionalField;

    public EditCustomerFormPanel() {
        setController(new ApplicationController());
        formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(0, 2, 10, 15));

        this.buttonsPanel = new JPanel();

        this.validationButton = new JButton("Validate");

        customerField = new JComboBox<>();
        emailField = new JTextField();
        localityField = new JTextField();
        postCodeField = new JTextField();
        addressNumberField = new JTextField();
        streetField = new JTextField();
        firstNameField = new JTextField();
        lastNameField = new JTextField();
        birthDateField = new JTextField();
        genderField = new JTextField();
        phoneNumberField = new JTextField();
        professionalField = new JTextField();

        try {
            // Load customers
            for (Customer customer : controller.getAllCustomers()) {
                customerField.addItem(customer);
            }
        } catch (UnfoundResearchException | SQLException | ConnectionException e) {
            throw new RuntimeException(e);
        }

        formPanel.add(new JLabel("Customer"));
        customerField.setSelectedItem(null);
        formPanel.add(customerField);

        formPanel.add(new JLabel("Email"));
        emailField.setEnabled(false);  // Désactiver le champ email pour interdire la modification
        formPanel.add(emailField);

        formPanel.add(new JLabel("Locality"));
        formPanel.add(localityField);

        formPanel.add(new JLabel("Post Code"));
        formPanel.add(postCodeField);

        formPanel.add(new JLabel("Address Number"));
        formPanel.add(addressNumberField);

        formPanel.add(new JLabel("Street"));
        formPanel.add(streetField);

        formPanel.add(new JLabel("First Name"));
        formPanel.add(firstNameField);

        formPanel.add(new JLabel("Last Name"));
        formPanel.add(lastNameField);

        formPanel.add(new JLabel("Birth Date"));
        formPanel.add(birthDateField);

        formPanel.add(new JLabel("Gender"));
        formPanel.add(genderField);

        formPanel.add(new JLabel("Phone Number"));
        formPanel.add(phoneNumberField);

        formPanel.add(new JLabel("Professional"));
        formPanel.add(professionalField);

        this.buttonsPanel.add(this.validationButton);

        this.add(this.formPanel);
        this.add(this.buttonsPanel);

        setFieldsEnabled(false);

        customerField.addActionListener(e -> {
            Customer selectedCustomer = (Customer) customerField.getSelectedItem();
            if (selectedCustomer != null) {
                setFieldsEnabled(true);

                // Remplir les champs avec les valeurs actuelles du client sélectionné
                emailField.setText(selectedCustomer.getEmail());
                localityField.setText(selectedCustomer.getLocality());
                postCodeField.setText(selectedCustomer.getPostCode());
                addressNumberField.setText(selectedCustomer.getAddressNumber().toString());
                streetField.setText(selectedCustomer.getStreet());
                firstNameField.setText(selectedCustomer.getFirstName());
                lastNameField.setText(selectedCustomer.getLastName());
                birthDateField.setText(selectedCustomer.getBirthDate().toString());
                genderField.setText(selectedCustomer.getGender());
                phoneNumberField.setText(selectedCustomer.getPhoneNumber());
                professionalField.setText(selectedCustomer.isProfessional() ? "Yes" : "No");

            } else {
                setFieldsEnabled(false);
                clearFields();
            }
        });

        validationButton.addActionListener(e -> {
            String errorMessage = checkForm();
            if (!errorMessage.isEmpty()) {
                JOptionPane.showMessageDialog(this, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                Customer selectedCustomer = (Customer) customerField.getSelectedItem();
                if (selectedCustomer != null) {
                    selectedCustomer.setLocality(localityField.getText());
                    selectedCustomer.setPostCode(postCodeField.getText());
                    selectedCustomer.setAddressNumber(Integer.parseInt(addressNumberField.getText()));
                    selectedCustomer.setStreet(streetField.getText());
                    selectedCustomer.setFirstName(firstNameField.getText());
                    selectedCustomer.setLastName(lastNameField.getText());
                    selectedCustomer.setBirthDate(java.sql.Date.valueOf(birthDateField.getText()));
                    selectedCustomer.setGender(genderField.getText());
                    selectedCustomer.setPhoneNumber(phoneNumberField.getText());
                    selectedCustomer.setIsProfessional(professionalField.getText().equalsIgnoreCase("Yes"));

                    controller.editCustomer(selectedCustomer);

                    JOptionPane.showMessageDialog(this, "Customer updated successfully.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Failed to update customer: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

    }

    private void setFieldsEnabled(boolean enabled) {
        emailField.setEnabled(enabled);  // Champ email désactivé même s'il est dans cette méthode pour uniformité
        localityField.setEnabled(enabled);
        postCodeField.setEnabled(enabled);
        addressNumberField.setEnabled(enabled);
        streetField.setEnabled(enabled);
        firstNameField.setEnabled(enabled);
        lastNameField.setEnabled(enabled);
        birthDateField.setEnabled(enabled);
        genderField.setEnabled(enabled);
        phoneNumberField.setEnabled(enabled);
        professionalField.setEnabled(enabled);
        validationButton.setEnabled(enabled);
    }

    private void clearFields() {
        emailField.setText("");
        localityField.setText("");
        postCodeField.setText("");
        addressNumberField.setText("");
        streetField.setText("");
        firstNameField.setText("");
        lastNameField.setText("");
        birthDateField.setText("");
        genderField.setText("");
        phoneNumberField.setText("");
        professionalField.setText("");
    }

    public String checkForm() {
        StringBuilder errorMessage = new StringBuilder();

        if (localityField.getText().isEmpty()) {
            errorMessage.append("Locality field is mandatory\n");
        }
        if (postCodeField.getText().isEmpty()) {
            errorMessage.append("Post Code field is mandatory\n");
        }
        if (addressNumberField.getText().isEmpty()) {
            errorMessage.append("Address Number field is mandatory\n");
        } else if (!addressNumberField.getText().matches("\\d+")) {
            errorMessage.append("Address Number must be numeric\n");
        }
        if (streetField.getText().isEmpty()) {
            errorMessage.append("Street field is mandatory\n");
        }
        if (firstNameField.getText().isEmpty()) {
            errorMessage.append("First Name field is mandatory\n");
        }
        if (lastNameField.getText().isEmpty()) {
            errorMessage.append("Last Name field is mandatory\n");
        }
        if (birthDateField.getText().isEmpty()) {
            errorMessage.append("Birth Date field is mandatory\n");
        } else {
            try {
                java.sql.Date.valueOf(birthDateField.getText());
            } catch (IllegalArgumentException e) {
                errorMessage.append("Invalid Birth Date format. Please use yyyy-MM-dd.\n");
            }
        }
        if (genderField.getText().isEmpty()) {
            errorMessage.append("Gender field is mandatory\n");
        }
        if (phoneNumberField.getText().isEmpty()) {
            errorMessage.append("Phone Number field is mandatory\n");
        }
        if (professionalField.getText().isEmpty()) {
            errorMessage.append("Professional field is mandatory\n");
        }

        return errorMessage.toString();
    }


    public void setController(ApplicationController controller) {
        this.controller = controller;
    }
}
