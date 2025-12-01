import java.sql.*;
import java.util.Scanner;

public class Main {
    // main main loop
    public static void main(String[] args) {
        Connection conn = getConnection();

        if (conn != null) {
            login(conn);
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
    public static void login(Connection conn) {
        Scanner sc = new Scanner(System.in);
        boolean loop = true;
        String input = "";
        // These three get created early so that no matter which one gets selected in login, its easy to pivot.
        Member member = new Member(conn, sc);
        Trainer trainer = new Trainer(conn, sc);
        Admin admin = new Admin(conn, sc);



        while (loop) {
            System.out.print(   ",___________________________________________________________,\n" +
                                "|             Welcome to Health and Fitness tool!           |\n" +
                                "|                                                           |\n" +
                                "|                      Login / register                     |\n" +
                                "|                  OPTIONS (case sensitive)                 |\n" +
                                "|       [ register | member | trainer | admin | exit ]      |\n" +
                                "|___________________________________________________________|\n" +
                                "| : ");

            boolean inner_loop = true;
            // infinite loop to handle random input (this might be the only error handling lolol)
            while (inner_loop) {
                input = sc.nextLine();

                switch (input) {
                    case "register", "member", "trainer", "admin", "exit":
                        inner_loop = false;
                        break;
                    default:
                        System.out.print("| Invalid input. Please try again\n| : ");
                        break;
                }
            }
            // switch to individual login sections for whatever class depending on what the user selects.
            switch (input) {
                case "register":
                    member.register();
                    break;
                case "member":
                    member.login();
                    break;
                case "trainer":
                    trainer.login();
                    break;
                case "admin":
                    admin.login();
                    break;
                case "exit":
                    loop = false;
                    break;
                default: // this should never hit, but its here just in case
                    System.out.println("Login Issue. Exiting");
                    break;
            }
        }
    }
}
