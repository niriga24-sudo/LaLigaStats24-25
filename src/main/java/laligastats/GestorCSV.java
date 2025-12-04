package laligastats;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class GestorCSV {

    private static final String RUTA_CSV_JUGADORS = "src/main/resources/player_data.csv";
    private static final String RUTA_CSV_EQUIPS = "src/main/resources/team_data.csv";

//Añadir id manualmente y completar funcion
    public static ArrayList<Jugador> llegirJugadors() {
        try {
            FileReader fr = new FileReader(RUTA_CSV_JUGADORS);
            BufferedReader br = new BufferedReader(fr);
            br.readLine(); //llinea dels titols

            String linia;
            while((linia = br.readLine()) != null) {
                String parts[] = linia.split(",");

                
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    }
