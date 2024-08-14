package dataAccessPackage;

import exceptionPackage.ConnectionException;
import exceptionPackage.ExistingElementException;
import exceptionPackage.UnfoundResearchException;
import exceptionPackage.WrongArgumentException;
import modelPackage.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public interface DataAccess {

    //recover all localities
    ArrayList<String> getLocalities() throws ConnectionException, UnfoundResearchException, SQLException;

    //CRUD customer
    ArrayList<Customer> getAllCustomers() throws ConnectionException, UnfoundResearchException, SQLException;
    void addCustomer(Customer customer) throws ConnectionException, ExistingElementException, SQLException;
    void editCustomer(Customer customer) throws ConnectionException, WrongArgumentException, UnfoundResearchException, ExistingElementException;
    void removeCustomer(Customer customer) throws ConnectionException;

    //searches
    Customer searchCustomerWithMail(String email) throws UnfoundResearchException, ConnectionException;
    List<Race> searchSpeedKartFromCustomerReservation(String mail) throws ConnectionException, UnfoundResearchException, SQLException;
    String searchTrackNameFromAddress(String label, String postalCode) throws ConnectionException, UnfoundResearchException;

    //recover all tracks
    ArrayList<Track> getAllTracks() throws ConnectionException, UnfoundResearchException, SQLException;

    ArrayList<Category> getAllCategories() throws UnfoundResearchException;

    List<Race> getRacesByTrackAndCategory(Track selectedTrack, Category selectedCategory, LocalDate date) throws ConnectionException, UnfoundResearchException;
    List<Race> getAllRaces() throws UnfoundResearchException;

    void addCustomerToARace(Customer customer, Race race, Category category, Track track) throws ConnectionException, ExistingElementException, SQLException;
    void addRace(Track track, Category category, LocalDateTime dateTime) throws ConnectionException, ExistingElementException, SQLException;
    void removeCustomerFromARace(Customer customer, Race race) throws ConnectionException, UnfoundResearchException;
    void removeRace(Race race, Category category, Track track) throws ConnectionException;

    List<Race> getRacesByCustomer(String email) throws ConnectionException;

    Track searchTrackFromAddress(Address address) throws ConnectionException;

    List<TrackReservation> raceAffluenceAnalyzer(String tracksName) throws ConnectionException;

    void editRace(int raceId, Track selectedTrack, Category selectedCategory, LocalDate date, LocalTime startTime) throws ConnectionException;

}
