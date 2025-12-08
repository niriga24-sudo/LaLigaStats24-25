package laligastats.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import laligastats.Connexio;

public class EquipDAO {

public Boolean crearTaulaEquips() {
        try (Connection con = Connexio.getConnectionBBDD()) {
            String sql = """
                            CREATE TABLE equips (
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
}
