import java.io.*;
import java.util.*;

public class HostelRoomAllocation {
    public static void main(String[] args) {
        // Initialize a list of available rooms from 1 to 10
        List<Integer> availableRooms = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            availableRooms.add(i);
        }

        // Set to keep track of already allocated rooms
        Set<Integer> allocatedRooms = new HashSet<>();

        // Load previously allocated rooms from file
        try (BufferedReader reader = new BufferedReader(new FileReader("student_allocations.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" - Allocated Room: ");
                if (parts.length == 2) {
                    try {
                        int roomNumber = Integer.parseInt(parts[1]);
                        allocatedRooms.add(roomNumber);
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid room number in file: " + parts[1]);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("All rooms are available.");
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }

        // Remove allocated rooms from available rooms
        availableRooms.removeAll(allocatedRooms);

        // Prepare for new allocations
        Random random = new Random();
        Scanner scanner = new Scanner(System.in);

        // Open file to append new allocations
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("student_allocations.txt", true))) {
            while (!availableRooms.isEmpty()) {
                System.out.print("Enter student name (or type 'done' to finish): ");
                String studentName = scanner.nextLine();
                if (studentName.equalsIgnoreCase("done")) {
                    break;
                }

                // Allocate a random room
                int roomIndex = random.nextInt(availableRooms.size());
                int allocatedRoom = availableRooms.get(roomIndex);
                System.out.println("Allocated Room for " + studentName + ": " + allocatedRoom);

                // Write allocation to file
                writer.write("Student: " + studentName + " - Allocated Room: " + allocatedRoom);
                writer.newLine();

                // Update available and allocated rooms
                availableRooms.remove(roomIndex);
                allocatedRooms.add(allocatedRoom);
            }

            if (availableRooms.isEmpty()) {
                
                System.out.println("All rooms are now allocated.");
                // Ask user if they want to clear the file
                System.out.print("Do you want to clear all allocations? (y/n): ");
                String response = scanner.nextLine();
                if (response.equalsIgnoreCase("y")) {
                    try (PrintWriter pw = new PrintWriter("student_allocations.txt")) {
                        pw.print("");  // Clear the file by overwriting it with an empty string
                        System.out.println("All allocations have been cleared.");
                    } catch (IOException e) {
                        System.err.println("Error clearing the file: " + e.getMessage());
                    }
                }
                
            }
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}