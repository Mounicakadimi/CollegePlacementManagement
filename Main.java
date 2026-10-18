
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        // Initialize existing services
        StudentService studentService = new StudentService();
        CompanyService companyService = new CompanyService();
        JobService jobService = new JobService();

        // Initialize placement services
        JobApplicationService applicationService =
                new JobApplicationService();

        PlacementResultService resultService =
                new PlacementResultService(applicationService);

        PlacementStatisticsService statisticsService =
                new PlacementStatisticsService();

        boolean running = true;

        while (running) {

            System.out.println("\n====================================");
            System.out.println(" COLLEGE PLACEMENT MANAGEMENT SYSTEM");
            System.out.println("====================================");
            System.out.println("1. Student Management");
            System.out.println("2. Company Management");
            System.out.println("3. Job Drive Management");
            System.out.println("4. Job Applications");
            System.out.println("5. Placement Results & Reports");
            System.out.println("6. Placement Statistics");
            System.out.println("7. Exit");

            System.out.print("Enter your choice: ");

            String input = sc.nextLine().trim();
            int choice;

            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println(
                        "Please enter a number from 1 to 7.");
                continue;
            }

            switch (choice) {

                // STUDENT MANAGEMENT
                case 1: {
                    boolean studentMenu = true;

                    while (studentMenu) {
                        System.out.println("\n--- STUDENT MANAGEMENT ---");
                        System.out.println("1. Add Student");
                        System.out.println("2. View All Students");
                        System.out.println("3. Search Student");
                        System.out.println("4. Update Student");
                        System.out.println("5. Delete Student");
                        System.out.println("6. Check Placement Eligibility");
                        System.out.println("7. Back to Main Menu");

                        System.out.print("Enter your choice: ");

                        int studentChoice;

                        try {
                            studentChoice = Integer.parseInt(
                                    sc.nextLine().trim());
                        } catch (NumberFormatException e) {
                            System.out.println("Enter a valid number.");
                            continue;
                        }

                        switch (studentChoice) {
                            case 1:
                                studentService.addStudent(sc);
                                break;
                            case 2:
                                studentService.viewStudents();
                                break;
                            case 3:
                                studentService.searchStudent(sc);
                                break;
                            case 4:
                                studentService.updateStudent(sc);
                                break;
                            case 5:
                                studentService.deleteStudent(sc);
                                break;
                            case 6:
                                studentService.checkEligibility(sc);
                                break;
                            case 7:
                                studentMenu = false;
                                break;
                            default:
                                System.out.println("Invalid choice.");
                        }
                    }
                    break;
                }

                // COMPANY MANAGEMENT
                case 2: {
                    boolean companyMenu = true;

                    while (companyMenu) {
                        System.out.println("\n--- COMPANY MANAGEMENT ---");
                        System.out.println("1. Add Company");
                        System.out.println("2. View Companies");
                        System.out.println("3. Search Company");
                        System.out.println("4. Update Company");
                        System.out.println("5. Delete Company");
                        System.out.println("6. Back to Main Menu");

                        System.out.print("Enter your choice: ");

                        int companyChoice;

                        try {
                            companyChoice = Integer.parseInt(
                                    sc.nextLine().trim());
                        } catch (NumberFormatException e) {
                            System.out.println("Enter a valid number.");
                            continue;
                        }

                        switch (companyChoice) {
                            case 1:
                                companyService.addCompany(sc);
                                break;
                            case 2:
                                companyService.viewCompanies();
                                break;
                            case 3:
                                companyService.searchCompany(sc);
                                break;
                            case 4:
                                companyService.updateCompany(sc);
                                break;
                            case 5:
                                companyService.deleteCompany(sc);
                                break;
                            case 6:
                                companyMenu = false;
                                break;
                            default:
                                System.out.println("Invalid choice.");
                        }
                    }
                    break;
                }

                // JOB DRIVE MANAGEMENT
                case 3: {
                    boolean jobMenu = true;

                    while (jobMenu) {
                        System.out.println("\n--- JOB DRIVE MANAGEMENT ---");
                        System.out.println("1. Add Job Drive");
                        System.out.println("2. View All Job Drives");
                        System.out.println("3. Search Job Drive");
                        System.out.println("4. Update Job Drive");
                        System.out.println("5. Delete Job Drive");
                        System.out.println("6. View Eligible Students");
                        System.out.println("7. Back to Main Menu");

                        System.out.print("Enter your choice: ");

                        int jobChoice;

                        try {
                            jobChoice = Integer.parseInt(
                                    sc.nextLine().trim());
                        } catch (NumberFormatException e) {
                            System.out.println("Enter a valid number.");
                            continue;
                        }

                        switch (jobChoice) {
                            case 1:
                                jobService.addJob(sc);
                                break;
                            case 2:
                                jobService.viewJobs();
                                break;
                            case 3:
                                jobService.searchJob(sc);
                                break;
                            case 4:
                                jobService.updateJob(sc);
                                break;
                            case 5:
                                jobService.deleteJob(sc);
                                break;
                            case 6:
                                jobService.viewEligibleStudents(
                                        sc, studentService);
                                break;
                            case 7:
                                jobMenu = false;
                                break;
                            default:
                                System.out.println("Invalid choice.");
                        }
                    }
                    break;
                }

                // JOB APPLICATIONS
                case 4: {
                    boolean applicationMenu = true;

                    while (applicationMenu) {
                        System.out.println("\n--- JOB APPLICATIONS ---");
                        System.out.println("1. Apply for a Job");
                        System.out.println("2. View All Applications");
                        System.out.println("3. Search Application");
                        System.out.println("4. Withdraw Application");
                        System.out.println("5. Back to Main Menu");

                        System.out.print("Enter your choice: ");

                        int appChoice;

                        try {
                            appChoice = Integer.parseInt(
                                    sc.nextLine().trim());
                        } catch (NumberFormatException e) {
                            System.out.println("Enter a valid number.");
                            continue;
                        }

                        switch (appChoice) {
                            case 1:
                                applicationService.applyForJob(
                                        sc, studentService, jobService);
                                break;
                            case 2:
                                applicationService.viewApplications();
                                break;
                            case 3:
                                applicationService.searchApplication(sc);
                                break;
                            case 4:
                                applicationService.withdrawApplication(sc);
                                break;
                            case 5:
                                applicationMenu = false;
                                break;
                            default:
                                System.out.println("Invalid choice.");
                        }
                    }
                    break;
                }

                // PLACEMENT RESULTS AND REPORTS
                case 5: {
                    boolean resultMenu = true;

                    while (resultMenu) {
                        System.out.println(
                                "\n--- PLACEMENT RESULTS & REPORTS ---");
                        System.out.println("1. View All Results");
                        System.out.println("2. Update Placement Result");
                        System.out.println("3. View Selected Applications");
                        System.out.println("4. View Rejected Applications Report");
                        System.out.println("5. View Overall Placement Summary");
                        System.out.println("6. Back to Main Menu");

                        System.out.print("Enter your choice: ");

                        int resultChoice;

                        try {
                            resultChoice = Integer.parseInt(
                                    sc.nextLine().trim());
                        } catch (NumberFormatException e) {
                            System.out.println("Enter a valid number.");
                            continue;
                        }

                        switch (resultChoice) {
                            case 1:
                                resultService.viewResults();
                                break;
                            case 2:
                                resultService.updateResult(sc);
                                break;
                            case 3:
                                resultService.viewSelectedStudents();
                                break;
                            case 4:
                                resultService.viewRejectedApplications();
                                break;
                            case 5:
                                resultService.viewPlacementSummary();
                                break;
                            case 6:
                                resultMenu = false;
                                break;
                            default:
                                System.out.println(
                                        "Invalid choice. Select 1 to 6.");
                        }
                    }
                    break;
                }

                // PLACEMENT STATISTICS
                case 6:
                    statisticsService.displayStatistics(
                            studentService,
                            jobService,
                            applicationService);
                    break;

                // EXIT
                case 7:
                    running = false;
                    System.out.println(
                            "Thank you for using the College Placement "
                            + "Management System!");
                    break;

                default:
                    System.out.println(
                            "Invalid choice. Select 1 to 7.");
            }
        }

        sc.close();
    }
}

