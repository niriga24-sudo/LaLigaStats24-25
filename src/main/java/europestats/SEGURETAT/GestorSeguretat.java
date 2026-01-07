package europestats.SEGURETAT;

import java.util.ArrayList;
import java.util.List;

import europestats.CLASES.Usuari;
import europestats.DAO.UsuariDAO;
import europestats.SERVEIS.SistemaService;

public class GestorSeguretat {
    private final UsuariDAO usuariDAO = new UsuariDAO();
    private final SistemaService sistemaService = new SistemaService();
    private final List<Usuari> usuarisOffline = new ArrayList<>();

    public GestorSeguretat() {
        // Mateixes credencials que a la BBDD per al mode sense connexió
        usuarisOffline.add(new Usuari("nico@europestats.com", "admin123", "Nico", "ADMIN"));
        usuarisOffline.add(new Usuari("pedro@europestats.com", "admin456", "Pedro", "ADMIN"));
    }

    public boolean intentarLogin(String email, String password) {
        // 1. Intentem validació ONLINE si el servidor està actiu
        if (sistemaService.isBBDDConnectada()) {
            if (usuariDAO.validarLogin(email, password)) {
                // Utilitzem el teu mètode iniciar()
                Sessio.getInstancia().iniciar(email);
                return true;
            }
        }

        // 2. Si falla l'anterior (per falta de BBDD o dades incorrectes), mirem OFFLINE
        for (Usuari u : usuarisOffline) {
            if (u.getEmail().equalsIgnoreCase(email) && u.getPassword().equals(password)) {
                // Utilitzem el teu mètode iniciar()
                Sessio.getInstancia().iniciar(u.getEmail());
                System.out.println("⚠️ Login Offline concedit per a: " + u.getNom());
                return true;
            }
        }

        return false;
    }
}