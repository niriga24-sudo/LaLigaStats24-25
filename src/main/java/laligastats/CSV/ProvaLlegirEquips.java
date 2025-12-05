package laligastats.CSV;

import java.util.ArrayList;

import laligastats.Equip;
import laligastats.GestorCSV;

public class ProvaLlegirEquips {
    public static void main(String[] args) {
        ArrayList<Equip> equips = GestorCSV.llegirEquips();

        for (Equip e : equips) {
            System.out.println(e.toString());
        }
    }
}
