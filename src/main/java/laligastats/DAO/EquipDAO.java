package laligastats.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import laligastats.Connexio;
import laligastats.Equip;
import laligastats.GestorCSV;

public class EquipDAO {

    public Boolean crearTaulaEquips() {
        try (Connection con = Connexio.getConnectionBBDD()) {
            String sql = """
                        CREATE TABLE equips (
                                ID INT UNIQUE AUTO_INCREMENT,
                                Posicio_Equip INT,
                                Equip VARCHAR(100) PRIMARY KEY,
                                Punts INT,
                                Partits_Jugats INT,
                                Victories INT,
                                Empats INT,
                                Derrotes INT,
                                Gols_Marcats INT,
                                Gols_Encaixats INT,
                                Diferencia_Gols INT,
                                Xuts_A_Porteria INT,
                                Faltes INT,
                                Targetes_Grogues INT,
                                Targetes_Vermelles INT
                        );
                    """;
            PreparedStatement ps = con.prepareStatement(sql);
            ps.executeUpdate();
            System.out.println("Taula equips creada");
            ps.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Boolean insertarCSVEquipsaBBDD() {
        GestorCSV gCSV = new GestorCSV();
        ArrayList<Equip> equips = gCSV.llegirEquips();

        try (Connection con = Connexio.getConnectionBBDD()) {
            for (Equip e : equips) {
                String sql = "INSERT INTO equips VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement ps = con.prepareStatement(sql);
                
                ps.setInt(1, e.getID());
                ps.setInt(2, e.getPosicio());
                ps.setString(3, e.getNom_Equip());
                ps.setInt(4, e.getPunts());
                ps.setInt(5, e.getPartits_Jugats());
                ps.setInt(6, e.getVictories());
                ps.setInt(7, e.getEmpats());
                ps.setInt(8, e.getDerrotes());
                ps.setInt(9, e.getGols_Marcats());
                ps.setInt(10, e.getGols_Encaixats());
                ps.setInt(11, e.getDiferencia_Gols());
                ps.setInt(12, e.getXuts_a_Porteria());
                ps.setInt(13, e.getFaltes());
                ps.setInt(14, e.getTargetes_Grogues());
                ps.setInt(15, e.getTargetes_Vermelles());
                ps.executeUpdate();
                ps.close();
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error insertant equips.csv a LaLiga_24_25_Stats");
            return false;
        }

    }
}
