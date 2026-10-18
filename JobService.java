
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class JobService {

    private int readInt(Scanner sc, String message) {
        while (true) {
            System.out.print(message);
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Enter a valid whole number.");
            }
        }
    }

    private double readDouble(Scanner sc, String message) {
        while (true) {
            System.out.print(message);
            try {
                return Double.parseDouble(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Enter a valid number.");
            }
        }
    }

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

    public ArrayList<Job> getJobs() {
        ArrayList<Job> jobs = new ArrayList<>();

        String sql = "SELECT job_id, company_name, job_role, location, " +
                     "package_lpa, minimum_cgpa FROM jobs";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                jobs.add(new Job(
                    rs.getInt("job_id"),
                    rs.getString("company_name"),
                    rs.getString("job_role"),
                    rs.getString("location"),
                    rs.getDouble("package_lpa"),
                    rs.getDouble("minimum_cgpa")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error loading jobs: " + e.getMessage());
        }

        return jobs;
    }

    private Job findJob(int jobId) {
        String sql = "SELECT job_id, company_name, job_role, location, " +
                     "package_lpa, minimum_cgpa FROM jobs WHERE job_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, jobId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Job(
                        rs.getInt("job_id"),
                        rs.getString("company_name"),
                        rs.getString("job_role"),
                        rs.getString("location"),
                        rs.getDouble("package_lpa"),
                        rs.getDouble("minimum_cgpa")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("Error searching job: " + e.getMessage());
        }

        return null;
    }

    public void addJob(Scanner sc) {
        System.out.println("\n--- Add Job Drive ---");

        int id = readInt(sc, "Enter Job ID: ");

        if (id <= 0) {
            System.out.println("Job ID must be positive.");
            return;
        }

        if (findJob(id) != null) {
            System.out.println("Job ID already exists.");
            return;
        }

        String company = readText(sc, "Enter Company Name: ");
        String role = readText(sc, "Enter Job Role: ");
        String location = readText(sc, "Enter Job Location: ");
        double packageLPA = readDouble(sc, "Enter Package in LPA: ");
        double cgpa = readDouble(sc, "Enter Minimum CGPA (0-10): ");

        if (packageLPA < 0) {
            System.out.println("Package cannot be negative.");
            return;
        }

        if (cgpa < 0 || cgpa > 10) {
            System.out.println("CGPA must be between 0 and 10.");
            return;
        }

        String sql = "INSERT INTO jobs " +
            "(job_id, company_name, job_role, location, package_lpa, minimum_cgpa) " +
            "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.setString(2, company);
            ps.setString(3, role);
            ps.setString(4, location);
            ps.setDouble(5, packageLPA);
            ps.setDouble(6, cgpa);

            if (ps.executeUpdate() > 0) {
                System.out.println("Job drive added successfully!");
            }
        } catch (SQLException e) {
            System.out.println("Could not add job: " + e.getMessage());
        }
    }

    public void viewJobs() {
        ArrayList<Job> jobs = getJobs();

        System.out.println("\n--- Available Job Drives ---");

        if (jobs.isEmpty()) {
            System.out.println("No job drives available, or jobs could not be loaded.");
            return;
        }

        for (Job job : jobs) {
            job.displayDetails();
        }

        System.out.println("Total job drives: " + jobs.size());
    }

    public void searchJob(Scanner sc) {
        int id = readInt(sc, "Enter Job ID to search: ");
        Job job = findJob(id);

        if (job != null) {
            job.displayDetails();
        } else {
            System.out.println("Job drive not found.");
        }
    }

    public void updateJob(Scanner sc) {
        int id = readInt(sc, "Enter Job ID to update: ");
        Job job = findJob(id);

        if (job == null) {
            System.out.println("Job drive not found.");
            return;
        }

        System.out.println("1. Update Company Name");
        System.out.println("2. Update Job Role");
        System.out.println("3. Update Location");
        System.out.println("4. Update Package");
        System.out.println("5. Update Minimum CGPA");

        int choice = readInt(sc, "Choose option: ");
        String column;
        String value = null;
        double number = 0;

        switch (choice) {
            case 1:
                column = "company_name";
                value = readText(sc, "New company name: ");
                break;
            case 2:
                column = "job_role";
                value = readText(sc, "New job role: ");
                break;
            case 3:
                column = "location";
                value = readText(sc, "New location: ");
                break;
            case 4:
                column = "package_lpa";
                number = readDouble(sc, "New package in LPA: ");
                if (number < 0) {
                    System.out.println("Package cannot be negative.");
                    return;
                }
                break;
            case 5:
                column = "minimum_cgpa";
                number = readDouble(sc, "New minimum CGPA: ");
                if (number < 0 || number > 10) {
                    System.out.println("CGPA must be between 0 and 10.");
                    return;
                }
                break;
            default:
                System.out.println("Invalid option.");
                return;
        }

        String sql = "UPDATE jobs SET " + column + " = ? WHERE job_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            if (choice <= 3) {
                ps.setString(1, value);
            } else {
                ps.setDouble(1, number);
            }

            ps.setInt(2, id);

            if (ps.executeUpdate() > 0) {
                System.out.println("Job drive updated successfully!");
            } else {
                System.out.println("No job drive was updated.");
            }
        } catch (SQLException e) {
            System.out.println("Could not update job: " + e.getMessage());
        }
    }

    public void deleteJob(Scanner sc) {
        int id = readInt(sc, "Enter Job ID to delete: ");

        String sql = "DELETE FROM jobs WHERE job_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            if (ps.executeUpdate() > 0) {
                System.out.println("Job drive deleted successfully!");
            } else {
                System.out.println("Job drive not found.");
            }
        } catch (SQLException e) {
            System.out.println("Could not delete job: " + e.getMessage());
        }
    }

    public void viewEligibleStudents(Scanner sc, StudentService studentService) {
        int id = readInt(sc, "Enter Job ID: ");
        Job job = findJob(id);

        if (job == null) {
            System.out.println("Job drive not found.");
            return;
        }

        boolean found = false;

        System.out.println("\nEligible students for " +
            job.getCompanyName() + " - " + job.getJobRole());

        for (Student student : studentService.getStudents()) {
            if (student.getCgpa() >= job.getMinimumCGPA()) {
                student.displayDetails();
                found = true;
            }
        }

        if (!found) {
            System.out.println("No eligible students found.");
        }
    }
}
