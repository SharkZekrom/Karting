package modelPackage;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class Race {

    private int id;                  // Identifiant unique pour la course
    private LocalDate date;          // Date de la course
    private LocalTime startTime;     // Heure de début
    private LocalTime endTime;       // Heure de fin
    private Category category;
    private Track track;
    private ArrayList<Customer> customers; // Liste des clients inscrits à la course
    private final int MAX_CUSTOMERS = 10; // Nombre maximum de clients inscrits à la course
    // Constructeur
    public Race(int id, LocalDate date, LocalTime startTime, LocalTime endTime, ArrayList<Customer> customers) {
        this.id = id;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.customers = new ArrayList<>();

        for (Customer customer : customers) {
            addCustomer(customer);
        }
    }

    // Getters et setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public ArrayList<Customer> getCustomers() {
        return customers;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    // Ajouter un client à la course
    public void addCustomer(Customer customer) {
        if (customers.size() < MAX_CUSTOMERS) {
            customers.add(customer);
        }
    }

    // Supprimer un client de la course
    public void removeCustomer(Customer customer) {
        customers.remove(customer);
    }

    // Vérifier si un client est inscrit à la course
    public boolean hasCustomer(String email) {
        for (Customer customer : customers) {
            if (customer.getEmail().equals(email)) {
                return true;
            }
        }
        return false;
    }

    // Vérifier si la course est complète
    public boolean isFull() {
        return customers.size() == MAX_CUSTOMERS;
    }

    // ajout track
    public void setTrack(Track track) {
        this.track = track;
    }

    public Track getTrack() {
        return track;
    }

    // ajout category
    public void setCategory(Category category) {
        this.category = category;
    }

    public Category getCategory() {
        return category;
    }


    @Override
    public String toString() {
        return "[" + startTime + " - " + endTime + "] " + customers.size() + "/10";
    }
}
