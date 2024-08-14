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

public class RemoveCustomerFormPanel extends JPanel {

    private ApplicationController controller;
    private JPanel formPanel;
    private JPanel buttonsPanel;

    private JButton validationButton;

    private JComboBox<Customer> customerFiled;

    public RemoveCustomerFormPanel() {
        setController(new ApplicationController());
        formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(0, 2, 10, 15));

        this.buttonsPanel = new JPanel();

        this.validationButton = new JButton("Validate");

        customerFiled = new JComboBox<>();
        try {
            for (Customer customer : controller.getAllCustomers()) {
                customerFiled.addItem(customer);
            }
        } catch (UnfoundResearchException | SQLException | ConnectionException e) {
            throw new RuntimeException(e);
        }

        formPanel.add(new JLabel("Customer"));
        customerFiled.setSelectedItem(null);
        formPanel.add(customerFiled);

        this.buttonsPanel.add(this.validationButton);

        this.add(this.formPanel);
        this.add(this.buttonsPanel);

        validationButton.addActionListener(e -> {
            String errorMessage = checkForm();
            if (!errorMessage.isEmpty()) {
                JOptionPane.showMessageDialog(null, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Customer selectedCustomer = (Customer) customerFiled.getSelectedItem();

            try {
                controller.removeCustomer(selectedCustomer);
                JOptionPane.showMessageDialog(null, "Customer removed from the database", "Success", JOptionPane.INFORMATION_MESSAGE);

                // Réinitialiser le formulaire après l'action réussie
                customerFiled.setSelectedItem(null);
            } catch (ConnectionException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });


    }

    public String checkForm() {
        StringBuilder errorMessage = new StringBuilder();

        // Vérification que le champ customer est sélectionné
        if (customerFiled.getSelectedItem() == null) {
            errorMessage.append("Customer field is mandatory\n");
        }

        return errorMessage.toString();
    }


    public void setController(ApplicationController controller) {
        this.controller = controller;
    }

}
