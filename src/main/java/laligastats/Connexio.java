package laligastats;
import java.sql.*;

public class Connexio {

    public static final String URL = "jdbc:mysql://localhost:3306/";
    public static final String URLBBDD = "jdbc:mysql://localhost:3306/LaLiga_24_25_Stats";
    public static final String USER =  "root";
    public static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

        public static Connection getConnectionBBDD() throws SQLException {
        return DriverManager.getConnection(URLBBDD, USER, PASSWORD);
    }
}
