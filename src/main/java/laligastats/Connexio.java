package laligastats;
import java.sql.*;

public class Connexio {

    public static final String URL = "jdbc:mysql://localhost:3306/";
    public static final String USER =  "root";
    public static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public boolean crearBBDD() {
        try (Connection con = Connexio.getConnection()) {
            String sql = "CREATE DATABASE LaLiga_23_24_Stats";
            PreparedStatement ps = con.prepareStatement(sql);
            System.out.println("BBDD LaLiga_23_24_Stats creada");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteBBDD() {
        try (Connection con = Connexio.getConnection()) {
            String sql = "DROP IF EXISTS LaLiga_23_24_Stats";
            PreparedStatement ps = con.prepareStatement(sql);
            System.out.println("BBDD LaLiga_23_24_Stats eliminada");
            

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
