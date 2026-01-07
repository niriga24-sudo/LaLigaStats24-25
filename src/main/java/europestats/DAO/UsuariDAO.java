package europestats.DAO;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HexFormat;

import europestats.BBDD.Connexio;

public class UsuariDAO {

    public void crearTaulaUsuaris() {
        String sql = """
                CREATE TABLE IF NOT EXISTS usuaris (
                    email VARCHAR(100) PRIMARY KEY,
                    password VARCHAR(100) NOT NULL,
                    nom VARCHAR(50),
                    rol VARCHAR(20) DEFAULT 'ADMIN'
                );
                """;
        try (Connection con = Connexio.getConnectionBBDD();
                Statement st = con.createStatement()) {
            st.executeUpdate(sql);
            System.out.println("✅ Verificació de la taula 'usuaris' completada.");
        } catch (SQLException e) {
            System.err.println("❌ Error creant la taula usuaris: " + e.getMessage());
        }
    }

    // Mètode per xifrar la contrasenya (SHA-256)
    public String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error xifrant contrasenya", e);
        }
    }

    public void inicialitzarAdmins() {
        String sql = "INSERT IGNORE INTO usuaris (email, password, nom, rol) VALUES (?, ?, ?, ?)";
        try (Connection con = Connexio.getConnectionBBDD();
                PreparedStatement ps = con.prepareStatement(sql)) {

            // Nico
            ps.setString(1, "nico@europestats.com");
            ps.setString(2, hashPassword("admin123"));
            ps.setString(3, "Nico");
            ps.setString(4, "ADMIN");
            ps.executeUpdate();

            // Pedro
            ps.setString(1, "pedro@europestats.com");
            ps.setString(2, hashPassword("admin456"));
            ps.setString(3, "Pedro");
            ps.setString(4, "ADMIN");
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean validarLogin(String email, String password) {
        // Intentem assegurar-nos que la taula existeix abans de mirar
        // crearTaulaUsuaris(); // Opcional: auto-reparació en cada intent

        String sql = "SELECT password FROM usuaris WHERE email = ?";
        try (Connection con = Connexio.getConnectionBBDD();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String hashGuardat = rs.getString("password");
                    return hashGuardat.equals(hashPassword(password));
                }
            }
        } catch (SQLException e) {
            System.err.println("⚠️ Error en validar login (BBDD possiblement KO): " + e.getMessage());
            // Llançar l'error perquè el controlador sàpiga que ha d'anar a mode Offline
            throw new RuntimeException("Error de connexió", e);
        }
        return false;
    }
}
