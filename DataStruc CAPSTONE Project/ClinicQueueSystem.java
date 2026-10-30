import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

/*
 * Clinic Queue and Patient Management System
 * Console-based Java app that uses:
 *   - ArrayLists (parallel lists) to store patient records
 *   - A Queue (LinkedList) to call patients in FIFO order
 *   - Bubble Sort and Shell Sort (written by hand) to sort the records
 *
 * This program is procedural. It has only one class (needed to hold main)
 * and everything else is static methods. No custom objects are used.
 */
public class ClinicQueueSystem {

    static Scanner input = new Scanner(System.in);

    // Parallel ArrayLists: index i in every list is the SAME patient
    static ArrayList<Integer> patientIds = new ArrayList<>();
    static ArrayList<String> patientNames = new ArrayList<>();
    static ArrayList<Integer> patientAges = new ArrayList<>();
    static ArrayList<String> appointmentTimes = new ArrayList<>(); // format HH:MM (24 hour)
    static ArrayList<String> complaints = new ArrayList<>();

    // The consultation queue holds patient IDs in the order they will be called
    static Queue<Integer> consultQueue = new LinkedList<>();

    static int nextId = 1001;
    static int servedCount = 0;

    // ---------------------------------------------------------------
    // MAIN
    // ---------------------------------------------------------------
    public static void main(String[] args) {
        boolean running = true;

        while (running) {
            showMenu();
            int choice = readInt("Enter your choice (1-14): ", 1, 14);
            System.out.println();

            switch (choice) {
                case 1:
                    registerPatient();
                    break;
                case 2:
                    displayPatients();
                    break;
                case 3:
                    searchPatient();
                    break;
                case 4:
                    updatePatient();
                    break;
                case 5:
                    deletePatient();
                    break;
                case 6:
                    enqueuePatient();
                    break;
                case 7:
                    dequeuePatient();
                    break;
                case 8:
                    peekPatient();
                    break;
                case 9:
                    displayQueue();
                    break;
                case 10:
                    checkQueueStatus();
                    break;
                case 11:
                    runSort(true);
                    break;
                case 12:
                    runSort(false);
                    break;
                case 13:
                    loadSampleData();
                    break;
                case 14:
                    running = false;
                    break;
            }

            if (running) {
                System.out.print("\nPress Enter to go back to the menu...");
                input.nextLine();
            }
        }

        System.out.println("Thank you for using the Clinic Queue System. Goodbye!");
        input.close();
    }

    // ---------------------------------------------------------------
    // MENU
    // ---------------------------------------------------------------
    static void showMenu() {
        System.out.println("\n==========================================");
        System.out.println("   CLINIC QUEUE AND PATIENT MANAGEMENT");
        System.out.println("==========================================");
        System.out.println(" PATIENT RECORDS");
        System.out.println("  1. Register new patient");
        System.out.println("  2. Display all patients");
        System.out.println("  3. Search patient");
        System.out.println("  4. Update patient");
        System.out.println("  5. Delete patient");
        System.out.println("\n CONSULTATION QUEUE");
        System.out.println("  6. Add patient to queue (Enqueue)");
        System.out.println("  7. Call next patient (Dequeue)");
        System.out.println("  8. Peek next patient");
        System.out.println("  9. Display waiting list");
        System.out.println(" 10. Check if queue is empty");
        System.out.println("\n SORTING");
        System.out.println(" 11. Sort records using Bubble Sort");
        System.out.println(" 12. Sort records using Shell Sort");
        System.out.println("\n OTHER");
        System.out.println(" 13. Load sample patients");
        System.out.println(" 14. Exit");
        System.out.println("==========================================");
    }

    // ---------------------------------------------------------------
    // INPUT HELPERS (with validation)
    // ---------------------------------------------------------------
    static int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String text = input.nextLine().trim();
            try {
                int value = Integer.parseInt(text);
                if (value < min || value > max) {
                    System.out.println("Invalid input. Please enter a number from " + min + " to " + max + ".");
                } else {
                    return value;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a whole number only.");
            }
        }
    }

    static String readName(String prompt) {
        while (true) {
            System.out.print(prompt);
            String text = input.nextLine().trim();
            if (text.matches("[A-Za-z .'-]+")) {
                return text;
            }
            System.out.println("Invalid name. Use letters and spaces only, and do not leave it blank.");
        }
    }

    static String readTime(String prompt) {
        while (true) {
            System.out.print(prompt);
            String text = input.nextLine().trim();
            if (text.matches("([01][0-9]|2[0-3]):[0-5][0-9]")) {
                return text;
            }
            System.out.println("Invalid time. Use 24 hour format HH:MM (example 09:30 or 14:45).");
        }
    }

    static String readText(String prompt) {
        while (true) {
            System.out.print(prompt);
            String text = input.nextLine().trim();
            if (!text.isEmpty()) {
                return text;
            }
            System.out.println("This field cannot be empty.");
        }
    }

    // ---------------------------------------------------------------
    // PATIENT RECORD METHODS
    // ---------------------------------------------------------------
    static void registerPatient() {
        System.out.println("--- Register New Patient ---");
        String name = readName("Name: ");
        int age = readInt("Age (1-100): ", 1, 100);
        String time = readTime("Appointment time (HH:MM): ");
        String complaint = readText("Complaint / reason for visit: ");

        patientIds.add(nextId);
        patientNames.add(name);
        patientAges.add(age);
        appointmentTimes.add(time);
        complaints.add(complaint);

        System.out.println("Patient registered successfully. Patient ID: " + nextId);
        nextId++;
    }

    static void displayPatients() {
        System.out.println("--- Patient Records ---");
        if (patientIds.isEmpty()) {
            System.out.println("No patient records yet. Register a patient first.");
            return;
        }
        printTable();
    }

    static void printTable() {
        System.out.printf("%-6s %-22s %-5s %-8s %-25s%n", "ID", "NAME", "AGE", "TIME", "COMPLAINT");
        System.out.println("-----------------------------------------------------------------------");
        for (int i = 0; i < patientIds.size(); i++) {
            System.out.printf("%-6d %-22s %-5d %-8s %-25s%n",
                    patientIds.get(i), patientNames.get(i), patientAges.get(i),
                    appointmentTimes.get(i), complaints.get(i));
        }
        System.out.println("Total records: " + patientIds.size());
    }

    static void printOneRecord(int index) {
        System.out.println("ID: " + patientIds.get(index)
                + " | Name: " + patientNames.get(index)
                + " | Age: " + patientAges.get(index)
                + " | Time: " + appointmentTimes.get(index)
                + " | Complaint: " + complaints.get(index));
    }

    static int findIndexById(int id) {
        for (int i = 0; i < patientIds.size(); i++) {
            if (patientIds.get(i) == id) {
                return i;
            }
        }
        return -1;
    }

    static void searchPatient() {
        System.out.println("--- Search Patient ---");
        if (patientIds.isEmpty()) {
            System.out.println("No patient records to search.");
            return;
        }
        System.out.println("1. Search by ID");
        System.out.println("2. Search by name");
        int option = readInt("Choose (1-2): ", 1, 2);

        if (option == 1) {
            int id = readInt("Enter patient ID: ", 1, 999999);
            int index = findIndexById(id);
            if (index == -1) {
                System.out.println("No patient found with ID " + id + ".");
            } else {
                System.out.println("Patient found:");
                printOneRecord(index);
            }
        } else {
            String keyword = readText("Enter name or part of the name: ").toLowerCase();
            boolean found = false;
            for (int i = 0; i < patientNames.size(); i++) {
                if (patientNames.get(i).toLowerCase().contains(keyword)) {
                    if (!found) {
                        System.out.println("Matching patients:");
                    }
                    printOneRecord(i);
                    found = true;
                }
            }
            if (!found) {
                System.out.println("No patient found with the name \"" + keyword + "\".");
            }
        }
    }

    static void updatePatient() {
        System.out.println("--- Update Patient ---");
        if (patientIds.isEmpty()) {
            System.out.println("No patient records to update.");
            return;
        }
        int id = readInt("Enter patient ID to update: ", 1, 999999);
        int index = findIndexById(id);
        if (index == -1) {
            System.out.println("No patient found with ID " + id + ".");
            return;
        }
        printOneRecord(index);
        System.out.println("1. Name");
        System.out.println("2. Age");
        System.out.println("3. Appointment time");
        System.out.println("4. Complaint");
        int field = readInt("What do you want to update? (1-4): ", 1, 4);

        if (field == 1) {
            patientNames.set(index, readName("New name: "));
        } else if (field == 2) {
            patientAges.set(index, readInt("New age (1-100): ", 1, 100));
        } else if (field == 3) {
            appointmentTimes.set(index, readTime("New appointment time (HH:MM): "));
        } else {
            complaints.set(index, readText("New complaint: "));
        }
        System.out.println("Record updated successfully.");
    }

    static void deletePatient() {
        System.out.println("--- Delete Patient ---");
        if (patientIds.isEmpty()) {
            System.out.println("No patient records to delete.");
            return;
        }
        int id = readInt("Enter patient ID to delete: ", 1, 999999);
        int index = findIndexById(id);
        if (index == -1) {
            System.out.println("No patient found with ID " + id + ".");
            return;
        }
        printOneRecord(index);
        int confirm = readInt("Are you sure? (1 = Yes, 2 = No): ", 1, 2);
        if (confirm == 2) {
            System.out.println("Delete cancelled.");
            return;
        }

        // remove from every list so the indexes stay matched
        patientIds.remove(index);
        patientNames.remove(index);
        patientAges.remove(index);
        appointmentTimes.remove(index);
        complaints.remove(index);

        // also remove from the queue if the patient was waiting
        if (consultQueue.remove(Integer.valueOf(id))) {
            System.out.println("The patient was also removed from the waiting list.");
        }
        System.out.println("Patient deleted successfully.");
    }

    // ---------------------------------------------------------------
    // QUEUE METHODS (FIFO)
    // ---------------------------------------------------------------
    static void enqueuePatient() {
        System.out.println("--- Add Patient to Queue (Enqueue) ---");
        if (patientIds.isEmpty()) {
            System.out.println("No patient records. Register a patient first.");
            return;
        }
        int id = readInt("Enter patient ID to add to the queue: ", 1, 999999);
        int index = findIndexById(id);
        if (index == -1) {
            System.out.println("No patient found with ID " + id + ".");
            return;
        }
        if (consultQueue.contains(id)) {
            System.out.println(patientNames.get(index) + " is already in the queue.");
            return;
        }
        consultQueue.add(id);
        System.out.println(patientNames.get(index) + " was added to the queue. Position: " + consultQueue.size());
    }

    static void dequeuePatient() {
        System.out.println("--- Call Next Patient (Dequeue) ---");
        if (consultQueue.isEmpty()) {
            System.out.println("The queue is empty. No patient to call.");
            return;
        }
        int id = consultQueue.poll();
        int index = findIndexById(id);
        servedCount++;
        System.out.println("Now calling:");
        printOneRecord(index);
        System.out.println("Patients still waiting: " + consultQueue.size());
    }

    static void peekPatient() {
        System.out.println("--- Peek Next Patient ---");
        if (consultQueue.isEmpty()) {
            System.out.println("The queue is empty. Nobody is waiting.");
            return;
        }
        int id = consultQueue.peek();
        int index = findIndexById(id);
        System.out.println("Next patient to be called (not removed from the queue):");
        printOneRecord(index);
    }

    static void displayQueue() {
        System.out.println("--- Waiting List ---");
        if (consultQueue.isEmpty()) {
            System.out.println("The queue is empty. No patients are waiting.");
            return;
        }
        int position = 1;
        for (int id : consultQueue) {
            int index = findIndexById(id);
            System.out.println(position + ". " + patientNames.get(index)
                    + " (ID " + id + ", " + complaints.get(index) + ")");
            position++;
        }
        System.out.println("Total waiting: " + consultQueue.size());
    }

    static void checkQueueStatus() {
        System.out.println("--- Queue Status ---");
        if (consultQueue.isEmpty()) {
            System.out.println("The queue is EMPTY.");
        } else {
            System.out.println("The queue is NOT empty. Waiting: " + consultQueue.size());
        }
        System.out.println("Patients already served: " + servedCount);
    }

    // ---------------------------------------------------------------
    // SORTING
    // ---------------------------------------------------------------
    static void runSort(boolean useBubble) {
        if (patientIds.size() < 2) {
            System.out.println("Not enough records to sort. You need at least 2 patients.");
            return;
        }

        System.out.println("\nSort by:");
        System.out.println("1. Name");
        System.out.println("2. Age");
        System.out.println("3. Appointment time");
        int criteria = readInt("Choose (1-3): ", 1, 3);

        System.out.println("Order:");
        System.out.println("1. Ascending");
        System.out.println("2. Descending");
        boolean ascending = readInt("Choose (1-2): ", 1, 2) == 1;

        System.out.println("\nBEFORE SORTING:");
        printTable();

        if (useBubble) {
            bubbleSort(criteria, ascending);
        } else {
            shellSort(criteria, ascending);
        }

        System.out.println("\nAFTER SORTING:");
        printTable();
    }

    // Bubble sort written by hand (no built-in sort methods)
    static void bubbleSort(int criteria, boolean ascending) {
        int n = patientIds.size();
        for (int pass = 0; pass < n - 1; pass++) {
            boolean swapped = false;
            for (int i = 0; i < n - 1 - pass; i++) {
                if (shouldSwap(i, i + 1, criteria, ascending)) {
                    swapRecords(i, i + 1);
                    swapped = true;
                }
            }
            if (!swapped) {
                break; // already sorted, stop early
            }
        }
    }

    // Shell sort written by hand (no built-in sort methods)
    static void shellSort(int criteria, boolean ascending) {
        int n = patientIds.size();
        for (int gap = n / 2; gap > 0; gap = gap / 2) {
            for (int i = gap; i < n; i++) {
                for (int j = i; j >= gap && shouldSwap(j - gap, j, criteria, ascending); j = j - gap) {
                    swapRecords(j - gap, j);
                }
            }
        }
    }

    // returns true if the record at index a should come AFTER the record at index b
    static boolean shouldSwap(int a, int b, int criteria, boolean ascending) {
        int result = 0;
        if (criteria == 1) {
            result = patientNames.get(a).compareToIgnoreCase(patientNames.get(b));
        } else if (criteria == 2) {
            result = patientAges.get(a) - patientAges.get(b);
        } else {
            result = timeToMinutes(appointmentTimes.get(a)) - timeToMinutes(appointmentTimes.get(b));
        }

        if (ascending) {
            return result > 0;
        } else {
            return result < 0;
        }
    }

    static int timeToMinutes(String time) {
        String[] parts = time.split(":");
        return Integer.parseInt(parts[0]) * 60 + Integer.parseInt(parts[1]);
    }

    // swaps ALL fields so the parallel lists stay matched
    static void swapRecords(int a, int b) {
        int tempId = patientIds.get(a);
        patientIds.set(a, patientIds.get(b));
        patientIds.set(b, tempId);

        String tempName = patientNames.get(a);
        patientNames.set(a, patientNames.get(b));
        patientNames.set(b, tempName);

        int tempAge = patientAges.get(a);
        patientAges.set(a, patientAges.get(b));
        patientAges.set(b, tempAge);

        String tempTime = appointmentTimes.get(a);
        appointmentTimes.set(a, appointmentTimes.get(b));
        appointmentTimes.set(b, tempTime);

        String tempComplaint = complaints.get(a);
        complaints.set(a, complaints.get(b));
        complaints.set(b, tempComplaint);
    }

    // ---------------------------------------------------------------
    // SAMPLE DATA (useful for testing and screenshots)
    // ---------------------------------------------------------------
    static void loadSampleData() {
        String[] names = {"Maria Santos", "Juan Dela Cruz", "Ana Reyes", "Carlos Garcia", "Bea Mendoza", "Luis Ramos"};
        int[] ages = {34, 58, 9, 45, 22, 71};
        String[] times = {"10:30", "08:15", "13:00", "09:45", "11:20", "08:45"};
        String[] reasons = {"Fever", "High blood pressure", "Cough and colds", "Back pain", "Headache", "Checkup"};

        for (int i = 0; i < names.length; i++) {
            patientIds.add(nextId);
            patientNames.add(names[i]);
            patientAges.add(ages[i]);
            appointmentTimes.add(times[i]);
            complaints.add(reasons[i]);
            nextId++;
        }
        System.out.println(names.length + " sample patients were added.");
    }
}
