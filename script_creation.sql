USE Karting;

-- Création des tables (si ce n'est pas déjà fait)
CREATE TABLE Karting.Locality (
                                  Label VARCHAR(255) NOT NULL,
                                  PostalCode VARCHAR(10) NOT NULL CHECK (PostalCode > 0),
                                  PRIMARY KEY (Label, PostalCode),
                                  CONSTRAINT check_locality_name CHECK (Label REGEXP '^[a-zA-ZÀ-ÿ\- ]*$')
);

CREATE TABLE Karting.Address (
                                 Number INT NOT NULL CHECK (Number > 0),
                                 StreetLabel VARCHAR(255) NOT NULL,
                                 Locality_Label VARCHAR(255) NOT NULL,
                                 Locality_PostalCode VARCHAR(10) NOT NULL,
                                 PRIMARY KEY (Number, StreetLabel),
                                 INDEX FK_locality_idx (Locality_Label, Locality_PostalCode) VISIBLE,
    INDEX StreetLabel_StreetNumber_idx (StreetLabel, Number) VISIBLE,
    CONSTRAINT check_address_street CHECK (StreetLabel REGEXP '^[a-zA-ZÀ-ÿ\- ]*$'),
    CONSTRAINT FK_locality_address
        FOREIGN KEY (Locality_Label, Locality_PostalCode)
        REFERENCES Karting.Locality (Label, PostalCode)
);

CREATE TABLE Karting.Customer (
                                  Email VARCHAR(255) NOT NULL,
                                  FirstName VARCHAR(30) NOT NULL,
                                  LastName VARCHAR(30) NOT NULL,
                                  BirthDate DATE NOT NULL,
                                  PhoneNumber VARCHAR(12) NULL DEFAULT NULL,
                                  Gender VARCHAR(10) NULL DEFAULT NULL,
                                  IsProfessional BOOLEAN NOT NULL,
                                  StreetLabel VARCHAR(255) NOT NULL,
                                  StreetNumber INT NOT NULL CHECK (StreetNumber > 0),
                                  Locality_Label VARCHAR(255) NOT NULL,
                                  Locality_PostalCode VARCHAR(10) NOT NULL,
                                  PRIMARY KEY (Email),
                                  INDEX FK_address_idx (StreetLabel, StreetNumber) VISIBLE,
    CONSTRAINT check_firstName CHECK (FirstName REGEXP '^[a-zA-ZÀ-ÿ\- ]*$'),
    CONSTRAINT check_lastName CHECK (LastName REGEXP '^[a-zA-ZÀ-ÿ\- ]*$'),
    CONSTRAINT check_gender CHECK (Gender REGEXP '^[a-zA-Z]*$'),
    CONSTRAINT check_email CHECK (Email REGEXP '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$'),
    CONSTRAINT FK_address_customer
        FOREIGN KEY (StreetLabel, StreetNumber)
        REFERENCES Karting.Address (StreetLabel, Number),
    CONSTRAINT FK_locality_customer
        FOREIGN KEY (Locality_Label, Locality_PostalCode)
        REFERENCES Karting.Locality (Label, PostalCode)
);

CREATE TABLE Karting.Track (
                               Label VARCHAR(255) NOT NULL,
                               Length INT NOT NULL,
                               IsOfficial BOOLEAN NOT NULL,
                               Address_Number INT,
                               Address_StreetLabel VARCHAR(255),
                               PRIMARY KEY (Label),
                               INDEX FK_address_idx (Address_Number, Address_StreetLabel) VISIBLE,
    CONSTRAINT FK_address_track
        FOREIGN KEY (Address_Number, Address_StreetLabel)
        REFERENCES Karting.Address (Number, StreetLabel)
);

CREATE TABLE IF NOT EXISTS Karting.Category (
                                                Label VARCHAR(255) NOT NULL,
                                                SpeedLimit INT NOT NULL,
                                                PRIMARY KEY (Label)
);

CREATE TABLE Karting.Kart (
                              Number INT NOT NULL,
                              Color VARCHAR(50) NOT NULL,
                              Category_Label VARCHAR(255),
                              PRIMARY KEY (Number),
                              INDEX FK_category_idx (Category_Label) VISIBLE,
    CONSTRAINT FK_category
        FOREIGN KEY (Category_Label)
        REFERENCES Karting.Category (Label)
);

CREATE TABLE Karting.Race (
                              Id INT AUTO_INCREMENT,
                              Track_Label VARCHAR(255) NOT NULL,
                              Category_Label VARCHAR(255) NOT NULL,
                              Date DATE NOT NULL,
                              StartTime TIME NOT NULL,
                              EndTime TIME NOT NULL,
                              PRIMARY KEY (Id),
                              INDEX FK_track_idx (Track_Label) VISIBLE,
    INDEX FK_category_idx (Category_Label) VISIBLE,
    CONSTRAINT FK_track_race
        FOREIGN KEY (Track_Label)
        REFERENCES Karting.Track (Label),
    CONSTRAINT FK_category_race
        FOREIGN KEY (Category_Label)
        REFERENCES Karting.Category (Label)
);

CREATE TABLE Karting.CustomerRace (
                                      Customer_Email VARCHAR(255) NOT NULL,
                                      Race_Id INT NOT NULL,
                                      PRIMARY KEY (Customer_Email, Race_Id),
                                      INDEX FK_customer_idx (Customer_Email) VISIBLE,
    INDEX FK_race_idx (Race_Id) VISIBLE,
    CONSTRAINT FK_customer_race
        FOREIGN KEY (Customer_Email)
        REFERENCES Karting.Customer (Email)
        ON DELETE CASCADE,
    CONSTRAINT FK_race_customer
        FOREIGN KEY (Race_Id)
        REFERENCES Karting.Race (Id)
        ON DELETE CASCADE
);