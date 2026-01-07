package europestats.CSV;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Locale;

import europestats.CLASES.Equip;
import europestats.CLASES.Jugador;
import europestats.DAO.EquipDAO;
import europestats.DAO.JugadorDAO;

public class ExportadorCSV {
    private static final String RUTA_DATA = "DATA/";

    public void exportarTaulaEquips() {
        verificarCarpeta();
        List<Equip> llista = new EquipDAO().obtenirTotsElsEquips();

        try (PrintWriter pw = new PrintWriter(new FileWriter(RUTA_DATA + "equips.csv"))) {
            pw.println(
                    "ID;ID_Lliga;Lliga;Posicio_Equip;Equip;Punts;Partits_Jugats;Victories;Empats;Derrotes;Gols_Marcats;Gols_Encaixats;Diferencia_Gols");

            for (Equip e : llista) {
                // Netegem el nom de l'equip i la lliga per evitar que els ";" trenquin el CSV
                String nomNet = e.getNom_Equip().replace(";", ",");
                String lligaNeta = e.getLliga().replace(";", ",");

                pw.printf(Locale.US, "%d;%d;%s;%d;%s;%d;%d;%d;%d;%d;%d;%d;%d%n",
                        e.getID(), e.getIdLliga(), lligaNeta, e.getPosicio(),
                        nomNet, e.getPunts(), e.getPartits_Jugats(),
                        e.getVictories(), e.getEmpats(), e.getDerrotes(),
                        e.getGols_Marcats(), e.getGols_Encaixats(), e.getDiferencia_Gols());
            }
            System.out.println("✅ Exportació d'equips completada.");
        } catch (IOException e) {
            System.err.println("❌ Error en exportar equips: " + e.getMessage());
        }
    }

    public void exportarTaulaJugadors() {
        verificarCarpeta();
        List<Jugador> llista = new JugadorDAO().obtenirTotsElsJugadors();

        try (PrintWriter pw = new PrintWriter(new FileWriter(RUTA_DATA + "jugadors.csv"))) {
            // Capçalera amb la columna Posicio ben posada
            pw.println("ID;Nom;Posicio;idEquip;ID_Lliga;Gols;Assists;Minuts;Grogues;Vermelles;Gols90;Assists90");

            for (Jugador j : llista) {
                // Neteja de seguretat per a camps de text
                String nomNet = j.getNom().replace(";", ",");
                String posicioNeta = (j.getPosicio() != null) ? j.getPosicio().replace(";", ",") : "Desconeguda";

                pw.printf(Locale.US, "%d;%s;%s;%d;%d;%d;%d;%d;%d;%d;%.2f;%.2f%n",
                        j.getID(),
                        nomNet,
                        posicioNeta,
                        j.getEquip().getID(),
                        j.getIdLliga(),
                        j.getGols_marcats(),
                        j.getAssistencies(),
                        j.getMinuts(),
                        j.getTargetes_Grogues(),
                        j.getTargetes_Vermelles(),
                        j.getGols_per_90(),
                        j.getAssist_per_90());
            }
            System.out.println("✅ Exportació de jugadors completada.");
        } catch (IOException e) {
            System.err.println("❌ Error en exportar jugadors: " + e.getMessage());
        }
    }

    private void verificarCarpeta() {
        File f = new File(RUTA_DATA);
        if (!f.exists())
            f.mkdirs();
    }
}