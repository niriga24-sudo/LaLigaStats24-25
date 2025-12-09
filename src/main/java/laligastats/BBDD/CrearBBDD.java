package laligastats.BBDD;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import laligastats.Connexio;
import laligastats.DAO.EquipDAO;
import laligastats.DAO.JugadorDAO;

public class CrearBBDD {
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
    public static void main(String[] args) {
        Boolean BBDDCreada = crearBBDD();
        System.out.println(BBDDCreada);

        EquipDAO eDAO = new EquipDAO();
        Boolean TaulaEquipsCreada = eDAO.crearTaulaEquips();
        System.out.println(TaulaEquipsCreada);

        JugadorDAO jDAO = new JugadorDAO();
        Boolean TaulaJugadorsCreada = jDAO.crearTaulaJugador();
        System.out.println(TaulaJugadorsCreada);
    }

}
