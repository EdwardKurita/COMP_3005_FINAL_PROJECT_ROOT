import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;

public class Main {
    public static void main() {
        String url = "jdbc:postgresql://localhost:5433/comp_3005_final_project";
        String user = "postgres";
        String password = "password";

        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection(url, user, password);
            if (connection != null) {
                System.out.println("Connected to the database");
            } else {
                System.out.println("Failed to connect to the database");
            }

        } catch (Exception e) {
        }
    }
}
