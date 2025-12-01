import java.sql.*;
import java.util.Scanner;

public class Member {
    public Connection conn;
    private final Scanner sc;
    private int member_id;
    private String first_name;
    private String last_name;

    public Member(Connection conn, Scanner sc) {
        this.conn = conn;
        this.sc = sc;
    }

    // default member loop
    public void member_loop(){
        System.out.println("welcome to member centre\n");

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
                return true;
           }

           return false;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // register new member
    public void register(){

        member_loop();
    }

    // profile management
    public void profile(){

    }

    // view/modify dashboard
    public void dashboard(){

    }

    // register for PT session
    public void PT_session() {

    }
}
