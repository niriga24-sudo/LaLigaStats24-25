package laligastats;

import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.PreparedStatement;

public class ImportarCsv {
    public static void main(String[] args) throws Exception {

        String url = "jdbc:mysql://localhost:3306/";
        String user = "root";
        String password = "";

        String rutaCsvJugadors = "src/main/resources/player_data.csv";
        String rutaCsvEquips = "src/main/resources/team_data.csv";

            // Creacio base de dades
            try (Connection conn = DriverManager.getConnection(url, user, password);
                    Statement stmt = conn.createStatement()) {

                System.out.println("Creant base de dades....");

                stmt.executeUpdate("DROP DATABASE IF EXISTS LaLiga_23_24_Stats");
                stmt.executeUpdate("CREATE DATABASE LaLiga_23_24_Stats");

                System.out.println("Base de dades creada!");
            } catch (Exception e) {
                System.err.println("Error al crear la base de dades " + e.getLocalizedMessage());
            }

            url = "jdbc:mysql://localhost:3306/LaLiga_23_24_Stats";

            // Creacio taules
            try (Connection conn = DriverManager.getConnection(url, user, password);
                    Statement stmt = conn.createStatement()) {

                // Creacio taula equips
                String crearTaulaEquips = """

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

                stmt.executeUpdate(crearTaulaEquips);
                System.out.println("Taula equips creada");

                // Creacio taula jugadors
                String crearTaulaJugadors = """

                            CREATE TABLE jugadores (
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

                stmt.executeUpdate(crearTaulaJugadors);
                System.out.println("Taula jugadors creada");

            } catch (Exception e) {
                System.err.println("Error al crear les taules " + e.getLocalizedMessage());
            }

            // Insercio dades
            try (Connection conn = DriverManager.getConnection(url, user, password)) {

                // Insercio dades jugadors
                String sqlInsertJugadores = """
                            INSERT INTO jugadores (
                                Posicio,
                                Nom,
                                Equip,
                                Gols_Marcats,
                                Partits,
                                Gols_Per_Partit,
                                Posicio_Assistencies,
                                Assistencies,
                                Assistencies_Per_Partit,
                                Posicio_Passades,
                                Passades_Completades,
                                Passades_Totals
                            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);
                        """;

                PreparedStatement ps = conn.prepareStatement(sqlInsertJugadores);
                FileReader fr = new FileReader(rutaCsvJugadors);
                try (BufferedReader br = new BufferedReader(fr)) {
                    br.readLine();

                    String linia;
                    while ((linia = br.readLine()) != null) {
                        String parts[] = linia.split(",");

                        ps.setInt(1, Integer.parseInt(parts[0]));
                        ps.setString(2, parts[1]);
                        ps.setString(3, parts[2]);
                        ps.setInt(4, Integer.parseInt(parts[3]));
                        ps.setInt(5, Integer.parseInt(parts[4]));
                        ps.setBigDecimal(6, new BigDecimal(parts[5]));
                        ps.setInt(7, Integer.parseInt(parts[6]));
                        ps.setInt(8, Integer.parseInt(parts[7]));
                        ps.setBigDecimal(9, new BigDecimal(parts[8]));
                        ps.setInt(10, Integer.parseInt(parts[9]));
                        ps.setInt(11, Integer.parseInt(parts[10]));
                        ps.setInt(12, Integer.parseInt(parts[11]));

                        ps.executeUpdate();
                    }

                    System.out.println("Jugadors importats.");
                }

            } catch (Exception e) {
                System.err.println("Error al introduir les dades dels jugadors " + e.getLocalizedMessage());
            }

            try (Connection conn = DriverManager.getConnection(url, user, password)) {

                // Insercio dades equips
                String sqlInsertEquips = "INSERT INTO equips VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                PreparedStatement ps = conn.prepareStatement(sqlInsertEquips);
                FileReader fr = new FileReader(rutaCsvEquips);
                try (BufferedReader br = new BufferedReader(fr)) {
                    br.readLine();

                    String linia;

                    while ((linia = br.readLine()) != null) {
                        String parts[] = linia.split(",");

                        ps.setInt(1, Integer.parseInt(parts[0]));
                        ps.setString(2, parts[1]);
                        ps.setInt(3, Integer.parseInt(parts[2]));
                        ps.setInt(4, Integer.parseInt(parts[3]));
                        ps.setInt(5, Integer.parseInt(parts[4]));
                        ps.setInt(6, Integer.parseInt(parts[5]));
                        ps.setInt(7, Integer.parseInt(parts[6]));
                        ps.setInt(8, Integer.parseInt(parts[7]));
                        ps.setInt(9, Integer.parseInt(parts[8]));
                        ps.setInt(10, Integer.parseInt(parts[9]));
                        ps.setInt(11, Integer.parseInt(parts[10]));
                        ps.setInt(12, Integer.parseInt(parts[11]));
                        ps.setInt(13, Integer.parseInt(parts[12]));
                        ps.setInt(14, Integer.parseInt(parts[13]));

                        ps.executeUpdate();
                    }

                    System.out.println("Equips importats.");
                }

            } catch (Exception e) {
                System.err.println("Error al introduir les dades dels equips " + e.getLocalizedMessage());
            }

    }
}
