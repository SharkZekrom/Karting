package viewPackage;

import controllerPackage.ApplicationController;
import exceptionPackage.ConnectionException;
import exceptionPackage.UnfoundResearchException;
import modelPackage.Customer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SearchCustomerWithMailWindow extends JFrame {

    private ApplicationController controller;

    private Customer customer;
    private JTextField customerMailInput;
    private JLabel customerMailLabel;
    private JPanel informationsPanel;
    private CustomerInformationsModel model;
    private JScrollPane scrollPane;
    private JTable table;

    private JButton searchButton;


    public SearchCustomerWithMailWindow(){
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

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    customer = controller.searchCustomerWithEmail(customerMailInput.getText());
                    model = new CustomerInformationsModel(customer);

                    if(scrollPane != null){
                        remove(scrollPane);
                    }

                    table = new JTable(model);
                    scrollPane = new JScrollPane(table);
                    add(scrollPane, BorderLayout.CENTER);
                    setVisible(true);
                }
                catch (UnfoundResearchException | ConnectionException exception){
                    JOptionPane.showMessageDialog(null, exception.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public void setController(ApplicationController controller) {
        this.controller = controller;
    }
}
