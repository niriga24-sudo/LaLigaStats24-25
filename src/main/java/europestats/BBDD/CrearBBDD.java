package europestats.BBDD;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import europestats.DAO.EquipDAO;
import europestats.DAO.JugadorDAO;
import europestats.DAO.UsuariDAO;

public class CrearBBDD {

    /**
     * OPCIÓ 1: RESET TOTAL (Borra la BBDD i la recrea de zero)
     * Utilitza la teva classe EliminarBBDD. Perds dades i usuaris,
     * però el sistema recrea els admins nico i pedro al final.
     */
    public static boolean resetTotalSistema() {
        // 1. Eliminem la BBDD sencera
        if (EliminarBBDD.deleteBBDD()) {
            // 2. Intentem connectar al servidor (sense BBDD) per crear-la de nou
            try (Connection con = Connexio.getConnection();
                    Statement stmt = con.createStatement()) {

                stmt.executeUpdate("CREATE DATABASE " + Connexio.NOM_BBDD +
                        " CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci");

                // 3. Recreem les taules i els usuaris per defecte
                inicialitzarEstructura();
                new UsuariDAO().inicialitzarAdmins();

                System.out.println("✅ Reset total completat. Sistema netejat i recreat.");
                return true;
            } catch (SQLException e) {
                System.err.println("❌ Error recreant després del drop: " + e.getMessage());
            }
        }
        return false;
    }

    /**
     * OPCIÓ 2: BUIDAR DADES ESPORTIVES (Manté usuaris)
     * Només neteja jugadors i equips. És la que faràs servir per importar CSVs
     * nous.
     */
    public static boolean buidarDadesEsportives() {
        try (Connection con = Connexio.getConnectionBBDD();
                Statement stmt = con.createStatement()) {

            stmt.execute("SET FOREIGN_KEY_CHECKS = 0");
            stmt.executeUpdate("TRUNCATE TABLE jugadors");
            stmt.executeUpdate("TRUNCATE TABLE equips");
            stmt.execute("SET FOREIGN_KEY_CHECKS = 1");

            System.out.println("✅ Jugadors i Equips eliminats. Usuaris conservats.");
            return true;
        } catch (SQLException e) {
            System.err.println("❌ Error buidant dades: " + e.getMessage());
            return false;
        }
    }

    /**
     * Verifica que les taules existeixin. Si no, les crea.
     */
    public static void inicialitzarEstructura() {
        new EquipDAO().crearTaulaEquips();
        new JugadorDAO().crearTaulaJugador();
        new UsuariDAO().crearTaulaUsuaris();
        System.out.println("✅ Estructura de taules verificada.");
    }

    public static void main(String[] args) {
        // Prova de les funcions
        resetTotalSistema();
        // buidarDadesEsportives();
    }
}