
public class Company {

    private int companyId;
    private String companyName;
    private String location;
    private String jobRole;
    private double packageLPA;
    private double minimumCGPA;

    // Constructor
    public Company(int companyId, String companyName,
                   String location, String jobRole,
                   double packageLPA, double minimumCGPA) {

        this.companyId = companyId;
        this.companyName = companyName;
        this.location = location;
        this.jobRole = jobRole;
        this.packageLPA = packageLPA;
        this.minimumCGPA = minimumCGPA;
    }

    // Getter methods
    public int getCompanyId() {
        return companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getLocation() {
        return location;
    }

    public String getJobRole() {
        return jobRole;
    }

    public double getPackageLPA() {
        return packageLPA;
    }

    public double getMinimumCGPA() {
        return minimumCGPA;
    }

    // Setter methods
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setJobRole(String jobRole) {
        this.jobRole = jobRole;
    }

    public void setPackageLPA(double packageLPA) {
        this.packageLPA = packageLPA;
    }

    public void setMinimumCGPA(double minimumCGPA) {
        this.minimumCGPA = minimumCGPA;
    }

    // Display company details
    public void displayDetails() {
        System.out.println("------------------------------------");
        System.out.println("Company ID   : " + companyId);
        System.out.println("Company Name : " + companyName);
        System.out.println("Location     : " + location);
        System.out.println("Job Role     : " + jobRole);
        System.out.println("Package      : " + packageLPA + " LPA");
        System.out.println("Minimum CGPA : " + minimumCGPA);
        System.out.println("------------------------------------");
    }
}
