package europestats.BBDD;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class EliminarBBDD {

    public static boolean deleteBBDD() {
        try (Connection con = Connexio.getConnection();
                Statement st = con.createStatement()) {

            System.out.println("Eliminant base de dades: " + Connexio.NOM_BBDD + "...");
            st.executeUpdate("DROP DATABASE IF EXISTS " + Connexio.NOM_BBDD);

            System.out.println("✅ Èxit: La base de dades ja no existeix.");
            return true;

        } catch (SQLException e) {
            System.err.println("❌ Error al eliminar: " + e.getMessage());
            return false;
        }
    }
}
