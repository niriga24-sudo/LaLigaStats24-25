package europestats.SEGURETAT;

/**
 * Aquesta classe utilitza el patró Singleton per mantenir l'estat de l'usuari
 * durant tota la vida de l'aplicació (des de que fa login fins que tanca).
 */
public class Sessio {
    private static Sessio instancia;

    private String email;
    private boolean activa;

    // Constructor privat per evitar instanciacions externes (Singleton)
    private Sessio() {
        this.activa = false;
    }

    public static Sessio getInstancia() {
        if (instancia == null) {
            instancia = new Sessio();
        }
        return instancia;
    }

    // Mètode que cridarà el GestorSeguretat quan el login sigui correcte
    public void iniciar(String email) {
        this.email = email;
        this.activa = true;
    }

    public void tancar() {
        this.email = null;
        this.activa = false;
    }

    // Getters
    public String getEmail() {
        return email;
    }

    public boolean isActiva() {
        return activa;
    }
}
