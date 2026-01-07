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

public class EquipDAO {

    /**
     * Crea la taula d'equips si no existeix.
     */
    public Boolean crearTaulaEquips() {
        String sql = """
                CREATE TABLE IF NOT EXISTS equips (
                    ID INT PRIMARY KEY,
                    ID_Lliga INT,
                    Lliga VARCHAR(50),
                    Posicio_Equip INT,
                    Equip VARCHAR(100),
                    Punts INT,
                    Partits_Jugats INT,
                    Victories INT,
                    Empats INT,
                    Derrotes INT,
                    Gols_Marcats INT,
                    Gols_Encaixats INT,
                    Diferencia_Gols INT
                );
                """;
        try (Connection con = Connexio.getConnectionBBDD();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.executeUpdate();
            System.out.println("✅ Taula 'equips' verificada/creada correctament.");
            return true;
        } catch (SQLException e) {
            System.err.println("❌ Error en crear la taula equips: " + e.getMessage());
            return false;
        }
    }

    /**
     * BUIDA LA TAULA COMPLETAMENT.
     * Necessari perquè la importació CSV esborri els registres que ja no existeixen
     * al fitxer.
     */
    public void buidarTaula() {
        String sql = "TRUNCATE TABLE equips";
        try (Connection conn = Connexio.getConnectionBBDD();
                Statement st = conn.createStatement()) {
            st.execute("SET FOREIGN_KEY_CHECKS = 0");
            st.executeUpdate(sql);
            st.execute("SET FOREIGN_KEY_CHECKS = 1");
            System.out.println("✅ Base de dades buidada per a la nova importació.");
        } catch (SQLException e) {
            System.err.println("❌ Error al buidar la taula per sincronitzar: " + e.getMessage());
        }
    }

    /**
     * Inserta o actualitza un equip complet.
     */
    public void insertarOActualitzarEquipComplet(Equip e) {
        String sql = """
                REPLACE INTO equips (
                    ID, ID_Lliga, Lliga, Posicio_Equip, Equip, Punts, Partits_Jugats,
                    Victories, Empats, Derrotes, Gols_Marcats, Gols_Encaixats, Diferencia_Gols
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = Connexio.getConnectionBBDD()) {
            try (Statement st = conn.createStatement()) {
                st.execute("SET FOREIGN_KEY_CHECKS = 0");

                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setInt(1, e.getID());
                    ps.setInt(2, e.getIdLliga());
                    ps.setString(3, e.getLliga());
                    ps.setInt(4, e.getPosicio());
                    ps.setString(5, e.getNom_Equip());
                    ps.setInt(6, e.getPunts());
                    ps.setInt(7, e.getPartits_Jugats());
                    ps.setInt(8, e.getVictories());
                    ps.setInt(9, e.getEmpats());
                    ps.setInt(10, e.getDerrotes());
                    ps.setInt(11, e.getGols_Marcats());
                    ps.setInt(12, e.getGols_Encaixats());
                    ps.setInt(13, e.getDiferencia_Gols());

                    ps.executeUpdate();
                }
                st.execute("SET FOREIGN_KEY_CHECKS = 1");
            }
        } catch (SQLException ex) {
            System.err.println("❌ Error SQL al guardar equip: " + e.getNom_Equip());
            ex.printStackTrace();
        }
    }

    /**
     * Recupera tots els equips de la base de dades.
     */
    public List<Equip> obtenirTotsElsEquips() {
        List<Equip> llista = new ArrayList<>();
        String sql = "SELECT * FROM equips";

        try (Connection con = Connexio.getConnectionBBDD();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                llista.add(mapejarRSaEquip(rs));
            }
        } catch (SQLException e) {
            System.err.println("❌ Error en obtenir tots els equips: " + e.getMessage());
        }
        return llista;
    }

    /**
     * Converteix una fila de la BBDD en un objecte Java Equip.
     */
    private Equip mapejarRSaEquip(ResultSet rs) throws SQLException {
        return new Equip(
                rs.getInt("ID"),
                rs.getInt("ID_Lliga"),
                rs.getString("Lliga"),
                rs.getInt("Posicio_Equip"),
                rs.getString("Equip"),
                rs.getInt("Punts"),
                rs.getInt("Partits_Jugats"),
                rs.getInt("Victories"),
                rs.getInt("Empats"),
                rs.getInt("Derrotes"),
                rs.getInt("Gols_Marcats"),
                rs.getInt("Gols_Encaixats"),
                rs.getInt("Diferencia_Gols"));
    }

    /**
     * Obté una llista de tots els IDs d'equips existents.
     */
    public List<Integer> obtenirTotsElsIds() {
        List<Integer> ids = new ArrayList<>();
        String sql = "SELECT ID FROM equips";
        try (Connection conn = Connexio.getConnectionBBDD();
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                ids.add(rs.getInt("ID"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ids;
    }

    /**
     * Obté els IDs dels equips d'una lliga específica.
     */
    public List<Integer> obtenirIdsPerLliga(int idLliga) {
        List<Integer> ids = new ArrayList<>();
        String sql = "SELECT ID FROM equips WHERE ID_Lliga = ?";
        try (Connection conn = Connexio.getConnectionBBDD();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idLliga);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ids.add(rs.getInt("ID"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ids;
    }

    /**
     * Elimina un equip de la base de dades pel seu ID.
     */
    public void eliminarEquip(int id) {
        String sql = "DELETE FROM equips WHERE ID = ?";

        try (Connection conn = Connexio.getConnectionBBDD()) {
            try (Statement st = conn.createStatement()) {
                st.execute("SET FOREIGN_KEY_CHECKS = 0");

                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setInt(1, id);
                    int filesAfectades = ps.executeUpdate();

                    if (filesAfectades > 0) {
                        System.out.println("✅ Equip amb ID " + id + " eliminat de MySQL.");
                    } else {
                        System.out.println("⚠️ No s'ha trobat cap equip amb ID " + id + " per eliminar.");
                    }
                }
                st.execute("SET FOREIGN_KEY_CHECKS = 1");
            }
        } catch (SQLException e) {
            System.err.println("❌ Error en eliminar l'equip: " + e.getMessage());
            e.printStackTrace();
        }
    }
}