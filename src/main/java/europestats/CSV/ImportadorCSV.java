package europestats.CSV;

import java.util.List;

import europestats.CLASES.Equip;
import europestats.CLASES.Jugador;
import europestats.DAO.EquipDAO;
import europestats.DAO.JugadorDAO;

public class ImportadorCSV {

    private static final EquipDAO equipDAO = new EquipDAO();
    private static final JugadorDAO jugadorDAO = new JugadorDAO();

    public static int[] importarTotDelsCSV() {
        System.out.println("🔄 Iniciant sincronització total amb CSV...");

        // L'ordre és vital per les Foreign Keys: primer buidem fills (jugadors),
        // després pares (equips)
        jugadorDAO.buidarTaula();
        equipDAO.buidarTaula();

        // Primer importem pares (equips)
        int totalEquips = importarEquipsDesDeCSV("DATA/equips.csv");

        // Després importem fills (jugadors)
        int totalJugadors = importarJugadorsDesDeCSV("DATA/jugadors.csv");

        System.out.println("✅ Sincronització finalitzada.");
        return new int[] { totalEquips, totalJugadors };
    }

    public static int importarEquipsDesDeCSV(String ruta) {
        List<Equip> equips = LectorCSV.carregarEquipsDesDeFitxer(ruta);
        int count = 0;
        if (equips != null) {
            for (Equip e : equips) {
                equipDAO.insertarOActualitzarEquipComplet(e);
                count++;
            }
        }
        System.out.println("ℹ️ Equips processats: " + count);
        return count;
    }

    public static int importarJugadorsDesDeCSV(String ruta) {
        List<Jugador> jugadors = LectorCSV.carregarJugadorsDesDeFitxer(ruta);
        int count = 0;
        if (jugadors != null) {
            for (Jugador j : jugadors) {
                jugadorDAO.insertarOActualitzarJugador(j);
                count++;
            }
        }
        System.out.println("ℹ️ Jugadors processats: " + count);
        return count;
    }
}