
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/college_placement";

    private static final String USER = "root";
    private static final String PASSWORD = "Mounica@kadimi08";

    public static Connection getConnection() throws SQLException {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException(
                "MySQL JDBC driver not found. Check the lib folder "
                + "and ensure the Connector/J JAR is on the classpath.",
                e
            );
        }

        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}

