package viewPackage;

import controllerPackage.ApplicationController;
import modelPackage.Customer;

import javax.swing.*;
import java.awt.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddCustomerFormPanel extends JPanel {

    private ApplicationController controller;
    private JPanel formPanel;
    private JPanel buttonsPanel;

    private JButton validationButton;
    private JButton resetButton;

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

    public AddCustomerFormPanel() {
        setController(new ApplicationController());
        formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(0, 2, 10, 15));

        this.buttonsPanel = new JPanel();

        this.validationButton = new JButton("Validate");
        this.resetButton = new JButton("Reset");

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

        formPanel.add(new JLabel("Email"));
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

        validationButton.addActionListener(e -> {
            String errorMessage = checkForm();
            if (!errorMessage.isEmpty()) {
                JOptionPane.showMessageDialog(this, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                Date birthDate = new SimpleDateFormat("yyyy-MM-dd").parse(birthDateField.getText());

                Customer customer = new Customer(
                        emailField.getText(),
                        localityField.getText(),
                        postCodeField.getText(),
                        Integer.parseInt(addressNumberField.getText()),
                        streetField.getText(),
                        firstNameField.getText(),
                        lastNameField.getText(),
                        birthDate,
                        genderField.getText(),
                        phoneNumberField.getText(),
                        Boolean.parseBoolean(professionalField.getText())
                );

                controller.addCustomer(customer);
                JOptionPane.showMessageDialog(null, "Customer added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);

                resetButton.doClick();
            } catch (Exception exception) {
                JOptionPane.showMessageDialog(null, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });


        resetButton.addActionListener(e -> {
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
        });


    }

    public String checkForm() {
        StringBuilder errorMessage = new StringBuilder();

        if (emailField.getText().isEmpty()) {
            errorMessage.append("Email field is mandatory\n");
        }
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
                new SimpleDateFormat("yyyy-MM-dd").parse(birthDateField.getText());
            } catch (Exception e) {
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
