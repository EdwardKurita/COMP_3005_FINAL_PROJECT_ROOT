import java.sql.*;
import java.util.Scanner;

public class Member {
    public Connection conn;
    private final Scanner sc;
    private int member_id;
    private String email;
    private String first_name;
    private String last_name;
    private int latest_dash;

    public Member(Connection conn, Scanner sc) {
        this.conn = conn;
        this.sc = sc;
    }

    // default member loop
    public void member_loop() {
        String input = "";
        while (true) {
            System.out.print(   ",___________________________________________________________,\n" +
                                "|                    Welcome to member home!                |\n" +
                                "|                                                           |\n" +
                                "|                  OPTIONS (case sensitive)                 |\n" +
                                "|        [ ptsession | profile | dashboard | exit ]         |\n" +
                                "|___________________________________________________________|\n" +
                                "| : ");

            boolean inner_loop = true;

            while (inner_loop) {
                input = sc.nextLine();

                switch (input) {
                    case "ptsession", "profile", "dashboard", "exit":
                        inner_loop = false;
                        break;
                    default:
                        System.out.print("| Invalid input. Please try again\n| : ");
                        break;
                }
            }

            switch (input) {
                case "ptsession":
                    PT_session();
                    break;
                case "profile":
                    profile();
                    break;
                case "dashboard":
                    dashboard();
                    break;
                case "exit":
                    return;
                default:
                    System.out.println("| Input handling issue.");
                    break;
            }

        }
    }

    // login as member (logout to exit)
    public void login(){
        boolean loop = true;
        String input;

        System.out.print(   ",___________________________________________________________,\n" +
                            "|                   Welcome to member login!                |\n" +
                            "|                                                           |\n" +
                            "|                           OPTIONS                         |\n" +
                            "|                 [ enter your email | exit ]               |\n" +
                            "|___________________________________________________________|\n" +
                            "| : ");
        // same infinite loop as in the main class
        while (loop) {
            input = sc.nextLine();

            if (input.equals("exit")) {
                return;
            }

            if (validate_email(input)) {
                loop = false;
            } else {
                System.out.print("| Invalid input. Please register or type email properly.\n| : ");
            }

        }

        latest_dashboard();

        member_loop();
    }
    public boolean validate_email(String email){
        try (PreparedStatement stmt = conn.prepareStatement("SELECT member_id, first_name, last_name FROM member WHERE email = ?")) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

           if(rs.next()){
                this.member_id = rs.getInt(1);
                this.first_name = rs.getString(2);
                this.last_name = rs.getString(3);
                rs.close();
                stmt.close();

                this.email = email;

                return true;
           }

           rs.close();
           stmt.close();

           return false;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void latest_dashboard() {
        String get_dash = "SELECT entry_num FROM member_fitness WHERE member_id = ? ORDER BY entry_num DESC LIMIT 1";

        try (PreparedStatement stmt = conn.prepareStatement(get_dash)) {
            stmt.setInt(1,  member_id);
            ResultSet rs = stmt.executeQuery();

            if(rs.next()){
                this.latest_dash = rs.getInt(1);

                rs.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // register new member
    public void register(){
        boolean loop = true;
        String input;
        System.out.print(   ",___________________________________________________________,\n" +
                            "|               Welcome to member registration!             |\n" +
                            "|                                                           |\n" +
                            "|                           OPTIONS                         |\n" +
                            "|                     [ register | exit ]                   |\n" +
                            "|___________________________________________________________|\n" +
                            "| : ");
        while (loop) {
            input = sc.nextLine();

            if (input.equals("exit")) {
                return;
            } else if (input.equals("register")) {
                loop = false;
            } else {
                System.out.print("| Invalid input. Please try again\n| : ");
            }
        }

        System.out.print(   ",___________________________________________________________,\n" +
                            "|                  Enter information panel!                 |\n" +
                            "|                                                           |\n" +
                            "|                          [ email ]                        |\n" +
                            "|___________________________________________________________|\n" +
                            "| : ");

        String email = sc.nextLine();

        System.out.print(   "|                        [ first name ]                     |\n" +
                            "|___________________________________________________________|\n" +
                            "| : ");
        String first_name = sc.nextLine();

        System.out.print(   "|                        [ last name ]                      |\n" +
                            "|___________________________________________________________|\n" +
                            "| : ");
        String last_name  = sc.nextLine();

        System.out.print(   "|                       [ heart rate ]                      |\n" +
                            "|___________________________________________________________|\n" +
                            "| : ");
        int heart_rate = sc.nextInt();
        sc.nextLine();

        System.out.print(   "|                         [ gender ]                        |\n" +
                            "|___________________________________________________________|\n" +
                            "| : ");
        String gender = sc.nextLine();

        System.out.print(   "|                         [ weight ]                        |\n" +
                            "|___________________________________________________________|\n" +
                            "| : ");
        int weight_val = sc.nextInt();
        sc.nextLine();

        System.out.print(   "|                        [ body fat ]                       |\n" +
                            "|___________________________________________________________|\n" +
                            "| : ");
        int body_fat = sc.nextInt();
        sc.nextLine();

        String insertmember = "INSERT INTO member (email, first_name, last_name) VALUES (?, ?, ?)";
        try (PreparedStatement stmt1 = conn.prepareStatement(insertmember, Statement.RETURN_GENERATED_KEYS)) {
            stmt1.setString(1, email);
            stmt1.setString(2, first_name);
            stmt1.setString(3, last_name);
            stmt1.executeUpdate();

            ResultSet rs = stmt1.getGeneratedKeys();
            if (rs.next()) {
                this.member_id = rs.getInt(2);
                this.first_name = rs.getString(3);
                this.last_name = rs.getString(4);
            }
            rs.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String insertfitness = "INSERT INTO member_fitness (member_id, entry_num, heart_rate, gender, weight_val, body_fat)" +
                             "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt2 = conn.prepareStatement(insertfitness)) {
            stmt2.setInt(1, member_id);
            stmt2.setInt(2, 1);
            stmt2.setInt(3, heart_rate);
            stmt2.setString(4, gender);
            stmt2.setInt(5, weight_val);
            stmt2.setInt(6, body_fat);

            stmt2.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        member_loop();
    }

    // profile management
    public void profile(){
        boolean loop = true;
        String input;

        System.out.print(   ",___________________________________________________________,\n" +
                            "|                   Welcome to your profile!                |\n" +
                            "|___________________________________________________________|\n" +
                            "| " + this.email + "\n" +
                            "| " + this.first_name + "\n" +
                            "| " + this.last_name + "\n" +
                            "|___________________________________________________________|\n" +
                            "|                  OPTIONS (case sensitive)                 |\n" +
                            "|                     [ update | exit ]                     |\n" +
                            "|___________________________________________________________|\n" +
                            "| : ");

        while(loop){
            input = sc.nextLine();

            switch (input) {
                case "exit":
                    return;
                case "update":
                    loop = false;
                    break;
                default:
                    System.out.print("| Invalid input. Please try again\n| : ");
                    break;
            }
        }

        System.out.print(   ",___________________________________________________________,\n" +
                            "|                  Enter information panel!                 |\n" +
                            "|                                                           |\n" +
                            "|                          [ email ]                        |\n" +
                            "|___________________________________________________________|\n" +
                            "| : ");
        String email = sc.nextLine();

        System.out.print(   "|                        [ first name ]                     |\n" +
                            "|___________________________________________________________|\n" +
                            "| : ");
        String first_name = sc.nextLine();

        System.out.print(   "|                        [ last name ]                      |\n" +
                            "|___________________________________________________________|\n" +
                            "| : ");
        String last_name  = sc.nextLine();

        String update_info = "UPDATE member SET first_name = ?, last_name = ?, email = ? WHERE member_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(update_info, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, first_name);
            stmt.setString(2, last_name);
            stmt.setString(3, email);
            stmt.setInt(4, member_id);
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                this.email = rs.getString(1);
                this.member_id = rs.getInt(2);
                this.first_name = rs.getString(3);
                this.last_name = rs.getString(4);
            }

            rs.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // view/modify dashboard
    public void dashboard(){
        boolean loop = true;
        String input;

        System.out.print(   ",___________________________________________________________,\n" +
                            "|                 Welcome to your dashboard!                |\n" +
                            "|___________________________________________________________|\n");

        String fetch_dashboard = "SELECT heart_rate, gender, weight_val, body_fat, goal FROM member_fitness WHERE member_id = ? ORDER BY entry_num DESC LIMIT 1";
        try (PreparedStatement stmt1 = conn.prepareStatement(fetch_dashboard, Statement.RETURN_GENERATED_KEYS)) {
            stmt1.setInt(1, member_id);
            ResultSet rs = stmt1.executeQuery();

            if (rs.next()) {
                System.out.print("| heart rate: "  + rs.getString(1) + "\n");
                System.out.print("| gender: "  + rs.getString(2) + "\n");
                System.out.print("| weight: "  + rs.getString(3) + "\n");
                System.out.print("| body_fat: "  + rs.getString(4) + "\n");
                System.out.print("| goal : "  + rs.getString(5) + "\n");
            }

            rs.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        System.out.print(
                            "|___________________________________________________________|\n" +
                            "|                  OPTIONS (case sensitive)                 |\n" +
                            "|                     [ update | exit ]                     |\n" +
                            "|___________________________________________________________|\n" +
                            "| : ");

        while(loop){
            input = sc.nextLine();

            switch (input) {
                case "exit":
                    return;
                case "update":
                    loop = false;
                    break;
                default:
                    System.out.print("| Invalid input. Please try again\n| : ");
                    break;
            }
        }

        System.out.print(   "|                       [ heart rate ]                      |\n" +
                            "|___________________________________________________________|\n" +
                            "| : ");
        int heart_rate = sc.nextInt();
        sc.nextLine();

        System.out.print(   "|                         [ gender ]                        |\n" +
                            "|___________________________________________________________|\n" +
                            "| : ");
        String gender = sc.nextLine();

        System.out.print(   "|                         [ weight ]                        |\n" +
                            "|___________________________________________________________|\n" +
                            "| : ");
        int weight_val = sc.nextInt();
        sc.nextLine();

        System.out.print(   "|                        [ body fat ]                       |\n" +
                            "|___________________________________________________________|\n" +
                            "| : ");
        int body_fat = sc.nextInt();
        sc.nextLine();

        System.out.print(   "|                          [ goal ]                         |\n" +
                            "|___________________________________________________________|\n" +
                            "| : ");
        String goal = sc.nextLine();

        String update_info = "INSERT INTO member_fitness (member_id, entry_num, heart_rate, gender, weight_val, body_fat, goal) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt2 = conn.prepareStatement(update_info)) {
            stmt2.setInt(1, member_id);
            stmt2.setInt(2, latest_dash + 1);
            stmt2.setInt(3, heart_rate);
            stmt2.setString(4, gender);
            stmt2.setInt(5, weight_val);
            stmt2.setInt(6, body_fat);
            stmt2.setString(7, goal);

            stmt2.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // register for PT session
    public void PT_session() {
        System.out.print(   ",___________________________________________________________,\n" +
                            "|            Welcome to the PT session scheduler!           |\n" +
                            "|                                                           |\n" +
                            "|                   Available PT slots                      |\n" +
                            "|___________________________________________________________|\n");

        String available_slots = "SELECT first_name, last_name, trainer_id, win_date, start_time, end_time FROM trainer t RIGHT JOIN availability_window a ON t.trainer_id = a.trainer_id";
        try (PreparedStatement stmt1 = conn.prepareStatement(available_slots, Statement.RETURN_GENERATED_KEYS)) {
            ResultSet rs = stmt1.executeQuery();

            if (!rs.isBeforeFirst()) {
                System.out.print("| No available PT slots found. Please try again later.\n");
                return;
            }

            while (rs.next()) {
                System.out.print(   "| " + rs.getString(1) + " " + rs.getString(2) + " - id: " + rs.getInt(3) + "\n" +
                                    "| " + rs.getDate(4) + " - " + rs.getTime(5) + " to " + rs.getTime(6) + "\n" +
                                    "|___________________________________________________________|\n");
            }

            rs.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // add pt session scheduling
        boolean loop = true;
        String input;

        System.out.print(   "|                  OPTIONS (case sensitive)                 |\n" +
                            "|                   [ select slot | exit ]                  |\n" +
                            "|___________________________________________________________|\n" +
                            "| : ");

        while (loop) {
            input = sc.nextLine();
            switch (input) {
                case "exit":
                    return;
                case "select slot":
                    loop = false;
                    break;
                default:
                    System.out.print("| Invalid input. Please try again.\n");
                    break;
            }
        }
        System.out.print(   "|___________________________________________________________|\n" +
                            "|                   Enter desired trainer id                |\n" +
                            "| : ");
        int trainer_id = sc.nextInt();
        sc.nextLine();

        System.out.print(   "|___________________________________________________________|\n" +
                            "|                     Enter PT session date                 |\n" +
                            "| : ");
        String session_date = sc.nextLine();

        System.out.print(   "|___________________________________________________________|\n" +
                            "|                  Enter PT session start time              |\n" +
                            "| : ");
        String start_time = sc.nextLine();

        System.out.print(   "|___________________________________________________________|\n" +
                            "|                    Enter PT session end time              |\n" +
                            "| : ");
        String end_time = sc.nextLine();

        
        try (PreparedStatement stmt = conn.prepareStatement())
    }
}
