import java.sql.*;
import java.util.Scanner;

public class Admin {
    public Connection conn;
    private final Scanner sc;

    public Admin(Connection conn, Scanner sc) {
        this.conn = conn;
        this.sc = sc;
    }

    // default admin loop logout to exit
    public void admin_loop(){

    }

    // admin login
    public void login(){

        admin_loop();
    }

    // room booking
    public void book_room(){

    }

    // equipment management
    public void update_equipment(){

    }
}
