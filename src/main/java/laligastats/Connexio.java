package laligastats;
import java.sql.*;

public class Connexio {

    public static final String URL = "jdbc:mysql://localhost:3306/";
    public static final String USER =  "root";
    public static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static boolean crearBBDD() {
        try (Connection con = Connexio.getConnection();
             Statement stmt = con.createStatement()) {
            String sql = "CREATE DATABASE LaLiga_24_25_Stats";
            stmt.executeUpdate(sql);
            System.out.println("BBDD LaLiga_24_25_Stats creada");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteBBDD() {
        try (Connection con = Connexio.getConnection();
             Statement st = con.createStatement()) {
            String sql = "DROP DATABASE IF EXISTS LaLiga_24_25_Stats";
            st.executeUpdate(sql);
            System.out.println("BBDD LaLiga_24_25_Stats eliminada");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
