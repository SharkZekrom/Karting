package testPackage;

import controllerPackage.ApplicationController;
import modelPackage.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class ApplicationControllerTest {

    private ApplicationController applicationController;
    private ArrayList<Customer> testCustomers;

    @BeforeEach
    public void setUp() {
        applicationController = new ApplicationController();

        // Créer des instances de Customer pour le test
        testCustomers = new ArrayList<>();
        testCustomers.add(new Customer("john.doe@example.com", "New York", "10001", 123, "5th Avenue", "John", "Doe", new Date(), "M", "1234567890", true));
        testCustomers.add(new Customer("jane.smith@example.com", "Los Angeles", "90001", 456, "Sunset Boulevard", "Jane", "Smith", new Date(), "F", "0987654321", false));

        // Dans un test unitaire réaliste, il faudrait s'assurer que la méthode getAllCustomers retourne cette liste
        // Ici, nous faisons une hypothèse pour les besoins du test.
    }

    @Test
    public void testGetAllCustomers() throws Exception {
        // Appeler la méthode getAllCustomers()
        ArrayList<Customer> customers = applicationController.getAllCustomers();

        // Vérifier que la taille de la liste des clients est correcte
        assertEquals(testCustomers.size(), customers.size(), "Le nombre de clients devrait correspondre");

        // Vérifier les détails du premier client
        assertEquals(testCustomers.get(0).getEmail(), customers.get(0).getEmail());
        assertEquals(testCustomers.get(0).getFirstName(), customers.get(0).getFirstName());
        assertEquals(testCustomers.get(0).getLastName(), customers.get(0).getLastName());
        assertEquals(testCustomers.get(0).getPhoneNumber(), customers.get(0).getPhoneNumber());

        // Vérifier les détails du second client
        assertEquals(testCustomers.get(1).getEmail(), customers.get(1).getEmail());
        assertEquals(testCustomers.get(1).getFirstName(), customers.get(1).getFirstName());
        assertEquals(testCustomers.get(1).getLastName(), customers.get(1).getLastName());
        assertEquals(testCustomers.get(1).getPhoneNumber(), customers.get(1).getPhoneNumber());
    }
}
