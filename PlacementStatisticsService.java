
import java.util.ArrayList;

public class PlacementStatisticsService {

    public void displayStatistics(
            StudentService studentService,
            JobService jobService,
            JobApplicationService applicationService) {

        // Get data from existing services
        ArrayList<Student> students =
                studentService.getStudents();

        ArrayList<Job> jobs =
                jobService.getJobs();

        ArrayList<JobApplication> applications =
                applicationService.getApplications();

        // Calculate totals
        int totalStudents = students.size();
        int totalJobs = jobs.size();
        int totalApplications = applications.size();

        int selected = 0;
        int rejected = 0;
        int pending = 0;
        int withdrawn = 0;

        // Count application statuses
        for (JobApplication application : applications) {

            String status = application.getStatus();

            if (status == null) {
                continue;
            }

            switch (status.trim().toUpperCase()) {

                case "SELECTED":
                    selected++;
                    break;

                case "REJECTED":
                    rejected++;
                    break;

                case "PENDING":
                    pending++;
                    break;

                case "WITHDRAWN":
                    withdrawn++;
                    break;

                default:
                    System.out.println(
                            "Unknown application status: " + status);
                    break;
            }
        }

        // Calculate selection percentage
        double placementPercentage = 0.0;

        if (totalStudents > 0) {
            placementPercentage =
                    (selected * 100.0) / totalStudents;
        }

        // Display statistics
        System.out.println("\n========================================");
        System.out.println("          PLACEMENT STATISTICS");
        System.out.println("========================================");

        System.out.println(
                "Total Students          : " + totalStudents);

        System.out.println(
                "Total Job Drives        : " + totalJobs);

        System.out.println(
                "Total Applications      : " + totalApplications);

        System.out.println(
                "Selected Applications   : " + selected);

        System.out.println(
                "Rejected Applications   : " + rejected);

        System.out.println(
                "Pending Applications    : " + pending);

        System.out.println(
                "Withdrawn Applications  : " + withdrawn);

        System.out.printf(
                "Selection Rate          : %.2f%%%n",
                placementPercentage);

        System.out.println("========================================");
    }
}

