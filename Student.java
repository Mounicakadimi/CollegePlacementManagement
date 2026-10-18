

public class Student {

    private int studentId;
    private String name;
    private String branch;
    private String email;
    private double cgpa;

    // Constructor
    public Student(int studentId, String name,
                   String branch, String email, double cgpa) {
        this.studentId = studentId;
        this.name = name;
        this.branch = branch;
        this.email = email;
        this.cgpa = cgpa;
    }

    // Getter methods
    public int getStudentId() {
        return studentId;
    }

    public String getName() {
        return name;
    }

    public String getBranch() {
        return branch;
    }

    public String getEmail() {
        return email;
    }

    public double getCgpa() {
        return cgpa;
    }

    // Setter methods
    public void setName(String name) {
        this.name = name;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCgpa(double cgpa) {
        this.cgpa = cgpa;
    }

    // Display student details
    public void displayDetails() {
        System.out.println("--------------------------------");
        System.out.println("Student ID : " + studentId);
        System.out.println("Name       : " + name);
        System.out.println("Branch     : " + branch);
        System.out.println("Email      : " + email);
        System.out.println("CGPA       : " + cgpa);
        System.out.println("--------------------------------");
    }
}
