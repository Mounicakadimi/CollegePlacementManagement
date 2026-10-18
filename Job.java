
public class Job {

    private int jobId;
    private String companyName;
    private String jobRole;
    private String location;
    private double packageLPA;
    private double minimumCGPA;

    public Job(int jobId, String companyName, String jobRole,
               String location, double packageLPA,
               double minimumCGPA) {

        this.jobId = jobId;
        this.companyName = companyName;
        this.jobRole = jobRole;
        this.location = location;
        this.packageLPA = packageLPA;
        this.minimumCGPA = minimumCGPA;
    }

    public int getJobId() {
        return jobId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getJobRole() {
        return jobRole;
    }

    public String getLocation() {
        return location;
    }

    public double getPackageLPA() {
        return packageLPA;
    }

    public double getMinimumCGPA() {
        return minimumCGPA;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setJobRole(String jobRole) {
        this.jobRole = jobRole;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setPackageLPA(double packageLPA) {
        this.packageLPA = packageLPA;
    }

    public void setMinimumCGPA(double minimumCGPA) {
        this.minimumCGPA = minimumCGPA;
    }

    public void displayDetails() {
        System.out.println("------------------------------------");
        System.out.println("Job ID       : " + jobId);
        System.out.println("Company      : " + companyName);
        System.out.println("Job Role     : " + jobRole);
        System.out.println("Location     : " + location);
        System.out.println("Package      : " + packageLPA + " LPA");
        System.out.println("Minimum CGPA : " + minimumCGPA);
        System.out.println("------------------------------------");
    }
}
