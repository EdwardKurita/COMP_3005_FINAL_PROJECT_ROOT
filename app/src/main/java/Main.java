import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;

public class Main {
    // main main loop
    public static void main() {
        Connection conn = getConnection();

        if (conn != null) {
            // call login if successful
        }

    }

    public static Connection getConnection() {
        String url = "jdbc:postgresql://localhost:5433/comp_3005_final_project";
        String user = "postgres";
        String password = "password";

        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection(url, user, password);

            System.out.println("Connected to the database");

            return connection;
        } catch (Exception e) {
            System.out.println("Failed to connect to the database");
        }

        return null;
    }

    // login function (login as member/trainer/admin or register) also creates a new admin/member/trainer class on switch

}
