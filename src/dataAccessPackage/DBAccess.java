package dataAccessPackage;

import exceptionPackage.ConnectionException;
import exceptionPackage.ExistingElementException;
import exceptionPackage.UnfoundResearchException;
import exceptionPackage.WrongArgumentException;
import modelPackage.*;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

public class DBAccess implements DataAccess {

    @Override
    public ArrayList<String> getLocalities() throws ConnectionException, UnfoundResearchException {
        try {
            Connection connection = SingletonConnection.getInstance();
            String query = "SELECT Label FROM Locality";
            Statement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery(query);
            ArrayList<String> localities = new ArrayList<>();
            while (resultSet.next()){
                localities.add(resultSet.getString("Label"));
            }
            return localities;
        } catch (SQLException sqlException) {
            System.out.println("Erreur : " + sqlException.getMessage());
            throw new UnfoundResearchException("Erreur : aucune localité enregistrée.");
        }
    }

    @Override
    public ArrayList<Customer> getAllCustomers() throws ConnectionException, UnfoundResearchException {
        try {
            Connection connection = SingletonConnection.getInstance();
            String query = "SELECT c.Email, c.FirstName, c.LastName, c.BirthDate, c.Gender, c.PhoneNumber, c.IsProfessional, " +
                    "a.Number AS StreetNumber, a.StreetLabel, l.Label, l.PostalCode " +
                    "FROM karting.customer c " +
                    "JOIN karting.address a ON c.StreetLabel = a.StreetLabel AND c.StreetNumber = a.Number " +
                    "JOIN karting.locality l ON c.Locality_Label = l.Label AND c.Locality_PostalCode = l.PostalCode";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            ArrayList<Customer> customers = new ArrayList<>();
            while (resultSet.next()) {
                Customer customer = new Customer(
                        resultSet.getString("Email"),
                        resultSet.getString("Label"),
                        resultSet.getString("PostalCode"),
                        resultSet.getInt("StreetNumber"),
                        resultSet.getString("StreetLabel"),
                        resultSet.getString("FirstName"),
                        resultSet.getString("LastName"),
                        resultSet.getDate("BirthDate"),
                        resultSet.getString("Gender"),
                        resultSet.getString("PhoneNumber"),
                        resultSet.getBoolean("IsProfessional")
                );
                customers.add(customer);
            }
            return customers;

        } catch (SQLException sqlException) {
            System.out.println("Erreur : " + sqlException.getMessage());
            throw new UnfoundResearchException("Erreur : aucun client trouvé");
        }
    }


    @Override
    public void addCustomer(Customer customer) throws ConnectionException, ExistingElementException {
        Connection connection = SingletonConnection.getInstance();
        try {

            connection.setAutoCommit(false); // Démarrer une transaction

            // 1. Vérifier et insérer la localité si elle n'existe pas
            String localityCheckQuery = "SELECT COUNT(*) FROM Karting.Locality WHERE Label = ? AND PostalCode = ?";
            PreparedStatement localityCheckStatement = connection.prepareStatement(localityCheckQuery);
            localityCheckStatement.setString(1, customer.getLocality());
            localityCheckStatement.setString(2, customer.getPostCode());
            ResultSet localityResultSet = localityCheckStatement.executeQuery();

            if (localityResultSet.next() && localityResultSet.getInt(1) == 0) {
                // Insérer la localité
                String insertLocalityQuery = "INSERT INTO Karting.Locality (Label, PostalCode) VALUES (?, ?)";
                PreparedStatement insertLocalityStatement = connection.prepareStatement(insertLocalityQuery);
                insertLocalityStatement.setString(1, customer.getLocality());
                insertLocalityStatement.setString(2, customer.getPostCode());
                insertLocalityStatement.executeUpdate();
            }

            // 2. Vérifier et insérer l'adresse si elle n'existe pas
            String addressCheckQuery = "SELECT COUNT(*) FROM Karting.Address WHERE StreetLabel = ? AND Number = ?";
            PreparedStatement addressCheckStatement = connection.prepareStatement(addressCheckQuery);
            addressCheckStatement.setString(1, customer.getStreet());
            addressCheckStatement.setInt(2, customer.getAddressNumber());
            ResultSet addressResultSet = addressCheckStatement.executeQuery();

            if (addressResultSet.next() && addressResultSet.getInt(1) == 0) {
                // Insérer l'adresse
                String insertAddressQuery = "INSERT INTO Karting.Address (Number, StreetLabel, Locality_Label, Locality_PostalCode) VALUES (?, ?, ?, ?)";
                PreparedStatement insertAddressStatement = connection.prepareStatement(insertAddressQuery);
                insertAddressStatement.setInt(1, customer.getAddressNumber());
                insertAddressStatement.setString(2, customer.getStreet());
                insertAddressStatement.setString(3, customer.getLocality());
                insertAddressStatement.setString(4, customer.getPostCode());
                insertAddressStatement.executeUpdate();
            }

            // 3. Vérifier si le client existe déjà
            String checkCustomerQuery = "SELECT COUNT(*) FROM Karting.Customer WHERE Email = ?";
            PreparedStatement checkCustomerStatement = connection.prepareStatement(checkCustomerQuery);
            checkCustomerStatement.setString(1, customer.getEmail());
            ResultSet customerResultSet = checkCustomerStatement.executeQuery();

            if (customerResultSet.next() && customerResultSet.getInt(1) > 0) {
                connection.rollback(); // Annuler la transaction en cas de doublon
                throw new ExistingElementException("Erreur : le client que vous souhaitez ajouter est déjà enregistré");
            }

            // 4. Insérer le nouveau client
            String insertCustomerQuery = "INSERT INTO Karting.Customer (Email, FirstName, LastName, BirthDate, Gender, PhoneNumber, IsProfessional, StreetLabel, StreetNumber, Locality_Label, Locality_PostalCode) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement insertCustomerStatement = connection.prepareStatement(insertCustomerQuery);
            insertCustomerStatement.setString(1, customer.getEmail());
            insertCustomerStatement.setString(2, customer.getFirstName());
            insertCustomerStatement.setString(3, customer.getLastName());
            insertCustomerStatement.setDate(4, new Date(customer.getBirthDate().getTime()));
            insertCustomerStatement.setString(5, customer.getGender());
            insertCustomerStatement.setString(6, customer.getPhoneNumber());
            insertCustomerStatement.setBoolean(7, customer.isProfessional());
            insertCustomerStatement.setString(8, customer.getStreet());
            insertCustomerStatement.setInt(9, customer.getAddressNumber());
            insertCustomerStatement.setString(10, customer.getLocality());
            insertCustomerStatement.setString(11, customer.getPostCode());
            insertCustomerStatement.executeUpdate();

            connection.commit(); // Valider la transaction

        } catch(SQLException sqlException) {
            try {
                connection.rollback(); // Annuler la transaction en cas d'erreur SQL
            } catch (SQLException rollbackException) {
                System.err.println("Erreur lors du rollback: " + rollbackException.getMessage());
            }
            System.err.println("SQL Error Code: " + sqlException.getErrorCode());
            System.err.println("SQL State: " + sqlException.getSQLState());
            System.err.println("Message: " + sqlException.getMessage());
            throw new ConnectionException("Erreur lors de la connexion à la base de données");
        } catch (IllegalArgumentException e) {
            System.err.println("Validation Error: " + e.getMessage());
            throw new IllegalArgumentException("Erreur : " + e.getMessage());
        } finally {
            try {
                connection.setAutoCommit(true); // Réactiver le mode de validation automatique
            } catch (SQLException e) {
                System.err.println("Erreur lors de la réactivation du mode de validation automatique: " + e.getMessage());
            }
        }
    }

    @Override
    public void editCustomer(Customer customer) throws ConnectionException, WrongArgumentException, UnfoundResearchException, ExistingElementException {
        Connection connection = SingletonConnection.getInstance();
        PreparedStatement selectLocalityStatement = null;
        PreparedStatement insertLocalityStatement = null;
        PreparedStatement updateCustomerStatement = null;
        PreparedStatement selectAddressStatement = null;
        PreparedStatement updateAddressStatement = null;
        ResultSet localityResult = null;
        ResultSet addressResult = null;

        try {
            connection.setAutoCommit(false); // Start transaction

            // Vérifier si la localité existe
            String selectLocalityQuery = "SELECT Label, PostalCode FROM Locality WHERE Label = ? AND PostalCode = ?";
            selectLocalityStatement = connection.prepareStatement(selectLocalityQuery);
            selectLocalityStatement.setString(1, customer.getLocality());
            selectLocalityStatement.setString(2, customer.getPostCode());
            localityResult = selectLocalityStatement.executeQuery();

            boolean localityExists = localityResult.next();

            if (!localityExists) {
                // Insérer la localité si elle n'existe pas
                String insertLocalityQuery = "INSERT INTO Locality (Label, PostalCode) VALUES (?, ?)";
                insertLocalityStatement = connection.prepareStatement(insertLocalityQuery);
                insertLocalityStatement.setString(1, customer.getLocality());
                insertLocalityStatement.setString(2, customer.getPostCode());
                int localityRowsInserted = insertLocalityStatement.executeUpdate();

                if (localityRowsInserted == 0) {
                    throw new SQLException("Failed to insert new locality.");
                }
            }

            // Vérifier si l'adresse existe déjà
            String selectAddressQuery = "SELECT Number, StreetLabel FROM Address WHERE Number = ? AND StreetLabel = ?";
            selectAddressStatement = connection.prepareStatement(selectAddressQuery);
            selectAddressStatement.setInt(1, customer.getAddressNumber());
            selectAddressStatement.setString(2, customer.getStreet());
            addressResult = selectAddressStatement.executeQuery();

            boolean addressExists = addressResult.next();

            if (addressExists) {
                // Mettre à jour l'adresse si elle existe
                String updateAddressQuery = "UPDATE Address SET Locality_Label = ?, Locality_PostalCode = ? WHERE Number = ? AND StreetLabel = ?";
                updateAddressStatement = connection.prepareStatement(updateAddressQuery);
                updateAddressStatement.setString(1, customer.getLocality());
                updateAddressStatement.setString(2, customer.getPostCode());
                updateAddressStatement.setInt(3, customer.getAddressNumber());
                updateAddressStatement.setString(4, customer.getStreet());

                int addressRowsUpdated = updateAddressStatement.executeUpdate();

                if (addressRowsUpdated == 0) {
                    throw new SQLException("Failed to update address.");
                }
            } else {
                // Insérer l'adresse si elle n'existe pas (Optionnel, dépend de votre logique métier)
                // Vous pourriez avoir un bloc ici pour insérer l'adresse si nécessaire
            }

            // Maintenant, mettons à jour les informations du client
            String updateCustomerQuery = "UPDATE Customer SET FirstName = ?, LastName = ?, BirthDate = ?, PhoneNumber = ?, Gender = ?, IsProfessional = ?, StreetLabel = ?, StreetNumber = ?, Locality_Label = ?, Locality_PostalCode = ? WHERE Email = ?";
            updateCustomerStatement = connection.prepareStatement(updateCustomerQuery);

            // Assigner les valeurs aux paramètres de la requête UPDATE
            updateCustomerStatement.setString(1, customer.getFirstName());
            updateCustomerStatement.setString(2, customer.getLastName());
            updateCustomerStatement.setDate(3, new java.sql.Date(customer.getBirthDate().getTime()));
            updateCustomerStatement.setString(4, customer.getPhoneNumber());
            updateCustomerStatement.setString(5, customer.getGender());
            updateCustomerStatement.setBoolean(6, customer.isProfessional());
            updateCustomerStatement.setString(7, customer.getStreet());
            updateCustomerStatement.setInt(8, customer.getAddressNumber());
            updateCustomerStatement.setString(9, customer.getLocality());
            updateCustomerStatement.setString(10, customer.getPostCode());
            updateCustomerStatement.setString(11, customer.getEmail());

            int rowsUpdated = updateCustomerStatement.executeUpdate();

            if (rowsUpdated == 0) {
                throw new UnfoundResearchException("Customer not found or update failed.");
            }

            connection.commit(); // Commit transaction

        } catch (SQLException e) {
            try {
                connection.rollback(); // Rollback transaction on error
            } catch (SQLException rollbackEx) {
                throw new ConnectionException("Rollback failed: " + rollbackEx.getMessage());
            }
            throw new ConnectionException("Erreur lors de la mise à jour du client : " + e.getMessage());
        }
    }






    @Override
    public List<Race> searchSpeedKartFromCustomerReservation(String email) throws ConnectionException, UnfoundResearchException, SQLException {
        List<Race> races = getRacesByCustomer(email);
        Connection connection = SingletonConnection.getInstance();
        String query = "SELECT cat.Label, cat.SpeedLimit " +
                "FROM Kart k " +
                "JOIN Category cat ON k.Category_Label = cat.Label " +
                "JOIN Race r ON cat.Label = r.Category_Label " +
                "JOIN CustomerRace cr ON r.Id = cr.Race_Id " +
                "WHERE r.Track_Label = ? AND cr.Customer_Email = ?";
        PreparedStatement statement = connection.prepareStatement(query);

        try {
            for (Race race : races) {
                statement.setInt(1, race.getId());
                statement.setString(2, email);
                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    String categoryLabel = resultSet.getString("Label");
                    int speedLimit = resultSet.getInt("SpeedLimit");

                    System.out.println("speed : " +speedLimit);
                    // Assuming Category has a constructor that takes label and speed limit
                    Category category = new Category(categoryLabel, speedLimit);
                    race.setCategory(category);  // Ensure this method exists in Race
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (races.isEmpty()) {
            throw new UnfoundResearchException("No kart reservations found for this customer");
        }

        return races;
    }






    @Override
    public String searchTrackNameFromAddress(String label, String postalCode) throws ConnectionException, UnfoundResearchException {
        return "";
    }

    @Override
    public ArrayList<Track> getAllTracks() throws UnfoundResearchException {
        try {
            Connection connection = SingletonConnection.getInstance();
            // La requête SQL pour récupérer les informations des pistes et des adresses
            String query = "SELECT t.Label AS TrackLabel, t.Length, t.IsOfficial, " +
                    "a.Number AS AddressNumber, a.StreetLabel AS AddressStreetLabel " +
                    "FROM karting.track t " +
                    "LEFT JOIN karting.address a ON t.Address_Number = a.Number AND t.Address_StreetLabel = a.StreetLabel";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            ArrayList<Track> tracks = new ArrayList<>();
            while (resultSet.next()) {
                // Créer un objet Address à partir des données récupérées
                Address address = new Address(
                        resultSet.getInt("AddressNumber"),
                        resultSet.getString("AddressStreetLabel")
                );
                // Créer un objet Track en passant l'objet Address comme paramètre
                Track track = new Track(
                        resultSet.getString("TrackLabel"),
                        resultSet.getInt("Length"),
                        resultSet.getBoolean("IsOfficial"),
                        address
                );
                tracks.add(track);
            }
            return tracks;

        } catch (SQLException | ConnectionException sqlException) {
            System.out.println("Erreur : " + sqlException.getMessage());
            throw new UnfoundResearchException("Erreur : aucune piste trouvée");
        }
    }


    public ArrayList<Category> getAllCategories() throws UnfoundResearchException {
        try {
            Connection connection = SingletonConnection.getInstance();
            // La requête SQL pour récupérer les informations des pistes et des adresses
            String query = "SELECT * FROM category";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            ArrayList<Category> categories = new ArrayList<>();
            while (resultSet.next()) {
                // Créer un objet Category à partir des données récupérées
                Category category = new Category(
                        resultSet.getString("label"),
                        resultSet.getInt("speedLimit")
                );

                categories.add(category);
            }
            return categories;

        } catch (SQLException | ConnectionException sqlException) {
            System.out.println("Erreur : " + sqlException.getMessage());
            throw new UnfoundResearchException("Erreur : aucune catégorie trouvée");
        }
    }

    public ArrayList<Race> getRacesByTrackAndCategory(Track track, Category category, LocalDate date) throws UnfoundResearchException, ConnectionException {
        ArrayList<Race> races = new ArrayList<>();
        Connection connection = SingletonConnection.getInstance();
        try {
            // La requête SQL pour récupérer toutes les courses pour la piste, la catégorie et la date sélectionnées
            String query = "SELECT r.Id, r.Track_Label, r.Date, r.StartTime, r.EndTime, " +
                    "c.Email, c.FirstName, c.LastName, c.BirthDate, c.Gender, c.PhoneNumber, c.IsProfessional, " +
                    "a.Number AS AddressNumber, a.StreetLabel AS Street, l.Label AS Locality, l.PostalCode AS PostCode " +
                    "FROM Race r " +
                    "LEFT JOIN CustomerRace cr ON r.Id = cr.Race_Id " +
                    "LEFT JOIN Customer c ON cr.Customer_Email = c.Email " +
                    "LEFT JOIN Address a ON c.StreetLabel = a.StreetLabel AND c.StreetNumber = a.Number AND c.Locality_Label = a.Locality_Label AND c.Locality_PostalCode = a.Locality_PostalCode " +
                    "LEFT JOIN Locality l ON a.Locality_Label = l.Label AND a.Locality_PostalCode = l.PostalCode " +
                    "WHERE r.Track_Label = ? " +
                    "AND r.Category_Label = ? " +
                    "AND r.Date = ?";

            PreparedStatement statement = connection.prepareStatement(query);

            // Définir les paramètres de la requête
            statement.setString(1, track.getLabel());
            statement.setString(2, category.getLabel());
            statement.setDate(3, java.sql.Date.valueOf(date));  // Convertir LocalDate en java.sql.Date

            ResultSet resultSet = statement.executeQuery();

            // Map pour stocker les courses par Id
            Map<Integer, Race> raceMap = new HashMap<>();

            while (resultSet.next()) {
                int raceId = resultSet.getInt("Id");
                Race race = raceMap.get(raceId);

                if (race == null) {
                    race = new Race(
                            raceId,
                            resultSet.getDate("Date").toLocalDate(),
                            resultSet.getTime("StartTime").toLocalTime(),
                            resultSet.getTime("EndTime").toLocalTime(),
                            new ArrayList<>() // Liste vide de clients au début
                    );
                    raceMap.put(raceId, race);
                    races.add(race);
                }

                // Ajout du client à la course s'il y a des données de client

                String email = resultSet.getString("Email");
                if (email != null) {
                    Customer customer = new Customer(
                            email,
                            resultSet.getString("Locality"),
                            resultSet.getString("PostCode"),
                            resultSet.getInt("AddressNumber"),
                            resultSet.getString("Street"),
                            resultSet.getString("FirstName"),
                            resultSet.getString("LastName"),
                            resultSet.getDate("BirthDate"),
                            resultSet.getString("Gender"),
                            resultSet.getString("PhoneNumber"),
                            resultSet.getBoolean("IsProfessional")
                    );
                    race.addCustomer(customer);
                }
            }
        } catch (SQLException sqlException) {
            System.out.println("Erreur : " + sqlException.getMessage());
            throw new UnfoundResearchException("Erreur : aucune course trouvée pour les critères donnés");
        }
        return races;
    }

    @Override
    public List<Race> getAllRaces() throws UnfoundResearchException {
        try {
            Connection connection = SingletonConnection.getInstance();
            String query = "SELECT r.Id, r.Date, r.StartTime, r.EndTime, r.Track_Label, r.Category_Label, " +
                    "t.Length, t.IsOfficial, t.Address_Number, t.Address_StreetLabel, " +
                    "c.SpeedLimit, " +
                    "cu.Email, cu.FirstName, cu.LastName, cu.BirthDate, cu.Gender, cu.PhoneNumber, cu.IsProfessional, " +
                    "a.Number AS AddressNumber, a.StreetLabel AS Street, l.Label AS Locality, l.PostalCode AS PostCode " +
                    "FROM karting.race r " +
                    "JOIN karting.track t ON r.Track_Label = t.Label " +
                    "JOIN karting.category c ON r.Category_Label = c.Label " +
                    "JOIN karting.address a ON t.Address_Number = a.Number AND t.Address_StreetLabel = a.StreetLabel " +
                    "LEFT JOIN karting.CustomerRace cr ON r.Id = cr.Race_Id " +
                    "LEFT JOIN karting.customer cu ON cr.Customer_Email = cu.Email " +
                    "LEFT JOIN karting.Locality l ON a.Locality_Label = l.Label AND a.Locality_PostalCode = l.PostalCode";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            Map<Integer, Race> raceMap = new HashMap<>();
            List<Race> races = new ArrayList<>();

            while (resultSet.next()) {
                int raceId = resultSet.getInt("Id");
                Race race = raceMap.get(raceId);

                if (race == null) {
                    // Créer l'objet Address
                    Address address = new Address(
                            resultSet.getInt("Address_Number"),
                            resultSet.getString("Address_StreetLabel")
                    );

                    // Créer l'objet Track
                    Track track = new Track(
                            resultSet.getString("Track_Label"),
                            resultSet.getInt("Length"),
                            resultSet.getBoolean("IsOfficial"),
                            address
                    );

                    // Créer l'objet Category
                    Category category = new Category(
                            resultSet.getString("Category_Label"),
                            resultSet.getInt("SpeedLimit")
                    );

                    // Créer l'objet Race
                    race = new Race(
                            raceId,
                            resultSet.getDate("Date").toLocalDate(),
                            resultSet.getTime("StartTime").toLocalTime(),
                            resultSet.getTime("EndTime").toLocalTime(),
                            new ArrayList<>() // Liste vide de clients à remplir
                    );

                    race.setTrack(track);   // Ajouter la piste
                    race.setCategory(category); // Ajouter la catégorie

                    raceMap.put(raceId, race);
                    races.add(race);
                }

                // Ajouter le client à la course si les informations sont disponibles
                String email = resultSet.getString("Email");
                if (email != null) {
                    Customer customer = new Customer(
                            email,
                            resultSet.getString("Locality"),
                            resultSet.getString("PostCode"),
                            resultSet.getInt("AddressNumber"),
                            resultSet.getString("Street"),
                            resultSet.getString("FirstName"),
                            resultSet.getString("LastName"),
                            resultSet.getDate("BirthDate"),
                            resultSet.getString("Gender"),
                            resultSet.getString("PhoneNumber"),
                            resultSet.getBoolean("IsProfessional")
                    );
                    race.addCustomer(customer);
                }
            }
            return races;

        } catch (SQLException | ConnectionException sqlException) {
            throw new UnfoundResearchException("Erreur : aucune course trouvée");
        }
    }




    @Override
    public void addCustomerToARace(Customer customer, Race race, Category category, Track track) throws ConnectionException {
        Connection connection = SingletonConnection.getInstance();
        try {
            // La requête SQL pour insérer un nouveau client dans une course
            String insertQuery = "INSERT INTO CustomerRace (Customer_Email, Race_Id) VALUES (?, ?)";
            PreparedStatement insertStatement = connection.prepareStatement(insertQuery);

            // Définir les paramètres de la requête
            insertStatement.setString(1, customer.getEmail());
            insertStatement.setInt(2, race.getId());

            // Exécuter la requête d'insertion
            insertStatement.executeUpdate();
        } catch (SQLException e) {
            throw new ConnectionException("Erreur lors de la connexion à la base de données : " + e.getMessage());
        }
    }

    @Override
    public void addRace(Track track, Category category, LocalDateTime dateTime) throws ConnectionException, ExistingElementException {

        Connection connection = SingletonConnection.getInstance();
        try {
            // Récupérer les courses existantes pour la piste, la catégorie et la date données
            LocalDate date = dateTime.toLocalDate();
            ArrayList<Race> existingRaces = getRacesByTrackAndCategory(track, category, date);

            // Vérifier si une course avec la même heure de début existe déjà
            for (Race race : existingRaces) {
                if (race.getStartTime().equals(dateTime.toLocalTime())) {
                    throw new ExistingElementException("Erreur : la course que vous souhaitez ajouter est déjà enregistrée.");
                }
            }

            // Requête pour insérer une nouvelle course
            String insertQuery = "INSERT INTO Race (Track_Label, Category_Label, Date, StartTime, EndTime) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
            insertStatement.setString(1, track.getLabel());
            insertStatement.setString(2, category.getLabel());
            insertStatement.setDate(3, java.sql.Date.valueOf(date));
            insertStatement.setTime(4, java.sql.Time.valueOf(dateTime.toLocalTime()));

            // Calculer l'heure de fin (30 minutes après l'heure de début)
            LocalDateTime endTime = dateTime.plusMinutes(30);
            insertStatement.setTime(5, java.sql.Time.valueOf(endTime.toLocalTime()));

            // Exécuter la requête d'insertion
            insertStatement.executeUpdate();

        } catch (SQLException e) {
            throw new ExistingElementException("Erreur : la race que vous souhaitez ajouter est déja enregistré");

        } catch (UnfoundResearchException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeCustomerFromARace(Customer customer, Race race) throws ConnectionException, UnfoundResearchException {
        Connection connection = SingletonConnection.getInstance();

        try {
            // Requête SQL pour supprimer une entrée de la table CustomerRace
            String query = "DELETE FROM CustomerRace WHERE Race_Id = ? AND Customer_Email = ?";

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, race.getId());
            statement.setString(2, customer.getEmail());

            statement.executeUpdate();
        } catch (SQLException sqlException) {
            System.out.println("Erreur : " + sqlException.getMessage());
            throw new ConnectionException("Erreur lors de la suppression du client de la course.");
        }
    }


    @Override
    public List<Race> getRacesByCustomer(String email) throws ConnectionException {
        List<Race> races = new ArrayList<>();
        Connection connection = SingletonConnection.getInstance();

        try {
            // La requête SQL pour récupérer toutes les courses associées à un client par email et tous les clients participants
            String query = "SELECT r.Id AS RaceId, r.Track_Label, r.Category_Label, r.Date, r.StartTime, r.EndTime, " +
                    "c.Email AS CustomerEmail, c.FirstName, c.LastName, c.BirthDate, c.Gender, c.PhoneNumber, c.IsProfessional, " +
                    "a.Number AS AddressNumber, a.StreetLabel AS Street, l.Label AS Locality, l.PostalCode AS PostCode, " +
                    "t.Length AS TrackLength, t.IsOfficial AS TrackIsOfficial, t.Address_Number AS TrackAddressNumber, t.Address_StreetLabel AS TrackStreetLabel, " +
                    "cat.SpeedLimit AS CategorySpeedLimit " +
                    "FROM Race r " +
                    "JOIN CustomerRace cr1 ON r.Id = cr1.Race_Id " +
                    "JOIN Customer c1 ON cr1.Customer_Email = c1.Email " +
                    "JOIN CustomerRace cr2 ON r.Id = cr2.Race_Id " +
                    "JOIN Customer c ON cr2.Customer_Email = c.Email " +
                    "LEFT JOIN Address a ON c.StreetLabel = a.StreetLabel AND c.StreetNumber = a.Number AND c.Locality_Label = a.Locality_Label AND c.Locality_PostalCode = a.Locality_PostalCode " +
                    "LEFT JOIN Locality l ON a.Locality_Label = l.Label AND a.Locality_PostalCode = l.PostalCode " +
                    "JOIN Track t ON r.Track_Label = t.Label " +
                    "JOIN Category cat ON r.Category_Label = cat.Label " +
                    "WHERE c1.Email = ?";

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, email);

            ResultSet resultSet = statement.executeQuery();

            // Map pour stocker les courses par Id
            Map<Integer, Race> raceMap = new HashMap<>();

            while (resultSet.next()) {
                int raceId = resultSet.getInt("RaceId");
                Race race = raceMap.get(raceId);

                if (race == null) {
                    // Créer une nouvelle course
                    Track track = new Track(
                            resultSet.getString("Track_Label"),
                            resultSet.getInt("TrackLength"),
                            resultSet.getBoolean("TrackIsOfficial"),
                            new Address(
                                    resultSet.getInt("TrackAddressNumber"),
                                    resultSet.getString("TrackStreetLabel")
                            )
                    );

                    Category category = new Category(
                            resultSet.getString("Category_Label"),
                            resultSet.getInt("CategorySpeedLimit")
                    );

                    race = new Race(
                            raceId,
                            resultSet.getDate("Date").toLocalDate(),
                            resultSet.getTime("StartTime").toLocalTime(),
                            resultSet.getTime("EndTime").toLocalTime(),
                            new ArrayList<>() // Liste vide de clients au début
                    );
                    race.setTrack(track);
                    race.setCategory(category);
                    raceMap.put(raceId, race);
                    races.add(race);
                }

                // Ajouter le client à la course s'il y a des données de client
                String customerEmail = resultSet.getString("CustomerEmail");
                if (customerEmail != null) {
                    Customer customer = new Customer(
                            customerEmail,
                            resultSet.getString("Locality"),
                            resultSet.getString("PostCode"),
                            resultSet.getInt("AddressNumber"),
                            resultSet.getString("Street"),
                            resultSet.getString("FirstName"),
                            resultSet.getString("LastName"),
                            resultSet.getDate("BirthDate"),
                            resultSet.getString("Gender"),
                            resultSet.getString("PhoneNumber"),
                            resultSet.getBoolean("IsProfessional")
                    );
                    // Ajouter le client à la course
                    if (!race.getCustomers().contains(customer)) {
                        race.addCustomer(customer);
                    }
                }
            }

            if (races.isEmpty()) {
                throw new UnfoundResearchException("Aucune course trouvée pour le client avec l'email : " + email);
            }

        } catch (SQLException | UnfoundResearchException sqlException) {
            System.out.println("Erreur : " + sqlException.getMessage());
            throw new ConnectionException("Erreur lors de la récupération des courses pour le client avec l'email : " + email);
        }

        return races;
    }


    @Override
    public void removeRace(Race race, Category category, Track track) throws ConnectionException {
        Connection connection = SingletonConnection.getInstance();

        try {
            connection.setAutoCommit(false); // Commencer une transaction

            // Supprimer d'abord les réservations associées dans CustomerRace
            String deleteCustomerRaceQuery = "DELETE FROM CustomerRace WHERE Race_Id = ?";
            PreparedStatement deleteCustomerRaceStmt = connection.prepareStatement(deleteCustomerRaceQuery);
            deleteCustomerRaceStmt.setInt(1, race.getId());
            deleteCustomerRaceStmt.executeUpdate();

            // Supprimer la course dans la table Race
            String deleteRaceQuery = "DELETE FROM Race WHERE Id = ? AND Track_Label = ? AND Category_Label = ?";
            PreparedStatement deleteRaceStmt = connection.prepareStatement(deleteRaceQuery);
            deleteRaceStmt.setInt(1, race.getId());
            deleteRaceStmt.setString(2, track.getLabel());
            deleteRaceStmt.setString(3, category.getLabel());
            deleteRaceStmt.executeUpdate();

            connection.commit(); // Valider la transaction

        } catch (SQLException sqlException) {
            try {
                connection.rollback(); // Annuler la transaction en cas d'erreur
            } catch (SQLException rollbackException) {
                System.out.println("Erreur lors du rollback : " + rollbackException.getMessage());
            }
            System.out.println("Erreur : " + sqlException.getMessage());
            throw new ConnectionException("Erreur lors de la suppression de la course avec l'ID : " + race.getId());
        } finally {
            try {
                connection.setAutoCommit(true); // Restaurer le mode auto-commit
            } catch (SQLException sqlException) {
                System.out.println("Erreur lors de la restauration du mode auto-commit : " + sqlException.getMessage());
            }
        }
    }

    @Override
    public Track searchTrackFromAddress(Address address) throws ConnectionException {
        Connection connection = SingletonConnection.getInstance();
        Track track = null;

        try {
            // La requête SQL pour récupérer un circuit par adresse
            String query = "SELECT t.Label, t.Length, t.IsOfficial, " +
                    "a.Number AS AddressNumber, a.StreetLabel AS Street " +
                    "FROM Track t " +
                    "LEFT JOIN Address a ON t.Address_Number = a.Number AND t.Address_StreetLabel = a.StreetLabel " +
                    "WHERE a.Number = ? AND a.StreetLabel = ?";

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, address.getAddressNumber());
            statement.setString(2, address.getAddressStreetLabel());

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                track = new Track(
                        resultSet.getString("Label"),
                        resultSet.getInt("Length"),
                        resultSet.getBoolean("IsOfficial"),
                        new Address(
                                resultSet.getInt("AddressNumber"),
                                resultSet.getString("Street")
                        )
                );
            } else {
                throw new UnfoundResearchException("Aucun circuit trouvé pour l'adresse : " + address);
            }

        } catch (SQLException sqlException) {
            System.out.println("Erreur : " + sqlException.getMessage());
            throw new ConnectionException("Erreur lors de la recherche du circuit pour l'adresse : " + address);
        } catch (UnfoundResearchException e) {
            throw new RuntimeException(e);
        }

        return track;
    }

    @Override
    public List<TrackReservation> raceAffluenceAnalyzer(String tracksNames) throws ConnectionException {
        List<TrackReservation> trackReservations = new ArrayList<>();
        Connection connection = SingletonConnection.getInstance();
        String[] tracks;

        try {
            if (tracksNames.equals("0")) {
                tracks = getAllTracks().stream().map(Track::getLabel).toList().toArray(new String[0]);
            } else {
                tracks = tracksNames.split(",");
            }

            // Pour chaque piste, créer un TrackReservation et compter les réservations
            for (String trackName : tracks) {
                trackName = trackName.trim();

                // La requête SQL pour compter les réservations pour une piste spécifique
                String query = "SELECT COUNT(cr.Customer_Email) AS ReservationCount " +
                        "FROM Race r " +
                        "LEFT JOIN CustomerRace cr ON r.Id = cr.Race_Id " +
                        "WHERE r.Track_Label = ?";

                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, trackName);

                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    int reservationCount = resultSet.getInt("ReservationCount");

                    // Créer un TrackReservation pour chaque piste, même si le nombre de réservations est 0
                    TrackReservation trackReservation = new TrackReservation(trackName);
                    for (int i = 0; i < reservationCount; i++) {
                        trackReservation.addReservation();
                    }
                    trackReservations.add(trackReservation);
                } else {
                    // Ajouter la piste avec 0 réservations si aucune réservation n'est trouvée
                    TrackReservation trackReservation = new TrackReservation(trackName);
                    trackReservations.add(trackReservation);
                }
            }

        } catch (SQLException | UnfoundResearchException sqlException) {
            throw new ConnectionException("Erreur lors de l'analyse de l'affluence des pistes");
        }

        // Trier les résultats par ordre décroissant de réservations
        trackReservations.sort(Comparator.comparingInt(TrackReservation::getReservationCount).reversed());

        return trackReservations;
    }




    @Override
    public Customer searchCustomerWithMail(String email) throws UnfoundResearchException, ConnectionException {
        Connection connection = SingletonConnection.getInstance();
        Customer customer = null;

        try {
            // La requête SQL pour récupérer un client par email
            String query = "SELECT c.Email, c.FirstName, c.LastName, c.BirthDate, c.Gender, c.PhoneNumber, c.IsProfessional, " +
                    "a.Number AS AddressNumber, a.StreetLabel AS Street, l.Label AS Locality, l.PostalCode AS PostCode " +
                    "FROM Customer c " +
                    "LEFT JOIN Address a ON c.StreetLabel = a.StreetLabel AND c.StreetNumber = a.Number AND c.Locality_Label = a.Locality_Label AND c.Locality_PostalCode = a.Locality_PostalCode " +
                    "LEFT JOIN Locality l ON a.Locality_Label = l.Label AND a.Locality_PostalCode = l.PostalCode " +
                    "WHERE c.Email = ?";

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, email);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                customer = new Customer(
                        resultSet.getString("Email"),
                        resultSet.getString("Locality"),
                        resultSet.getString("PostCode"),
                        resultSet.getInt("AddressNumber"),
                        resultSet.getString("Street"),
                        resultSet.getString("FirstName"),
                        resultSet.getString("LastName"),
                        resultSet.getDate("BirthDate"),
                        resultSet.getString("Gender"),
                        resultSet.getString("PhoneNumber"),
                        resultSet.getBoolean("IsProfessional")
                );
            } else {
                throw new UnfoundResearchException("Aucun client trouvé avec l'email : " + email);
            }

        } catch (SQLException sqlException) {
            System.out.println("Erreur : " + sqlException.getMessage());
            throw new ConnectionException("Erreur lors de la recherche du client avec l'email : " + email);
        }

        return customer;
    }

    public void editRace(int raceId, Track track, Category category, LocalDate date, LocalTime startTime) throws ConnectionException {
        String query = "UPDATE Race SET Track_Label = ?, Category_Label = ?, Date = ?, StartTime = ? WHERE Id = ?";
        Connection connection = SingletonConnection.getInstance();

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, track.getLabel());
            statement.setString(2, category.getLabel());
            statement.setDate(3, java.sql.Date.valueOf(date));
            statement.setTime(4, java.sql.Time.valueOf(startTime));
            statement.setInt(5, raceId);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                throw new ExistingElementException("No race found with the given ID.");
            }
        } catch (SQLException | ExistingElementException e) {
            throw new ConnectionException("Error while connecting to the database: " + e.getMessage());
        }
    }

    @Override
    public void removeCustomer(Customer customer) throws ConnectionException {
        Connection connection = null;
        try {
            // Obtenez une connexion à la base de données
            connection = SingletonConnection.getInstance();
            connection.setAutoCommit(false); // Démarrer une transaction

            // Vérifier si le client existe
            String checkCustomerQuery = "SELECT COUNT(*) FROM Karting.Customer WHERE Email = ?";
            PreparedStatement checkCustomerStatement = connection.prepareStatement(checkCustomerQuery);
            checkCustomerStatement.setString(1, customer.getEmail());
            ResultSet customerResultSet = checkCustomerStatement.executeQuery();

            if (customerResultSet.next() && customerResultSet.getInt(1) == 0) {
                throw new IllegalArgumentException("Erreur : le client n'existe pas");
            }

            // Supprimer les relations dans CustomerRace
            String deleteCustomerRaceQuery = "DELETE FROM Karting.CustomerRace WHERE Customer_Email = ?";
            PreparedStatement deleteCustomerRaceStatement = connection.prepareStatement(deleteCustomerRaceQuery);
            deleteCustomerRaceStatement.setString(1, customer.getEmail());
            deleteCustomerRaceStatement.executeUpdate();

            // Supprimer le client
            String deleteCustomerQuery = "DELETE FROM Karting.Customer WHERE Email = ?";
            PreparedStatement deleteCustomerStatement = connection.prepareStatement(deleteCustomerQuery);
            deleteCustomerStatement.setString(1, customer.getEmail());
            deleteCustomerStatement.executeUpdate();

            // Valider la transaction
            connection.commit();

        } catch (SQLException sqlException) {
            // Annuler la transaction en cas d'erreur
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackException) {
                    System.err.println("Erreur lors du rollback: " + rollbackException.getMessage());
                }
            }
            System.err.println("SQL Error Code: " + sqlException.getErrorCode());
            System.err.println("SQL State: " + sqlException.getSQLState());
            System.err.println("Message: " + sqlException.getMessage());
            throw new ConnectionException("Erreur lors de la connexion à la base de données");
        }
    }

}