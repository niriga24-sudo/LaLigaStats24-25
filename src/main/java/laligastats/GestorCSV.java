package laligastats;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class GestorCSV {

    private static final String RUTA_CSV_JUGADORS = "src/main/resources/player_data.csv";
    private static final String RUTA_CSV_EQUIPS = "src/main/resources/team_data.csv";

    public static ArrayList<Equip> llegirEquips() {
        try {
            FileReader fr = new FileReader(RUTA_CSV_EQUIPS);
            BufferedReader br = new BufferedReader(fr);
            ArrayList<Equip> equips = new ArrayList<>();
            br.readLine();
            int id = 1;
            String linia;

            while ((linia = br.readLine()) != null) {
                // Eliminar comillas dobles al principio y al final
                linia = linia.replaceAll("^\"|\"$", "");
                String parts[] = linia.split(",");
                int posicio = Integer.parseInt(parts[0]);
                String nomEquip = parts[1];
                int punts = Integer.parseInt(parts[2]);
                int partits_jugats = Integer.parseInt(parts[3]);
                int victories = Integer.parseInt(parts[4]);
                int empats = Integer.parseInt(parts[5]);
                int derrotes = Integer.parseInt(parts[6]);
                int gols_marcats = Integer.parseInt(parts[7]);
                int gols_encaixats = Integer.parseInt(parts[8]);
                // Eliminar el símbolo + de la diferencia de gols
                int diferencia_gols = Integer.parseInt(parts[9].replace("+", ""));
                int xuts_a_porteria = Integer.parseInt(parts[10]);
                int faltes = Integer.parseInt(parts[11]);
                int targetes_grogues = Integer.parseInt(parts[12]);
                int targetes_vermelles = Integer.parseInt(parts[13]);

                Equip e = new Equip(id, posicio, nomEquip, punts, partits_jugats, victories, empats, derrotes,
                        gols_marcats, gols_encaixats, diferencia_gols, xuts_a_porteria,
                        faltes, targetes_grogues, targetes_vermelles);

                equips.add(e);
                id++;
            }
            return equips;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static Equip cercarEquipxNom(String nomEquip) {
        ArrayList<Equip> equips = llegirEquips();
        try {
            for (Equip e : equips) {
                if (e.getNom_Equip().equals(nomEquip)) {
                    return e;
                }
            }
        } catch (Exception e) {
            System.out.println("error trobant equip per nom");
        }
        return null;
    }

    // Añadir id manualmente y completar funcion
    public static ArrayList<Jugador> llegirJugadors() {
        try {
            FileReader fr = new FileReader(RUTA_CSV_JUGADORS);
            BufferedReader br = new BufferedReader(fr);
            ArrayList<Jugador> jugadors = new ArrayList<>();
            br.readLine(); // llinea dels titols
            int id = 1;
            String linia;
            while ((linia = br.readLine()) != null) {
                // Eliminar comillas dobles al principio y al final
                linia = linia.replaceAll("^\"|\"$", "");
                String parts[] = linia.split(",");
                int posicio = Integer.parseInt(parts[0]);
                String nom = parts[1];
                String nomEquip = parts[2];
                Equip equip = cercarEquipxNom(nomEquip);
                int gols_marcats = Integer.parseInt(parts[3]);
                int partits = Integer.parseInt(parts[4]);
                double Gols_x_partit = Double.parseDouble(parts[5]);
                int posicio_assistencies = Integer.parseInt(parts[6]);
                int assistencies = Integer.parseInt(parts[7]);
                double assist_x_partit = Double.parseDouble(parts[8]);
                int posicio_passades = Integer.parseInt(parts[9]);
                int passades_completades = Integer.parseInt(parts[10]);
                int passades_totals = Integer.parseInt(parts[11]);

                Jugador j = new Jugador(id, posicio, nom, equip, gols_marcats, partits,
                        Gols_x_partit, posicio_assistencies, assistencies, assist_x_partit,
                        posicio_passades, passades_completades, passades_totals);

                jugadors.add(j);
                id++;
            }
            return jugadors;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

}
