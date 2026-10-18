
public class JobApplication {

    private int applicationId;
    private int studentId;
    private int jobId;
    private String status;

    public JobApplication(int applicationId, int studentId, int jobId) {
        this.applicationId = applicationId;
        this.studentId = studentId;
        this.jobId = jobId;
        this.status = "PENDING";
    }

    public int getApplicationId() {
        return applicationId;
    }

    public int getStudentId() {
        return studentId;
    }

    public int getJobId() {
        return jobId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void displayDetails() {
        System.out.println("------------------------------");
        System.out.println("Application ID : " + applicationId);
        System.out.println("Student ID     : " + studentId);
        System.out.println("Job ID         : " + jobId);
        System.out.println("Status         : " + status);
        System.out.println("------------------------------");
    }
}
