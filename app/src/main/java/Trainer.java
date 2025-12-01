import java.sql.*;
import java.util.Scanner;

public class Trainer {
    public Connection conn;
    private final Scanner sc;
    private int trainer_id;

    public Trainer(Connection conn, Scanner sc) {
        this.conn = conn;
        this.sc = sc;
    }

    // default trainer loop (logout to exit)
    public void trainer_loop(){
        String input = "";
        while (true) {
            System.out.print(   ",___________________________________________________________,\n" +
                                "|                     Trainer Main Menu                     |\n" +
                                "|                                                           |\n" +
                                "|                           OPTIONS                         |\n" +
                                "|               [ availability | search | exit ]            |\n" +
                                "|___________________________________________________________|\n" +
                                "| : ");

            boolean inner_loop = true;

            while (inner_loop) {
                input = sc.nextLine();

                switch (input) {
                    case "availability", "search", "exit":
                        inner_loop = false;
                        break;
                    default:
                        System.out.print("| Invalid input. Please try again.\n| : ");
                        break;
                }
            }

            switch (input) {
                case "availability":
                    availability();
                    break;
                case "search":
                    search_member();
                    break;
                case "exit":
                    return;
                default:
                    System.out.println("| Input handling issue.");
                    break;
            }
        }
    }

    // Trainer login (called immediately)
    public void login(){
        boolean loop = true;
        String input;

        System.out.print(   ",___________________________________________________________,\n" +
                            "|                  Welcome to trainer login!                |\n" +
                            "|                                                           |\n" +
                            "|                           OPTIONS                         |\n" +
                            "|                   [ enter your id | exit ]                |\n" +
                            "|___________________________________________________________|\n" +
                            "| : ");
        // same infinite loop as in the main class
        while (loop) {
            input = sc.nextLine();

            if (input.equals("exit")) {
                return;
            }

            if (validate_id(input)) {
                loop = false;
            } else {
                System.out.print("| Invalid input. Please try again.\n| : ");
            }

        }

        trainer_loop();
    }

    // validate trainer id
    public boolean validate_id(String id){
        // Preliminary check for integer in string
        try {
            Integer.parseInt(id);
        } catch (NumberFormatException e) {
            return false;
        }

        // Checking if id exists in database
        try (PreparedStatement stmt = conn.prepareStatement("SELECT trainer_id FROM trainer WHERE trainer_id = ?")) {
            stmt.setInt(1, Integer.parseInt(id));
            ResultSet rs = stmt.executeQuery();

            if (rs.next()){
                this.trainer_id = rs.getInt(1);

                rs.close();

                return true;
            }

            rs.close();

            return false;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // create availability slot
    public void availability(){
        boolean loop = true;
        String input;
        System.out.print(   ",___________________________________________________________,\n" +
                            "|              Welcome to availability setting!             |\n" +
                            "|                                                           |\n" +
                            "|                           OPTIONS                         |\n" +
                            "|                        [ set | exit ]                     |\n" +
                            "|___________________________________________________________|\n" +
                            "| : ");

        while (loop) {
            input = sc.nextLine();

            switch (input) {
                case "set":
                    loop = false;
                    break;
                case "exit":
                    return;
                default:
                    System.out.print("| Invalid input. Please try again.\n| : ");
                    break;
            }
        }

        System.out.print(   ",___________________________________________________________,\n" +
                            "|                    Enter availability!                    |\n" +
                            "|                                                           |\n" +
                            "|                           date                            |\n" +
                            "|                      [ YYYY-MM-DD ]                       |\n" +
                            "|___________________________________________________________|\n" +
                            "| : ");
        String date = sc.nextLine();

        System.out.print(   "|                        start time                         |\n" +
                            "|                         [ HH:MI ]                         |\n" +
                            "|___________________________________________________________|\n" +
                            "| : ");
        String start_time = sc.nextLine();

        System.out.print(   "|                          end time                         |\n" +
                            "|                         [ HH:MI ]                         |\n" +
                            "|___________________________________________________________|\n" +
                            "| : ");
        String end_time = sc.nextLine();

        String set_availability_sql = "INSERT INTO availability_window (trainer_id, win_date, start_time, end_time) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(set_availability_sql)) {
            stmt.setInt(1, this.trainer_id);
            stmt.setDate(2, Date.valueOf(date));
            stmt.setTime(3, Time.valueOf(start_time + ":00"));
            stmt.setTime(4, Time.valueOf(end_time + ":00"));

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    // member search and view
    public void search_member() {
        boolean loop = true;
        String input;
        System.out.print(   ",___________________________________________________________,\n" +
                            "|                 Welcome to member search!                 |\n" +
                            "|                                                           |\n" +
                            "|                           OPTIONS                         |\n" +
                            "|                      [ search | exit ]                    |\n" +
                            "|___________________________________________________________|\n" +
                            "| : ");
        while (loop) {
            input = sc.nextLine();

            switch (input) {
                case "search":
                    loop = false;
                    break;
                case "exit":
                    return;
                default:
                    System.out.print("| Invalid input. Please try again.\n| : ");
                    break;
            }
        }

        String fetch_members = "SELECT DISTINCT member_id, first_name, last_name FROM member_fitness_dashboard ORDER BY member_id";
        try (PreparedStatement stmt1 = conn.prepareStatement(fetch_members)) {
            ResultSet rs = stmt1.executeQuery();

            System.out.println(     ",___________________________________________________________,\n" +
                                    "|                          Member List                      |\n" +
                                    "|___________________________________________________________|");
            while (rs.next()) {
                System.out.print( "| " + rs.getInt(1) + " : " + rs.getString(2) + " " + rs.getString(3) + "\n");
            }

            rs.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        System.out.print(   "|___________________________________________________________|\n" +
                            "|                         [ first_name ]                    |\n" +
                            "| : ");
        String first_name = sc.nextLine();

        System.out.print(   "|___________________________________________________________|\n" +
                            "|                         [ last_name ]                     |\n" +
                            "| : ");
        String last_name = sc.nextLine();

        String search_member_sql = "SELECT heart_rate, gender, weight_val, body_fat, goal FROM member_fitness_dashboard WHERE first_name = ? AND last_name = ? ORDER BY entry_num DESC";
        try (PreparedStatement stmt2 = conn.prepareStatement(search_member_sql)) {
            stmt2.setString(1, first_name);
            stmt2.setString(2, last_name);
            ResultSet rs = stmt2.executeQuery();

            System.out.println(     ",___________________________________________________________,\n" +
                                    "|                          Member                           |\n" +
                                    "|___________________________________________________________|");
            while (rs.next()) {
                System.out.print(   "| Heart Rate: " + rs.getInt(1) + " bpm\n" +
                                    "| Gender: " + rs.getString(2) + "\n" +
                                    "| Weight: " + rs.getInt(3) + " lbs\n" +
                                    "| Body Fat: " + rs.getInt(4) + " %\n" +
                                    "| Goal: " + rs.getString(5) + "\n" +
                                    "|___________________________________________________________|\n");
            }

            System.out.print(   "| Press enter to continue...                                |\n" +
                                "|___________________________________________________________|\n");
            sc.nextLine();

            rs.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}