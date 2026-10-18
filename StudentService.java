

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class StudentService {

    // Read an integer safely
    private int readInt(Scanner sc, String message) {
        while (true) {
            System.out.print(message);
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid whole number.");
            }
        }
    }

    // Read a decimal safely
    private double readDouble(Scanner sc, String message) {
        while (true) {
            System.out.print(message);
            try {
                return Double.parseDouble(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    // Read non-empty text
    private String readText(Scanner sc, String message) {
        while (true) {
            System.out.print(message);
            String value = sc.nextLine().trim();

            if (!value.isEmpty()) {
                return value;
            }

            System.out.println("This field cannot be empty.");
        }
    }

    // Get all students from MySQL
    public ArrayList<Student> getStudents() {
        ArrayList<Student> students = new ArrayList<>();

        String sql = "SELECT student_id, name, branch, email, cgpa FROM students";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                students.add(new Student(
                    rs.getInt("student_id"),
                    rs.getString("name"),
                    rs.getString("branch"),
                    rs.getString("email"),
                    rs.getDouble("cgpa")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Error loading students: " + e.getMessage());
        }

        return students;
    }

    // Find student by ID in MySQL
    private Student findStudent(int id) {
        String sql = "SELECT * FROM students WHERE student_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Student(
                        rs.getInt("student_id"),
                        rs.getString("name"),
                        rs.getString("branch"),
                        rs.getString("email"),
                        rs.getDouble("cgpa")
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println("Error searching student: " + e.getMessage());
        }

        return null;
    }

    // Add student
    public void addStudent(Scanner sc) {
        System.out.println("\n--- Add Student ---");

        int id = readInt(sc, "Enter Student ID: ");

        if (id <= 0) {
            System.out.println("Student ID must be positive.");
            return;
        }

        if (findStudent(id) != null) {
            System.out.println("This student ID already exists.");
            return;
        }

        String name = readText(sc, "Enter Name: ");
        String branch = readText(sc, "Enter Branch: ");

        String email;
        while (true) {
            email = readText(sc, "Enter Email: ");

            if (email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
                break;
            }

            System.out.println("Please enter a valid email address.");
        }

        double cgpa = readDouble(sc, "Enter CGPA (0-10): ");

        if (cgpa < 0 || cgpa > 10) {
            System.out.println("CGPA must be between 0 and 10.");
            return;
        }

        String sql = "INSERT INTO students (student_id, name, branch, email, cgpa) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.setString(2, name);
            ps.setString(3, branch);
            ps.setString(4, email);
            ps.setDouble(5, cgpa);

            ps.executeUpdate();
            System.out.println("Student added successfully to MySQL!");

        } catch (SQLException e) {
            System.out.println("Could not add student: " + e.getMessage());
        }
    }

    // View all students
    public void viewStudents() {
        System.out.println("\n--- All Students ---");

        ArrayList<Student> students = getStudents();

        if (students.isEmpty()) {
            System.out.println("No student records found.");
            return;
        }

        for (Student student : students) {
            student.displayDetails();
        }

        System.out.println("Total Students: " + students.size());
    }

    // Search student
    public void searchStudent(Scanner sc) {
        System.out.println("\n--- Search Student ---");

        int id = readInt(sc, "Enter Student ID: ");
        Student student = findStudent(id);

        if (student != null) {
            student.displayDetails();
        } else {
            System.out.println("Student not found.");
        }
    }

    // Update student
    public void updateStudent(Scanner sc) {
        System.out.println("\n--- Update Student ---");

        int id = readInt(sc, "Enter Student ID to update: ");
        Student student = findStudent(id);

        if (student == null) {
            System.out.println("Student not found.");
            return;
        }

        System.out.println("1. Update Name");
        System.out.println("2. Update Branch");
        System.out.println("3. Update Email");
        System.out.println("4. Update CGPA");

        int choice = readInt(sc, "Choose field to update: ");

        String column;
        String newText = null;
        double newCgpa = 0;

        switch (choice) {
            case 1:
                column = "name";
                newText = readText(sc, "Enter new name: ");
                break;

            case 2:
                column = "branch";
                newText = readText(sc, "Enter new branch: ");
                break;

            case 3:
                column = "email";

                while (true) {
                    newText = readText(sc, "Enter new email: ");

                    if (newText.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
                        break;
                    }

                    System.out.println("Please enter a valid email.");
                }
                break;

            case 4:
                column = "cgpa";
                newCgpa = readDouble(sc, "Enter new CGPA (0-10): ");

                if (newCgpa < 0 || newCgpa > 10) {
                    System.out.println("Invalid CGPA. Update cancelled.");
                    return;
                }
                break;

            default:
                System.out.println("Invalid update option.");
                return;
        }

        String sql = "UPDATE students SET " + column + " = ? WHERE student_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            if (choice == 4) {
                ps.setDouble(1, newCgpa);
            } else {
                ps.setString(1, newText);
            }

            ps.setInt(2, id);

            if (ps.executeUpdate() > 0) {
                System.out.println("Student updated successfully in MySQL!");
            } else {
                System.out.println("Student was not updated.");
            }

        } catch (SQLException e) {
            System.out.println("Could not update student: " + e.getMessage());
        }
    }

    // Delete student
    public void deleteStudent(Scanner sc) {
        System.out.println("\n--- Delete Student ---");

        int id = readInt(sc, "Enter Student ID to delete: ");

        String sql = "DELETE FROM students WHERE student_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            if (ps.executeUpdate() > 0) {
                System.out.println("Student deleted successfully from MySQL!");
            } else {
                System.out.println("Student not found.");
            }

        } catch (SQLException e) {
            System.out.println("Could not delete student: " + e.getMessage());
        }
    }

    // Check placement eligibility
    public void checkEligibility(Scanner sc) {
        System.out.println("\n--- Check Placement Eligibility ---");

        int id = readInt(sc, "Enter Student ID: ");
        Student student = findStudent(id);

        if (student == null) {
            System.out.println("Student not found.");
            return;
        }

        double minimumCgpa =
            readDouble(sc, "Enter required minimum CGPA: ");

        if (minimumCgpa < 0 || minimumCgpa > 10) {
            System.out.println("Minimum CGPA must be between 0 and 10.");
            return;
        }

        System.out.println("Student: " + student.getName());
        System.out.println("Student CGPA: " + student.getCgpa());
        System.out.println("Required CGPA: " + minimumCgpa);

        if (student.getCgpa() >= minimumCgpa) {
            System.out.println("Result: Eligible based on CGPA.");
        } else {
            System.out.println("Result: Not eligible based on CGPA.");
        }

        System.out.println(
            "Note: Actual company eligibility may include other criteria.");
    }
}
