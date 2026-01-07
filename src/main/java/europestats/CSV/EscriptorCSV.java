package europestats.CSV;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Locale;

import europestats.CLASES.Equip;
import europestats.CLASES.Jugador;

public class EscriptorCSV {

    public static void guardarJugadors(List<Jugador> llista, String ruta) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ruta))) {
            // Capçalera exacta del teu CSV
            writer.println("ID;Nom;idEquip;ID_Lliga;Gols;Assists;Minuts;Grogues;Vermelles;Gols90;Assists90");

            for (Jugador j : llista) {
                // Obtenim l'ID de l'equip del objecte Equip que té el Jugador
                int idEquip = (j.getEquip() != null) ? j.getEquip().getID() : 0;

                // Usem Locale.US per assegurar que els decimals surtin amb punt (.) i no amb
                // coma (,)
                writer.println(String.format(Locale.US, "%d;%s;%d;%d;%d;%d;%d;%d;%d;%.2f;%.2f",
                        j.getID(),
                        j.getNom(),
                        idEquip,
                        j.getIdLliga(),
                        j.getGols_marcats(),
                        j.getAssistencies(),
                        j.getMinuts(),
                        j.getTargetes_Grogues(),
                        j.getTargetes_Vermelles(),
                        j.getGols_per_90(),
                        j.getAssist_per_90()));
            }
            System.out.println("✅ CSV de jugadors actualitzat (format ;)");
        } catch (IOException e) {
            System.err.println("❌ Error escrivint jugadors.csv: " + e.getMessage());
        }
    }

    public static void guardarEquips(List<Equip> llista, String ruta) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ruta))) {
            writer.println(
                    "ID;ID_Lliga;Lliga;Posicio_Equip;Equip;Punts;Partits_Jugats;Victories;Empats;Derrotes;Gols_Marcats;Gols_Encaixats;Diferencia_Gols");

            for (Equip e : llista) {
                // Usem printf amb Locale.US per coherència amb el mètode de jugadors
                writer.printf(Locale.US, "%d;%d;%s;%d;%s;%d;%d;%d;%d;%d;%d;%d;%d%n",
                        e.getID(),
                        e.getIdLliga(),
                        e.getLliga(),
                        e.getPosicio(),
                        e.getNom_Equip(),
                        e.getPunts(),
                        e.getPartits_Jugats(),
                        e.getVictories(),
                        e.getEmpats(),
                        e.getDerrotes(),
                        e.getGols_Marcats(),
                        e.getGols_Encaixats(),
                        e.getDiferencia_Gols());
            }
            System.out.println("✅ CSV d'equips actualitzat (format ;)");
        } catch (IOException e) {
            System.err.println("❌ Error escrivint equips.csv: " + e.getMessage());
        }
    }
}