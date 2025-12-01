import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;

public class Trainer {
    public Connection conn;

    public Trainer(Connection conn) {
        this.conn = conn;
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
