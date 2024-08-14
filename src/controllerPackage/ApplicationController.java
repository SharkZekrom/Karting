package controllerPackage;

import businessPackage.BusinessManager;
import exceptionPackage.ConnectionException;
import exceptionPackage.ExistingElementException;
import exceptionPackage.UnfoundResearchException;
import modelPackage.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ApplicationController {

    private final BusinessManager businessManager;

    public ApplicationController(){
        businessManager = new BusinessManager();
    }

    public ArrayList<Customer> getAllCustomers() throws UnfoundResearchException, SQLException, ConnectionException {
        return businessManager.getAllCustomers();
    }


    public ArrayList<Track> getAllTracks() throws UnfoundResearchException, SQLException, ConnectionException {
        return businessManager.getAllTracks();
    }

    public  ArrayList<Category> getAllCategories() throws UnfoundResearchException {
        return businessManager.getAllCategories();
    }

    public List<Race> getRacesByTrackAndCategory(Track selectedTrack, Category selectedCategory, LocalDate date) throws UnfoundResearchException, ConnectionException {
        return businessManager.getRacesByTrackAndCategory(selectedTrack, selectedCategory, date);
    }

    public void addCustomerToARace(Customer selectedCustomer, Race selectedRace, Category selectedCategory, Track selectedTrack) throws ExistingElementException, SQLException, ConnectionException {
        businessManager.addCustomerToARace(selectedCustomer, selectedRace, selectedCategory, selectedTrack);
    }

    public void addRace(Track track, Category category, LocalDateTime dateTime) throws ExistingElementException, SQLException, ConnectionException {
        businessManager.addRace(track, category, dateTime);
    }

    public List<Race> getRacesByCustomer(String email) throws ConnectionException {
        return businessManager.getRacesByCustomer(email);
    }

    public void removeCustomerFromARace(Customer customer, Race race) throws UnfoundResearchException, ConnectionException {
        businessManager.removeCustomerFromARace(customer, race);
    }

    public void removeRace(Race race, Category category, Track track) throws UnfoundResearchException, ConnectionException {
        businessManager.removeRace(race, category, track);
    }

    public Customer searchCustomerWithEmail(String email) throws UnfoundResearchException, ConnectionException {
        return businessManager.searchCustomerWithMail(email);
    }

    public List<Race> searchSpeedKartFromCustomerReservation(String mail) throws ConnectionException, UnfoundResearchException, SQLException {
        return businessManager.searchSpeedKartFromCustomerReservation(mail);
    }

    public Track searchTrackFromAddress(Address address) throws ConnectionException, UnfoundResearchException {
        return businessManager.searchTrackFromAddress(address);
    }

    public List<TrackReservation> raceAffluenceAnalyzer(String tracksName) throws ConnectionException {
        return businessManager.raceAffluenceAnalyzer(tracksName);
    }

    public void editRace(int raceId, Track selectedTrack, Category selectedCategory, LocalDate date, LocalTime startTime) throws ConnectionException {
        businessManager.editRace(raceId, selectedTrack, selectedCategory, date, startTime);
    }

    public List<Race> getAllRaces() throws UnfoundResearchException {
        return businessManager.getAllRaces();
    }

    public void editCustomer(Customer selectedCustomer) throws UnfoundResearchException, ExistingElementException, ConnectionException {
        businessManager.editCustomer(selectedCustomer);
    }


    public void addCustomer(Customer customer) throws ConnectionException, ExistingElementException, SQLException {
        businessManager.addCustomer(customer);
    }

    public void removeCustomer(Customer customer) throws ConnectionException {
        businessManager.removeCustomer(customer);

    }
}
