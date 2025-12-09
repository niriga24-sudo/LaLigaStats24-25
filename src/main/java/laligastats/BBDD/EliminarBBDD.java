package laligastats.BBDD;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import laligastats.Connexio;

public class EliminarBBDD {
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
    public static void main(String[] args) {
       Boolean BBDDEliminada = deleteBBDD();
       System.out.println(BBDDEliminada);
    }
}
