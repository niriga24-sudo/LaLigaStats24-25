package laligastats.DAO;

import java.sql.*;
import java.util.ArrayList;

import laligastats.Connexio;
import laligastats.Equip;
import laligastats.GestorCSV;
import laligastats.Jugador;

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


/*    public boolean insertarJugador(Jugador j) {
        try (Connection con = Connexio.getConnection()){
            String sql = "INSERT INTO jugadors"
            PreparedStatement ps = con.prepareStatement(sql);

        } catch (SQLException e) {
            // TODO: handle exception
        }
    }
        */
}
