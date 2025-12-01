import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;

public class Member {
    public Connection conn;

    public Member(Connection conn) {
        this.conn = conn;
    }

    // default member loop
    public void member_loop(){

    }

    // login as member (logout to exit)
    public void login(){

        member_loop();
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
