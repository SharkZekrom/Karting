package viewPackage;

import controllerPackage.ApplicationController;
import exceptionPackage.ConnectionException;
import exceptionPackage.UnfoundResearchException;
import modelPackage.Customer;
import modelPackage.Race;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

public class SearchAllRaceFromCustomerWithMail extends JFrame {

    private ApplicationController controller;

    private List<Race> races;
    private JTextField customerMailInput;
    private JLabel customerMailLabel;
    private JPanel informationsPanel;
    private RaceInformationsModel model;
    private JScrollPane scrollPane;
    private JTable table;

    private JButton searchButton;

    public SearchAllRaceFromCustomerWithMail() {
        super("Customer search");
        this.setBounds(100,100,900,600);
        setController(new ApplicationController());
        customerMailLabel = new JLabel("Enter the search customer email");
        customerMailInput = new JTextField();
        customerMailInput.setPreferredSize(new Dimension(150, 30));

        informationsPanel = new JPanel();
        informationsPanel.add(customerMailLabel);
        informationsPanel.add(customerMailInput);
        setVisible(true);
        add(informationsPanel,BorderLayout.NORTH);

        searchButton = new JButton("Search");
        informationsPanel.add(searchButton);

        searchButton.addActionListener(e -> {
            try {
                races = controller.getRacesByCustomer(customerMailInput.getText());
                model = new RaceInformationsModel(races);

                if (scrollPane != null) {
                    remove(scrollPane);
                }

                table = new JTable(model);
                scrollPane = new JScrollPane(table);
                add(scrollPane, BorderLayout.CENTER);
                revalidate();
                repaint();
            } catch (ConnectionException exception) {
                JOptionPane.showMessageDialog(null, exception.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    public void setController(ApplicationController controller) {
        this.controller = controller;
    }
}
