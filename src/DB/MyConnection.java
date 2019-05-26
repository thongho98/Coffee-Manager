package DB;

import java.sql.Connection;
import java.sql.DriverManager;

public class MyConnection {
	static Connection cnn = null;

    public static Connection getConnection() {
        try {
            String uRL = "jdbc:sqlserver://;databaseName=COFFEEHOUSE";
            String user = "sa";
            String pass = "123";
            cnn = DriverManager.getConnection(uRL, user, pass);
        } catch (Exception e) {
            System.err.println("Ket noi that bai.");
        }
        return cnn;
    }
}
