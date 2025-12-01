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
                    //update_equipment();
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

        String sessions = "SELECT session_id FROM session WHERE room_id IS NULL";
        try (PreparedStatement stmt = conn.prepareStatement(sessions)) {
            ResultSet rs = stmt.executeQuery();

            System.out.print(",___________________________________________________________,\n" +
                    "|                     Available Room Sessions               |\n" +
                    "|___________________________________________________________|\n" +
                    "|  Session ID  |          Date          |   Time Slot      |\n" +
                    "|___________________________________________________________|\n");

            while (rs.next()) {
                int session_id = rs.getInt("session_id");

                //System.out.print("|      |       YYYY-MM-DD       |    HH:MI - HH:MI  |\n", session_id);
            }

            System.out.print("|___________________________________________________________|\n");

            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();

        }

        // equipment management

    }

    public void update_equipment(){

    }
}