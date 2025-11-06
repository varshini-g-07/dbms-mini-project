package com.courseregistration; 

import com.courseregistration.service.RegistrationService;
import com.courseregistration.model.Students; 
import java.util.InputMismatchException;
import java.util.Scanner;

public class Application {

    public static void main(String[] args) {
        System.out.println("--- Console Registration System v1.0 ---");

        Scanner scanner = new Scanner(System.in);
        RegistrationService service = new RegistrationService();
        boolean running = true;

        while (running) {
            System.out.println("\n========================================");
            System.out.println("Please select an action:");
            System.out.println("1. Register a NEW Student");
            System.out.println("2. Enroll Student in a Course");
            System.out.println("3. View Student's Enrolled Courses");
            System.out.println("4. Update Student Information");
            System.out.println("5. Unenroll from a Course"); 
            System.out.println("6. Delete Student Account");      
            System.out.println("7. Add NEW Course to Catalog");   
            System.out.println("8. Delete Course from Catalog");
            System.out.println("9. Exit Application");
            System.out.println("\n========================================");
            System.out.print("Enter choice (1-9): ");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        registerNewStudent(scanner, service);
                        break;
                    case 2:
                        enrollStudentInCourse(scanner, service);
                        break;
                    case 3:
                        viewEnrolledCourses(scanner, service);
                        break;
                    case 4:
                        updateStudentDetails(scanner, service); 
                        break;
                    case 5:
                        unenrollStudentFromCourse(scanner, service); 
                        break;
                    case 6:
                        deleteStudentAccount(scanner, service); 
                        break;
                    case 7:
                        addNewCourse(scanner, service); 
                        break;
                    case 8:
                        deleteCourse(scanner, service);
                        break;
                    case 9:
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter a number between 1 and 9.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();
            }
        }

        scanner.close();
        System.out.println("\n--- Application Shutting Down. Goodbye! ---");
    }

    private static void addNewCourse(Scanner scanner, RegistrationService service) {
        System.out.println("\n-- ADD NEW COURSE --");
        try {
            System.out.print("Enter Course ID: ");
            String courseId = scanner.nextLine();
            System.out.print("Enter Course Name: ");
            String name = scanner.nextLine();
            System.out.print("Enter Partner: ");
            String partner = scanner.nextLine();
            System.out.print("Enter Rating: ");
            String rating = scanner.nextLine();
            System.out.print("Enter Certificate Type: ");
            String certificateType = scanner.nextLine();
            System.out.print("Enter Duration: ");
            String duration = scanner.nextLine();
            
            service.addNewCourse(courseId, name, partner, rating, certificateType, duration);
            
        } catch (Exception e) {
            System.out.println("An unexpected error occurred while adding the course: " + e.getMessage());
        }
    }

    private static void deleteCourse(Scanner scanner, RegistrationService service) {
        System.out.println("\n-- DELETE COURSE FROM CATALOG --");
        try {
            System.out.print("Enter Course ID to DELETE: ");
            String courseId = scanner.nextLine();
            
            System.out.println("Attempting to delete course " + courseId + ". This will remove all associated student enrollments.");
            
            service.deleteCourseFromCatalog(courseId);
            
        } catch (Exception e) {
            System.out.println("An unexpected error occurred while deleting the course: " + e.getMessage());
        }
    }

    private static void registerNewStudent(Scanner scanner, RegistrationService service) {
        System.out.println("\n-- NEW STUDENT REGISTRATION --");
        System.out.print("First Name: ");
        String firstName = scanner.nextLine();
        System.out.print("Last Name: ");
        String lastName = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();

        service.addNewStudent(firstName, lastName, email);
    }

        private static void deleteStudentAccount(Scanner scanner, RegistrationService service) {
        System.out.println("\n-- DELETE STUDENT ACCOUNT --");
        try {
            System.out.print("Enter Student ID to DELETE: ");
            int studentId = scanner.nextInt();
            scanner.nextLine();
            
            System.out.println("Deleting student account " + studentId + ".");
            
            service.deleteStudentAccount(studentId);
            
        } catch (InputMismatchException e) {
            System.out.println("Error: Student ID must be a number.");
            scanner.nextLine();
        }
    }

    private static void enrollStudentInCourse(Scanner scanner, RegistrationService service) {
        System.out.println("\n-- COURSE ENROLLMENT --");
        try {
            System.out.print("Enter Student ID: ");
            int studentId = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Enter Course ID: ");
            String courseId = scanner.nextLine();
            
            String semester = "Spring";
            int year = 2026;
            
            System.out.printf("Enrolling Student %d in %s (%s %d)...\n", studentId, courseId, semester, year);

            service.handleRegistration(studentId, courseId, semester, year);
        } catch (InputMismatchException e) {
            System.out.println("Error: Student ID must be a number.");
            scanner.nextLine();
        }
    }

    private static void unenrollStudentFromCourse(Scanner scanner, RegistrationService service) {
        System.out.println("\n-- COURSE UNENROLLMENT --");
        try {
            System.out.print("Enter Student ID to unenroll: ");
            int studentId = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Enter Course ID to drop: ");
            String courseId = scanner.nextLine();
            
            System.out.printf("Attempting to unenroll Student %d from %s...\n", studentId, courseId);

            service.handleUnenrollment(studentId, courseId);
            
        } catch (InputMismatchException e) {
            System.out.println("Error: Student ID must be a number.");
            scanner.nextLine();
        }
    }

    private static void viewEnrolledCourses(Scanner scanner, RegistrationService service) {
        System.out.println("\n-- VIEW ENROLLMENTS --");
        try {
            System.out.print("Enter Student ID to view courses: ");
            int studentId = scanner.nextInt();
            scanner.nextLine(); 
            
            service.displayEnrolledCourses(studentId);
        } catch (InputMismatchException e) {
            System.out.println("Error: Student ID must be a number.");
            scanner.nextLine();
        }
    }

    private static void updateStudentDetails(Scanner scanner, RegistrationService service) {
        System.out.println("\n-- UPDATE STUDENT INFORMATION --");
        int studentId;
        try {
            System.out.print("Enter Student ID to update: ");
            studentId = scanner.nextInt();
            scanner.nextLine(); 
        } catch (InputMismatchException e) {
            System.out.println("Error: Student ID must be a number.");
            scanner.nextLine();
            return;
        }

        Students existingStudent = service.getStudentDetails(studentId);
        
        if (existingStudent == null) {
            System.out.printf("Student with ID %d not found.\n", studentId);
            return;
        }
        
        String finalFirstName = existingStudent.getFirstName();
        String finalLastName = existingStudent.getLastName();
        String finalEmail = existingStudent.getEmail();
        
        boolean updating = true;
        
        while(updating) {
            System.out.println("\n--- Current Details for ID " + studentId + " ---");
            System.out.printf("1. First Name: %s\n", finalFirstName);
            System.out.printf("2. Last Name: %s\n", finalLastName);
            System.out.printf("3. Email: %s\n", finalEmail);
            System.out.println("4. Commit Changes and Proceed");
            System.out.print("Select field to update (1-4): ");
            
            try {
                int columnChoice = scanner.nextInt();
                scanner.nextLine();
                
                String newValue;
                
                switch(columnChoice) {
                    case 1:
                        System.out.print("Enter NEW First Name: ");
                        newValue = scanner.nextLine();
                        if (!newValue.isEmpty()) finalFirstName = newValue;
                        System.out.println("First Name updated locally.");
                        break;
                    case 2:
                        System.out.print("Enter NEW Last Name: ");
                        newValue = scanner.nextLine();
                        if (!newValue.isEmpty()) finalLastName = newValue;
                        System.out.println("Last Name updated locally.");
                        break;
                    case 3:
                        System.out.print("Enter NEW Email: ");
                        newValue = scanner.nextLine();
                        if (!newValue.isEmpty()) finalEmail = newValue;
                        System.out.println("Email updated locally.");
                        break;
                    case 4:
                        updating = false;
                        break;
                    default:
                        System.out.println("Invalid field choice.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();
            }
        }

        Students updatedStudent = new Students(studentId, finalFirstName, finalLastName, finalEmail);
        
        service.updateStudent(updatedStudent);
    }
}
