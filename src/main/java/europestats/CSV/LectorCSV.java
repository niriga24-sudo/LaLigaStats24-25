package europestats.CSV;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import europestats.CLASES.Equip;
import europestats.CLASES.Jugador;

public class LectorCSV {

    private static String netejarText(String text) {
        if (text == null)
            return "";
        // Eliminem entitats HTML i netegem espais
        return text.replace("&apos;", "'")
                .replace("&amp;", "&")
                .replace("&quot;", "\"")
                .replace("&lt;", "<")
                .replace("&gt;", ">")
                .trim();
    }

    public static List<Equip> carregarEquipsDesDeFitxer(String ruta) {
        List<Equip> llista = new ArrayList<>();
        File file = new File(ruta);
        if (!file.exists())
            return llista;

        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream(file), StandardCharsets.UTF_8))) {

            br.readLine(); // Saltar capçalera
            String linia;
            while ((linia = br.readLine()) != null) {
                linia = linia.trim();
                if (linia.isEmpty())
                    continue;

                try {
                    String[] d = linia.split(";");
                    if (d.length < 13)
                        continue;

                    Equip e = new Equip(
                            Integer.parseInt(d[0].trim()),
                            Integer.parseInt(d[1].trim()),
                            netejarText(d[2]),
                            Integer.parseInt(d[3].trim()),
                            netejarText(d[4]),
                            Integer.parseInt(d[5].trim()),
                            Integer.parseInt(d[6].trim()),
                            Integer.parseInt(d[7].trim()),
                            Integer.parseInt(d[8].trim()),
                            Integer.parseInt(d[9].trim()),
                            Integer.parseInt(d[10].trim()),
                            Integer.parseInt(d[11].trim()),
                            Integer.parseInt(d[12].trim()));
                    llista.add(e);
                } catch (Exception ex) {
                    System.err.println("⚠️ Error format equip: " + linia);
                }
            }
        } catch (Exception ex) {
            System.err.println("❌ Error crític equips: " + ex.getMessage());
        }
        return llista;
    }

    public static List<Jugador> carregarJugadorsDesDeFitxer(String ruta) {
        List<Jugador> llista = new ArrayList<>();
        File file = new File(ruta);
        if (!file.exists())
            return llista;

        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream(file), StandardCharsets.UTF_8))) {

            br.readLine(); // Saltar capçalera
            String linia;
            while ((linia = br.readLine()) != null) {
                linia = linia.trim();
                if (linia.isEmpty())
                    continue;

                try {
                    String[] d = linia.split(";");

                    // Ara que hem corregit l'exportador, el format sempre hauria de ser de 12
                    // columnes
                    if (d.length < 12) {
                        // Mantenim una petita compatibilitat per si de cas, però prioritzem el format
                        // de 12
                        if (d.length == 11) {
                            processarJugador11Columnes(llista, d);
                        }
                        continue;
                    }

                    int id = Integer.parseInt(d[0].trim());
                    String nom = netejarText(d[1]);
                    String posicio = netejarText(d[2]);
                    int idEquip = Integer.parseInt(d[3].trim());
                    int idLliga = Integer.parseInt(d[4].trim());
                    int gols = Integer.parseInt(d[5].trim());
                    int assists = Integer.parseInt(d[6].trim());
                    int minuts = Integer.parseInt(d[7].trim());
                    int grogues = Integer.parseInt(d[8].trim());
                    int vermelles = Integer.parseInt(d[9].trim());

                    // Usem replace per si el CSV ve amb comes en comptes de punts
                    double gols90 = Double.parseDouble(d[10].trim().replace(",", "."));
                    double assists90 = Double.parseDouble(d[11].trim().replace(",", "."));

                    Equip equipTemp = new Equip(idEquip, idLliga, "", 0, "", 0, 0, 0, 0, 0, 0, 0, 0);
                    llista.add(new Jugador(id, nom, posicio, equipTemp, idLliga, gols, assists, minuts, grogues,
                            vermelles, gols90, assists90));

                } catch (Exception e) {
                    System.err.println("⚠️ Error format jugador: " + linia);
                }
            }
        } catch (Exception ex) {
            System.err.println("❌ Error crític jugadors: " + ex.getMessage());
        }
        return llista;
    }

    // Mètode auxiliar per no embrutar el bucle principal
    private static void processarJugador11Columnes(List<Jugador> llista, String[] d) {
        try {
            int id = Integer.parseInt(d[0].trim());
            String nom = netejarText(d[1]);
            String posicio = "Desconeguda";
            int idEquip = Integer.parseInt(d[2].trim());
            int idLliga = Integer.parseInt(d[3].trim());
            int gols = Integer.parseInt(d[4].trim());
            int assists = Integer.parseInt(d[5].trim());
            int minuts = Integer.parseInt(d[6].trim());
            int grogues = Integer.parseInt(d[7].trim());
            int vermelles = Integer.parseInt(d[8].trim());
            double gols90 = Double.parseDouble(d[9].trim().replace(",", "."));
            double assists90 = Double.parseDouble(d[10].trim().replace(",", "."));

            Equip equipTemp = new Equip(idEquip, idLliga, "", 0, "", 0, 0, 0, 0, 0, 0, 0, 0);
            llista.add(new Jugador(id, nom, posicio, equipTemp, idLliga, gols, assists, minuts, grogues, vermelles,
                    gols90, assists90));
        } catch (Exception ignored) {
        }
    }
}