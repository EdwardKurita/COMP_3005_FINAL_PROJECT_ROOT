import java.sql.*;
import java.util.Scanner;

public class Admin {
    public Connection conn;
    private final Scanner sc;
    private int admin_id;

    public Admin(Connection conn, Scanner sc) {
        this.conn = conn;
        this.sc = sc;
    }

    // default admin loop logout to exit
    public void admin_loop(){
        String input = "";
        while (true) {
            System.out.print(   ",___________________________________________________________,\n" +
                                "|                       Admin Main Menu                     |\n" +
                                "|                                                           |\n" +
                                "|                           OPTIONS                         |\n" +
                                "|                 [ rooms | equipment | exit ]              |\n" +
                                "|___________________________________________________________|\n" +
                                "| : ");

            boolean inner_loop = true;

            while (inner_loop) {
                input = sc.nextLine();

                switch (input) {
                    case "rooms", "equipment", "exit":
                        inner_loop = false;
                        break;
                    default:
                        System.out.print("| Invalid input. Please try again.\n| : ");
                        break;
                }
            }

            switch (input) {
                case "rooms":
                    book_room();
                    break;
                case "equipment":
                    update_equipment();
                    break;
                case "exit":
                    return;
                default:
                    System.out.println("| Input handling issue.");
                    break;
            }
        }
    }

    // admin login
    public void login(){
        boolean loop = true;
        String input;

        System.out.print(   ",___________________________________________________________,\n" +
                            "|                   Welcome to admin login!                 |\n" +
                            "|                                                           |\n" +
                            "|                           OPTIONS                         |\n" +
                            "|                   [ enter your id | exit ]                |\n" +
                            "|___________________________________________________________|\n" +
                            "| : ");

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
        admin_loop();
    }

    public boolean validate_id(String input){
        try {
            Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return false;
        }

        try (PreparedStatement stmt = conn.prepareStatement("SELECT admin_id FROM admin WHERE admin_id = ?")) {
            stmt.setInt(1, Integer.parseInt(input));
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                this.admin_id = rs.getInt(1);

                rs.close();

                return true;
            }

            rs.close();

            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // room booking
    public void book_room() {
        boolean loop = true;
        String input;
        System.out.print(   ",___________________________________________________________,\n" +
                            "|                       Room Booking Menu                   |\n" +
                            "|                                                           |\n" +
                            "|                           OPTIONS                         |\n" +
                            "|                        [ view | exit ]                    |\n" +
                            "|___________________________________________________________|\n" +
                            "| : ");

        while (loop) {
            input = sc.nextLine();

            switch (input) {
                case "view":
                    loop = false;
                    break;
                case "exit":
                    return;
                default:
                    System.out.print("| Invalid input. Please try again.\n| : ");
                    break;
            }
        }

        String sessions = "SELECT session_id, room_id, sess_date, start_time, end_time FROM session";
        try (PreparedStatement stmt1 = conn.prepareStatement(sessions)) {
            ResultSet rs = stmt1.executeQuery();

            System.out.print(   ",___________________________________________________________,\n" +
                                "|                     Available Room Sessions               |\n" +
                                "|___________________________________________________________|\n");

            while (rs.next()) {
                int session_id = rs.getInt(1);

                System.out.print(   "| ID: " +  rs.getInt(1) + " | ROOM: " + rs.getInt(2) + "\n" +
                                    "| Date: " + rs.getDate(3) + " | " + rs.getTime(4) + " - " + rs.getTime(5) + "\n" +
                                    "|___________________________________________________________|\n");
            }

            rs.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        System.out.print(   ",___________________________________________________________,\n" +
                            "|                          Assign Room                      |\n" +
                            "|                                                           |\n" +
                            "|                           OPTIONS                         |\n" +
                            "|                      [ assign | exit ]                    |\n" +
                            "|___________________________________________________________|\n" +
                            "| : ");

        loop = true;

        while (loop) {
            input = sc.nextLine();

            switch (input) {
                case "assign":
                    loop = false;
                    break;
                case "exit":
                    return;
                default:
                    System.out.print("| Invalid input. Please try again.\n| : ");
                    break;
            }
        }

        System.out.print(   "|                         [ ROOM ]                          |\n" +
                            "|___________________________________________________________|\n" +
                            "| : ");
        int room_id = sc.nextInt();
        sc.nextLine();

        System.out.print(   "|                          [ ID ]                           |\n" +
                            "|___________________________________________________________|\n" +
                            "| : ");
        int id = sc.nextInt();
        sc.nextLine();

        String assign_room = "UPDATE session SET room_id = ? WHERE session_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(assign_room)) {
            stmt.setInt(1, room_id);
            stmt.setInt(2, id);

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void update_equipment(){
        boolean loop = true;
        String input = "";

        System.out.print(   ",___________________________________________________________,\n" +
                            "|               Welcome to the equipment logs!              |\n" +
                            "|___________________________________________________________|\n");

        String fetch_logs = "SELECT * FROM equipment_logs";
        try (PreparedStatement stmt1 = conn.prepareStatement(fetch_logs)){
            ResultSet rs = stmt1.executeQuery();
            if (!rs.isBeforeFirst()) {
                System.out.println("| There are currently no logs.");
            }
            while (rs.next()){
                System.out.print(   "| Issue # " + rs.getInt(1) + " | " + rs.getString(4) + " | " + rs.getString(3) + "\n" +
                                    "| Description: " + rs.getString(2) + "\n" +
                                    "|___________________________________________________________|\n");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.print(   "|                  OPTIONS (case sensitive)                 |\n" +
                            "|                   [ new | fixed | exit ]                  |\n" +
                            "|___________________________________________________________|\n" +
                            "| : ");

        while (loop) {
            input = sc.nextLine();
            switch (input) {
                case "new", "fixed", "exit":
                    loop = false;
                    break;
                default:
                    System.out.print("| Invalid input. Please try again.\n");
                    break;
            }
        }

        if (input.equals("exit")) {
            return;
        }

        if (input.equals("new")) {
            System.out.print(   "|                       [ equipment ]                       |\n" +
                                "|___________________________________________________________|\n" +
                                "| : ");
            String equipment = sc.nextLine();

            System.out.print(   "|                      [ description ]                      |\n" +
                                "|___________________________________________________________|\n" +
                                "| : ");
            String description = sc.nextLine();

            String new_log = "INSERT INTO equipment_logs (descript, stat, equipment) VALUES (?, ?, ?)";
            try (PreparedStatement stmt2 = conn.prepareStatement(new_log)){
                stmt2.setString(1, description);
                stmt2.setString(2, "open");
                stmt2.setString(3, equipment);
                stmt2.executeUpdate();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.print(   "|                        [ Issue # ]                        |\n" +
                                "|___________________________________________________________|\n" +
                                "| : ");
            int issue_id = sc.nextInt();
            sc.nextLine();

            String update_log = "UPDATE equipment_logs SET stat = ? WHERE issue_id = ?";
            try (PreparedStatement stmt3 = conn.prepareStatement(update_log)) {
                stmt3.setString(1, "closed");
                stmt3.setInt(2, issue_id);
                stmt3.executeUpdate();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}