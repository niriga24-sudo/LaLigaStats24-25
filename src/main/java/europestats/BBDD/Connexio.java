package europestats.BBDD;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connexio {
    // Nom de la base de dades
    public static final String NOM_BBDD = "Europe_Stats_23_24";

    // URL per a operacions de sistema (sense base de dades específica)
    private static final String URL_ROOT = "jdbc:mysql://localhost:3306/?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC";

    // URL per a la base de dades del projecte
    public static final String URL_BBDD = "jdbc:mysql://localhost:3306/" + NOM_BBDD
            + "?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC";

    public static final String USER = "root";
    public static final String PASSWORD = "";

    /**
     * Connexió al servidor (per a operacions DROP/CREATE BBDD)
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL_ROOT, USER, PASSWORD);
    }

    /**
     * Connexió específica a la BBDD del projecte (per a operacions de taules i
     * dades)
     */
    public static Connection getConnectionBBDD() throws SQLException {
        return DriverManager.getConnection(URL_BBDD, USER, PASSWORD);
    }
}
