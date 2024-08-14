package viewPackage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MenuWindow extends JFrame {

    private JMenuBar menuBar;
    private JLabel imageLabel;
    private JButton playPauseButton;
    private JPanel imagePanel;
    private JPanel buttonPanel;
    private JMenu application;
    private JMenu slots;
    private JMenu researches;
    private JMenu customer;
    private JMenu businessTask;

    private JMenuItem quit;
    private JMenuItem addSlotCustomerRace;
    private JMenuItem addSlotRace;
    private JMenuItem removesSlotCustomerRace;
    private JMenuItem removeSlotRace;
    private JMenuItem editSlotRace;
    private JMenuItem searchCustomerWithMail;
    private JMenuItem searchSpeedKartFromCustomerReservation;
    private JMenuItem searchTrackFromAddress;
    private JMenuItem searchAllRaceFromCustomer;
    private JMenuItem editCustomer;
    private JMenuItem removeCustomer;
    private JMenuItem addCustomer;
    private JMenuItem raceAffluenceAnalyzer;
    private JMenuItem playPauseAnimation;

    private KartingThread kartingThread;
    private boolean isPlaying = true;

    public MenuWindow() {
        super("Karting");
        this.setBounds(100, 100, 600, 600);
        JOptionPane.showMessageDialog(null, "Welcome to the Karting", "Welcome", JOptionPane.INFORMATION_MESSAGE);

        kartingThread = new KartingThread(this);
        kartingThread.start();

        this.menuBar = new JMenuBar();
        this.setJMenuBar(menuBar);
        this.application = new JMenu("Application");
        this.menuBar.add(application);
        this.slots = new JMenu("Slots");
        this.menuBar.add(slots);
        this.researches = new JMenu("Researches");
        this.menuBar.add(researches);
        this.customer = new JMenu("Customer");
        this.menuBar.add(customer);
        this.businessTask = new JMenu("Business Task");
        this.menuBar.add(businessTask);

        quit = new JMenuItem("Quit");
        application.add(quit);
        quit.addActionListener(e -> System.exit(0));

        addSlotCustomerRace = new JMenuItem("Add Slot Customer Race");
        slots.add(addSlotCustomerRace);
        addSlotRace = new JMenuItem("Slot Race");
        slots.add(addSlotRace);
        removesSlotCustomerRace = new JMenuItem("Remove Slot Customer Race");
        slots.add(removesSlotCustomerRace);
        removeSlotRace = new JMenuItem("Remove Slot Race");
        slots.add(removeSlotRace);
        editSlotRace = new JMenuItem("Edit Slot Race");
        slots.add(editSlotRace);

        searchCustomerWithMail = new JMenuItem("Search customer with mail");
        researches.add(searchCustomerWithMail);
        searchSpeedKartFromCustomerReservation = new JMenuItem("Search speed kart from customer reservation");
        researches.add(searchSpeedKartFromCustomerReservation);
        searchTrackFromAddress = new JMenuItem("Search track from address");
        researches.add(searchTrackFromAddress);
        searchAllRaceFromCustomer = new JMenuItem("Search all race from customer");
        researches.add(searchAllRaceFromCustomer);

        editCustomer = new JMenuItem("Edit Customer");
        customer.add(editCustomer);
        removeCustomer = new JMenuItem("Remove Customer");
        customer.add(removeCustomer);
        addCustomer = new JMenuItem("Add Customer");
        customer.add(addCustomer);

        raceAffluenceAnalyzer = new JMenuItem("Race Affluence Analyzer");
        businessTask.add(raceAffluenceAnalyzer);

        playPauseButton = new JButton("Pause Animation");
        playPauseButton.addActionListener(e -> toggleAnimation());

        // Create and configure panels
        imagePanel = new JPanel(new BorderLayout());
        buttonPanel = new JPanel();

        imageLabel = new JLabel();
        imagePanel.add(imageLabel, BorderLayout.CENTER);

        buttonPanel.add(playPauseButton);

        addSlotCustomerRace.addActionListener(e -> {
            AddSlotCustomerRaceFormPanel slotCustomerRaceFormPanel = new AddSlotCustomerRaceFormPanel();
            setContentPane(slotCustomerRaceFormPanel);
            revalidate();
        });

        addSlotRace.addActionListener(e -> {
            AddSlotRaceFormPanel slotRaceFormPanel = new AddSlotRaceFormPanel();
            setContentPane(slotRaceFormPanel);
            revalidate();
        });

        removesSlotCustomerRace.addActionListener(e -> {
            RemoveSlotCustomerRaceFormPanel removeSlotCustomerRaceFormPanel = new RemoveSlotCustomerRaceFormPanel();
            setContentPane(removeSlotCustomerRaceFormPanel);
            revalidate();
        });

        removeSlotRace.addActionListener(e -> {
            RemoveSlotRaceFormPanel removeSlotRaceFormPanel = new RemoveSlotRaceFormPanel();
            setContentPane(removeSlotRaceFormPanel);
            revalidate();
        });

        editSlotRace.addActionListener(e -> {
            EditSlotRaceFormPanel editSlotRaceFormPanel = new EditSlotRaceFormPanel();
            setContentPane(editSlotRaceFormPanel);
            revalidate();
        });

        searchCustomerWithMail.addActionListener(e -> {
            SearchCustomerWithMailWindow searchCustomerWithMailWindow = new SearchCustomerWithMailWindow();
            searchCustomerWithMailWindow.setVisible(true);
        });

        searchSpeedKartFromCustomerReservation.addActionListener(e -> {
            SearchSpeedKartFromCustomerReservation searchSpeedKartFromCustomerReservation = new SearchSpeedKartFromCustomerReservation();
            searchSpeedKartFromCustomerReservation.setVisible(true);
        });

        searchTrackFromAddress.addActionListener(e -> {
            SearchTrackFromAddress searchTrackNameFromAddress = new SearchTrackFromAddress();
            searchTrackNameFromAddress.setVisible(true);
        });

        searchAllRaceFromCustomer.addActionListener(e -> {
            SearchAllRaceFromCustomerWithMail searchAllRaceFromCustomerWithMail = new SearchAllRaceFromCustomerWithMail();
            searchAllRaceFromCustomerWithMail.setVisible(true);
        });

        editCustomer.addActionListener(e -> {
            EditCustomerFormPanel editCustomerFormPanel = new EditCustomerFormPanel();
            setContentPane(editCustomerFormPanel);
            revalidate();
        });

        removeCustomer.addActionListener(e -> {
            RemoveCustomerFormPanel removeCustomerFormPanel = new RemoveCustomerFormPanel();
            setContentPane(removeCustomerFormPanel);
            revalidate();
        });

        addCustomer.addActionListener(e -> {
            AddCustomerFormPanel addCustomerFormPanel = new AddCustomerFormPanel();
            setContentPane(addCustomerFormPanel);
            revalidate();
        });

        raceAffluenceAnalyzer.addActionListener(e -> {
            RaceAffluenceAnalyzer raceAffluenceAnalyzer = new RaceAffluenceAnalyzer();
            raceAffluenceAnalyzer.setVisible(true);
        });

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        // Add panels to the frame
        this.setLayout(new BorderLayout());
        add(imagePanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        this.setVisible(true);


    }

    private void toggleAnimation() {
        if (isPlaying) {
            kartingThread.pauseAnimation();
            playPauseButton.setText("Play Animation");
        } else {
            kartingThread.resumeAnimation();
            playPauseButton.setText("Pause Animation");
        }
        isPlaying = !isPlaying;
    }

    public void updateImageIcon(ImageIcon icon) {
        imageLabel.setIcon(icon);
    }
}
