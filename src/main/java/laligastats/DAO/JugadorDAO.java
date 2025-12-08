package laligastats.DAO;

import java.sql.*;

import laligastats.Connexio;
import laligastats.Jugador;

public class JugadorDAO {

    public Boolean crearTaulaJugador() {
        try (Connection con = Connexio.getConnectionBBDD()) {
            String sql = """
                            CREATE TABLE jugadors (
                                    Id INT AUTO_INCREMENT PRIMARY KEY,
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
