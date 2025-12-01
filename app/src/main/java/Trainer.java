import java.sql.*;
import java.util.Scanner;

public class Trainer {
    public Connection conn;
    private final Scanner sc;

    public Trainer(Connection conn, Scanner sc) {
        this.conn = conn;
        this.sc = sc;
    }

    // default trainer loop (logout to exit)
    public void trainer_loop(){

    }

    // Trainer login (called immediately)
    public void login(){

        trainer_loop();
    }

    // create availability slot
    public void availability(){

    }

    // member search and view
    public void search_member() {

    }
}
