package laligastats.DAO;

import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

    public Boolean insertarEquip(Equip e) {
        try (Connection con = Connexio.getConnectionBBDD()) {
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
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public Boolean actualitzarEquip(Equip e) {
        try (Connection con = Connexio.getConnectionBBDD()) {
            String sql = """
                        UPDATE equips SET
                            Posicio_Equip = ?,
                            Punts = ?,
                            Partits_Jugats = ?,
                            Victories = ?,
                            Empats = ?,
                            Derrotes = ?,
                            Gols_Marcats = ?,
                            Gols_Encaixats = ?,
                            Diferencia_Gols = ?,
                            Xuts_A_Porteria = ?,
                            Faltes = ?,
                            Targetes_Grogues = ?,
                            Targetes_Vermelles = ?
                        WHERE Equip = ?;
                    """;
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, e.getPosicio());
            ps.setInt(2, e.getPunts());
            ps.setInt(3, e.getPartits_Jugats());
            ps.setInt(4, e.getVictories());
            ps.setInt(5, e.getEmpats());
            ps.setInt(6, e.getDerrotes());
            ps.setInt(7, e.getGols_Marcats());
            ps.setInt(8, e.getGols_Encaixats());
            ps.setInt(9, e.getDiferencia_Gols());
            ps.setInt(10, e.getXuts_a_Porteria());
            ps.setInt(11, e.getFaltes());
            ps.setInt(12, e.getTargetes_Grogues());
            ps.setInt(13, e.getTargetes_Vermelles());
            ps.setString(14, e.getNom_Equip());
            ps.executeUpdate();
            ps.close();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
    
    public Equip obtenirEquipPerNom(String nomEquip) {
        try (Connection con = Connexio.getConnectionBBDD()) {
            String sql = "SELECT * FROM equips WHERE Equip LIKE ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, "%" + nomEquip + "%");
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Equip e = new Equip(
                        rs.getInt("ID"),
                        rs.getInt("Posicio_Equip"),
                        rs.getString("Equip"),
                        rs.getInt("Punts"),
                        rs.getInt("Partits_Jugats"),
                        rs.getInt("Victories"),
                        rs.getInt("Empats"),
                        rs.getInt("Derrotes"),
                        rs.getInt("Gols_Marcats"),
                        rs.getInt("Gols_Encaixats"),
                        rs.getInt("Diferencia_Gols"),
                        rs.getInt("Xuts_A_Porteria"),
                        rs.getInt("Faltes"),
                        rs.getInt("Targetes_Grogues"),
                        rs.getInt("Targetes_Vermelles"));
                rs.close();
                ps.close();
                return e;
            } else {
                System.out.println("Equip no trobat amb nom: " + nomEquip);
                rs.close();
                ps.close();
                return null;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public ArrayList<Equip> obtenirTotsElsEquips() {
        ArrayList<Equip> equips = new ArrayList<>();
        try (Connection con = Connexio.getConnectionBBDD()) {
            String sql = "SELECT * FROM equips";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Equip e = new Equip(
                        rs.getInt("ID"),
                        rs.getInt("Posicio_Equip"),
                        rs.getString("Equip"),
                        rs.getInt("Punts"),
                        rs.getInt("Partits_Jugats"),
                        rs.getInt("Victories"),
                        rs.getInt("Empats"),
                        rs.getInt("Derrotes"),
                        rs.getInt("Gols_Marcats"),
                        rs.getInt("Gols_Encaixats"),
                        rs.getInt("Diferencia_Gols"),
                        rs.getInt("Xuts_A_Porteria"),
                        rs.getInt("Faltes"),
                        rs.getInt("Targetes_Grogues"),
                        rs.getInt("Targetes_Vermelles"));
                equips.add(e);
            }
            rs.close();
            ps.close();
            return equips;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return equips;
        }
    }

    public Boolean eliminarEquipPerNom(String nomEquip) {
        try (Connection con = Connexio.getConnectionBBDD()) {
            String sql = "DELETE FROM equips WHERE Equip LIKE ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, "%" + nomEquip + "%");
            int filesEliminades = ps.executeUpdate();
            ps.close();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}