
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class PlacementResultService {

    private final JobApplicationService applicationService;

    public PlacementResultService(
            JobApplicationService applicationService) {
        this.applicationService = applicationService;
    }

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

    // 1. View all placement results
    public void viewResults() {
        System.out.println("\n--- Placement Results ---");
        displayResults(null);
    }

    // Display results, optionally filtered by status
    private void displayResults(String statusFilter) {

        String sql =
                "SELECT result_id, application_id, "
              + "result_status, joining_date "
              + "FROM placement_results";

        if (statusFilter != null) {
            sql += " WHERE result_status = ?";
        }

        sql += " ORDER BY result_id";

        boolean found = false;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            if (statusFilter != null) {
                ps.setString(1, statusFilter);
            }

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    found = true;

                    int applicationId =
                            rs.getInt("application_id");

                    System.out.println("\nResult ID: "
                            + rs.getInt("result_id"));

                    System.out.println("Application ID: "
                            + applicationId);

                    System.out.println("Result Status: "
                            + rs.getString("result_status"));

                    System.out.println("Joining Date: "
                            + rs.getDate("joining_date"));

                    JobApplication application =
                            applicationService.findApplication(
                                    applicationId);

                    if (application != null) {
                        application.setStatus(
                                rs.getString("result_status"));
                        application.displayDetails();
                    }

                    System.out.println("-------------------------");
                }
            }

        } catch (SQLException e) {
            System.out.println("Error viewing results: "
                    + e.getMessage());
        }

        if (!found) {
            System.out.println("No placement results available.");
        }
    }

    // 2. Update a placement result in MySQL
    public void updateResult(Scanner sc) {

        int applicationId =
                readInt(sc, "Enter Application ID: ");

        JobApplication application =
                applicationService.findApplication(applicationId);

        if (application == null) {
            System.out.println("Application not found.");
            return;
        }

        if ("WITHDRAWN".equalsIgnoreCase(
                application.getStatus())) {
            System.out.println(
                    "Cannot update a withdrawn application.");
            return;
        }

        System.out.println("\nSelect Placement Result:");
        System.out.println("1. Selected");
        System.out.println("2. Rejected");
        System.out.println("3. Pending");

        int choice = readInt(sc, "Enter your choice: ");

        String status;

        switch (choice) {
            case 1:
                status = "SELECTED";
                break;
            case 2:
                status = "REJECTED";
                break;
            case 3:
                status = "PENDING";
                break;
            default:
                System.out.println("Invalid choice.");
                return;
        }

        try (Connection con = DBConnection.getConnection()) {

            // Find existing result
            int resultId = -1;

            String findSql =
                    "SELECT result_id FROM placement_results "
                  + "WHERE application_id = ?";

            try (PreparedStatement ps =
                    con.prepareStatement(findSql)) {

                ps.setInt(1, applicationId);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        resultId = rs.getInt("result_id");
                    }
                }
            }

            if (resultId != -1) {

                // Update existing result
                String updateSql =
                        "UPDATE placement_results "
                      + "SET result_status = ? "
                      + "WHERE result_id = ?";

                try (PreparedStatement ps =
                        con.prepareStatement(updateSql)) {

                    ps.setString(1, status);
                    ps.setInt(2, resultId);
                    ps.executeUpdate();
                }

            } else {

                // Generate a new result ID
                int newResultId;

                try (Statement stmt = con.createStatement();
                     ResultSet rs = stmt.executeQuery(
                             "SELECT COALESCE(MAX(result_id), 0) + 1 "
                           + "FROM placement_results")) {

                    rs.next();
                    newResultId = rs.getInt(1);
                }

                // Insert new result
                String insertSql =
                        "INSERT INTO placement_results "
                      + "(result_id, application_id, result_status) "
                      + "VALUES (?, ?, ?)";

                try (PreparedStatement ps =
                        con.prepareStatement(insertSql)) {

                    ps.setInt(1, newResultId);
                    ps.setInt(2, applicationId);
                    ps.setString(3, status);
                    ps.executeUpdate();
                }
            }

            // Synchronize application status
            String applicationSql =
                    "UPDATE job_applications "
                  + "SET status = ? WHERE application_id = ?";

            try (PreparedStatement ps =
                    con.prepareStatement(applicationSql)) {

                ps.setString(1, status);
                ps.setInt(2, applicationId);
                ps.executeUpdate();
            }

            application.setStatus(status);

            System.out.println(
                    "Placement result saved successfully!");
            application.displayDetails();

        } catch (SQLException e) {
            System.out.println("Error updating result: "
                    + e.getMessage());
        }
    }

    // 3. View selected applications
    public void viewSelectedStudents() {
        System.out.println("\n--- Selected Students ---");
        displayResults("SELECTED");
    }

    // 4. View rejected applications
    public void viewRejectedApplications() {
        System.out.println("\n--- Rejected Applications Report ---");
        displayResults("REJECTED");
    }

    // 5. View overall placement summary
    public void viewPlacementSummary() {

        String sql =
                "SELECT result_status, COUNT(*) AS total "
              + "FROM placement_results "
              + "GROUP BY result_status";

        int selected = 0;
        int rejected = 0;
        int pending = 0;
        int withdrawn = 0;
        int total = 0;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                String status = rs.getString("result_status");
                int count = rs.getInt("total");

                total += count;

                if (status == null) {
                    continue;
                }

                switch (status.trim().toUpperCase()) {
                    case "SELECTED":
                        selected += count;
                        break;

                    case "REJECTED":
                        rejected += count;
                        break;

                    case "PENDING":
                        pending += count;
                        break;

                    case "WITHDRAWN":
                        withdrawn += count;
                        break;
                }
            }

            System.out.println("\n====================================");
            System.out.println("      PLACEMENT SUMMARY REPORT");
            System.out.println("====================================");

            System.out.println("Total Results : " + total);
            System.out.println("Selected      : " + selected);
            System.out.println("Rejected      : " + rejected);
            System.out.println("Pending       : " + pending);
            System.out.println("Withdrawn     : " + withdrawn);

            double selectionRate =
                    total > 0 ? selected * 100.0 / total : 0.0;

            System.out.printf(
                    "Selection Rate: %.2f%%%n", selectionRate);

            System.out.println("====================================");

        } catch (SQLException e) {
            System.out.println(
                    "Error generating placement summary: "
                    + e.getMessage());
        }
    }
}

