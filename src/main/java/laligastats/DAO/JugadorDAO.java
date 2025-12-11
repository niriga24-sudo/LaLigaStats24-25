package laligastats.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import laligastats.BBDD.Connexio;
import laligastats.CLASES.Jugador;
import laligastats.CSV.GestorCSV;

public class JugadorDAO {

    public Boolean crearTaulaJugador() {
        try (Connection con = Connexio.getConnectionBBDD()) {
            String sql = """
                        CREATE TABLE jugadors (
                                ID INT AUTO_INCREMENT PRIMARY KEY,
                                Posicio INT,
                                Nom VARCHAR(100),
                                Equip VARCHAR(100),
                                Gols_Marcats INT,
                                Partits INT,
                                Gols_Per_Partit DECIMAL(4,2),
                                Posicio_Assistencies INT,
                                Assistencies INT,
                                Assistencies_Per_Partit DECIMAL(4,2),
                                Posicio_Passades INT,
                                Passades_Completades INT,
                                Passades_Totals INT,
                                FOREIGN KEY (Equip) REFERENCES equips(Equip)
                        );
                    """;
            PreparedStatement ps = con.prepareStatement(sql);
            ps.executeUpdate();
            System.out.println("Taula jugadors creada");
            ps.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Boolean insertarCSVJugadorsaBBDD() {
        GestorCSV gCSV = new GestorCSV();
        ArrayList<Jugador> jugadors = gCSV.llegirJugadors();

        try (Connection con = Connexio.getConnectionBBDD()) {
            for (Jugador j : jugadors) {
                String sql = "INSERT INTO jugadors VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement ps = con.prepareStatement(sql);

                ps.setInt(1, j.getID());
                ps.setInt(2, j.getPosicio());
                ps.setString(3, j.getNom());
                ps.setString(4, j.getEquip().getNom_Equip());
                ps.setInt(5, j.getGols_marcats());
                ps.setInt(6, j.getPartits());
                ps.setDouble(7, j.getGols_x_Partit());
                ps.setInt(8, j.getPosicio_Assistencies());
                ps.setInt(9, j.getAssistencies());
                ps.setDouble(10, j.getAssist_x_Partit());
                ps.setInt(11, j.getPosicio_Passades());
                ps.setInt(12, j.getPassades_Completades());
                ps.setInt(13, j.getPassades_Totals());
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

    public int insertarJugador(Jugador j) {
        String sql = """
                    INSERT INTO jugadors
                    (Posicio, Nom, Equip, Gols_Marcats, Partits,
                     Gols_Per_Partit, Posicio_Assistencies, Assistencies, Assistencies_Per_Partit,
                     Posicio_Passades, Passades_Completades, Passades_Totals)
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection con = Connexio.getConnectionBBDD();
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, j.getPosicio());
            ps.setString(2, j.getNom());
            ps.setString(3, j.getEquip().getNom_Equip());
            ps.setInt(4, j.getGols_marcats());
            ps.setInt(5, j.getPartits());
            ps.setDouble(6, j.getGols_x_Partit());
            ps.setInt(7, j.getPosicio_Assistencies());
            ps.setInt(8, j.getAssistencies());
            ps.setDouble(9, j.getAssist_x_Partit());
            ps.setInt(10, j.getPosicio_Passades());
            ps.setInt(11, j.getPassades_Completades());
            ps.setInt(12, j.getPassades_Totals());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int nouID = rs.getInt(1);
                j.setId(nouID);
                return nouID;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public Boolean actualitzarJugador(Jugador j) {
        try (Connection con = Connexio.getConnectionBBDD()) {
            String sql = """
                    UPDATE jugadors SET
                        Posicio=?,
                        Nom=?,
                        Equip=?,
                        Gols_Marcats=?,
                        Partits=?,
                        Gols_Per_Partit=?,
                        Posicio_Assistencies=?,
                        Assistencies=?,
                        Assistencies_Per_Partit=?,
                        Posicio_Passades=?,
                        Passades_Completades=?,
                        Passades_Totals=?
                        WHERE ID=?""";
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, j.getPosicio());
            ps.setString(2, j.getNom());
            ps.setString(3, j.getEquip().getNom_Equip());
            ps.setInt(4, j.getGols_marcats());
            ps.setInt(5, j.getPartits());
            ps.setDouble(6, j.getGols_x_Partit());
            ps.setInt(7, j.getPosicio_Assistencies());
            ps.setInt(8, j.getAssistencies());
            ps.setDouble(9, j.getAssist_x_Partit());
            ps.setInt(10, j.getPosicio_Passades());
            ps.setInt(11, j.getPassades_Completades());
            ps.setInt(12, j.getPassades_Totals());
            ps.setInt(13, j.getID());
            ps.executeUpdate();
            ps.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Jugador obtenirJugadorxID(int id) {
        try (Connection con = Connexio.getConnectionBBDD()) {
            String sql = "SELECT * FROM jugadors WHERE ID=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                GestorCSV gCSV = new GestorCSV();
                Jugador j = new Jugador(
                        rs.getInt("ID"),
                        rs.getInt("Posicio"),
                        rs.getString("Nom"),
                        gCSV.cercarEquipxNom(rs.getString("Equip")),
                        rs.getInt("Gols_Marcats"),
                        rs.getInt("Partits"),
                        rs.getDouble("Gols_Per_Partit"), rs.getInt("Posicio_Assistencies"),
                        rs.getInt("Assistencies"),
                        rs.getDouble("Assistencies_Per_Partit"),
                        rs.getInt("Posicio_Passades"),
                        rs.getInt("Passades_Completades"),
                        rs.getInt("Passades_Totals"));
                rs.close();
                ps.close();
                return j;
            } else {
                System.out.println("Jugador no trobat amb ID: " + id);
                rs.close();
                ps.close();
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<Jugador> obtenirTotsJugadors() {
        ArrayList<Jugador> jugadors = new ArrayList<>();
        try (Connection con = Connexio.getConnectionBBDD()) {
            String sql = "SELECT * FROM jugadors";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            GestorCSV gCSV = new GestorCSV();
            while (rs.next()) {
                Jugador j = new Jugador(
                        rs.getInt("ID"),
                        rs.getInt("Posicio"),
                        rs.getString("Nom"),
                        gCSV.cercarEquipxNom(rs.getString("Equip")),
                        rs.getInt("Gols_Marcats"),
                        rs.getInt("Partits"),
                        rs.getDouble("Gols_Per_Partit"), rs.getInt("Posicio_Assistencies"),
                        rs.getInt("Assistencies"),
                        rs.getDouble("Assistencies_Per_Partit"),
                        rs.getInt("Posicio_Passades"),
                        rs.getInt("Passades_Completades"),
                        rs.getInt("Passades_Totals"));
                jugadors.add(j);
            }
            rs.close();
            ps.close();
            return jugadors;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Boolean eliminarJugador(int id) {
        try (Connection con = Connexio.getConnectionBBDD()) {
            String sql = "DELETE FROM jugadors WHERE ID=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            ps.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
