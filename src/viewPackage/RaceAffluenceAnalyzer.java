package viewPackage;

import controllerPackage.ApplicationController;
import exceptionPackage.ConnectionException;
import exceptionPackage.UnfoundResearchException;
import modelPackage.Race;
import modelPackage.TrackReservation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

public class RaceAffluenceAnalyzer extends JFrame {

    private ApplicationController controller;

    private List<TrackReservation> trackReservations;
    private JTextField trackNameInput;
    private JLabel trackNameLabel;
    private JPanel informationsPanel;
    private RaceAffluenceAnalyzerModel model;
    private JScrollPane scrollPane;
    private JTable table;

    private JButton searchButton;

    public RaceAffluenceAnalyzer() {
        super("Track affluence analyzer");
        this.setBounds(100, 100, 900, 600);
        setController(new ApplicationController());
        trackNameLabel = new JLabel("Enter the names of the tracks (separated by a comma):");
        trackNameInput = new JTextField();
        trackNameInput.setPreferredSize(new Dimension(150, 30));


        informationsPanel = new JPanel();
        informationsPanel.add(trackNameLabel);
        informationsPanel.add(trackNameInput);
        setVisible(true);
        add(informationsPanel, BorderLayout.NORTH);

        searchButton = new JButton("Search");
        informationsPanel.add(searchButton);

        searchButton.addActionListener(e -> {
            try {
                trackReservations = controller.raceAffluenceAnalyzer(trackNameInput.getText());
            } catch (ConnectionException ex) {
                throw new RuntimeException(ex);
            }
            model = new RaceAffluenceAnalyzerModel(trackReservations);

            if (scrollPane != null) {
                remove(scrollPane);
            }

            table = new JTable(model);
            scrollPane = new JScrollPane(table);
            add(scrollPane, BorderLayout.CENTER);
            revalidate();
            repaint();
        });
    }

    public void setController(ApplicationController controller) {
        this.controller = controller;
    }
}
