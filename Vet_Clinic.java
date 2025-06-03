// Vet_Clinic.java
import java.util.*;
import java.sql.*;

public class Vet_Clinic {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice = 0;
        ArrayList<Service> services = new ArrayList<>();

        services.add(new WellnessExam());
        services.add(new SpayNeuter());
        services.add(new DentalCare());
        services.add(new EmergencyCare());

        Animal currentAnimal = null;

        while (true) {
            System.out.println("=== Vet Clinic Main Menu ===");
            System.out.println("1. Type of Service");
            System.out.println("2. Type of Pain");
            System.out.println("3. Enter Animal Info");
            System.out.println("4. Show Services from DB (JDBC)");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            if (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next();
                continue;
            }
            choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 1) {
                if (currentAnimal == null) {
                    System.out.println("Please enter your animal info first (option 3).");
                    continue;
                }
                System.out.println("Available Services:");
                for (int i = 0; i < services.size(); i++) {
                    System.out.printf("%d. %s ($%.2f)\n", (i + 1), services.get(i).getName(), services.get(i).getFee());
                }
                System.out.print("Select a service to perform: ");
                if (scanner.hasNextInt()) {
                    int job_done = scanner.nextInt();
                    scanner.nextLine();
                    if (job_done >= 1 && job_done <= services.size()) {
                        Service selected = services.get(job_done - 1);
                        selected.performService();
                        System.out.printf("Service fee: $%.2f\n", selected.getFee());
                        System.out.print("Proceed to payment? (yes/no): ");
                        String pay = scanner.nextLine();
                        if (pay.equalsIgnoreCase("yes")) {
                            System.out.print("Enter payment amount: $");
                            double amount = 0;
                            if (scanner.hasNextDouble()) {
                                amount = scanner.nextDouble();
                                scanner.nextLine();
                                if (amount >= selected.getFee()) {
                                    System.out.println("Payment successful. Thank you!");
                                    if (amount > selected.getFee()) {
                                        System.out.printf("Change: $%.2f\n", amount - selected.getFee());
                                    }
                                } else {
                                    System.out.println("Insufficient payment. Transaction cancelled.");
                                }
                            } else {
                                System.out.println("Invalid amount.");
                                scanner.next();
                            }
                        } else {
                            System.out.println("Payment cancelled.");
                        }
                    } else {
                        System.out.println("Invalid service selection.");
                    }
                } else {
                    System.out.println("Invalid input.");
                    scanner.next();
                }

            } else if (choice == 2) {
                System.out.print("Enter the pain level (Mild, Moderate, Severe): ");
                String painLevel = scanner.nextLine();
                Pain pain = new CustomPain(painLevel);
                pain.describePain();

            } else if (choice == 3) {
                System.out.print("Enter the animal type (e.g., Dog, Cat, Bird, Rabbit, or any other): ");
                String type = scanner.nextLine().trim();
                switch (type.toLowerCase()) {
                    case "dog":
                        currentAnimal = new Dog();
                        break;
                    case "cat":
                        currentAnimal = new Cat();
                        break;
                    case "bird":
                        currentAnimal = new Bird();
                        break;
                    case "rabbit":
                        currentAnimal = new Rabbit();
                        break;
                    default:
                        currentAnimal = new OtherAnimal(type);
                        break;
                }
                currentAnimal.displayType();
            } else if (choice == 4) {
                showServicesFromDB();
            } else if (choice == 5) {
                System.out.println("Exiting program. Goodbye!");
                break;
            } else {
                System.out.println("Invalid choice. Please try again.");
            }

 
import java.util.*;
import java.sql.*;
 
// ========================= INTERFACE & ABSTRACT CLASSES ====================
 
interface Service {
    void performService();
    double getFee();
    String getName();
}
 
abstract class Animal {
    String name;
    abstract void displayType();
}
 
abstract class Pain {
    String level;
    abstract void describePain();
}
 
// ========================= ANIMAL & PAIN IMPLEMENTATIONS ===================
 
class OtherAnimal extends Animal {
    String type;
    OtherAnimal(String type) {
        this.type = type;
        this.name = type;
    }
    void displayType() { System.out.println("Animal: " + type); }
}
 
class CustomPain extends Pain {
    CustomPain(String level) { this.level = level; }
    void describePain() { System.out.println("Pain Level: " + level); }
}
 
// ========================= SERVICE IMPLEMENTATIONS =========================
 
class WellnessExam implements Service {
    public void performService() { System.out.println("Routine Check-up Exam performed."); }
    public double getFee() { return 50.0; }
    public String getName() { return "Wellness Exam"; }
}
class SpayNeuter implements Service {
    public void performService() { System.out.println("Spaying/Neutering performed."); }
    public double getFee() { return 120.0; }
    public String getName() { return "Spay/Neuter"; }
}
class DentalCare implements Service {
    public void performService() { System.out.println("Dental Care performed."); }
    public double getFee() { return 80.0; }
    public String getName() { return "Dental Care"; }
}
class EmergencyCare implements Service {
    public void performService() { System.out.println("Emergency Care performed."); }
    public double getFee() { return 200.0; }
    public String getName() { return "Emergency Care"; }
}
 
// ========================= VACCINATION SERVICE =============================
 
class Vaccination implements Service {
    private String type;
    private double fee;
    Vaccination(String type, double fee) {
        this.type = type;
        this.fee = fee;
    }
    public void performService() { System.out.println(type + " vaccination performed."); }
    public double getFee() { return fee; }
    public String getName() { return type + " Vaccination"; }
}
 
// ========================= MAIN APPLICATION CLASS ==========================
 
public class Vet_Clinic {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice = 0;
        ArrayList<Service> careServices = new ArrayList<>();
        ArrayList<Service> vaccinations = new ArrayList<>();
 
        // Vaccination options
        vaccinations.add(new Vaccination("Core", 60.0));
        vaccinations.add(new Vaccination("Non-Core", 40.0));
 
        // Care services
        careServices.add(new WellnessExam());
        careServices.add(new SpayNeuter());
        careServices.add(new DentalCare());
        careServices.add(new EmergencyCare());
 
        // Load services from DB (optional, see below)
        showServicesFromDB();
 
        Animal currentAnimal = null;
 
        // Prompt for animal info before menu loop
        while (currentAnimal == null) {
            System.out.print("Enter the animal type: ");
            String type = scanner.nextLine().trim();
            currentAnimal = new OtherAnimal(type);
        }
 
        double totalDue = 0.0;
 
        // ========================= MAIN MENU LOOP ===========================
        while (true) {
            System.out.println("=== Vet Clinic Main Menu ===");
            System.out.println("1. Vaccination");
            System.out.println("2. Care Service");
            System.out.println("3. Pay");
            System.out.println("4. Show Services from Database");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
           
            if (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next();
                continue;
            }
            choice = scanner.nextInt();
            scanner.nextLine();
 
            if (choice == 1) {
                // ===================== VACCINATION MENU =====================
                System.out.println("Vaccination Options:");
                for (int i = 0; i < vaccinations.size(); i++) {
                    System.out.printf("%d. %s ($%.2f)\n", (i+1), vaccinations.get(i).getName(), vaccinations.get(i).getFee());
                }
                System.out.print("Select a vaccination: ");
                if (scanner.hasNextInt()) {
                    int vaxChoice = scanner.nextInt();
                    scanner.nextLine();
                    if (vaxChoice >= 1 && vaxChoice <= vaccinations.size()) {
                        Service selected = vaccinations.get(vaxChoice-1);
                        selected.performService();
                        System.out.printf("Vaccination fee: $%.2f\n", selected.getFee());
                        totalDue += selected.getFee();
                    } else {
                        System.out.println("Invalid vaccination selection.");
                    }
                } else {
                    System.out.println("Invalid input.");
                    scanner.next();
                }
            } else if (choice == 2) {
                // ===================== CARE SERVICE MENU ====================
                System.out.println("Available Care Services:");
                for (int i = 0; i < careServices.size(); i++) {
                    System.out.printf("%d. %s ($%.2f)\n", (i+1), careServices.get(i).getName(), careServices.get(i).getFee());
                }
                System.out.print("Select a care service: ");
                if (scanner.hasNextInt()) {
                    int careChoice = scanner.nextInt();
                    scanner.nextLine();
                    if (careChoice >= 1 && careChoice <= careServices.size()) {
                        Service selected = careServices.get(careChoice-1);
                        selected.performService();
                        System.out.printf("Service fee: $%.2f\n", selected.getFee());
                        totalDue += selected.getFee();
                    } else {
                        System.out.println("Invalid service selection.");
                    }
                } else {
                    System.out.println("Invalid input.");
                    scanner.next();
                }
            } else if (choice == 3) {
                // ===================== PAYMENT SECTION ======================
                System.out.printf("Total due: $%.2f\n", totalDue);
                if (totalDue > 0) {
                    System.out.print("Proceed to payment? (yes/no): ");
                    String pay = scanner.nextLine();
                    if (pay.equalsIgnoreCase("yes")) {
                        System.out.print("Enter payment amount: $");
                        double amount = 0;
                        if (scanner.hasNextDouble()) {
                            amount = scanner.nextDouble();
                            scanner.nextLine();
                            if (amount >= totalDue) {
                                System.out.println("Payment successful. Thank you!");
                                if (amount > totalDue) {
                                    System.out.printf("Change: $%.2f\n", amount - totalDue);
                                }
                                totalDue = 0.0;
                            } else {
                                System.out.println("Insufficient payment. Transaction cancelled.");
                            }
                        } else {
                            System.out.println("Invalid amount.");
                            scanner.next();
                        }
                    } else {
                        System.out.println("Payment cancelled.");
                    }
                } else {
                    System.out.println("No outstanding balance.");
                }
            } else if (choice == 4) {
                // ===================== SHOW DB SERVICES =====================
                showServicesFromDB();
            } else if (choice == 5) {
                // ===================== EXIT PROGRAM ========================
                System.out.println("Exiting program. Goodbye!");
                break;
            } else {
                System.out.println("Invalid menu option.");
            }
        }
    }
 
    // ========================= DATABASE SERVICE DISPLAY ======================
    // Connects to the database and displays services from the 'menu' table
    public static void showServicesFromDB() {
        String url = "jdbc:mysql://localhost:3306/vet_clinic";
        String user = "root";
        String password = "password";
        try (Connection conn = DriverManager.getConnection(url, user, password);
            Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM menu")) {
            System.out.println("=== Services from Database ===");
            while (rs.next()) {
                System.out.println(rs.getInt("id") + ": " + rs.getString("name") + " ($" + rs.getDouble("fee") + ")");
            }
        } catch (SQLException e) {
            System.out.println("Could not connect to database or fetch data: " + e.getMessage());
        }
    }
}
 
// ========================= END OF FILE 1: Vet_Clinic.java ===================
 
 
            System.out.println();
        }
        scanner.close();
    }

    public static void showServicesFromDB() {
        String url = "jdbc:mysql://localhost:3306/vet_clinic";
        String user = "root";
        String password = "password";
        try (Connection conn = DriverManager.getConnection(url, user, password);
            Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM menu")) {
            System.out.println("Services from Database:");
            while (rs.next()) {
                System.out.println(rs.getInt("id") + ": " + rs.getString("name"));
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }
}
