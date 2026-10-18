
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class CompanyService {

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

    // Load companies from MySQL
    public ArrayList<Company> getCompanies() {
        ArrayList<Company> companies = new ArrayList<>();

        String sql = "SELECT company_id, company_name, location, "
                   + "job_role, package_lpa, minimum_cgpa FROM companies";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                companies.add(new Company(
                    rs.getInt("company_id"),
                    rs.getString("company_name"),
                    rs.getString("location"),
                    rs.getString("job_role"),
                    rs.getDouble("package_lpa"),
                    rs.getDouble("minimum_cgpa")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Error loading companies: " + e.getMessage());
        }

        return companies;
    }

    // Find company by ID in MySQL
    private Company findCompany(int id) {
        String sql = "SELECT * FROM companies WHERE company_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Company(
                        rs.getInt("company_id"),
                        rs.getString("company_name"),
                        rs.getString("location"),
                        rs.getString("job_role"),
                        rs.getDouble("package_lpa"),
                        rs.getDouble("minimum_cgpa")
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println("Error searching company: " + e.getMessage());
        }

        return null;
    }

    // Add company
    public void addCompany(Scanner sc) {
        System.out.println("\n--- Add Company ---");

        int id = readInt(sc, "Enter Company ID: ");

        if (id <= 0) {
            System.out.println("Company ID must be positive.");
            return;
        }

        if (findCompany(id) != null) {
            System.out.println("Company ID already exists.");
            return;
        }

        String name = readText(sc, "Enter Company Name: ");
        String location = readText(sc, "Enter Company Location: ");
        String role = readText(sc, "Enter Job Role: ");

        double packageLPA = readDouble(sc, "Enter Package in LPA: ");

        if (packageLPA < 0) {
            System.out.println("Package cannot be negative.");
            return;
        }

        double minimumCGPA = readDouble(sc, "Enter Minimum CGPA (0-10): ");

        if (minimumCGPA < 0 || minimumCGPA > 10) {
            System.out.println("Minimum CGPA must be between 0 and 10.");
            return;
        }

        String sql = "INSERT INTO companies "
                   + "(company_id, company_name, location, job_role, "
                   + "package_lpa, minimum_cgpa) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.setString(2, name);
            ps.setString(3, location);
            ps.setString(4, role);
            ps.setDouble(5, packageLPA);
            ps.setDouble(6, minimumCGPA);

            ps.executeUpdate();
            System.out.println("Company added successfully to MySQL!");

        } catch (SQLException e) {
            System.out.println("Could not add company: " + e.getMessage());
        }
    }

    // View all companies
    public void viewCompanies() {
        System.out.println("\n--- Company List ---");

        ArrayList<Company> companies = getCompanies();

        if (companies.isEmpty()) {
            System.out.println("No companies registered.");
            return;
        }

        for (Company company : companies) {
            company.displayDetails();
        }

        System.out.println("Total Companies: " + companies.size());
    }

    // Search company
    public void searchCompany(Scanner sc) {
        System.out.println("\n--- Search Company ---");

        int id = readInt(sc, "Enter Company ID to search: ");
        Company company = findCompany(id);

        if (company != null) {
            company.displayDetails();
        } else {
            System.out.println("Company not found.");
        }
    }

    // Update company
    public void updateCompany(Scanner sc) {
        System.out.println("\n--- Update Company ---");

        int id = readInt(sc, "Enter Company ID to update: ");

        if (findCompany(id) == null) {
            System.out.println("Company not found.");
            return;
        }

        System.out.println("1. Update Company Name");
        System.out.println("2. Update Location");
        System.out.println("3. Update Job Role");
        System.out.println("4. Update Package");
        System.out.println("5. Update Minimum CGPA");

        int choice = readInt(sc, "Choose option: ");

        String column;
        String textValue = null;
        double numberValue = 0;

        switch (choice) {
            case 1:
                column = "company_name";
                textValue = readText(sc, "Enter new name: ");
                break;

            case 2:
                column = "location";
                textValue = readText(sc, "Enter new location: ");
                break;

            case 3:
                column = "job_role";
                textValue = readText(sc, "Enter new job role: ");
                break;

            case 4:
                column = "package_lpa";
                numberValue = readDouble(sc, "Enter new package in LPA: ");

                if (numberValue < 0) {
                    System.out.println("Package cannot be negative.");
                    return;
                }
                break;

            case 5:
                column = "minimum_cgpa";
                numberValue = readDouble(sc, "Enter new minimum CGPA: ");

                if (numberValue < 0 || numberValue > 10) {
                    System.out.println("CGPA must be between 0 and 10.");
                    return;
                }
                break;

            default:
                System.out.println("Invalid option.");
                return;
        }

        String sql = "UPDATE companies SET " + column
                   + " = ? WHERE company_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            if (choice <= 3) {
                ps.setString(1, textValue);
            } else {
                ps.setDouble(1, numberValue);
            }

            ps.setInt(2, id);

            if (ps.executeUpdate() > 0) {
                System.out.println("Company updated successfully in MySQL!");
            } else {
                System.out.println("Company was not updated.");
            }

        } catch (SQLException e) {
            System.out.println("Could not update company: " + e.getMessage());
        }
    }

    // Delete company
    public void deleteCompany(Scanner sc) {
        System.out.println("\n--- Delete Company ---");

        int id = readInt(sc, "Enter Company ID to delete: ");

        String sql = "DELETE FROM companies WHERE company_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            if (ps.executeUpdate() > 0) {
                System.out.println("Company deleted successfully from MySQL!");
            } else {
                System.out.println("Company not found.");
            }

        } catch (SQLException e) {
            System.out.println("Could not delete company: " + e.getMessage());
        }
    }
}

