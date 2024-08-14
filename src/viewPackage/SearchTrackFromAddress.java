package viewPackage;

import controllerPackage.ApplicationController;
import exceptionPackage.ConnectionException;
import exceptionPackage.UnfoundResearchException;
import modelPackage.Address;
import modelPackage.Track;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class SearchTrackFromAddress extends JFrame {

    private ApplicationController controller;
    private JTextField addressNumberInput;
    private JTextField addressStreetLabelInput;
    private JLabel addressLabel;
    private JPanel informationsPanel;
    private TrackInformationsModel model;
    private JScrollPane scrollPane;
    private JTable table;
    private JButton searchButton;

    public SearchTrackFromAddress() {
        super("Track search");
        this.setBounds(100, 100, 900, 600);
        setController(new ApplicationController());
        addressLabel = new JLabel("Enter the address number and street of the track");
        addressNumberInput = new JTextField();
        addressStreetLabelInput = new JTextField();
        addressNumberInput.setPreferredSize(new Dimension(150, 30));
        addressStreetLabelInput.setPreferredSize(new Dimension(150, 30));

        informationsPanel = new JPanel();
        informationsPanel.add(addressLabel);
        informationsPanel.add(new JLabel("Number:"));
        informationsPanel.add(addressNumberInput);
        informationsPanel.add(new JLabel("Street:"));
        informationsPanel.add(addressStreetLabelInput);
        setVisible(true);
        add(informationsPanel, BorderLayout.NORTH);

        searchButton = new JButton("Search");
        informationsPanel.add(searchButton);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int addressNumber = Integer.parseInt(addressNumberInput.getText());
                    String addressStreetLabel = addressStreetLabelInput.getText();
                    Address address = new Address(addressNumber, addressStreetLabel);
                    Track track = controller.searchTrackFromAddress(address);

                    model = new TrackInformationsModel(track);

                    if (scrollPane != null) {
                        remove(scrollPane);
                    }

                    table = new JTable(model);
                    scrollPane = new JScrollPane(table);
                    add(scrollPane, BorderLayout.CENTER);
                    revalidate();
                    repaint();
                } catch (UnfoundResearchException | ConnectionException exception) {
                    JOptionPane.showMessageDialog(null, exception.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid address number", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public void setController(ApplicationController controller) {
        this.controller = controller;
    }
}
