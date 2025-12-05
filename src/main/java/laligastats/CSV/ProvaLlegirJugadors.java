package laligastats.CSV;

import java.util.ArrayList;

import laligastats.Jugador;
import laligastats.GestorCSV;

public class ProvaLlegirJugadors {
    public static void main(String[] args) {
        ArrayList<Jugador> jugadors = GestorCSV.llegirJugadors();

        for (Jugador j : jugadors) {
            System.out.println(j.toString());
        }
    }
}
