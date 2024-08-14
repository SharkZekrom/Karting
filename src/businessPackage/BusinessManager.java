package businessPackage;

import dataAccessPackage.DBAccess;
import dataAccessPackage.DataAccess;
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

public class BusinessManager {

    private DataAccess da;

    public BusinessManager(){
        setDa(new DBAccess());
    }

    public void setDa(DataAccess da) {
        this.da = da;
    }

    public ArrayList<String> getLocalities() throws UnfoundResearchException, ConnectionException, SQLException {
        return da.getLocalities();
    }

    public ArrayList<Customer> getAllCustomers() throws ConnectionException, UnfoundResearchException, SQLException {
        return da.getAllCustomers();
    }

    public void addCustomer(Customer customer) throws ExistingElementException, SQLException, ConnectionException {
        da.addCustomer(customer);
    }

    public void editCustomer(Customer customer) throws ConnectionException, UnfoundResearchException, ExistingElementException {
        da.editCustomer(customer);
    }

    public List<Race> searchSpeedKartFromCustomerReservation(String mail) throws ConnectionException, UnfoundResearchException, SQLException {
        return da.searchSpeedKartFromCustomerReservation(mail);
    }

    public String searchTrackNameFromAddress(String label, String postalCode) throws ConnectionException, UnfoundResearchException {
        return da.searchTrackNameFromAddress(label, postalCode);
    }


    public ArrayList<Track> getAllTracks() throws UnfoundResearchException, ConnectionException, SQLException {
        return da.getAllTracks();
    }

    public ArrayList<Category> getAllCategories() throws UnfoundResearchException {
        return da.getAllCategories();
    }

    public List<Race> getRacesByTrackAndCategory(Track selectedTrack, Category selectedCategory, LocalDate date) throws UnfoundResearchException, ConnectionException {
        return da.getRacesByTrackAndCategory(selectedTrack, selectedCategory, date);
    }

    public void addCustomerToARace(Customer selectedCustomer, Race selectedRace, Category selectedCategory, Track selectedTrack) throws ExistingElementException, SQLException, ConnectionException {
        da.addCustomerToARace(selectedCustomer, selectedRace, selectedCategory, selectedTrack);
    }

    public void addRace(Track track, Category category, LocalDateTime dateTime) throws ExistingElementException, SQLException, ConnectionException {
        da.addRace(track, category, dateTime);
    }

    public List<Race> getRacesByCustomer(String email) throws ConnectionException {
        return da.getRacesByCustomer(email);
    }

    public void removeCustomerFromARace(Customer customer, Race race) throws ConnectionException, UnfoundResearchException {
        da.removeCustomerFromARace(customer, race);
    }

    public void removeRace(Race race, Category category, Track track) throws ConnectionException {
        da.removeRace(race, category, track);
    }

    public Customer searchCustomerWithMail(String email) throws UnfoundResearchException, ConnectionException {
        return da.searchCustomerWithMail(email);
    }

    public Track searchTrackFromAddress(Address address) throws ConnectionException {
        return da.searchTrackFromAddress(address);
    }

    public List<TrackReservation> raceAffluenceAnalyzer(String tracksName) throws ConnectionException {
        return da.raceAffluenceAnalyzer(tracksName);
    }

    public void editRace(int raceId, Track selectedTrack, Category selectedCategory, LocalDate date, LocalTime startTime) throws ConnectionException {
        da.editRace(raceId, selectedTrack, selectedCategory, date, startTime);
    }

    public List<Race> getAllRaces() throws UnfoundResearchException {
        return da.getAllRaces();
    }


    public void removeCustomer(Customer customer) throws ConnectionException {
        da.removeCustomer(customer);
    }
}
