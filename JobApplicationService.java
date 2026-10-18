

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

public class JobApplicationService {

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

    public ArrayList<JobApplication> getApplications() {
        ArrayList<JobApplication> applications = new ArrayList<>();

        String sql = "SELECT application_id, student_id, job_id, status "
                   + "FROM job_applications ORDER BY application_id";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                JobApplication application = new JobApplication(
                    rs.getInt("application_id"),
                    rs.getInt("student_id"),
                    rs.getInt("job_id")
                );

                application.setStatus(rs.getString("status"));
                applications.add(application);
            }

        } catch (SQLException e) {
            System.out.println("Error loading applications: " + e.getMessage());
        }

        return applications;
    }

    public JobApplication findApplication(int id) {
        String sql = "SELECT application_id, student_id, job_id, status "
                   + "FROM job_applications WHERE application_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    JobApplication application = new JobApplication(
                        rs.getInt("application_id"),
                        rs.getInt("student_id"),
                        rs.getInt("job_id")
                    );

                    application.setStatus(rs.getString("status"));
                    return application;
                }
            }

        } catch (SQLException e) {
            System.out.println("Error searching application: " + e.getMessage());
        }

        return null;
    }

    public void applyForJob(
            Scanner sc,
            StudentService studentService,
            JobService jobService) {

        System.out.println("\n--- Apply for Job ---");

        int studentId = readInt(sc, "Enter Student ID: ");
        Student student = null;

        for (Student s : studentService.getStudents()) {
            if (s.getStudentId() == studentId) {
                student = s;
                break;
            }
        }

        if (student == null) {
            System.out.println("Student not found.");
            return;
        }

        int jobId = readInt(sc, "Enter Job ID: ");
        Job job = null;

        for (Job j : jobService.getJobs()) {
            if (j.getJobId() == jobId) {
                job = j;
                break;
            }
        }

        if (job == null) {
            System.out.println("Job drive not found.");
            return;
        }

        if (student.getCgpa() < job.getMinimumCGPA()) {
            System.out.println(
                "You are not eligible for this job based on CGPA."
            );
            return;
        }

        String duplicateSql =
            "SELECT COUNT(*) FROM job_applications "
          + "WHERE student_id = ? AND job_id = ? AND status <> 'WITHDRAWN'";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement check = con.prepareStatement(duplicateSql)) {

            check.setInt(1, studentId);
            check.setInt(2, jobId);

            try (ResultSet rs = check.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    System.out.println(
                        "This student has already applied for this job."
                    );
                    return;
                }
            }

            // Generate the next application ID.
            // This is suitable for a small, single-user console project.
            int applicationId;

            String idSql =
                "SELECT COALESCE(MAX(application_id), 0) + 1 "
              + "FROM job_applications";

            try (Statement statement = con.createStatement();
                 ResultSet rs = statement.executeQuery(idSql)) {
                rs.next();
                applicationId = rs.getInt(1);
            }

            String insertSql =
                "INSERT INTO job_applications "
              + "(application_id, student_id, job_id, status) "
              + "VALUES (?, ?, ?, ?)";

            try (PreparedStatement insert =
                     con.prepareStatement(insertSql)) {

                insert.setInt(1, applicationId);
                insert.setInt(2, studentId);
                insert.setInt(3, jobId);
                insert.setString(4, "PENDING");

                insert.executeUpdate();
            }

            JobApplication application =
                new JobApplication(applicationId, studentId, jobId);

            System.out.println("Application submitted successfully!");
            application.displayDetails();

        } catch (SQLException e) {
            System.out.println(
                "Could not submit application: " + e.getMessage()
            );
        }
    }

    public void viewApplications() {
        System.out.println("\n--- All Job Applications ---");

        ArrayList<JobApplication> applications = getApplications();

        if (applications.isEmpty()) {
            System.out.println("No applications available.");
            return;
        }

        for (JobApplication application : applications) {
            application.displayDetails();
        }

        System.out.println("Total applications: " + applications.size());
    }

    public void searchApplication(Scanner sc) {
        int id = readInt(sc, "Enter Application ID: ");
        JobApplication application = findApplication(id);

        if (application != null) {
            application.displayDetails();
        } else {
            System.out.println("Application not found.");
        }
    }

    public void withdrawApplication(Scanner sc) {
        int id = readInt(sc, "Enter Application ID to withdraw: ");
        JobApplication application = findApplication(id);

        if (application == null) {
            System.out.println("Application not found.");
            return;
        }

        if (!"PENDING".equals(application.getStatus())) {
            System.out.println(
                "Only pending applications can be withdrawn."
            );
            return;
        }

        String sql =
            "UPDATE job_applications SET status = 'WITHDRAWN' "
          + "WHERE application_id = ? AND status = 'PENDING'";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            if (ps.executeUpdate() > 0) {
                System.out.println("Application withdrawn successfully.");
            } else {
                System.out.println(
                    "Application status changed. Please check it again."
                );
            }

        } catch (SQLException e) {
            System.out.println(
                "Could not withdraw application: " + e.getMessage()
            );
        }
    }
}
