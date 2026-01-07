package europestats.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import europestats.BBDD.Connexio;
import europestats.CLASES.Equip;
import europestats.CLASES.Jugador;

public class JugadorDAO {

    /**
     * Comprova si un ID d'equip existeix a la taula d'equips.
     */
    public boolean comprovarSiExisteixEquip(int idEquip) {
        String sql = "SELECT COUNT(*) FROM equips WHERE ID = ?";
        try (Connection con = Connexio.getConnectionBBDD();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idEquip);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error validant existència d'equip: " + e.getMessage());
        }
        return false;
    }

    /**
     * Crea la taula de jugadors si no existeix.
     */
    public boolean crearTaulaJugador() {
        String sql = "CREATE TABLE IF NOT EXISTS jugadors (" +
                "ID INT PRIMARY KEY, " +
                "Nom VARCHAR(100), " +
                "Posicio VARCHAR(50), " +
                "idEquip INT, " +
                "ID_Lliga INT, " +
                "Gols_Marcats INT, " +
                "Assistencies INT, " +
                "Minuts INT, " +
                "Targetes_Grogues INT, " +
                "Targetes_Vermelles INT, " +
                "Gols_per_90 DECIMAL(5,2), " +
                "Assist_per_90 DECIMAL(5,2), " +
                "FOREIGN KEY (idEquip) REFERENCES equips(ID))";

        try (Connection con = Connexio.getConnectionBBDD();
                Statement st = con.createStatement()) {
            st.executeUpdate(sql);
            return true;
        } catch (SQLException e) {
            System.err.println("❌ Error creant taula jugadors: " + e.getMessage());
            return false;
        }
    }

    /**
     * Insereix o actualitza un jugador de forma segura.
     * Utilitzem ON DUPLICATE KEY UPDATE per evitar pèrdua de dades d'altres
     * lligues.
     */
    public void insertarOActualitzarJugador(Jugador j) {
        if (j.getMinuts() < 0)
            return;

        // CANVI CRUCIAL: Passem de REPLACE a INSERT ... ON DUPLICATE KEY UPDATE
        String sql = "INSERT INTO jugadors (ID, Nom, Posicio, idEquip, ID_Lliga, Gols_Marcats, Assistencies, " +
                "Minuts, Targetes_Grogues, Targetes_Vermelles, Gols_per_90, Assist_per_90) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE " +
                "Nom = VALUES(Nom), " +
                "Posicio = VALUES(Posicio), " +
                "idEquip = VALUES(idEquip), " +
                "ID_Lliga = VALUES(ID_Lliga), " +
                "Gols_Marcats = VALUES(Gols_Marcats), " +
                "Assistencies = VALUES(Assistencies), " +
                "Minuts = VALUES(Minuts), " +
                "Targetes_Grogues = VALUES(Targetes_Grogues), " +
                "Targetes_Vermelles = VALUES(Targetes_Vermelles), " +
                "Gols_per_90 = VALUES(Gols_per_90), " +
                "Assist_per_90 = VALUES(Assist_per_90)";

        try (Connection con = Connexio.getConnectionBBDD();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, j.getID());
            ps.setString(2, j.getNom());
            ps.setString(3, j.getPosicio());
            ps.setInt(4, j.getEquip().getID());
            ps.setInt(5, j.getIdLliga());
            ps.setInt(6, j.getGols_marcats());
            ps.setInt(7, j.getAssistencies());
            ps.setInt(8, j.getMinuts());
            ps.setInt(9, j.getTargetes_Grogues());
            ps.setInt(10, j.getTargetes_Vermelles());
            ps.setDouble(11, j.getGols_per_90());
            ps.setDouble(12, j.getAssist_per_90());

            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("❌ Error guardant jugador " + j.getNom() + ": " + e.getMessage());
        }
    }

    /**
     * Recupera tots els jugadors fent un JOIN amb equips.
     */
    public List<Jugador> obtenirTotsElsJugadors() {
        List<Jugador> jugadors = new ArrayList<>();
        // Afegim ORDER BY per lliga i equip per a que el CSV surti ordenat
        String sql = "SELECT j.*, e.Lliga as NomLliga, e.Equip as NomEquip FROM jugadors j " +
                "LEFT JOIN equips e ON j.idEquip = e.ID " +
                "ORDER BY j.ID_Lliga, e.Equip, j.Gols_Marcats DESC";

        try (Connection con = Connexio.getConnectionBBDD();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Equip e = new Equip(
                        rs.getInt("idEquip"),
                        rs.getInt("ID_Lliga"),
                        rs.getString("NomLliga") != null ? rs.getString("NomLliga") : "Desconeguda",
                        0,
                        rs.getString("NomEquip") != null ? rs.getString("NomEquip") : "Equip " + rs.getInt("idEquip"),
                        0, 0, 0, 0, 0, 0, 0, 0);

                jugadors.add(new Jugador(
                        rs.getInt("ID"),
                        rs.getString("Nom"),
                        rs.getString("Posicio"),
                        e,
                        rs.getInt("ID_Lliga"),
                        rs.getInt("Gols_Marcats"),
                        rs.getInt("Assistencies"),
                        rs.getInt("Minuts"),
                        rs.getInt("Targetes_Grogues"),
                        rs.getInt("Targetes_Vermelles"),
                        rs.getDouble("Gols_per_90"),
                        rs.getDouble("Assist_per_90")));
            }
        } catch (SQLException e) {
            System.err.println("❌ Error obtenint jugadors: " + e.getMessage());
        }
        return jugadors;
    }

    public void eliminarJugador(int id) {
        String sql = "DELETE FROM jugadors WHERE ID = ?";
        try (Connection conn = Connexio.getConnectionBBDD();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println("✅ Jugador amb ID " + id + " eliminat.");
        } catch (SQLException e) {
            System.err.println("❌ Error al eliminar jugador: " + e.getMessage());
        }
    }

    public void buidarTaula() {
        try (Connection conn = Connexio.getConnectionBBDD();
                Statement st = conn.createStatement()) {
            st.execute("SET FOREIGN_KEY_CHECKS = 0");
            st.executeUpdate("TRUNCATE TABLE jugadors");
            st.execute("SET FOREIGN_KEY_CHECKS = 1");
            System.out.println("✅ Taula 'jugadors' buidada.");
        } catch (SQLException e) {
            System.err.println("❌ Error al buidar jugadors: " + e.getMessage());
        }
    }
}