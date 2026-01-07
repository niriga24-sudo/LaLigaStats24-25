package europestats.SERVEIS;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;

import europestats.BBDD.Connexio;
import europestats.DAO.EquipDAO;
import europestats.DAO.JugadorDAO;

public class SistemaService {

    public boolean isBBDDConnectada() {
        try (Connection conn = Connexio.getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (Exception e) {
            return false;
        }
    }

    public int getTotalEquips() {
        // 1. Intentem BBDD
        if (isBBDDConnectada()) {
            try {
                return new EquipDAO().obtenirTotsElsEquips().size();
            } catch (Exception e) {
                /* fallback al CSV */ }
        }

        // 2. Si falla BBDD, llegim del CSV
        return comptarLiniesCSV("DATA/equips.csv");
    }

    public int getTotalJugadors() {
        // 1. Intentem BBDD
        if (isBBDDConnectada()) {
            try {
                return new JugadorDAO().obtenirTotsElsJugadors().size();
            } catch (Exception e) {
                /* fallback al CSV */ }
        }

        // 2. Si falla BBDD, llegim del CSV
        return comptarLiniesCSV("DATA/jugadors.csv");
    }

    // Mètode auxiliar per llegir dades sense necessitat de MySQL
    private int comptarLiniesCSV(String ruta) {
        File file = new File(ruta);
        if (!file.exists())
            return 0;

        int comptador = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            br.readLine(); // Saltem la capçalera
            while (br.readLine() != null) {
                comptador++;
            }
        } catch (Exception e) {
            return 0;
        }
        return comptador;
    }

    public String getDataUltimaSincro() {
        File file = new File("DATA/equips.csv");
        if (file.exists()) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            return sdf.format(new Date(file.lastModified()));
        }
        return "Mai (Sincronitza dades primer)";
    }
}
