import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;

public class Admin {
    public Connection conn;

    public Admin(Connection conn) {
        this.conn = conn;
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
