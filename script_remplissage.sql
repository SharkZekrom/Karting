USE Karting;

-- Insertion des localités
INSERT INTO Karting.Locality (Label, PostalCode) VALUES
                                                     ('Bruxelles', '1000'),
                                                     ('Antwerpen', '2000'),
                                                     ('Gent', '9000'),
                                                     ('Liège', '4000'),
                                                     ('Charleroi', '6000');

-- Insertion des adresses
INSERT INTO Karting.Address (Number, StreetLabel, Locality_Label, Locality_PostalCode) VALUES
                                                                                           (1, 'Avenue Louise', 'Bruxelles', '1000'),
                                                                                           (25, 'Korte Keten', 'Antwerpen', '2000'),
                                                                                           (15, 'Burgstraat', 'Gent', '9000'),
                                                                                           (10, 'Rue du Parc', 'Liège', '4000'),
                                                                                           (5, 'Boulevard Joseph II', 'Charleroi', '6000');

-- Insertion des clients
INSERT INTO Karting.Customer (Email, FirstName, LastName, BirthDate, PhoneNumber, Gender, IsProfessional, StreetLabel, StreetNumber, Locality_Label, Locality_PostalCode) VALUES
                                                                                                                                                                              ('jean.dupond@example.com', 'Jean', 'Dupond', '1980-05-15', '0471234567', 'Male', FALSE, 'Avenue Louise', 1, 'Bruxelles', '1000'),
                                                                                                                                                                              ('marie.durand@example.com', 'Marie', 'Durand', '1992-08-22', '0498765432', 'Female', TRUE, 'Korte Keten', 25, 'Antwerpen', '2000'),
                                                                                                                                                                              ('pierre.martin@example.com', 'Pierre', 'Martin', '1985-12-01', '0466543210', 'Male', FALSE, 'Burgstraat', 15, 'Gent', '9000'),
                                                                                                                                                                              ('sophie.dupuis@example.com', 'Sophie', 'Dupuis', '1978-03-14', '0478654321', 'Female', TRUE, 'Rue du Parc', 10, 'Liège', '4000'),
                                                                                                                                                                              ('lucas.fernand@example.com', 'Lucas', 'Fernand', '1990-11-30', '0487564321', 'Male', FALSE, 'Boulevard Joseph II', 5, 'Charleroi', '6000'),
                                                                                                                                                                              ('alice.roche@example.com', 'Alice', 'Roche', '1989-04-17', '0477890123', 'Female', FALSE, 'Avenue Louise', 1, 'Bruxelles', '1000'),
                                                                                                                                                                              ('julien.morel@example.com', 'Julien', 'Morel', '1995-02-12', '0498765433', 'Male', TRUE, 'Korte Keten', 25, 'Antwerpen', '2000'),
                                                                                                                                                                              ('emilie.girard@example.com', 'Emilie', 'Girard', '1984-07-23', '0466543211', 'Female', FALSE, 'Burgstraat', 15, 'Gent', '9000'),
                                                                                                                                                                              ('nicolas.denis@example.com', 'Nicolas', 'Denis', '1981-11-08', '0478654322', 'Male', TRUE, 'Rue du Parc', 10, 'Liège', '4000'),
                                                                                                                                                                              ('claire.dupont@example.com', 'Claire', 'Dupont', '1993-05-05', '0487564322', 'Female', FALSE, 'Boulevard Joseph II', 5, 'Charleroi', '6000');

-- Insertion des catégories
INSERT INTO Karting.Category (Label, SpeedLimit) VALUES
                                                     ('Novice', 40),
                                                     ('Intermediary', 60),
                                                     ('Advanced', 80),
                                                     ('Expert', 100);

-- Insertion des karts
INSERT INTO Karting.Kart (Number, Color, Category_Label) VALUES
                                                             (1, 'Red', 'Novice'),
                                                             (2, 'Blue', 'Intermediary'),
                                                             (3, 'Green', 'Advanced'),
                                                             (4, 'Yellow', 'Expert');

-- Insertion des pistes
INSERT INTO Karting.Track (Label, Length, IsOfficial, Address_Number, Address_StreetLabel) VALUES
                                                                                               ('Circuit de Bruxelles', 1500, TRUE, 1, 'Avenue Louise'),
                                                                                               ('Antwerp Track', 1200, TRUE, 25, 'Korte Keten'),
                                                                                               ('Gent Karting', 1000, FALSE, 15, 'Burgstraat'),
                                                                                               ('Liège Speedway', 1600, TRUE, 10, 'Rue du Parc'),
                                                                                               ('Charleroi Circuit', 1400, FALSE, 5, 'Boulevard Joseph II');

-- Insertion des courses avec créneaux horaires de 30 minutes
INSERT INTO Karting.Race (Track_Label, Category_Label, Date, StartTime, EndTime) VALUES
                                                                                     ('Circuit de Bruxelles', 'Novice', '2024-08-01', '10:00:00', '10:30:00'),
                                                                                     ('Antwerp Track', 'Intermediary', '2024-08-05', '14:00:00', '14:30:00'),
                                                                                     ('Gent Karting', 'Advanced', '2024-08-10', '09:00:00', '09:30:00'),
                                                                                     ('Liège Speedway', 'Expert', '2024-08-15', '13:00:00', '13:30:00'),
                                                                                     ('Charleroi Circuit', 'Novice', '2024-08-20', '11:00:00', '11:30:00');

-- Associer 10 clients à une course avec Race_Id = 1
INSERT INTO Karting.CustomerRace (Customer_Email, Race_Id) VALUES
                                                               ('jean.dupond@example.com', 1),
                                                               ('marie.durand@example.com', 1),
                                                               ('pierre.martin@example.com', 1),
                                                               ('sophie.dupuis@example.com', 1),
                                                               ('lucas.fernand@example.com', 1),
                                                               ('alice.roche@example.com', 1),
                                                               ('julien.morel@example.com', 1),
                                                               ('emilie.girard@example.com', 1),
                                                               ('nicolas.denis@example.com', 1),
                                                               ('claire.dupont@example.com', 1);
